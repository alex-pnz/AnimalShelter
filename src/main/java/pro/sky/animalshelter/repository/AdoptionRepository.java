package pro.sky.animalshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.animalshelter.model.Adoption;
import pro.sky.animalshelter.model.enums.ProbationTermsStatus;

import java.time.LocalDate;
import java.util.List;

/**
 * Хранилище сведений об "усыновлении" животных из приютов
 */
public interface AdoptionRepository extends JpaRepository<Adoption, Long> {
    public Adoption findByVisitorId(Long id);
    public List<Adoption> findByStatus(ProbationTermsStatus status);

    public List<Adoption> findByAdoptionDateLessThanEqual(LocalDate date);
}
