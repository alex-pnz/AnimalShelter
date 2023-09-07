package pro.sky.animalshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.animalshelter.model.Visitor;

/**
 * Класс, хранящий данные о посетителе приюта
 */
public interface VisitorRepository extends JpaRepository<Visitor, Long> {
    Visitor findByChatId(Long chatId);
}
