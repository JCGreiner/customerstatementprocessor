package nl.luminis.customerstatementprocessor.control;

import nl.luminis.customerstatementprocessor.entities.RecordDto;
import nl.luminis.customerstatementprocessor.entities.ResultDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RecordValidatorTest {
    private static final Long REC1 = 1L;
    private static final Long REC2 = 2L;

    private RecordValidator recordValidator;
    @Mock
    private RecordDto record;
    @Mock
    private RecordDto record2;

    @BeforeEach
    public void setUp(){
        this.recordValidator = new RecordValidator();
    }

    @Test
    public void verifyDuplicateTransactionReferenceYieldsAnErrorReason(){
        when(record.getReference()).thenReturn(REC1);
        when(record2.getReference()).thenReturn(REC1);
        ResultDto resultDto = recordValidator.processRecords(List.of(record, record2));
        assertFalse(resultDto.isSuccess());
        Map<Long, List<String>> reasonsForFailure = resultDto.getReasonsForFailure();
        assertEquals(1, reasonsForFailure.size());

        String expected = String.format(RecordValidator.MULTIPLE_RECORDS_EXIST_WITH_TRANSACTION_REFERENCE, REC1);
        assertEquals(expected, reasonsForFailure.get(REC1).get(0));

    }
    @Test
    public void verifyMultipleValueOfSameReferenceAreReported(){
        when(record.getReference()).thenReturn(REC1);
        when(record2.getReference()).thenReturn(REC2);
        ResultDto resultDto = recordValidator.processRecords(List.of(record, record2));
        assertFalse(resultDto.isSuccess());
    }

    @Test
    public void verifyNullStartBalanceYieldsError(){
        when(record.getReference()).thenReturn(REC1);
        List<String> errors = RecordValidator.validate(record);
        String expected = String.format(RecordValidator.START_BALANCE_HAS_NOT_BEEN_SET_FOR_RECORD_WITH_TRANSACTION_REFERENCE, REC1);
        assertTrue(errors.contains(expected));
    }

    @Test
    public void verifyNullMutationYieldsError(){
        when(record.getReference()).thenReturn(REC1);
        List<String> errors = RecordValidator.validate(record);
        String expected = String.format(RecordValidator.MUTATION_AMOUNT_HAS_NOT_BEEN_SET_FOR_RECORD_WITH_TRANSACTION_REFERENCE, REC1);
        assertTrue(errors.contains(expected));
    }

    @Test
    public void verifyNullEndBalanceYieldsError(){
        when(record.getReference()).thenReturn(REC1);
        List<String> errors = RecordValidator.validate(record);
        String expected = String.format(RecordValidator.END_BALANCE_AMOUNT_HAS_NOT_BEEN_SET_FOR_RECORD_WITH_TRANSACTION_REFERENCE, REC1);
        assertTrue(errors.contains(expected));
    }

    @Test
    public void verifyIncorrectEndBalanceYieldsError(){
        when(record.getReference()).thenReturn(REC1);
        when(record.getStartBalance()).thenReturn(BigDecimal.TEN);
        when(record.getMutation()).thenReturn(BigDecimal.ONE);
        when(record.getEndBalance()).thenReturn(BigDecimal.TEN);
        List<String> errors = RecordValidator.validate(record);
        String expected = String.format(RecordValidator.INCORRECT_END_BALANCE, BigDecimal.TEN, "+", BigDecimal.ONE, BigDecimal.TEN, REC1);
        assertEquals(1, errors.size());
        assertTrue(errors.contains(expected));
    }
}
