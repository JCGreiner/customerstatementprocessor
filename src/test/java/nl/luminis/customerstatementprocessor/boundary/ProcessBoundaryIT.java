package nl.luminis.customerstatementprocessor.boundary;

import nl.luminis.customerstatementprocessor.control.CsvParser;
import nl.luminis.customerstatementprocessor.control.RecordValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProcessBoundary.class)
@WebAppConfiguration
public class ProcessBoundaryIT {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testUploadXmlFile() throws Exception {
        InputStream inputStream = getClass().getResourceAsStream("test_records.xml");
        String xml = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

        RequestBuilder request = MockMvcRequestBuilders
                .post("/upload-xml-file")
                .accept(MediaType.APPLICATION_JSON)
                .content(xml)
                .contentType(MediaType.APPLICATION_XML);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        public CsvParser csvParser() {
            return new CsvParser();
        }

        @Bean
        public RecordValidator recordValidator(){
            return new RecordValidator();
        }
    }
}
