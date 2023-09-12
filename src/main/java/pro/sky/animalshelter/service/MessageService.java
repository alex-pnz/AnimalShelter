package pro.sky.animalshelter.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    private final TelegramBot bot;
    private final VisitorRepository visitorRepository;
    private final VisitRepository visitRepository;
    private final ShelterRepository shelterRepository;
    private final MenuService menuService;

    public MessageService(TelegramBot bot, VisitorRepository visitorRepository, VisitRepository visitRepository,
                          ShelterRepository shelterRepository, MenuService menuService) {
        this.bot = bot;
        this.visitorRepository = visitorRepository;
        this.visitRepository = visitRepository;
        this.shelterRepository = shelterRepository;
        this.menuService = menuService;
    }

    /**
     * Выводит данные службы охраны для оформления пропуска на машину
     *
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
     *
     * @param chatId указать номер чата, в который бот отправит сообщение
     */
    public SendResponse showInfoAboutShelter(Long chatId) {
        SendMessage sendMessage;
        AnimalType shelterType = getShelterType(chatId);
        if (shelterType == AnimalType.CAT) {
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

        return null;
    }

    /**
     * Метод возвращающий тип приюта по chatId
     *
     * @param chatId
     * @return AnimalType
     */
    private AnimalType getShelterType(Long chatId) {
//        Visitor visitor = visitorRepository.findByChatId(chatId);
//        Visit visit = visitRepository.findByVisitor(visitor);
//        return visit.getShelter().getShelterType();
        return AnimalType.CAT;
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
     *
     * @param chatId указать номер чата, в который бот отправит сообщение
     */
    public SendResponse showHelp(Long chatId) {
        SendMessage sendMessage = new SendMessage(chatId, SHELTER_HELP);
        return bot.execute(sendMessage);
    }

    /**
     * Выводит информацию о правилах знакомства с животным
     *
     * @param chatId указать номер чата, в который бот отправит сообщение
     */
    public SendResponse showPetHelloRules(Long chatId) {
        SendMessage sendMessage = new SendMessage(chatId, PET_HELLO);

        return null;
    }
    /**
     * Выводит информацию о правилах перевозки животных
     *
     * @param chatId указать номер чата, в который бот отправит сообщение
     */
    public SendResponse showPetTransportRules(Long chatId) {
        SendMessage sendMessage = new SendMessage(chatId, PET_TRANSPORT);

        return null;
    }
    /**
     * Выводит информацию о причинах отказа в усыновлении
     *
     * @param chatId указать номер чата, в который бот отправит сообщение
     */
    public SendResponse showRefusePolicy(Long chatId) {
        SendMessage sendMessage = new SendMessage(chatId, REFUSE_POLICY);

        return null;
    }

    /**
     * Выводит советы кинолога для собак
     *
     * @param chatId указать номер чата, в который бот отправит сообщение
     */
    public SendResponse showDogWhispererInfo(Long chatId) {
        SendMessage sendMessage;
        AnimalType shelterType = getShelterType(chatId);
        if (shelterType == AnimalType.CAT) {
            sendMessage = new SendMessage(chatId, PET_HELLO);
            return bot.execute(sendMessage);
        } else if (shelterType == AnimalType.DOG) {
            sendMessage = new SendMessage(chatId, HOW_TO_HELLO_DOG);
            sendMessage.replyMarkup(menuService.setHelpButton(chatId));
            return bot.execute(sendMessage);
        }
        return null;
    }
    /**
     * Выводит контакт проверенного кинолога для собак
     *
     * @param chatId указать номер чата, в который бот отправит сообщение
     */
    public SendResponse showBestKinologInfo(Long chatId) {
        SendMessage sendMessage;
        AnimalType shelterType = getShelterType(chatId);
        if (shelterType == AnimalType.CAT) {
            return null;
        } else if (shelterType == AnimalType.DOG) {
            sendMessage = new SendMessage(chatId, KINOLOG_INFO);
            sendMessage.replyMarkup(menuService.setHelpButton(chatId));
            return bot.execute(sendMessage);
        }
        return null;
    }
}
