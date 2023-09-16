package pro.sky.animalshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import pro.sky.animalshelter.model.Visit;
import pro.sky.animalshelter.model.Visitor;

import java.util.List;

/**
 * Класс, хранящий данные о визите посетителя в приют
 */
public interface VisitRepository extends JpaRepository<Visit, Long> {
    @Query(value = "select * from visitors_per_shelter where visitor_id = ?1 order by id desc limit 1", nativeQuery = true)
    Visit findByVisitor(Long visitorId);

    List<Visit> findAllById(Long id);
}
