package org.kost.tempSensorType;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class TempSensorType {
    private Integer tempSensorTypeId;

    @NotEmpty
    private String sensorTypeName;

    @NotEmpty
    private String regulatingISO;

    private String description;

}
