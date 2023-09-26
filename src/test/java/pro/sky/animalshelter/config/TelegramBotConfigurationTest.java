package pro.sky.animalshelter.config;

import com.pengrad.telegrambot.TelegramBot;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TelegramBotConfigurationTest {
    @Spy
    private TelegramBotConfiguration telegramBotConfiguration;
    @Test
    public void testTelegramBot(){
        TelegramBot telegramBot = telegramBotConfiguration.telegramBot();
        assertNotNull(telegramBot);
    }

}