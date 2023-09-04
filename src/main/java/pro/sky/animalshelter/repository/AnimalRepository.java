package pro.sky.animalshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.animalshelter.model.Animal;

public interface AnimalRepository extends JpaRepository<Animal, Long> {
}
