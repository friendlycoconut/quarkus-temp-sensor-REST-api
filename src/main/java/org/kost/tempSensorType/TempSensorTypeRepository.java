package org.kost.tempSensorType;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TempSensorTypeRepository implements PanacheRepositoryBase<TempSensorTypeEntity, Integer> {
}
