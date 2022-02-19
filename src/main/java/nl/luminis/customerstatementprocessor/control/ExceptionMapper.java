package nl.luminis.customerstatementprocessor.control;

import nl.luminis.customerstatementprocessor.entities.ResultDto;
import org.springframework.util.MimeType;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionMapper {
    @ResponseBody
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResultDto handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex){
        String supported = ex.getSupportedMediaTypes().stream().map(MimeType::toString).collect(Collectors.joining(", "));
        String msg = "Unsupported mediatype: " + ex.getMessage() + ". Supported types are: " + supported;
        return new ResultDto(false, msg, null);
    }
}
