package pro.sky.animalshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.animalshelter.model.Animal;

import java.util.Collection;

/**
 * Класс, хранящий данные о животном
 */
public interface AnimalRepository extends JpaRepository<Animal, Long> {
   Collection<Animal> findByShelterId(Long shelterId);
}
