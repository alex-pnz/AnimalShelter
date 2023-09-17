package pro.sky.animalshelter.listener;

import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.verification.VerificationMode;
import org.springframework.beans.factory.annotation.Autowired;
import pro.sky.animalshelter.service.MenuService;
import pro.sky.animalshelter.service.MessageService;
import pro.sky.animalshelter.service.VisitService;
import pro.sky.animalshelter.service.VisitorService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static pro.sky.animalshelter.utils.Constants.*;

@ExtendWith(MockitoExtension.class)
class TelegramBotUpdateListenerTest {
    @Mock
    private TelegramBot bot;
    @Mock
    private MenuService menuService;
    @Mock
    private MessageService messageService;
    @Mock
    private VisitorService visitorService;
    @Mock
    private VisitService visitService;
    @Mock
    private ChatWithVolunteer chat;

    @InjectMocks
    @Autowired
    private TelegramBotUpdateListener telegramBotUpdateListener;

    private final Long CHAT_ID = 123L;

    // Testing "/start"

    static Stream<Arguments> provideParametersStart() {
        return Stream.of(
                Arguments.of(COMMAND_START, atLeastOnce()),
                Arguments.of("any other message", never())
        );
    }

    @ParameterizedTest
    @MethodSource("provideParametersStart")
    public void testStart(String command, VerificationMode mode) throws URISyntaxException, IOException {
        telegramBotUpdateListener.process(Collections.singletonList(getUpdate(command, true)));

        verify(menuService, mode).showMainMenu(CHAT_ID);

    }

    // Testing "/about"

    static Stream<Arguments> provideParametersAbout() {
        return Stream.of(
                Arguments.of(COMMAND_ABOUT, atLeastOnce()),
                Arguments.of("any other message", never())
        );
    }

    @ParameterizedTest
    @MethodSource("provideParametersAbout")
    public void testAbout(String command, VerificationMode mode) throws URISyntaxException, IOException {
        telegramBotUpdateListener.process(Collections.singletonList(getUpdate(command, true)));

        verify(messageService, mode).showInfoAboutShelter(CHAT_ID);

    }

    // Testing "/schedule"

    static Stream<Arguments> provideParametersSchedule() {
        return Stream.of(
                Arguments.of(COMMAND_SCHEDULE, atLeastOnce()),
                Arguments.of("any other message", never())
        );
    }

    @ParameterizedTest
    @MethodSource("provideParametersSchedule")
    public void testSchedule(String command, VerificationMode mode) throws URISyntaxException, IOException {
        telegramBotUpdateListener.process(Collections.singletonList(getUpdate(command, true)));

        verify(messageService, mode).showShelterSchedule(CHAT_ID);

    }

    // Testing "/security"

    static Stream<Arguments> provideParametersSecurity() {
        return Stream.of(
                Arguments.of(COMMAND_SECURITY, atLeastOnce()),
                Arguments.of("any other message", never())
        );
    }

    @ParameterizedTest
    @MethodSource("provideParametersSecurity")
    public void testSecurity(String command, VerificationMode mode) throws URISyntaxException, IOException {
        telegramBotUpdateListener.process(Collections.singletonList(getUpdate(command, true)));

        verify(messageService, mode).showSecurityInfo(CHAT_ID);

    }

    // Testing "/safety"
    static Stream<Arguments> provideParametersSafety() {
        return Stream.of(
                Arguments.of(COMMAND_SAFETY, atLeastOnce()),
                Arguments.of("any other message", never())
        );
    }

    @ParameterizedTest
    @MethodSource("provideParametersSafety")
    public void testSafety(String command, VerificationMode mode) throws URISyntaxException, IOException {
        telegramBotUpdateListener.process(Collections.singletonList(getUpdate(command, true)));

        verify(messageService, mode).showSafetyMeasures(CHAT_ID);

    }

