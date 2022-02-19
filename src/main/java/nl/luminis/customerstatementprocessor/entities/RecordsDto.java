package nl.luminis.customerstatementprocessor.entities;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "records")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class RecordsDto {
    @XmlElement(name = "record")
    List<RecordDto> records;
}
