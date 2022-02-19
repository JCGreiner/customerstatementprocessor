package nl.luminis.customerstatementprocessor.control;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import nl.luminis.customerstatementprocessor.entities.RecordDto;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

@Component
public class CsvParser {
    public List<RecordDto> parseCsv(InputStream inputStream) throws IllegalStateException, IOException {
        try (Reader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            CsvToBean<RecordDto> csvToBean = new CsvToBeanBuilder(reader)
                    .withType(RecordDto.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            return csvToBean.parse();
        }
    }
}
