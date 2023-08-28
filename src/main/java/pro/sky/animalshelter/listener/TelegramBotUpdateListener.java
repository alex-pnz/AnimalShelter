package pro.sky.animalshelter.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TelegramBotUpdateListener implements UpdatesListener {
    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdateListener.class);

    @Autowired
    private TelegramBot bot;

    @PostConstruct
    public void init() {
        bot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            // пишем обработчики в виде функций void functionName(Update update), вызываем здесь
            String command = update.message().text();
            switch (command) {
                case "/start" -> {
                    //
                }
                case "/about" -> {
                    //
                }
                case "/schedule" -> {
                    //
                }
                case "/security" -> {
                    //
                }
                case "/safety" -> {
                    //
                }
                default -> {
                    defaultHandler(update);
                }
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void defaultHandler(Update update) {
        SendMessage message = new SendMessage(update.message().chat().id(),
                "This command is not yet supported");
        SendResponse response = bot.execute(message);
    }
}
