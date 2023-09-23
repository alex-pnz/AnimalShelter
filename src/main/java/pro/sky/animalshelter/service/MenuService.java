package pro.sky.animalshelter.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.springframework.stereotype.Service;
import pro.sky.animalshelter.model.AnimalType;

@Service
public class MenuService {

    private final TelegramBot bot;

    public MenuService(TelegramBot bot) {
        this.bot = bot;
    }

    /**
     * Выводит главное меню (Кошки/Собаки) по команде /start
     * @param chatId указать номер чата, в который бот отправит сообщение
     * @return для облегчения процесса тестирования метода, возвращаем объект класса SendResponse
     */
    public SendResponse showMainMenu(Long chatId) {
        if (chatId != null && chatId >= 0) {
            SendMessage sendMessage = new SendMessage(chatId,"Правет Друг! Выбери приют:");

            InlineKeyboardButton[][] inlineKeyboardButtons = {
                    {new InlineKeyboardButton("\uD83D\uDC08 Кошки").callbackData("cat"),
                     new InlineKeyboardButton("\uD83D\uDC15 Собаки").callbackData("dog")},
                    {new InlineKeyboardButton("Войти как волонтер").callbackData("startVolunteerSession")}
            };
            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup(inlineKeyboardButtons);
            sendMessage.replyMarkup(markupInline);
            return bot.execute(sendMessage);
        }
        return null;
    }
    /**
     * Выводит меню опций доступных для волонтера
     * @param chatId указать номер чата, в который бот отправит сообщение
     * @return для облегчения процесса тестирования метода, возвращаем объект класса SendResponse
     */
    public SendResponse showVolunteerMenu(Long chatId) {
        if (chatId != null && chatId >= 0) {
            SendMessage sendMessage = new SendMessage(chatId,"Меню волонтера");

            InlineKeyboardButton[][] inlineKeyboardButtons = {
                    {new InlineKeyboardButton("Связаться с опекуном").callbackData("contactAdopter")},
                    {new InlineKeyboardButton("Отправить предупреждение").callbackData("sendAdopterWarning")},
                    {new InlineKeyboardButton("Изменить состояние испытательного срока").callbackData("changeProbationTerms")},
                    {new InlineKeyboardButton("Закончить смену").callbackData("endVolunteerSession")}
            };
            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup(inlineKeyboardButtons);
            sendMessage.replyMarkup(markupInline);
            return bot.execute(sendMessage);
        }
        return null;
    }

    /**
     * Выводит меню опций после выбора приюта
     * @param chatId указать номер чата, в который бот отправит сообщение
     * @return для облегчения процесса тестирования метода, возвращаем объект класса SendResponse
     */
    public SendResponse showShelterMenu(Long chatId) {
        if (chatId != null && chatId >= 0) {
            SendMessage sendMessage = new SendMessage(chatId,"Меню приюта");

            InlineKeyboardButton[][] inlineKeyboardButtons = {
                    {new InlineKeyboardButton("Узнать информацию о приюте").callbackData("shelterInfoMenu")},
                    {new InlineKeyboardButton("Как взять животное из приюта").callbackData("adoptionInfo")},
                    {new InlineKeyboardButton("Прислать отчет о питомце").callbackData("sendReportToVolunteer")},
                    {new InlineKeyboardButton("Позвать волонтера").callbackData("callVolunteer")}
            };
            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup(inlineKeyboardButtons);
            sendMessage.replyMarkup(markupInline);
            return bot.execute(sendMessage);
        }
        return null;
    }

    /**
     * Выводит меню выбора различной информации по приюту
     * @param chatId указать номер чата, в который бот отправит сообщение
     * @return для облегчения процесса тестирования метода, возвращаем объект класса SendResponse
     */
    public SendResponse showShelterInfoMenu(Long chatId) {
        if (chatId != null && chatId >= 0) {
            SendMessage sendMessage = new SendMessage(chatId,"Информация по приюту");

            InlineKeyboardButton[][] inlineKeyboardButtons = {
                    {new InlineKeyboardButton("О приюте").callbackData("shelterInfo"),
                            new InlineKeyboardButton("Расписание, адрес").callbackData("timetableAndAddress")},
                    {new InlineKeyboardButton("Пропуск в приют").callbackData("shelterAdmission"),
                            new InlineKeyboardButton("Техника безопасности").callbackData("safetyRules")},
                    {new InlineKeyboardButton("Оставить контактные данные").callbackData("saveVisitorContacts"),
                            new InlineKeyboardButton("Позвать волонтера").callbackData("callVolunteer")},
                    {new InlineKeyboardButton("Возврат к выбору приюта").callbackData("backToMainMenu")}
            };
            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup(inlineKeyboardButtons);
            sendMessage.replyMarkup(markupInline);
            return bot.execute(sendMessage);
        }
        return null;
    }

    /**
     * Выводит меню выбора различной информации о том как взять и заботиться о животном
     * @param chatId указать номер чата, в который бот отправит сообщение
     * @return для облегчения процесса тестирования метода, возвращаем объект класса SendResponse
     */
    public SendResponse showAnimalAdoptionMenu(Long chatId, AnimalType shelterType) {
        if (chatId != null && chatId >= 0) {

            SendMessage sendMessage = new SendMessage(chatId,"Информация о том как взять и заботиться о " +
                    (shelterType == AnimalType.CAT?"кошке":"собаке"));

            InlineKeyboardButton[] inlineKeyboardButtonsTemp = new InlineKeyboardButton[2]; // Кнопки в меню которые будут меняться в зависимости от типа приюта

            if (shelterType == AnimalType.CAT){
                inlineKeyboardButtonsTemp[0] = new InlineKeyboardButton("Дом для котенка").callbackData("kittenHouseInfo");
                inlineKeyboardButtonsTemp[1] = new InlineKeyboardButton("Дом для взрослого кота").callbackData("catHouseInfo");
            } else {
                inlineKeyboardButtonsTemp[0] = new InlineKeyboardButton("Cоветы от кинологов").callbackData("showDogWhispererInfo");
                inlineKeyboardButtonsTemp[1] = new InlineKeyboardButton("Связь с кинологом").callbackData("showBestKinologInfo");
            }

            InlineKeyboardButton[][] inlineKeyboardButtons = {
                    {new InlineKeyboardButton("Знакомство с животным").callbackData("helloPetInfo"),
                            new InlineKeyboardButton("Необходимые документы").callbackData("necessaryPapers")},
                    {new InlineKeyboardButton("Транспортировка животного").callbackData("transportAnimalInfo")},

                    inlineKeyboardButtonsTemp,

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
}
