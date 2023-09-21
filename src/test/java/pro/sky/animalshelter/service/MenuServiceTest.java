package pro.sky.animalshelter.service;

import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private TelegramBot bot;
    @InjectMocks
    private MenuService menuService;

    // Testing showMainMenu
    static Stream<Arguments> provideParametersShowMain() {
        return Stream.of(
                Arguments.of((Long) null),
                Arguments.of(-1L)
        );
    }

    @ParameterizedTest
    @MethodSource("provideParametersShowMain")
    public void showMainMenuNull(Long chatId) {

        assertNull(menuService.showMainMenu(chatId));
    }


    @Test
    public void showMainMenuPass() {

        menuService.showMainMenu(123L);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(bot,times(1)).execute(argumentCaptor.capture());
        SendMessage actualMessage = argumentCaptor.getValue();

        Assertions.assertThat(actualMessage.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actualMessage.getParameters().get("text")).isEqualTo("Правет Друг! Выбери приют:");

    }

    // Testing setHelpButton

    static Stream<Arguments> provideParametersHelpButtonNull() {
        return Stream.of(
                Arguments.of((Long) null),
                Arguments.of(-1L)
        );
    }

    @ParameterizedTest
    @MethodSource("provideParametersHelpButtonNull")
    public void testSetHelpButtonNull(Long chatId) {

        assertNull(menuService.setHelpButton(chatId));
    }


    @Test
    public void testSetHelpButtonPass() throws InstantiationException, IllegalAccessException {

        ReplyKeyboardMarkup actual = menuService.setHelpButton(1L);
        assertNotNull(actual);
        Assertions.assertThat(actual).isInstanceOf(ReplyKeyboardMarkup.class);

    }
}