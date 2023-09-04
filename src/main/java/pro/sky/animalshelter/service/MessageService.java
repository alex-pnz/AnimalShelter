package pro.sky.animalshelter.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    @Autowired
    private TelegramBot bot;
    private SendMessage sendMessage = null;

    /**
     * Выводит данные службы охраны для оформления пропуска на машину
     * @param chatId указать номер чата, в который бот отправит сообщение
     * @return для облегчения процесса тестирования метода, возвращаем объект класса SendResponse
     */
    public SendResponse showSecurityInfo(Long chatId) {
        if (chatId != null && chatId >= 0) {
            String securityInfo = """
                    Данные службы охраны для оформления пропуска на машину:
                    \uD83D\uDCDE 3534522
                    \uD83D\uDCE7 security@dev.pro
                    """;
            sendMessage = new SendMessage(chatId, securityInfo);
            return bot.execute(sendMessage);
        }
        return null;
    }

}
