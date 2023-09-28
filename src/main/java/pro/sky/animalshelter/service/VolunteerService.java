package pro.sky.animalshelter.service;

import org.springframework.stereotype.Service;
import pro.sky.animalshelter.model.Volunteer;
import pro.sky.animalshelter.model.enums.Action;
import pro.sky.animalshelter.repository.VolunteerRepository;

/**
 * Сервис, отвечающий за работу с волонтерами
 */
@Service
public class VolunteerService {
    private final VolunteerRepository volunteerRepository;

    public VolunteerService(VolunteerRepository volunteerRepository) {
        this.volunteerRepository = volunteerRepository;
    }

    public Volunteer createVolunteer(Volunteer volunteer) {
        return volunteerRepository.save(volunteer);
    }

    public Volunteer findVolunteer(long id) {
        return volunteerRepository.findById(id).get();
    }

    public Volunteer editVolunteer(Volunteer volunteer) {
        return volunteerRepository.save(volunteer);
    }

    public void deleteVolunteer(long id) {
        volunteerRepository.deleteById(id);
    }

    /**
     * Изменяет статус волонтера
     *
     * @param chatId
     * @param state
     */
    public void setVolunteerFree(Long chatId, boolean state) {
        Volunteer volunteer = volunteerRepository.findByChatId(chatId);
        volunteer.setFree(state);
        volunteerRepository.save(volunteer);
    }

    /**
     * Проверяет является ли пользователь волонтером
     *
     * @param chatId
     */
    public boolean isVolunteer(Long chatId) {
        Volunteer volunteer = volunteerRepository.findByChatId(chatId);
        return volunteer != null;
    }

    /**
     * Сохраняет действие волонтера
     *
     * @param chatId
     * @param action
     */
    public void saveAction(Long chatId, Action action) {
        Volunteer volunteer = volunteerRepository.findByChatId(chatId);
        volunteer.setAction(action);
        volunteerRepository.save(volunteer);
    }

    /**
     * Проверяет выполняет ли волонтер действие в данный момент
     *
     * @param chatId
     */
    public boolean isAction(Long chatId){
        Volunteer volunteer = volunteerRepository.findByChatId(chatId);
        return volunteer.getAction() != null;
    }
}
