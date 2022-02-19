package nl.luminis.customerstatementprocessor.boundary;

import nl.luminis.customerstatementprocessor.control.CsvParser;
import nl.luminis.customerstatementprocessor.control.RecordValidator;
import nl.luminis.customerstatementprocessor.entities.ResultDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

public class ProcessBoundaryTest {

    @InjectMocks
    private ProcessBoundary processBoundary;
    @Mock
    CsvParser csvParser;
    @Mock
    private RecordValidator recordValidator;
    @Mock
    MultipartFile file;
    @Mock
    InputStream inputStream;


    @Test
    public void testUploadEmptyCsvFile() {
        when(file.isEmpty()).thenReturn(true);
        ResultDto resultDto = processBoundary.uploadCSVFile(file);
        assertEquals(ProcessBoundary.EMPTY_FILE_RESPONSE, resultDto);
    }

    @Test
    public void whenCsvFileCantBeParsedExpectError() throws IOException {
        when(file.getInputStream()).thenReturn(inputStream);
        when(csvParser.parseCsv(inputStream)).thenThrow(IOException.class);
        ResultDto resultDto = processBoundary.uploadCSVFile(file);
        assertEquals(ProcessBoundary.ERROR_READING_CSV, resultDto);
    }

    @Test
    public void whenCsvFileCantBeProcessedExpectError() throws IOException {
        when(file.getInputStream()).thenReturn(inputStream);
        when(csvParser.parseCsv(inputStream)).thenThrow(IllegalStateException.class);
        ResultDto resultDto = processBoundary.uploadCSVFile(file);
        assertEquals(ProcessBoundary.CSV_PROCESSING_ERROR, resultDto);
    }

    @Test
    public void testUploadEmptyXml() {
        ResultDto resultDto = processBoundary.uploadXmlFile(null);
        assertEquals(ProcessBoundary.EMPTY_FILE_RESPONSE, resultDto);
    }

}
