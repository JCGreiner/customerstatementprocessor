package nl.luminis.customerstatementprocessor.control;

import nl.luminis.customerstatementprocessor.entities.RecordDto;
import nl.luminis.customerstatementprocessor.entities.ResultDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RecordValidator {

    static final String MULTIPLE_RECORDS_EXIST_WITH_TRANSACTION_REFERENCE = "Multiple records exist with transaction reference %d.";
    static final String START_BALANCE_HAS_NOT_BEEN_SET_FOR_RECORD_WITH_TRANSACTION_REFERENCE = "Start balance has not been set for record with transaction reference %d.";
    static final String MUTATION_AMOUNT_HAS_NOT_BEEN_SET_FOR_RECORD_WITH_TRANSACTION_REFERENCE = "Mutation amount has not been set for record with transaction reference %d";
    static final String END_BALANCE_AMOUNT_HAS_NOT_BEEN_SET_FOR_RECORD_WITH_TRANSACTION_REFERENCE = "End balance amount has not been set for record with transaction reference %d";
    static final String INCORRECT_END_BALANCE = "%s %s %s does not equal %s for record with transaction reference %d ";

    public ResultDto processRecords(List<RecordDto> records){
        Map<Long, List<String>> result = new HashMap<>();

        // validate uniqueness ids:
        Set<Long> uniqueTransactionReferences = new HashSet<>();
        final Set<Long> duplicateTransactionReferences = records.stream()
                .map(RecordDto::getReference)
                .filter(e -> !uniqueTransactionReferences.add(e))
                .collect(Collectors.toSet());

        // validate end balance
        Map<Long, List<String>> validationResult = records.stream()
                .filter(r -> !duplicateTransactionReferences.contains(r.getReference()))
                .collect(Collectors.toMap(RecordDto::getReference, RecordValidator::validate));
        validationResult.entrySet().removeIf(entry -> entry.getValue().isEmpty());


        for (Long transactionReference : duplicateTransactionReferences){
            String reason = String.format(MULTIPLE_RECORDS_EXIST_WITH_TRANSACTION_REFERENCE, transactionReference);
            result.put(transactionReference, List.of(reason));
        }
        validationResult.putAll(result);
        return new ResultDto(validationResult.isEmpty(), null, validationResult);
    }

    static List<String> validate(RecordDto record) {
        List<String> reasons = new ArrayList<>();

        boolean isStartBalanceNull = record.getStartBalance() == null;
        if (isStartBalanceNull){
            reasons.add(String.format(START_BALANCE_HAS_NOT_BEEN_SET_FOR_RECORD_WITH_TRANSACTION_REFERENCE, record.getReference()));
        }
        boolean isMutationNull = record.getMutation() == null;
        if (isMutationNull){
            reasons.add(String.format(MUTATION_AMOUNT_HAS_NOT_BEEN_SET_FOR_RECORD_WITH_TRANSACTION_REFERENCE, record.getReference()));
        }
        boolean isEndBalanceNull = record.getEndBalance() == null;
        if (isEndBalanceNull){
            reasons.add(String.format(END_BALANCE_AMOUNT_HAS_NOT_BEEN_SET_FOR_RECORD_WITH_TRANSACTION_REFERENCE, record.getReference()));
        }
        if (!isStartBalanceNull && !isMutationNull && !isEndBalanceNull &&
                (record.getStartBalance().setScale(2, RoundingMode.HALF_UP).add(record.getMutation().setScale(2, RoundingMode.HALF_UP)).compareTo(record.getEndBalance().setScale(2, RoundingMode.HALF_UP)) != 0)){
            String mutationString = record.getMutation().compareTo(BigDecimal.ZERO) > 0 ? "+" : "";
            String reason = String.format(INCORRECT_END_BALANCE, record.getStartBalance(), mutationString, record.getMutation(), record.getEndBalance(), record.getReference());
            reasons.add(reason);
        }
        return reasons;
    }
}
