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
import org.springframework.stereotype.Service;
import pro.sky.animalshelter.model.Visitor;
import pro.sky.animalshelter.service.MenuService;
import pro.sky.animalshelter.service.MessageService;
import pro.sky.animalshelter.service.VisitorService;

import java.util.List;

import static pro.sky.animalshelter.utils.Constants.*;

/**
 * Основной класс, содержащий цикл обработки сообщений
 */
@Service
public class TelegramBotUpdateListener implements UpdatesListener {
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdateListener.class);

    private final TelegramBot bot;
    private final MenuService menuService;
    private final MessageService messageService;
    private final VisitorService visitorService;
    private final ChatWithVolunteer chat;

    public TelegramBotUpdateListener(TelegramBot bot, MenuService menuService, MessageService messageService,
                                     VisitorService visitorService, ChatWithVolunteer chat) {
        this.bot = bot;
        this.menuService = menuService;
        this.messageService = messageService;
        this.visitorService = visitorService;
        this.chat = chat;
    }

    @PostConstruct
    public void init() {
        bot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            // пишем обработчики в виде функций void functionName(Update update), вызываем здесь

            // создаем нового посетителя, если таковой раньше не заходил
            Visitor currentVisitor = visitorService.getVisitor(update);

            if (update.message() != null) { // Меню InlineKeyboard не передает message, поэтому ловим  callback который передаем в callbackData
                Long chatId = update.message().chat().id();
                String text = update.message().text();
                if (text.startsWith("/")) {
                    switch (text) {
                        case COMMAND_START -> {
                            menuService.showMainMenu(chatId);
                        }
                        case COMMAND_ABOUT -> {
                            messageService.showInfoAboutShelter(chatId);
                        }
                        case COMMAND_SCHEDULE -> {
                            messageService.showShelterSchedule(chatId);
                        }
                        case COMMAND_SECURITY -> {
                            messageService.showSecurityInfo(chatId);
                        }
                        case COMMAND_SAFETY -> {
                            messageService.showSafetyMeasures(chatId);
                        }
                        case COMMAND_VOLUNTEER -> {
                            messageService.showFindVolunteerInfo(chatId);
                            chat.findVolunteer(chatId);
                        }
                        case COMMAND_HELP -> {
                            messageService.showHelp(chatId);
                        }
                        case COMMAND_STOP_CHAT -> {
                            chat.stopChat(chatId);
                        }
                        default -> {
                            defaultHandler(update);
                        }
                    }
                    //проверка находится ли пользователь в чате с волонтером
                } else if (chat.checkVisitor(chatId)) {
                    chat.continueChat(chatId, null, text);
                } else if (chat.checkVolunteer(chatId)) {
                    chat.continueChat(null, chatId, text);
                }
            } else if (update.callbackQuery() != null) {   // Здесь обрабатываем callback полученный из меню, потом надо добавить другие кейсы из других меню которые сделаем позже
                CallbackQuery callbackQuery = update.callbackQuery();
                Long chatId = callbackQuery.from().id();

                SendResponse sendResponse = messageService.showInfoAboutShelter(chatId);
            }

        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    /**
     * функция обработки событий, для которых не реализованы специфические
     * обработчики
     *
     * @param update
     */
    private void defaultHandler(Update update) {
        SendMessage message = new SendMessage(update.message().chat().id(),
                "This command is not yet supported");
        SendResponse response = bot.execute(message);
    }
}
