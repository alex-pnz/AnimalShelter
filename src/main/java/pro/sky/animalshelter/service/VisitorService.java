package pro.sky.animalshelter.service;

import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Service;
import pro.sky.animalshelter.model.Visitor;
import pro.sky.animalshelter.repository.VisitorRepository;

/**
 * Сервис, отвечающий за работу с посетителями
 */
@Service
public class VisitorService {
    private final VisitorRepository repository;

    public VisitorService(VisitorRepository repository) {
        this.repository = repository;
    }

    /**
     * Метод, получающий посетителя
     * при повторном посещении - получаем посетителя из базы данных
     * при первичном - создаем нового и помещаем в БД
     * @param update
     * @return Visitor
     */
    public Visitor getVisitor(Update update) {
        // ищем посетителя в базе данных
        Visitor visitor = findVisitor(update);

        // если в БД его нет - создаем нового
        if (visitor == null) {
            visitor = createVisitor(update);
        }

        return visitor;
    }

    public Visitor getVisitorByChatId(Long chatId) {
        return repository.findByChatId(chatId);
    }

    /**
     * Метод ищет постетителя в БД через репозиторий
     * @param update
     * @return Visitor
     */
    public Visitor findVisitor(Update update) {
        // при получении сообщения достаем chatId из message
        // при нажатии на кнопку - из callbackQuery
        var chatId = update.message() != null
                ? update.message().chat().id()
                : update.callbackQuery().from().id();
        return repository.findByChatId(chatId);
    }

    /**
     * Метод создает нового пользователя из данных, имеющихся в телеге
     * поэтому емейл и телефон - упс :(
     * могут добавляться через веб-интерфейс волонтером при визите в приют
     * @param update
     * @return Visitor
     */
    public Visitor createVisitor(Update update) {
        // при создании нового посетителя сохраняем его в БД
        Visitor visitor = new Visitor();

        // при получении сообщения достаем chatId из message
        // при нажатии на кнопку - из callbackQuery
        var chatId = update.message() != null
                ? update.message().chat().id()
                : update.callbackQuery().from().id();
        visitor.setChatId(chatId);

        var name = update.message() != null
                ? update.message().chat().firstName() + " " + update.message().chat().lastName()
                : update.callbackQuery().from().firstName() + " " + update.callbackQuery().from().lastName();
        visitor.setVisitorName(name);

        return repository.save(visitor);
    }

    public Visitor addVisitor(Visitor visitor) {
        return repository.save(visitor);
    }
}
