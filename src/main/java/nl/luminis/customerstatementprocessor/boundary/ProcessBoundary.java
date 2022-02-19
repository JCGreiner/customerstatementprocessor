package nl.luminis.customerstatementprocessor.boundary;

import nl.luminis.customerstatementprocessor.control.CsvParser;
import nl.luminis.customerstatementprocessor.control.RecordValidator;
import nl.luminis.customerstatementprocessor.entities.RecordDto;
import nl.luminis.customerstatementprocessor.entities.RecordsDto;
import nl.luminis.customerstatementprocessor.entities.ResultDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

@RestController
public class ProcessBoundary {
    static final ResultDto EMPTY_FILE_RESPONSE = new ResultDto(false, "Unable to process an empty file", null);
    static final ResultDto CSV_PROCESSING_ERROR = new ResultDto(false, "An error occurred while processing the csv file", null);
    static final ResultDto ERROR_READING_CSV = new ResultDto(false, "An error occurred while reading the csv file", null);

    private final CsvParser csvParser;
    private final RecordValidator recordValidator;


    @Autowired
    public ProcessBoundary(CsvParser csvParser, RecordValidator recordValidator) {
        this.csvParser = csvParser;
        this.recordValidator = recordValidator;
    }

    @PostMapping(value = "/upload-csv-file", consumes = "multipart/form-data")
    public ResultDto uploadCSVFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return EMPTY_FILE_RESPONSE;
        } else {
            try {
                List<RecordDto> records = csvParser.parseCsv(file.getInputStream());
                return recordValidator.processRecords(records);
            } catch (IllegalStateException ex) {
                return CSV_PROCESSING_ERROR;
            } catch (IOException e) {
                return ERROR_READING_CSV;
            }
        }
    }


    @PostMapping(value = "/upload-xml-file", consumes = APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultDto uploadXmlFile(@RequestBody RecordsDto records){
        if (records == null || records.getRecords().isEmpty()){
            return EMPTY_FILE_RESPONSE;
        } else{
            return recordValidator.processRecords(records.getRecords());
        }
    }
}
