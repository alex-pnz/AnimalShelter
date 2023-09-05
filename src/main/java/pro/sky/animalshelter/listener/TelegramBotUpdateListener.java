package pro.sky.animalshelter.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.animalshelter.service.MenuService;
import pro.sky.animalshelter.service.MessageService;

import java.util.List;

/**
 * Основной класс, содержащий цикл обработки сообщений
 */
@Service
public class TelegramBotUpdateListener implements UpdatesListener {
    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdateListener.class);

    @Autowired
    private TelegramBot bot;

    @Autowired
    private MenuService menuService;

    @Autowired
    private MessageService messageService;

    @PostConstruct
    public void init() {
        bot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            // пишем обработчики в виде функций void functionName(Update update), вызываем здесь

            if (update.message() != null) { // Меню InlineKeyboard не передает message, поэтому ловим  callback который передаем в callbackData
                Long chatId = update.message().chat().id();
                String command = update.message().text();
                switch (command) {
                    case "/start" -> {
                        menuService.showMainMenu(chatId);
                    }
                    case "/about" -> {
                        messageService.showInfoAboutShelter(chatId);
                    }
                    case "/schedule" -> {
                        //
                    }
                    case "/security" -> {
                        messageService.showSecurityInfo(chatId);
                    }
                    case "/safety" -> {
                        messageService.showSafetyMeasures(chatId);
                    }
                    default -> {
                        defaultHandler(update);
                    }
                }
            } else if (update.callbackQuery() != null) {   // Здесь обрабатываем callback полученный из меню, потом надо добавить другие кейсы из других меню которые сделаем позже
                CallbackQuery callbackQuery = update.callbackQuery();
                String callback = callbackQuery.data();
                SendMessage message = null;
                switch (callback) {
                    case "cat" -> {
                        message = new SendMessage(update.callbackQuery().message().chat().id(), "Test: Попадаем в раздел кошки"); // Для тестирования, потом заменить
                    }
                    case "dog" -> {
                        message = new SendMessage(update.callbackQuery().message().chat().id(), "Test: Попадаем в раздел собаки");//Для тестирования, потом заменить
                    }
                    default -> {
                        defaultHandler(update);
                    }
                }

                bot.execute(message);
            }

        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    /**
     * функция обработки событий, для которых не реализованы специфические
     * обработчики
     * @param update
     */
    private void defaultHandler(Update update) {
        SendMessage message = new SendMessage(update.message().chat().id(),
                "This command is not yet supported");
        SendResponse response = bot.execute(message);
    }
}
