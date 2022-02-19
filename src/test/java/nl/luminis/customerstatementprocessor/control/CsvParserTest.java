package nl.luminis.customerstatementprocessor.control;

import nl.luminis.customerstatementprocessor.entities.RecordDto;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class CsvParserTest {
    private final CsvParser csvParser = new CsvParser();

    @Test
    public void verifyCsvFileCanBeParsedToRecords() throws IOException {
        InputStream inputStream = CsvParserTest.class.getResourceAsStream("test_records.csv");
        List<RecordDto> records = csvParser.parseCsv(inputStream);
        assertEquals(10, records.size());
        RecordDto firstRecord = records.iterator().next();
        assertEquals(176067, firstRecord.getReference());
        assertEquals("NL43AEGO0773393871", firstRecord.getAccountNumber());
        assertEquals("Candy from Peter de Vries", firstRecord.getDescription());
        assertEquals(BigDecimal.valueOf(24.66), firstRecord.getStartBalance());
        assertEquals(BigDecimal.valueOf(-10.81), firstRecord.getMutation());
        assertEquals(BigDecimal.valueOf(13.85), firstRecord.getEndBalance());
    }
}
