package pro.sky.animalshelter.listener;

import com.pengrad.telegrambot.response.SendResponse;
import org.springframework.stereotype.Component;
import pro.sky.animalshelter.model.Adoption;
import pro.sky.animalshelter.model.Visitor;
import pro.sky.animalshelter.model.Volunteer;
import pro.sky.animalshelter.model.enums.Action;
import pro.sky.animalshelter.model.enums.ProbationTermsStatus;
import pro.sky.animalshelter.repository.AdoptionRepository;
import pro.sky.animalshelter.repository.VisitorRepository;
import pro.sky.animalshelter.repository.VolunteerRepository;
import pro.sky.animalshelter.service.MessageService;
import pro.sky.animalshelter.service.VolunteerService;

import static pro.sky.animalshelter.utils.Constants.FAIL_PROBATION_TERMS_MESSAGE;
import static pro.sky.animalshelter.utils.Constants.WARNING_MESSAGE;

/**
 * Сущность, отвечающая за взаимодействие пользователя
 * с волонтером
 */
@Component
public class ChatWithVolunteer {
    private final VolunteerRepository volunteerRepository;
    private final VisitorRepository visitorRepository;
    private final MessageService messageService;
    private final VolunteerService volunteerService;
    private final AdoptionRepository adoptionRepository;

    public ChatWithVolunteer(VolunteerRepository volunteerRepository, VisitorRepository visitorRepository, MessageService messageService, VolunteerService volunteerService, AdoptionRepository adoptionRepository) {
        this.volunteerRepository = volunteerRepository;
        this.visitorRepository = visitorRepository;
        this.messageService = messageService;
        this.volunteerService = volunteerService;
        this.adoptionRepository = adoptionRepository;
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

        messageService.sendMessage(chatId, "С вами будет общаться " + volunteer.getFirstName() + "\nЧтобы завершить в чат введите команду /stopChat");
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
        if (volunteer != null && volunteer.getVisitor() != null) {
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
    /**
     * Метод отвечающий за выполнение команд волонтера
     *
     * @param volunteerChatId
     * @param visitorChatId
     */
    public SendResponse doAction(Long volunteerChatId, String visitorChatId) {
        String text = "Некорректный id пользователя, попробуйте еще раз.";
        if(isNumeric(visitorChatId)) {
            Volunteer volunteer = volunteerRepository.findByChatId(volunteerChatId);
            Adoption adoption = findAdoption(Long.valueOf(visitorChatId));
            Action action = volunteer.getAction();

            switch (action) {
                case ADD_30_DAYS -> {
                    int days = adoption.getReportsRequired();
                    adoption.setReportsRequired(days + 30);
                    text = "К вашему испытательному сроку было добавлено 30 дней.";
                }
                case ADD_14_DAYS -> {
                    int days = adoption.getReportsRequired();
                    adoption.setReportsRequired(days + 14);
                    text = "К вашему испытательному сроку было добавлено 14 дней.";
                }
                case FAIL_PROBATION_TERMS -> {
                    adoption.setStatus(ProbationTermsStatus.FAIL);
                    text = FAIL_PROBATION_TERMS_MESSAGE;

                }
                case COMPLETE_PROBATION_TERMS -> {
                    adoption.setStatus(ProbationTermsStatus.COMPLETE);
                    text = "Поздравляем! Вы успешно завершили испытательный срок";
                }
                case SEND_WARNING_MESSAGE -> {
                    text = WARNING_MESSAGE;
                }
                case CALL_VISITOR -> {
                    startChat(Long.valueOf(visitorChatId), volunteer);
                }
            }
            adoptionRepository.save(adoption);
            volunteerService.saveAction(volunteerChatId, null);
            return messageService.sendMessage(Long.valueOf(visitorChatId), text);
        }
        return messageService.sendMessage(volunteerChatId, text);
    }
    /**
     * Метод находит информацию об усыновлении по его chatId
     *
     * @param chatId
     * @return Adoption
     */
    public Adoption findAdoption(Long chatId) {
        Visitor visitor = visitorRepository.findByChatId(chatId);
        Long visitorId = visitor.getId();
        return adoptionRepository.findByVisitorId(visitorId);
    }

    /**
     * Проверяет является ли строка числом
     *
     * @param str
     */
    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

}
