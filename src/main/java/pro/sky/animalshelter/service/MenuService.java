package pro.sky.animalshelter.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.springframework.stereotype.Service;
import pro.sky.animalshelter.model.enums.AnimalType;

import static pro.sky.animalshelter.utils.Constants.*;

@Service
public class MenuService {

    private final TelegramBot bot;
    private final VolunteerService volunteerService;

    public MenuService(TelegramBot bot, VolunteerService volunteerService) {
        this.bot = bot;
        this.volunteerService = volunteerService;
    }

    /**
     * Выводит главное меню (Кошки/Собаки) по команде /start
     *
     * @param chatId указать номер чата, в который бот отправит сообщение
     * @return для облегчения процесса тестирования метода, возвращаем объект класса SendResponse
     */
    public SendResponse showMainMenu(Long chatId) {
        if (chatId != null && chatId >= 0) {
            SendMessage sendMessage = new SendMessage(chatId, "Правет Друг! Выбери приют:");
            if(volunteerService.isVolunteer(chatId)){
            InlineKeyboardButton[][] inlineKeyboardButtons = {
                    {new InlineKeyboardButton("\uD83D\uDC08 Кошки").callbackData(CALLBACK_MENU_CAT),
                            new InlineKeyboardButton("\uD83D\uDC15 Собаки").callbackData(CALLBACK_MENU_DOG)},
                    {new InlineKeyboardButton("Войти как волонтер").callbackData("startVolunteerSession")}
            };
            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup(inlineKeyboardButtons);
            sendMessage.replyMarkup(markupInline);
            return bot.execute(sendMessage);
            }else{
                InlineKeyboardButton[][] inlineKeyboardButtons = {
                        {new InlineKeyboardButton("\uD83D\uDC08 Кошки").callbackData(CALLBACK_MENU_CAT),
                                new InlineKeyboardButton("\uD83D\uDC15 Собаки").callbackData(CALLBACK_MENU_DOG)}
                };
                InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup(inlineKeyboardButtons);
                sendMessage.replyMarkup(markupInline);
                return bot.execute(sendMessage);
            }
        }
        return null;
    }

    /**
     * Выводит меню опций доступных для волонтера
     *
     * @param chatId указать номер чата, в который бот отправит сообщение
     * @return для облегчения процесса тестирования метода, возвращаем объект класса SendResponse
     */
    public SendResponse showVolunteerMenu(Long chatId) {
        if (chatId != null && chatId >= 0) {
            SendMessage sendMessage = new SendMessage(chatId, "Меню волонтера");

            InlineKeyboardButton[][] inlineKeyboardButtons = {
                    {new InlineKeyboardButton("Связаться с опекуном").callbackData(CALLBACK_CONTACT_ADOPTER)},
                    {new InlineKeyboardButton("Отправить предупреждение").callbackData(CALLBACK_SEND_ADOPTER_WARNING)},
                    {new InlineKeyboardButton("Изменить состояние испытательного срока").callbackData(CALLBACK_CHANGE_PROBATION_TERMS)},
                    {new InlineKeyboardButton("Закончить смену").callbackData(CALLBACK_END_VOLUNTEER_SESSION)}
            };
            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup(inlineKeyboardButtons);
            sendMessage.replyMarkup(markupInline);
            return bot.execute(sendMessage);
        }
        return null;
    }

    /**
     * Выводит меню опций после выбора приюта
     *
     * @param chatId указать номер чата, в который бот отправит сообщение
     * @return для облегчения процесса тестирования метода, возвращаем объект класса SendResponse
     */
    public SendResponse showShelterMenu(Long chatId) {
        if (chatId != null && chatId >= 0) {
            SendMessage sendMessage = new SendMessage(chatId, "Меню приюта");

            InlineKeyboardButton[][] inlineKeyboardButtons = {
                    {new InlineKeyboardButton("Узнать информацию о приюте").callbackData(CALLBACK_SHELTER_INFO_MENU)},
                    {new InlineKeyboardButton("Как взять животное из приюта").callbackData(CALLBACK_ADOPTION_INFO)},
                    {new InlineKeyboardButton("Прислать отчет о питомце").callbackData(CALLBACK_SEND_REPORT_TO_VOLUNTEER)},
                    {new InlineKeyboardButton("Позвать волонтера").callbackData(CALLBACK_CALL_VOLUNTEER)}
            };
            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup(inlineKeyboardButtons);
            sendMessage.replyMarkup(markupInline);
            return bot.execute(sendMessage);
        }
        return null;
    }

    /**
     * Выводит меню выбора различной информации по приюту
     *
     * @param chatId указать номер чата, в который бот отправит сообщение
     * @return для облегчения процесса тестирования метода, возвращаем объект класса SendResponse
     */
    public SendResponse showShelterInfoMenu(Long chatId) {
        if (chatId != null && chatId >= 0) {
            SendMessage sendMessage = new SendMessage(chatId, "Информация по приюту");

            InlineKeyboardButton[][] inlineKeyboardButtons = {
                    {new InlineKeyboardButton("О приюте").callbackData(CALLBACK_SHELTER_INFO),
                            new InlineKeyboardButton("Расписание, адрес").callbackData(CALLBACK_SCHEDULE_ADDRESS)},
                    {new InlineKeyboardButton("Пропуск в приют").callbackData(CALLBACK_SHELTER_ADMISSION),
                            new InlineKeyboardButton("Техника безопасности").callbackData(CALLBACK_SAFETY_RULES)},
                    {new InlineKeyboardButton("Оставить контактные данные").callbackData(CALLBACK_SAVE_VISITOR_CONTACTS),
                            new InlineKeyboardButton("Позвать волонтера").callbackData(CALLBACK_CALL_VOLUNTEER)},
                    {new InlineKeyboardButton("Возврат к выбору приюта").callbackData(CALLBACK_BACK_TO_MAIN_MENU)}
            };
            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup(inlineKeyboardButtons);
            sendMessage.replyMarkup(markupInline);
            return bot.execute(sendMessage);
        }
        return null;
    }

