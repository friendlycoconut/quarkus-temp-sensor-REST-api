package org.kost.manufacturer;

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
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.jboss.logging.Logger;
@ApplicationScoped
@AllArgsConstructor
@Slf4j
public class ManufacturerService {
    private final ManufacturerRepository manufacturerRepository;
    private final ManufacturerMapper manufacturerMapper;


    public List<Manufacturer> findAll() {


        return
                this.manufacturerMapper.toDomainList(manufacturerRepository.findAll().list());
    }

    public Optional<Manufacturer> findById(@NonNull Integer manufacturerId) {
        return manufacturerRepository.findByIdOptional(manufacturerId)
                .map(manufacturerMapper::toDomain);
    }

    @Transactional
    public void save(@Valid Manufacturer manufacturer) {
        log.debug("Saving Customer: {}", manufacturer);
        ManufacturerEntity entity = manufacturerMapper.toEntity(manufacturer);
        manufacturerRepository.persist(entity);
        manufacturerMapper.updateDomainFromEntity(entity, manufacturer);
    }

    @Transactional
    public void update(@Valid Manufacturer manufacturer) {
        log.debug("Updating Manufacturer: {}", manufacturer);
        if (Objects.isNull(manufacturer.getManufacturerId())) {
            throw new ServiceException("Manufacturer does not have a manufacturerId");
        }
        ManufacturerEntity entity = manufacturerRepository.findByIdOptional(manufacturer.getManufacturerId())
                .orElseThrow(() -> new ServiceException("No Manufacturer found for manufacturer[%s]", manufacturer.getManufacturerId()));
        manufacturerMapper.updateEntityFromDomain(manufacturer, entity);
        manufacturerRepository.persist(entity);
        manufacturerMapper.updateDomainFromEntity(entity, manufacturer);
    }

    @Transactional
    public void delete(Integer manufacturerId) {
        log.debug("Deleting Manufacturer: {}", manufacturerId);
        List<ManufacturerEntity> list = manufacturerRepository.findAll().list();
        ManufacturerEntity entity = manufacturerRepository.findByIdOptional(manufacturerId)
                .orElseThrow(() -> new ServiceException("No Manufacturer found for manufacturer[%s]",manufacturerId));
        manufacturerRepository.delete(entity);
    }



}
