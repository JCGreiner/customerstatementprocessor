package nl.luminis.customerstatementprocessor.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class ResultDto {
    private boolean success;
    private String processingError;
    Map<Long, List<String>> reasonsForFailure;
}
