package org.kost.tempSensor;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.kost.exceptions.ServiceException;
import org.kost.manufacturer.ManufacturerEntity;
import org.kost.manufacturer.ManufacturerRepository;
import org.kost.tempSensorType.TempSensorType;
import org.kost.tempSensorType.TempSensorTypeEntity;
import org.kost.tempSensorType.TempSensorTypeRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@ApplicationScoped
@AllArgsConstructor
@Slf4j
public class TempSensorService {
    private final TempSensorRepository tempSensorRepository;
    private final TempSensorMapper tempSensorMapper;
    private final TempSensorTypeRepository tempSensorTypeRepository;
    private final ManufacturerRepository manufacturerRepository;
    public List<TempSensor> findAll() {
        List<TempSensorEntity> allTempSensorEntity = tempSensorRepository.findAll().list();
        List<TempSensor> tempSensors = new ArrayList<>();

        for (TempSensorEntity tempSensorEntity: allTempSensorEntity){
            Optional<TempSensor> tempSensor = tempSensorRepository.findByIdOptional(tempSensorEntity.getTempSensorId())
                    .map(tempSensorMapper::toDomain);

            if(tempSensorEntity.getTempSensorTypeEntity() != null){
                tempSensor.get().setTempSensorTypeId(tempSensorEntity.
                        getTempSensorTypeEntity().
                        getTempSensorTypeId());
            }

            if(tempSensorEntity.getTempSensorManufacturerEntity() != null){
                tempSensor.get().setTempSensorManufacturerId(tempSensorEntity.
                        getTempSensorManufacturerEntity().
                        getManufacturerId());
            }
            tempSensors.add(tempSensor.get());
        }

        return tempSensors;
    }

    public Optional<TempSensor> findById(@NonNull Integer tempSensorId) {
       Optional<TempSensor> tempSensor = tempSensorRepository.findByIdOptional(tempSensorId)
               .map(tempSensorMapper::toDomain);

       tempSensor.get().setTempSensorTypeId(tempSensorRepository.
                                        findByIdOptional(tempSensorId).
                                        get().
                                        getTempSensorTypeEntity().
                                        getTempSensorTypeId());

        tempSensor.get().setTempSensorManufacturerId(tempSensorRepository.
                findByIdOptional(tempSensorId).
                get().
                getTempSensorManufacturerEntity().
                getManufacturerId());
        return tempSensorRepository.findByIdOptional(tempSensorId)
                .map(tempSensorMapper::toDomain);
    }

    @Transactional
    public void save(@Valid TempSensor tempSensor) {
        log.debug("Saving TempSensorType: {}", tempSensor);
        TempSensorEntity entity = tempSensorMapper.toEntity(tempSensor);
        System.out.println(entity);
        entity.setTempSensorTypeEntity(tempSensorTypeRepository.findById(tempSensor.tempSensorTypeId));
        entity.setTempSensorManufacturerEntity(manufacturerRepository.findById(tempSensor.tempSensorManufacturerId));
        entity.setCreationDateTime(LocalDateTime.now());


        tempSensorRepository.persist(entity);
        tempSensorMapper.updateDomainFromEntity(entity, tempSensor);
    }

    @Transactional
    public void update(@Valid TempSensor tempSensor) {
        log.debug("Updating tempSensor: {}", tempSensor);
        if (Objects.isNull(tempSensor.getTempSensorId())) {
            throw new ServiceException("tempSensor does not have a tempSensorID");
        }
        TempSensorEntity entity = tempSensorRepository.findByIdOptional(tempSensor.getTempSensorId())
                .orElseThrow(() -> new ServiceException("No tempSensor found for tempSensor[%s]", tempSensor.getTempSensorId()));
        tempSensorMapper.updateEntityFromDomain(tempSensor, entity);
        entity.setTempSensorTypeEntity(tempSensorTypeRepository.findById(tempSensor.tempSensorTypeId));
        entity.setTempSensorManufacturerEntity(manufacturerRepository.findById(tempSensor.tempSensorManufacturerId));
        entity.setLastTimeChecked(LocalDateTime.now());
        entity.setCreationDateTime(tempSensor.creationDateTime);
        tempSensorRepository.persist(entity);
        tempSensorMapper.updateDomainFromEntity(entity, tempSensor);
    }


    @Transactional
    public void delete(Integer tempSensorId) {
        log.debug("Deleting TempSensor: {}", tempSensorId);

        TempSensorEntity entity = tempSensorRepository.findByIdOptional(tempSensorId)
                .orElseThrow(() -> new ServiceException("No TempSensor found for tempSensors[%s]",tempSensorId));
        tempSensorRepository.delete(entity);
    }



}
