package pro.sky.animalshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.animalshelter.model.Shelter;

public interface ShelterRepository extends JpaRepository<Shelter, Long> {
}
