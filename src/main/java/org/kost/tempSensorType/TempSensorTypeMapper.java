package org.kost.tempSensorType;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "cdi")
public interface TempSensorTypeMapper {
    List<TempSensorType> toDomainList(List<TempSensorTypeEntity> entities);

    TempSensorType toDomain(TempSensorTypeEntity entity);

    @InheritInverseConfiguration(name = "toDomain")
    TempSensorTypeEntity toEntity(TempSensorType domain);

    void updateEntityFromDomain(TempSensorType domain, @MappingTarget TempSensorTypeEntity entity);

    void updateDomainFromEntity(TempSensorTypeEntity entity, @MappingTarget TempSensorType domain);
}
