package pro.sky.animalshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.animalshelter.model.Visit;

public interface VisitRepository extends JpaRepository<Visit, Long> {
}
