package org.kost.manufacturer;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class Manufacturer {
    private Integer manufacturerId;

    @NotEmpty
    private String manufacturerName;

    private String description;

    @Email
    private String email;
}
