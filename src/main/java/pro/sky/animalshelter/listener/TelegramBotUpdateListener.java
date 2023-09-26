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
import pro.sky.animalshelter.service.*;

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
    private final VisitService visitService;
    private final VolunteerService volunteerService;
    private static final String PHONE_NUMBER_EMAIL_REGEXP = "^[\\d].+|^\\+[\\d].+";


    public TelegramBotUpdateListener(TelegramBot bot, MenuService menuService, MessageService messageService,
                                     VisitorService visitorService, ChatWithVolunteer chat,
                                     VisitService visitService, VolunteerService volunteerService) {
        this.bot = bot;
        this.menuService = menuService;
        this.messageService = messageService;
        this.visitorService = visitorService;
        this.chat = chat;
        this.visitService = visitService;
        this.volunteerService = volunteerService;
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
                        case COMMAND_STOP_CHAT -> {
                            chat.stopChat(chatId);
                        }
                        default -> {
                            messageService.defaultHandler(update);
                        }
                    }
                }else if(volunteerService.isVolunteer(chatId) && volunteerService.isAction(chatId)){
                    chat.doAction(chatId, Long.valueOf(text));

                    //проверка находится ли пользователь в чате с волонтером
                } else if (chat.checkVisitor(chatId)) {
                    chat.continueChat(chatId, null, text);
                } else if (chat.checkVolunteer(chatId)) {
                    chat.continueChat(null, chatId, text);
                    //обрабатываем номер телефона и почту
                } else if (text.matches(PHONE_NUMBER_EMAIL_REGEXP) && text.contains("@")) {
                    messageService.saveContactsPhoneNumber(chatId, text);
                    //кнопка "обратно в меню"
                } else {
                    messageService.defaultHandler(update);
                }
            } else if (update.callbackQuery() != null) {   // Здесь обрабатываем callback полученный из меню, потом надо добавить другие кейсы из других меню которые сделаем позже
                CallbackQuery callbackQuery = update.callbackQuery();
                String callback = callbackQuery.data();
                if (callback.equals(CALLBACK_MENU_CAT)||callback.equals(CALLBACK_MENU_DOG)){
                    visitService.addVisit(update);
                }
                Long chatId = callbackQuery.from().id();
                switch (callback) {

                    //MainMenu
                    case CALLBACK_MENU_CAT, CALLBACK_MENU_DOG, CALLBACK_BACK_TO_CHOOSE_SHELTER -> menuService.showShelterMenu(chatId);
                    case CALLBACK_START_VOLUNTEER_SESSION -> {
                        menuService.showVolunteerMenu(chatId);
                        volunteerService.setVolunteerFree(chatId,true);
                    } // Войти как волонтер

                    // VolunteerMenu
                    case CALLBACK_CONTACT_ADOPTER -> {
                        volunteerService.saveAction(chatId,"callVisitor");
                        messageService.sendMessage(chatId,"Введите айди пользователя");
                    } // Связаться с опекуном
                    case CALLBACK_SEND_ADOPTER_WARNING -> {
                        volunteerService.saveAction(chatId,"sendWarningMessage");
                        messageService.sendMessage(chatId,"Введите айди пользователя");
                    } // Отправить предупреждение
                    case CALLBACK_CHANGE_PROBATION_TERMS -> menuService.showProbationTermsMenu(chatId) ; // Изменить состояние испытательного срока
                    case CALLBACK_END_VOLUNTEER_SESSION -> {
                        volunteerService.setVolunteerFree(chatId,false);
                        messageService.sendMessage(chatId,"Смена закрыта");
                    } // Закончить смену

                    //ProbationTermsMenu
                    case CALLBACK_ADD_14_DAYS -> {
                        volunteerService.saveAction(chatId,"add14Days");
                        messageService.sendMessage(chatId,"Введите айди пользователя");
                    }
                    case CALLBACK_ADD_30_DAYS -> {
                        volunteerService.saveAction(chatId,"add30Days");
                        messageService.sendMessage(chatId,"Введите айди пользователя");
                    }
                    case CALLBACK_COMPLETE_PROBATION_TERMS -> {
                        volunteerService.saveAction(chatId,"completeProbationTerms");
                        messageService.sendMessage(chatId,"Введите айди пользователя");
                    }
                    case CALLBACK_FAIL_PROBATION_TERMS -> {
                        volunteerService.saveAction(chatId,"failProbationTerms");
                        messageService.sendMessage(chatId,"Введите айди пользователя");
                    }

                    // ShelterMenu
                    case CALLBACK_SHELTER_INFO_MENU -> menuService.showShelterInfoMenu(chatId);
                    case CALLBACK_ADOPTION_INFO -> menuService.showAnimalAdoptionMenu(chatId, messageService.getShelterType(chatId));
                    //case CALLBACK_SEND_REPORT_TO_VOLUNTEER -> ; // Прислать отчет о питомце
                    case CALLBACK_CALL_VOLUNTEER -> {
                        messageService.showFindVolunteerInfo(chatId);
                        chat.findVolunteer(chatId);
                    }

                    // ShelterInfoMenu
                    case CALLBACK_SHELTER_INFO -> messageService.showInfoAboutShelter(chatId);
                    case CALLBACK_SCHEDULE_ADDRESS -> messageService.showShelterSchedule(chatId);
                    case CALLBACK_SHELTER_ADMISSION -> messageService.showSecurityInfo(chatId);
                    case CALLBACK_SAFETY_RULES -> messageService.showSafetyMeasures(chatId);
                    case CALLBACK_SAVE_VISITOR_CONTACTS -> bot.execute(new SendMessage(chatId,
                            "Напишите одним сообщением Ваш номер телефона и электронную почту:"));
                    case CALLBACK_BACK_TO_MAIN_MENU -> menuService.showMainMenu(chatId);

                    // AnimalAdoptionMenu
                    case CALLBACK_HELLO_PET -> messageService.showPetHelloRules(chatId);
                    case CALLBACK_NECESSARY_PAPERS -> messageService.showListOfDocuments(chatId);
                    case CALLBACK_TRANSPORT_ANIMAL_INFO -> messageService.showPetTransportRules(chatId);
                    case CALLBACK_KITTEN_HOUSE_INFO -> messageService.showKittenPuppyInfo(chatId); // Дом для котенка
                    case CALLBACK_PUPPY_HOUSE_INFO -> messageService.showKittenPuppyInfo(chatId); // Дом для щенка
                    case CALLBACK_CAT_HOUSE_INFO -> messageService.showAdultAnimalInfo(chatId); // Дом для взрослого кота
                    case CALLBACK_DOG_HOUSE_INFO -> messageService.showAdultAnimalInfo(chatId); // Дом для взрослой собаки
                    case CALLBACK_DOG_WHISPERER_INFO -> messageService.showDogWhispererInfo(chatId);
                    case CALLBACK_BEST_KINOLOG_INFO -> messageService.showBestKinologInfo(chatId);
                    case CALLBACK_HANDICAPPED_ANIMAL_HOUSE_INFO -> messageService.houseForAnimalWithDisabilities(chatId); // Дом для животного с ограниченными возможностями
                    case CALLBACK_ADOPTION_REFUSAL_INFO -> messageService.showRefusePolicy(chatId);

                    default -> messageService.defaultHandler(update);

                }
            }

        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    public void sendGenericMessage(Long chatId, String text) {
        SendMessage message = new SendMessage(chatId, text);
        SendResponse response = bot.execute(message);
    }
}