    /**
     * Выводит меню выбора различной информации о том как взять и заботиться о животном
     *
     * @param chatId указать номер чата, в который бот отправит сообщение
     * @return для облегчения процесса тестирования метода, возвращаем объект класса SendResponse
     */
    public SendResponse showAnimalAdoptionMenu(Long chatId, AnimalType shelterType) {
        if (chatId != null && chatId >= 0) {

            SendMessage sendMessage = new SendMessage(chatId, "Информация о том как взять и заботиться о " +
                    (shelterType == AnimalType.CAT ? "кошке" : "собаке"));

            InlineKeyboardButton[] inlineKeyboardButtonsTemp1 = new InlineKeyboardButton[2]; // Кнопки в меню которые будут меняться в зависимости от типа приюта
            InlineKeyboardButton[] inlineKeyboardButtonsTemp2 = null; // Кнопки в меню которые будут меняться в зависимости от типа приюта

            if (shelterType == AnimalType.CAT) {

                inlineKeyboardButtonsTemp1[0] = new InlineKeyboardButton("Дом для котенка").callbackData("kittenHouseInfo");
                inlineKeyboardButtonsTemp1[1] = new InlineKeyboardButton("Дом для взрослого кота").callbackData("catHouseInfo");
                inlineKeyboardButtonsTemp2 = new InlineKeyboardButton[0];
            }
            if (shelterType == AnimalType.DOG){
                inlineKeyboardButtonsTemp1[0] = new InlineKeyboardButton("Дом для щенка").callbackData("puppyHouseInfo");
                inlineKeyboardButtonsTemp1[1] = new InlineKeyboardButton("Дом для взрослой собаки").callbackData("dogHouseInfo");
                inlineKeyboardButtonsTemp2 = new InlineKeyboardButton[2];
                inlineKeyboardButtonsTemp2[0] = new InlineKeyboardButton("Cоветы от кинологов").callbackData("showDogWhispererInfo");
                inlineKeyboardButtonsTemp2[1] = new InlineKeyboardButton("Связь с кинологом").callbackData("showBestKinologInfo");

            }

            InlineKeyboardButton[][] inlineKeyboardButtons = {
                    {new InlineKeyboardButton("Знакомство с животным").callbackData(CALLBACK_HELLO_PET),
                            new InlineKeyboardButton("Необходимые документы").callbackData(CALLBACK_NECESSARY_PAPERS)},
                    {new InlineKeyboardButton("Транспортировка животного").callbackData(CALLBACK_TRANSPORT_ANIMAL_INFO)},

                    inlineKeyboardButtonsTemp1,
                    inlineKeyboardButtonsTemp2,

                    {new InlineKeyboardButton("Дом для животного с ограниченными возможностями").callbackData("handicappedAnimalHouseInfo")},
                    {new InlineKeyboardButton("Причины отказа в выдаче животного").callbackData("adoptionRefusalInfo"),
                            new InlineKeyboardButton("Оставить контактные данные").callbackData("saveVisitorContacts")},
                    {new InlineKeyboardButton("Позвать волонтера").callbackData("callVolunteer"),
                            new InlineKeyboardButton("Возврат к выбору приюта").callbackData("backToMainMenu")},

            };
            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup(inlineKeyboardButtons);
            sendMessage.replyMarkup(markupInline);
            return bot.execute(sendMessage);
        }
        return null;
    }

    /**
     * Выводит меню выбора действия волонтера
     *
     * @param chatId указать номер чата, в который бот отправит сообщение
     * @return для облегчения процесса тестирования метода, возвращаем объект класса SendResponse
     */
    public SendResponse showProbationTermsMenu(Long chatId) {
        if (chatId != null && chatId >= 0) {
            SendMessage sendMessage = new SendMessage(chatId, "Выберите действие");

            InlineKeyboardButton[][] inlineKeyboardButtons = {
                    {new InlineKeyboardButton("Добавить 30 дней").callbackData(CALLBACK_ADD_30_DAYS)},
                    {new InlineKeyboardButton("Добавить 14 дней").callbackData(CALLBACK_ADD_14_DAYS)},
                    {new InlineKeyboardButton("Завершить испытательный срок").callbackData(CALLBACK_FAIL_PROBATION_TERMS)},
                    {new InlineKeyboardButton("Завалить испытательный срок").callbackData(CALLBACK_COMPLETE_PROBATION_TERMS)}
            };
            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup(inlineKeyboardButtons);
            sendMessage.replyMarkup(markupInline);
            return bot.execute(sendMessage);
        }
        return null;
    }
}
