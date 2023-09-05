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

    @Autowired
    private TelegramBot bot;
    @Autowired
    private VisitorRepository visitorRepository;
    @Autowired
    private VisitRepository visitRepository;
    @Autowired
    private ShelterRepository shelterRepository;
    private SendMessage sendMessage = null;

    /**
     * Выводит данные службы охраны для оформления пропуска на машину
     * @param chatId указать номер чата, в который бот отправит сообщение
     * @return для облегчения процесса тестирования метода, возвращаем объект класса SendResponse
     */
    public SendResponse showSecurityInfo(Long chatId) {
        if (chatId != null && chatId >= 0) {
            sendMessage = new SendMessage(chatId, SECURITY_CONTACT_INFO);
            return bot.execute(sendMessage);
        }
        return null;
    }
    /**
     * Выводит информацию о выбранном ранее приюте
     * @param chatId указать номер чата, в который бот отправит сообщение
     */
    public SendResponse showInfoAboutShelter(Long chatId) {
        AnimalType shelterType = getShelterType(chatId);
        if(shelterType == AnimalType.CAT){
            sendMessage = new SendMessage(chatId, CAT_SHELTER_DESCRIPTION);
            return bot.execute(sendMessage);
        } else if (shelterType == AnimalType.DOG) {
            sendMessage = new SendMessage(chatId, DOG_SHELTER_DESCRIPTION);
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
        sendMessage = new SendMessage(chatId, SHELTER_SCHEDULE);

        return null;
    }
    /**
     * Метод возвращающий тип приюта по chatId
     * @param chatId
     * @return AnimalType
     */
    private AnimalType getShelterType(Long chatId) {
        Visitor visitor = visitorRepository.findByChatId(chatId);
        Visit visit = visitRepository.findByVisitor(visitor);
        return visit.getShelter().getShelterType();
    }


}
