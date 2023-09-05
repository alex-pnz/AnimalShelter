package pro.sky.animalshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.animalshelter.model.Adoption;

/**
 * Хранилище сведений об "усыновлении" животных из приютов
 */
public interface AdoptionRepository extends JpaRepository<Adoption, Long> {
}
