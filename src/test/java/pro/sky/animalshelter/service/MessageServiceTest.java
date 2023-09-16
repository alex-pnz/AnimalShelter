package pro.sky.animalshelter.service;

import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import pro.sky.animalshelter.exception.InvalidChatException;
import pro.sky.animalshelter.model.Visitor;
import pro.sky.animalshelter.repository.VisitorRepository;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static pro.sky.animalshelter.utils.Constants.SECURITY_CONTACT_INFO;
import static pro.sky.animalshelter.utils.Constants.SHELTER_HELP;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @Mock
    private VisitorRepository visitorRepository;
    @Mock
    private TelegramBot bot;
    @InjectMocks
    @Autowired
    private MessageService messageService;

    private final Long CHAT_ID = 123L;

//    @BeforeEach
//    public void beforeEach() {
//        Mockito.when(bot.execute(any())).thenReturn(
//                BotUtils.fromJson(
//                        """
//                            {
//                              "ok": true
//                            }
//                            """,
//                        SendResponse.class
//                )
//        );
//    }

    // testing showSecurityInfo

    static Stream<Arguments> provideParametersShowSecurityInfoNull() {
        return Stream.of(
                Arguments.of((Long) null),
                Arguments.of(-1L)
        );
    }

    @ParameterizedTest
    @MethodSource("provideParametersShowSecurityInfoNull")
    public void showSecurityInfoNull(Long chatId) {

        assertThrows(InvalidChatException.class, () -> messageService.showSecurityInfo(chatId));

    }

    @Test
    public void showSecurityInfo() {

        messageService.showSecurityInfo(CHAT_ID);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(bot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        assertThat(actual.getParameters().get("chat_id")).isEqualTo(CHAT_ID);
        assertThat(actual.getParameters().get("text")).isEqualTo(SECURITY_CONTACT_INFO);

    }

    @Test
    public void showInfoAboutShelter() {
    }

    @Test
    public void showShelterSchedule() {
    }

    @Test
    public void showSafetyMeasures() {
    }

    // Testing showHelp
    static Stream<Arguments> provideParametersShowHelpNull() {
        return Stream.of(
                Arguments.of((Long) null),
                Arguments.of(-1L)
        );
    }

    @ParameterizedTest
    @MethodSource("provideParametersShowHelpNull")
    public void showHelpNull(Long chatId) {

        assertThrows(InvalidChatException.class, () -> messageService.showHelp(chatId));

    }

    @Test
    public void showHelp() {

        messageService.showHelp(CHAT_ID);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(bot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        assertThat(actual.getParameters().get("chat_id")).isEqualTo(CHAT_ID);
        assertThat(actual.getParameters().get("text")).isEqualTo(SHELTER_HELP);

    }

    // Testing saveContactsPhoneNumber

    static Stream<Arguments> provideParametersSaveContactsPhoneNumberNull() {
        return Stream.of(
                Arguments.of((Long) null, "any message"),
                Arguments.of(-1L, "any message")
        );
    }

    @ParameterizedTest
    @MethodSource("provideParametersSaveContactsPhoneNumberNull")
    public void saveContactsPhoneNumberNull(Long chatId, String message) {
        assertThrows(InvalidChatException.class, () -> messageService.saveContactsPhoneNumber(chatId, message));
    }

    static Stream<Arguments> provideParametersSaveContactsPhoneNumberWrongMail() { // не правильный адрес эл почты
        return Stream.of(
                Arguments.of("any message @"),
                Arguments.of("any message @ "),
                Arguments.of("@any message"),
                Arguments.of(" @any message @ "),
                Arguments.of("77777777777 @ "),
                Arguments.of("77777777777 @"),
                Arguments.of("77777777777 @."),
                Arguments.of("77777777777 test@."),
                Arguments.of("77777777777 @test.com")
        );
    }

    @ParameterizedTest
    @MethodSource("provideParametersSaveContactsPhoneNumberWrongMail")
    public void saveContactsPhoneNumberWrongMail(String message) {

        messageService.saveContactsPhoneNumber(CHAT_ID, message);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(bot, times(1)).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        assertThat(actual.getParameters().get("chat_id")).isEqualTo(CHAT_ID);
        assertThat(actual.getParameters().get("text")).isEqualTo("К сожалению бот не смог распознать эл почту'. " +
                "(/add_contacts - попробовать ввести еще раз)");

    }

    static Stream<Arguments> provideParametersSaveContactsPhoneNumberWrongNumber() { // не правильный номер телефона
        return Stream.of(
                Arguments.of("any message test@test.com"),
                Arguments.of(" test@test.com"),
                Arguments.of("+ test@test.com"),
                Arguments.of("777 test@test.com"),
                Arguments.of("+777 test@test.com"),
                Arguments.of("21234567890 test@test.com"),
                Arguments.of("+21234567890 test@test.com"),
                Arguments.of("17777777777 test@test.com"),
                Arguments.of("+1(777)7777777 test@test.com"),
                Arguments.of("+1 (777) 777 77 77 test@test.com")
        );
    }

    @ParameterizedTest
    @MethodSource("provideParametersSaveContactsPhoneNumberWrongNumber")
    public void saveContactsPhoneNumberWrongNumber(String message) {
        when(visitorRepository.findByChatId(CHAT_ID)).thenReturn(new Visitor(CHAT_ID,"Test Name",null,null));

        messageService.saveContactsPhoneNumber(CHAT_ID, message);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(bot, times(2)).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        assertThat(actual.getParameters().get("chat_id")).isEqualTo(CHAT_ID);
        assertThat(actual.getParameters().get("text")).isEqualTo("К сожалению бот не смог распознать номер телефона. " +
                "(/add_contacts - попробовать ввести еще раз)");

        ArgumentCaptor<Visitor> argumentCaptorVisitor = ArgumentCaptor.forClass(Visitor.class);
        verify(visitorRepository,times(1)).save(argumentCaptorVisitor.capture());
        Visitor actualVisitor = argumentCaptorVisitor.getValue();

        assertThat(actualVisitor.getVisitorName()).isEqualTo("Test Name");
        assertThat(actualVisitor.getEmail()).isEqualTo("test@test.com");

    }

    static Stream<Arguments> provideParametersSaveContactsPhoneNumberPass() { // правильный номер телефона и почта
        return Stream.of(
                Arguments.of("+7 (777) 777 77 77 test@test.com", "+77777777777"),
                Arguments.of("8 (777) 777 77 77 test@test.com", "+77777777777"),
                Arguments.of("8 777 777 77 77 test@test.com", "+77777777777"),
                Arguments.of("87777777777 test@test.com", "+77777777777"),
                Arguments.of("+77777777777 test@test.com", "+77777777777"),
                Arguments.of("+7(777)7777777 test@test.com", "+77777777777"),
                Arguments.of("8(777)7777777 test@test.com", "+77777777777"),
                Arguments.of("8(777)7777777      test@test.com     ", "+77777777777")
        );
    }

    @ParameterizedTest
    @MethodSource("provideParametersSaveContactsPhoneNumberPass")
    public void saveContactsPhoneNumberPass(String message, String phoneNumber) {
        when(visitorRepository.findByChatId(CHAT_ID)).thenReturn(new Visitor(CHAT_ID,"Test Name",null,null));

        messageService.saveContactsPhoneNumber(CHAT_ID, message);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(bot, times(2)).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        assertThat(actual.getParameters().get("chat_id")).isEqualTo(CHAT_ID);
        assertThat(actual.getParameters().get("text")).isEqualTo("Номер " + phoneNumber + " сохранен!");

        ArgumentCaptor<Visitor> argumentCaptorVisitor = ArgumentCaptor.forClass(Visitor.class);
        verify(visitorRepository,times(2)).save(argumentCaptorVisitor.capture());
        Visitor actualVisitor = argumentCaptorVisitor.getValue();

        assertThat(actualVisitor.getVisitorName()).isEqualTo("Test Name");
        assertThat(actualVisitor.getEmail()).isEqualTo("test@test.com");
        assertNotNull(actualVisitor.getPhoneNumber());

    }


    // testing saveContactsEmail


    static Stream<Arguments> provideParametersSaveContactsEmailNull() {
        return Stream.of(
                Arguments.of((Long) null, "any message"),
                Arguments.of(-1L, "any message")
        );
    }

    @ParameterizedTest
    @MethodSource("provideParametersSaveContactsEmailNull")
    public void saveContactsEmailPassNull(Long chatId, String message) {

        assertThrows(InvalidChatException.class, () -> messageService.saveContactsEmail(chatId, message));

    }

    @Test
    public void saveContactsEmailPass() {
        Visitor expected = new Visitor(CHAT_ID,"Test Name",null,"test@test.com");

        when(visitorRepository.findByChatId(CHAT_ID)).thenReturn(new Visitor(CHAT_ID,"Test Name",null,null));

        messageService.saveContactsEmail(CHAT_ID,"test@test.com");

        ArgumentCaptor<Visitor> argumentCaptor = ArgumentCaptor.forClass(Visitor.class);

        verify(visitorRepository, times(1)).save(argumentCaptor.capture());

        Visitor actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
        Assertions.assertThat(actual.getVisitorName()).isEqualTo(expected.getVisitorName());
    }

    static Stream<Arguments> provideParametersSaveContactsEmailFail() { // передаем не правильный адрес эл почты
        return Stream.of(
                Arguments.of("test"),
                Arguments.of("@test.com"),
                Arguments.of(" @test.com"),
                Arguments.of("@test. "),
                Arguments.of("@ .com"),
                Arguments.of(" @ . "),
                Arguments.of("test@.com"),
                Arguments.of("test@   .com")
        );
    }

    @ParameterizedTest
    @MethodSource("provideParametersSaveContactsEmailFail")
    public void saveContactsEmailFail(String message) {

        messageService.saveContactsEmail(CHAT_ID, message);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(bot,times(1)).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        assertThat(actual.getParameters().get("chat_id")).isEqualTo(CHAT_ID);
        assertThat(actual.getParameters().get("text")).isEqualTo(
                "К сожалению бот не смог распознать адрес электронной почты. " +
                        "(/add_contacts - попробовать ввести еще раз)");

    }

    @Test
    public void showFindVolunteerInfo() {
    }

    @Test
    public void sendMessage() {
    }

    static Stream<Arguments> provideParametersdefaultHandlerMessage() { // передаем не существующую команду
        return Stream.of(
                Arguments.of("test"),
                Arguments.of("/test"),
                Arguments.of("///"),
                Arguments.of("/some/")
        );
    }

    @ParameterizedTest
    @MethodSource("provideParametersdefaultHandlerMessage")
    public void defaultHandlerMessage(String command) {
        String json = """
                {
                  "message": {
                    "chat": {
                      "id": 123
                    },
                    "text": "%text%"
                  }
                }
                """;

        Update update = BotUtils.fromJson(json.replace("%text%", command), Update.class);
        messageService.defaultHandler(update);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(bot,times(1)).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        assertThat(actual.getParameters().get("chat_id")).isEqualTo(CHAT_ID);
        assertThat(actual.getParameters().get("text")).isEqualTo(
                "This command is not yet supported");


    }

    static Stream<Arguments> provideParametersdefaultHandlerCallback() { // передаем не существующий коллбек
        return Stream.of(
                Arguments.of("test"),
                Arguments.of("/test"),
                Arguments.of("///"),
                Arguments.of("/some/")
        );
    }

    @ParameterizedTest
    @MethodSource("provideParametersdefaultHandlerCallback")
    public void defaultHandlerCallback(String callback) {
        String json = """
                {
                  "callback_query": {
                    "from": {
                      "id": 123
                    },
                    "data": "%text%"
                  }
                }
                """;
        Update update = BotUtils.fromJson(json.replace("%text%", callback), Update.class);
        messageService.defaultHandler(update);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(bot,times(1)).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        assertThat(actual.getParameters().get("chat_id")).isEqualTo(CHAT_ID);
        assertThat(actual.getParameters().get("text")).isEqualTo(
                "This command is not yet supported");

    }
}