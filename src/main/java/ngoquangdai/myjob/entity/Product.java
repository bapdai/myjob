package ngoquangdai.myjob.entity;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
//@Builder
//@Entity
//@Table(name = "product")
public class Product {
    private int id;
    private String name;
}