    // Testing "/add_contacts"
    @Test
    public void testAddContacts() throws URISyntaxException, IOException {
        telegramBotUpdateListener.process(Collections.singletonList(getUpdate(COMMAND_ADD_CONTACTS, true)));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(bot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        assertThat(actual.getParameters().get("chat_id")).isEqualTo(CHAT_ID);
        assertThat(actual.getParameters().get("text")).isEqualTo(
                "Напишите одним сообщением Ваш номер телефона и электронную почту:");

    }


    // Testing "/volunteer"

    static Stream<Arguments> provideParametersVolunteer() {
        return Stream.of(
                Arguments.of(COMMAND_VOLUNTEER, atLeastOnce()),
                Arguments.of("any other message", never())
        );
    }
    @ParameterizedTest
    @MethodSource("provideParametersVolunteer")
    public void testVolunteer(String command, VerificationMode mode) throws URISyntaxException, IOException {
        telegramBotUpdateListener.process(Collections.singletonList(getUpdate(command, true)));

        verify(messageService, mode).showFindVolunteerInfo(CHAT_ID);
        verify(chat, mode).findVolunteer(CHAT_ID);

    }

    // Testing "/stopChat"
    static Stream<Arguments> provideParametersStopChat() {
        return Stream.of(
                Arguments.of(COMMAND_STOP_CHAT, atLeastOnce()),
                Arguments.of("any other message", never())
        );
    }
    @ParameterizedTest
    @MethodSource("provideParametersStopChat")
    public void testStopChat(String command, VerificationMode mode) throws URISyntaxException, IOException {
        telegramBotUpdateListener.process(Collections.singletonList(getUpdate(command, true)));

        verify(chat, mode).stopChat(CHAT_ID);

    }

    // Testing "\uD83D\uDC3E Помощь"

    static Stream<Arguments> provideParametersHelp() {
        return Stream.of(
                Arguments.of(COMMAND_HELP, atLeastOnce()),
                Arguments.of("any other message", never())
        );
    }
    @ParameterizedTest
    @MethodSource("provideParametersHelp")
    public void testHelp(String command, VerificationMode mode) throws URISyntaxException, IOException {
        telegramBotUpdateListener.process(Collections.singletonList(getUpdate(command, true)));

        verify(messageService, mode).showHelp(any());

    }

    // Testing continueChat with Volunteer

    static Stream<Arguments> provideParametersVisitor() {
        return Stream.of(
                Arguments.of(true, atLeastOnce()),
                Arguments.of(false, never())
        );
    }

    @ParameterizedTest
    @MethodSource("provideParametersVisitor")
    public void testCheckVisitor(boolean check, VerificationMode mode) throws URISyntaxException, IOException {
        when(chat.checkVisitor(any())).thenReturn(check);

        telegramBotUpdateListener.process(Collections.singletonList(getUpdate(anyString(), true)));

        verify(chat, mode).continueChat(anyLong(),any(),any());
    }

    @ParameterizedTest
    @MethodSource("provideParametersVisitor")
    public void testCheckVolunteer(boolean check, VerificationMode mode) throws URISyntaxException, IOException {
        when(chat.checkVolunteer(any())).thenReturn(check);

        telegramBotUpdateListener.process(Collections.singletonList(getUpdate(anyString(), true)));

        verify(chat, mode).continueChat(any(),anyLong(),any());

    }

    // Testing phone number & email save method call

    static Stream<Arguments> provideParametersSaveContacts() {
        return Stream.of(
                Arguments.of("+71111111111 mail@mail.com", atLeastOnce()),
                Arguments.of("71111111111 mail@mail.com", atLeastOnce()),
                Arguments.of("71111111111", never()),
                Arguments.of("7wrong number@", atLeastOnce()),
                Arguments.of("+7mail@mail.com", atLeastOnce()),
                Arguments.of("something else", never())
        );
    }

    @ParameterizedTest
    @MethodSource("provideParametersSaveContacts")
    public void testSaveContactsPhoneNumber(String message, VerificationMode mode) throws URISyntaxException, IOException {

        telegramBotUpdateListener.process(Collections.singletonList(getUpdate(message, true)));

        verify(messageService, mode).saveContactsPhoneNumber(CHAT_ID,message);
    }

    // Testing showInfoAboutShelter


    static Stream<Arguments> provideParametersInfoAbout() {
        return Stream.of(
                Arguments.of(CALLBACK_MENU_CAT, atLeastOnce()),
                Arguments.of(CALLBACK_MENU_DOG, atLeastOnce()),
                Arguments.of("something else", never())
        );
    }
    @ParameterizedTest
    @MethodSource("provideParametersInfoAbout")
    public void testShowInfoAboutShelter(String callback, VerificationMode mode) throws URISyntaxException, IOException {

        telegramBotUpdateListener.process(Collections.singletonList(getUpdate(callback,false)));

        verify(messageService, mode).showInfoAboutShelter(anyLong());
    }


    // Testing default

    static Stream<Arguments> provideParametersDefault() {
        return Stream.of(
                Arguments.of("some message", atLeastOnce(), true),
                Arguments.of("/unknown_command", atLeastOnce(), true),
                Arguments.of("/start", never(), true),
                Arguments.of("some callback", atLeastOnce(), false),
                Arguments.of(CALLBACK_MENU_CAT, never(), false),
                Arguments.of(CALLBACK_MENU_DOG, never(), false)
        );
    }
    @ParameterizedTest
    @MethodSource("provideParametersDefault")
    public void testDefault(String command, VerificationMode mode, boolean check) throws URISyntaxException, IOException {
        Update update = getUpdate(command, check);
        telegramBotUpdateListener.process(Collections.singletonList(update));

        verify(messageService, mode).defaultHandler(update);

    }

    @Test
    public void testInit(){
        telegramBotUpdateListener.init();
        verify(bot, atLeastOnce()).setUpdatesListener(telegramBotUpdateListener);
    }

    // Вспомогательный метод
    private Update getUpdate(String command, boolean message) throws URISyntaxException, IOException {
        String json = Files.readString(
                Paths.get(TelegramBotUpdateListenerTest.class.getResource(message?
                        "text_update_message.json":"text_update_callback.json").toURI()));
        return BotUtils.fromJson(json.replace("%text%", command), Update.class);
    }

}