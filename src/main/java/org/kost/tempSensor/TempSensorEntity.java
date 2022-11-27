package org.kost.tempSensor;

import lombok.Data;
import org.kost.tempSensorType.TempSensorTypeEntity;
import javax.persistence.*;


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

    @Column(name = "name")
    private String name;
}
