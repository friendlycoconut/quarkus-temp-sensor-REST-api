package org.kost.tempSensor;

import org.kost.tempSensorType.TempSensorType;
import org.kost.tempSensorType.TempSensorTypeEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "cdi")
public interface TempSensorMapper {
    List<TempSensor> toDomainList(List<TempSensorEntity> entities);

    TempSensor toDomain(TempSensorEntity entity);

    @InheritInverseConfiguration(name = "toDomain")
    TempSensorEntity toEntity(TempSensor domain);

    void updateEntityFromDomain(TempSensor domain, @MappingTarget TempSensorEntity entity);

    void updateDomainFromEntity(TempSensorEntity entity, @MappingTarget TempSensor domain);
}
