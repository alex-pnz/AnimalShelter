package pro.sky.animalshelter.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

            InlineKeyboardButton[][] inlineKeyboardButtons = {{new InlineKeyboardButton("\uD83D\uDC08 Кошки").callbackData("cat"),
                    new InlineKeyboardButton(
                            "\uD83D\uDC15 Собаки").callbackData("dog")}};
            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup(inlineKeyboardButtons);
            sendMessage.replyMarkup(markupInline);
            return bot.execute(sendMessage);
        }
        return null;
    }

}
