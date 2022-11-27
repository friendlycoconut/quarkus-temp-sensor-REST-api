package org.kost.manufacturer;

import org.mapstruct.Mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "cdi")
public interface ManufacturerMapper {
    List<Manufacturer> toDomainList(List<ManufacturerEntity> entities);

    Manufacturer toDomain(ManufacturerEntity entity);

    @InheritInverseConfiguration(name = "toDomain")
    ManufacturerEntity toEntity(Manufacturer domain);

    void updateEntityFromDomain(Manufacturer domain, @MappingTarget ManufacturerEntity entity);

    void updateDomainFromEntity(ManufacturerEntity entity, @MappingTarget Manufacturer domain);

}
