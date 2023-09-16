package pro.sky.animalshelter.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.animalshelter.listener.ChatWithVolunteer;
import pro.sky.animalshelter.model.AnimalType;
import pro.sky.animalshelter.model.Shelter;
import pro.sky.animalshelter.model.Visit;
import pro.sky.animalshelter.model.Visitor;
import pro.sky.animalshelter.repository.ShelterRepository;
import pro.sky.animalshelter.repository.VisitRepository;
import pro.sky.animalshelter.repository.VisitorRepository;

import java.time.LocalTime;

import static pro.sky.animalshelter.utils.Constants.*;

@Service
public class MessageService {
    private final Logger logger = LoggerFactory.getLogger(MessageService.class);

    private final TelegramBot bot;
    private final VisitorRepository visitorRepository;
    private final VisitService visitService;
    private final ShelterRepository shelterRepository;
    private final MenuService menuService;

    public MessageService(TelegramBot bot, VisitorRepository visitorRepository, VisitService visitService,
                          ShelterRepository shelterRepository, MenuService menuService) {
        this.bot = bot;
        this.visitorRepository = visitorRepository;
        this.visitService = visitService;
        this.shelterRepository = shelterRepository;
        this.menuService = menuService;
    }

    /**
     * Выводит данные службы охраны для оформления пропуска на машину
     * @param chatId указать номер чата, в который бот отправит сообщение
     * @return для облегчения процесса тестирования метода, возвращаем объект класса SendResponse
     */
    public SendResponse showSecurityInfo(Long chatId) {
        if (chatId != null && chatId >= 0) {
            SendMessage sendMessage = new SendMessage(chatId, SECURITY_CONTACT_INFO);
            return bot.execute(sendMessage);
        }
        return null;
    }
    /**
     * Выводит информацию о выбранном ранее приюте
     * @param chatId указать номер чата, в который бот отправит сообщение
     */
    public SendResponse showInfoAboutShelter(Long chatId) {
        SendMessage sendMessage;
        AnimalType shelterType = getShelterType(chatId);
        if(shelterType == AnimalType.CAT){
            sendMessage = new SendMessage(chatId, CAT_SHELTER_DESCRIPTION);
            sendMessage.replyMarkup(menuService.setHelpButton(chatId));
            return bot.execute(sendMessage);
        } else if (shelterType == AnimalType.DOG) {
            sendMessage = new SendMessage(chatId, DOG_SHELTER_DESCRIPTION);
            sendMessage.replyMarkup(menuService.setHelpButton(chatId));
            return bot.execute(sendMessage);
        }
        return null;
    }
    /**
     * Выводит информацию о расписании в выбранном приюте
     *
     * @param chatId указать номер чата, в который бот отправит сообщение
     */
    public SendResponse showShelterSchedule(Long chatId) {
        SendMessage sendMessage = new SendMessage(chatId, SHELTER_SCHEDULE);

        return bot.execute(sendMessage);
    }
    /**
     * Метод возвращающий тип приюта по chatId
     * @param chatId
     * @return AnimalType
     */
    private AnimalType getShelterType(Long chatId) {
        Visitor visitor = visitorRepository.findByChatId(chatId);
//        logger.info("Visitor: {} for chat id {}", visitor.getId(), visitor.getChatId());
        Visit visit = visitService.getCurrentVisitByVisitorId(visitor);
//        logger.info("Visit {} to shelter {}", visit.getId(), visit.getShelter());
        return visit.getShelter().getShelterType();
    }

    public SendResponse showSafetyMeasures(Long chatId) {
        SendMessage sendMessage;
        AnimalType shelterType = getShelterType(chatId);
        if (shelterType == AnimalType.CAT) {
            sendMessage = new SendMessage(chatId, CAT_SHELTER_SAFETY);
            return bot.execute(sendMessage);
        } else if (shelterType == AnimalType.DOG) {
            sendMessage = new SendMessage(chatId, DOG_SHELTER_SAFETY);
            return bot.execute(sendMessage);
        }
        return null;
    }

    /**
     * Выводит сообщение со всеми доступными командами
     * @param chatId указать номер чата, в который бот отправит сообщение
     */
    public SendResponse showHelp(Long chatId) {
        SendMessage sendMessage = new SendMessage(chatId, SHELTER_HELP);
        return bot.execute(sendMessage);
    }

    /**
     * Проверяет поступающий в message номер телефона с помощью регулярного выражения
     * и сохраняет его в базу данных
     * @param chatId указать номер чата, в который бот отправит сообщение
     * @param message передает номер телефона для обработки
     */
    public SendResponse saveContactsPhoneNumber(Long chatId, String message){
        message = message.replaceAll(" |\\(|\\)|\\+","");

        if (message.matches("[0-9]{11}") && message.matches("^[78].+")){
            if(message.startsWith("8")) message = message.replaceFirst("8","7");
            Visitor visitor = visitorRepository.findByChatId(chatId);
            visitor.setPhoneNumber("+" + message);
            visitorRepository.save(visitor);
            return bot.execute(new SendMessage(chatId, "Номер " + visitor.getPhoneNumber() + " сохранен!"));
        }

        return bot.execute(new SendMessage(chatId, "К сожалению бот не смог распознать номер телефона. " +
                "(/add_contacts - попробовать ввести еще раз)"));
    }

    /**
     * Проверяет поступающий в message адрес электронной почты с помощью регулярного выражения
     * и сохраняет его в базу данных
     * @param chatId указать номер чата, в который бот отправит сообщение
     * @param message передает адрес электронной почты для обработки
     */
    public SendResponse saveContactsEmail(Long chatId, String message){
        message = message.trim();

        if (message.matches("[\\w-.]+@[\\w-]+\\.[a-z0-9]+")){
            Visitor visitor = visitorRepository.findByChatId(chatId);
            visitor.setEmail(message);
            visitorRepository.save(visitor);
            return bot.execute(new SendMessage(chatId, "Электронная почта " + visitor.getEmail() + " сохранена!"));
        }
        return bot.execute(new SendMessage(chatId, "К сожалению бот не смог распознать адрес электронной почты. " +
                "(/add_contacts - попробовать ввести еще раз)"));
    }

    /**
     * Запускает поиск волонтера и информирует об этом пользователя
     * @param chatId указать номер чата, в который бот отправит сообщение
     */
    public SendResponse showFindVolunteerInfo(Long chatId) {
        SendMessage sendMessage = new SendMessage(chatId, "Идет поиск волонтера");
        return bot.execute(sendMessage);
    }
    /**
     * Отправляет сообщение в указанный чат
     * @param chatId указать номер чата, в который бот отправит сообщение
     * @param message сообщение, которое будет отправлено
     */
    public SendResponse sendMessage(Long chatId, String message) {
        SendMessage sendMessage = new SendMessage(chatId, message);
        return bot.execute(sendMessage);
    }
}
