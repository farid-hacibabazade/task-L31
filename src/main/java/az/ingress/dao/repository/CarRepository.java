package az.ingress.dao.repository;


import az.ingress.dao.entity.CarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CarRepository extends JpaRepository<CarEntity, Long> {

    @Override
    List<CarEntity> findAll();
}
