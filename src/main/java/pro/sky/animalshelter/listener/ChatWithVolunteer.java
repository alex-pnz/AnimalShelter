package pro.sky.animalshelter.listener;

import org.springframework.stereotype.Component;
import pro.sky.animalshelter.model.Visitor;
import pro.sky.animalshelter.model.Volunteer;
import pro.sky.animalshelter.repository.VisitorRepository;
import pro.sky.animalshelter.repository.VolunteerRepository;
import pro.sky.animalshelter.service.MessageService;
/**
 * Сущность, отвечающая за взаимодействие пользователя
 * с волонтером
 *
 */
@Component
public class ChatWithVolunteer {
    private final VolunteerRepository volunteerRepository;
    private final VisitorRepository visitorRepository;
    private final MessageService messageService;

    public ChatWithVolunteer(VolunteerRepository volunteerRepository, VisitorRepository visitorRepository, MessageService messageService) {
        this.volunteerRepository = volunteerRepository;
        this.visitorRepository = visitorRepository;
        this.messageService = messageService;
    }
    /**
     * Поиск свободных волонтеров
     *
     * @param chatId
     */
    public void findVolunteer(Long chatId) {
        Visitor visitor = visitorRepository.findByChatId(chatId);
        if (visitor.getVolunteer() != null) {
            messageService.sendMessage(chatId, "Вы уже находитесь в чате с волонтером!");
            return;
        }
        Volunteer volunteer = volunteerRepository.getByIsFree(true);
        if (volunteer != null) {
            startChat(chatId, volunteer);
        } else {
            messageService.sendMessage(chatId, "К сожалению сейчас нет свободных волонтеров.");
        }
    }
    /**
     * Запуск чата между волонтером и пользователем
     *
     * @param chatId
     * @param volunteer
     */
    private void startChat(Long chatId, Volunteer volunteer) {
        Visitor visitor = visitorRepository.findByChatId(chatId);

        volunteer.setFree(false);
        volunteer.setVisitor(visitor);
        volunteerRepository.save(volunteer);

        visitor.setVolunteer(volunteer);
        visitorRepository.save(visitor);

        messageService.sendMessage(chatId, "С вами будет общаться " + volunteer.getFirstName());
        messageService.sendMessage(volunteer.getChatId(),
                "Посетитель нуждается в вашей помощи! Его имя - " + visitor.getVisitorName());

    }
    /**
     * Остановка чата между волонтером и пользователем
     *
     * @param chatId
     */
    public void stopChat(Long chatId) {
        Volunteer volunteer;
        Visitor visitor;
        if (checkVolunteer(chatId)) {
            volunteer = volunteerRepository.findByChatId(chatId);
            visitor = volunteer.getVisitor();
        } else if (checkVisitor(chatId)) {
            visitor = visitorRepository.findByChatId(chatId);
            volunteer = visitor.getVolunteer();
        } else {
            messageService.sendMessage(chatId, "Вы не находитесь в чате с волонтером.");
            return;
        }

        volunteer.setFree(true);
        volunteer.setVisitor(null);
        volunteerRepository.save(volunteer);

        visitor.setVolunteer(null);
        visitorRepository.save(visitor);

        messageService.sendMessage(visitor.getChatId(), "Чат с волонтером завершен");
        messageService.sendMessage(volunteer.getChatId(),
                "Чат с посетителем завершен");

    }
    /**
     * Проверка находится ли волонтер в чате с пользователем
     *
     * @param chatId
     */
    public boolean checkVolunteer(Long chatId) {
        Volunteer volunteer = volunteerRepository.findByChatId(chatId);
        if (volunteer != null) {
            return !volunteer.isFree();
        }
        return false;
    }
    /**
     * Проверка находится ли пользователь в чате с волонтером
     *
     * @param chatId
     */
    public boolean checkVisitor(Long chatId) {
        Visitor visitor = visitorRepository.findByChatId(chatId);
        if (visitor != null) {
            return visitor.getVolunteer() != null;
        }
        return false;
    }
    /**
     * Пересылка сообщений между пользователем и волонтером
     *
     * @param visitorId
     * @param volunteerId
     * @param message
     */
    public void continueChat(Long visitorId, Long volunteerId, String message) {
        //отправка сообщения посетителю
        if (volunteerId == null && visitorId != null) {
            sendMessageToVolunteer(visitorId, message);
            //отправка сообщения волонтеру
        } else if (volunteerId != null && visitorId == null) {
            sendMessageToVisitor(volunteerId, message);
        }
    }
    /**
     * Отправка сообщения волонтеру
     *
     * @param chatId
     */
    private void sendMessageToVolunteer(Long chatId, String message) {
        Visitor visitor = visitorRepository.findByChatId(chatId);
        Volunteer volunteer = visitor.getVolunteer();
        Long volunteerId = volunteer.getChatId();
        messageService.sendMessage(volunteerId, message);
    }
    /**
     * Отправка сообщения пользователю
     *
     * @param chatId
     */
    private void sendMessageToVisitor(Long chatId, String message) {
        Volunteer volunteer = volunteerRepository.findByChatId(chatId);
        Visitor visitor = volunteer.getVisitor();
        Long visitorId = visitor.getChatId();
        messageService.sendMessage(visitorId, message);
    }

}
