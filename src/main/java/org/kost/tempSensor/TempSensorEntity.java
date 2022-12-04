package org.kost.tempSensor;

import lombok.Data;
import org.kost.manufacturer.ManufacturerEntity;
import org.kost.tempSensorType.TempSensorTypeEntity;
import javax.persistence.*;
import java.time.LocalDateTime;


@Entity(name = "TempSensor")
@Table(name = "tempSensor")
@Data
public class TempSensorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "temp_sensor_id")
    private Integer tempSensorId;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public TempSensorTypeEntity tempSensorTypeEntity;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public ManufacturerEntity tempSensorManufacturerEntity;

    @Column(name = "name")
    private String name;

    @Column(name = "numberOpenPorts")
    private String numberOpenPorts;

    @Column(name = "creationDateTime")
    private LocalDateTime creationDateTime;

    @Column(name = "lastTimeChecked")
    private LocalDateTime lastTimeChecked;

    @Column(name = "tempValue")
    private Double tempValue;
}
