package pro.sky.animalshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import pro.sky.animalshelter.model.Visit;
import pro.sky.animalshelter.model.Visitor;

/**
 * Класс, хранящий данные о визите посетителя в приют
 */
public interface VisitRepository extends JpaRepository<Visit, Long> {
    Visit findByVisitor(Visitor visitor);
}
