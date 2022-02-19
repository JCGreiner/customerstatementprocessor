package nl.luminis.customerstatementprocessor.entities;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name="record")
@XmlAccessorType(XmlAccessType.FIELD)
public class RecordDto {

    @CsvBindByName(column = "Reference")
    @XmlAttribute
    private Long reference; //     A numeric value
    @CsvBindByName(column = "Account Number")
    private String accountNumber; // An IBAN
    @CsvBindByName(column = "Start Balance")
    private BigDecimal startBalance; // The starting balance in Euros
    @CsvBindByName(column = "Mutation")
    private BigDecimal mutation; // Either an addition (+) or a deduction (-)
    @CsvBindByName(column = "Description")
    private String description; //Free text
    @CsvBindByName(column = "End Balance")
    private BigDecimal endBalance; //  The end balance in Euros
}
