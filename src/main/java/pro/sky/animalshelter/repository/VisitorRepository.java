package pro.sky.animalshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.animalshelter.model.Visitor;

public interface VisitorRepository extends JpaRepository<Visitor, Long> {
    Visitor findByChatId(Long chatId);
}
