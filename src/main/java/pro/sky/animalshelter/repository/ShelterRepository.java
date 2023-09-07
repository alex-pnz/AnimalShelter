package pro.sky.animalshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.animalshelter.model.Shelter;

/**
 * Класс, хранящий данные о приюте
 */
public interface ShelterRepository extends JpaRepository<Shelter, Long> {
}
