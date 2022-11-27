package org.kost.tempSensor;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TempSensorRepository implements PanacheRepositoryBase<TempSensorEntity, Integer> {
}
