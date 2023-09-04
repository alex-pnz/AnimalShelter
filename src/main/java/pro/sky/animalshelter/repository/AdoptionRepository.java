package pro.sky.animalshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.animalshelter.model.Adoption;

public interface AdoptionRepository extends JpaRepository<Adoption, Long> {
}
