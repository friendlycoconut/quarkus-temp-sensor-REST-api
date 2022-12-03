package org.kost.tempSensor;

import lombok.Data;
import org.kost.tempSensorType.TempSensorType;

import javax.inject.Inject;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Data
public class TempSensor {
    private Integer tempSensorId;

    @NotEmpty
    private String name;

    @NotNull
    public Integer tempSensorTypeId;
}
