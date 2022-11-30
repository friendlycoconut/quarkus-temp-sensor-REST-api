package org.kost.tempSensorType;

import lombok.Data;
import org.kost.tempSensor.TempSensorEntity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

@Entity(name="TempSensorType")
@Table(name = "tempSensorType")
@Data
public class TempSensorTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "temp_sensor_type_id")
    private Integer tempSensorTypeId;

    @Column(name = "sensor_type_name")
    @NotEmpty
    private String sensorTypeName;

    @Column(name = "description")
    private String description;

    @Column(name = "regulating_ISO")
    @NotEmpty
    private String regulatingISO;

    @OneToMany(mappedBy = "tempSensorTypeEntity")
    private Set<TempSensorEntity> tempSensorEntities = new HashSet<>();
}
