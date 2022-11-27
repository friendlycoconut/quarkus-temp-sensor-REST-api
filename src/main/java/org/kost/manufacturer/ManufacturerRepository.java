package org.kost.manufacturer;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ManufacturerRepository implements PanacheRepositoryBase<ManufacturerEntity, Integer> {
}
