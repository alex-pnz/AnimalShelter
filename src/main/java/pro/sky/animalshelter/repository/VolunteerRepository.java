package pro.sky.animalshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.animalshelter.model.Volunteer;
/**
 * Класс, хранящий данные о волонтерах
 */
public interface VolunteerRepository extends JpaRepository<Volunteer,Long> {
    Volunteer getByIsFree(boolean isFree);
    Volunteer findByChatId(Long chatId);
}
