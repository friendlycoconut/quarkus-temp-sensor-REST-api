package org.kost.tempSensorType;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.kost.exceptions.ServiceException;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@ApplicationScoped
@AllArgsConstructor
@Slf4j
public class TempSensorTypeService {
    private final TempSensorTypeRepository tempSensorTypeRepository;
    private final TempSensorTypeMapper tempSensorTypeMapper;

    public List<TempSensorType> findAll() {
        return
                this.tempSensorTypeMapper.toDomainList(tempSensorTypeRepository.findAll().list());
    }

    public Optional<TempSensorType> findById(@NonNull Integer tempSensorTypeId) {
        return tempSensorTypeRepository.findByIdOptional(tempSensorTypeId)
                .map(tempSensorTypeMapper::toDomain);
    }

    @Transactional
    public void save(@Valid TempSensorType tempSensorType) {
        log.debug("Saving TempSensorType: {}", tempSensorType);
        TempSensorTypeEntity entity = tempSensorTypeMapper.toEntity(tempSensorType);
        tempSensorTypeRepository.persist(entity);
        tempSensorTypeMapper.updateDomainFromEntity(entity, tempSensorType);
    }

    @Transactional
    public void update(@Valid TempSensorType tempSensorType) {
        log.debug("Updating tempSensorType: {}", tempSensorType);
        if (Objects.isNull(tempSensorType.getTempSensorTypeId())) {
            throw new ServiceException("tempSensorType does not have a tempSensorTypeID");
        }
        TempSensorTypeEntity entity = tempSensorTypeRepository.findByIdOptional(tempSensorType.getTempSensorTypeId())
                .orElseThrow(() -> new ServiceException("No tempSensorType found for tempSensorType[%s]", tempSensorType.getTempSensorTypeId()));
        tempSensorTypeMapper.updateEntityFromDomain(tempSensorType, entity);
        tempSensorTypeRepository.persist(entity);
        tempSensorTypeMapper.updateDomainFromEntity(entity, tempSensorType);
    }

}
