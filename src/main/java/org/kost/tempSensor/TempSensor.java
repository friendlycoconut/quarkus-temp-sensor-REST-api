package org.kost.tempSensor;

import lombok.Data;
import org.kost.tempSensorType.TempSensorType;

import javax.inject.Inject;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Data
public class TempSensor {
    private Integer tempSensorId;

    @NotEmpty
    private String name;

    @NotNull
    public Integer tempSensorTypeId;

    @NotNull
    public Integer numberOpenPorts;

    @NotNull
    public LocalDateTime creationDateTime;

    @NotNull
    public LocalDateTime lastTimeChecked;

    @NotNull
    public Integer tempSensorManufacturerId;

    @NotNull
    public  Double tempValue;
}
