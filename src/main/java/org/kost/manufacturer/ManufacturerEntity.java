package org.kost.manufacturer;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;


@Entity(name="Manufacturer")
@Table(name = "manufacturer")
@Data
public class ManufacturerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "manufacturer_id")
    private Integer manufacturerId;

    @Column(name = "manufacturer_name")
    @NotEmpty
    private String manufacturerName;

    @Column(name = "description")
    private String description;

    @Column(name = "email")
    @NotEmpty
    private String email;

}
