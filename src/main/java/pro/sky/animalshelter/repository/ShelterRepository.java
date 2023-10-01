package pro.sky.animalshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pro.sky.animalshelter.model.Shelter;

import java.util.List;

/**
 * Класс, хранящий данные о приюте
 */
public interface ShelterRepository extends JpaRepository<Shelter, Long> {

    @Query(value = "select * from shelters where shelter_type like ?1", nativeQuery = true)
    List<Shelter> findByShelterType(String type);
}
