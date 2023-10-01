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
import pro.sky.animalshelter.model.enums.Action;
import pro.sky.animalshelter.service.*;

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
import static pro.sky.animalshelter.model.enums.Action.*;
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
    private VolunteerService volunteerService;
    @Mock
    private VisitorService visitorService;
    @Mock
    private VisitService visitService;
    @Mock
    private ChatWithVolunteer chat;

    @InjectMocks
    private TelegramBotUpdateListener telegramBotUpdateListener;

    private final Long chatId = 123L;

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

        verify(menuService, mode).showMainMenu(chatId);

    }

    // Testing ShowShelterInfoMenu
    static Stream<Arguments> provideParametersShelterInfoMenu() {
        return Stream.of(
                Arguments.of(CALLBACK_SHELTER_INFO_MENU, atLeastOnce()),
                Arguments.of("something else", never())
        );
    }
    @ParameterizedTest
    @MethodSource("provideParametersShelterInfoMenu")
    public void testShowShelterInfoMenu(String callback, VerificationMode mode) throws URISyntaxException, IOException {

        telegramBotUpdateListener.process(Collections.singletonList(getUpdate(callback,false)));

        verify(menuService, mode).showShelterInfoMenu(anyLong());
    }

    // Testing ShowAnimalAdoptionMenu
    static Stream<Arguments> provideParametersShowAnimalAdoptionMenu() {
        return Stream.of(
                Arguments.of(CALLBACK_ADOPTION_INFO, atLeastOnce()),
                Arguments.of("something else", never())
        );
    }
    @ParameterizedTest
    @MethodSource("provideParametersShowAnimalAdoptionMenu")
    public void testShowAnimalAdoptionMenu(String callback, VerificationMode mode) throws URISyntaxException, IOException {

        telegramBotUpdateListener.process(Collections.singletonList(getUpdate(callback,false)));

        verify(menuService, mode).showAnimalAdoptionMenu(anyLong(),any());
    }

    // Testing ShowMainMenu
    static Stream<Arguments> provideParametersShowMainMenu() {
        return Stream.of(
                Arguments.of(CALLBACK_BACK_TO_MAIN_MENU, atLeastOnce()),
                Arguments.of("something else", never())
        );
    }
    @ParameterizedTest
    @MethodSource("provideParametersShowMainMenu")
    public void testShowMainMenu(String callback, VerificationMode mode) throws URISyntaxException, IOException {

        telegramBotUpdateListener.process(Collections.singletonList(getUpdate(callback,false)));

        verify(menuService, mode).showMainMenu(anyLong());
    }

    // Testing ShowMainMenu
    static Stream<Arguments> provideParametersShowDogWhispererInfo() {
        return Stream.of(
                Arguments.of(CALLBACK_DOG_WHISPERER_INFO, atLeastOnce()),
                Arguments.of("something else", never())
        );
    }
    @ParameterizedTest
    @MethodSource("provideParametersShowDogWhispererInfo")
    public void testShowDogWhispererInfo(String callback, VerificationMode mode) throws URISyntaxException, IOException {

        telegramBotUpdateListener.process(Collections.singletonList(getUpdate(callback,false)));

        verify(messageService, mode).showDogWhispererInfo(anyLong());
    }

    // Testing ShowMainMenu
    static Stream<Arguments> provideParametersShowBestKinologInfo() {
        return Stream.of(
                Arguments.of(CALLBACK_BEST_KINOLOG_INFO, atLeastOnce()),
                Arguments.of("something else", never())
        );
    }
    @ParameterizedTest
    @MethodSource("provideParametersShowBestKinologInfo")
    public void testShowBestKinologInfo(String callback, VerificationMode mode) throws URISyntaxException, IOException {

        telegramBotUpdateListener.process(Collections.singletonList(getUpdate(callback,false)));

        verify(messageService, mode).showBestKinologInfo(anyLong());
    }


    // Testing About

    static Stream<Arguments> provideParametersAbout() {
        return Stream.of(
                Arguments.of(CALLBACK_SHELTER_INFO, atLeastOnce()),
                Arguments.of("any other message", never())
        );
    }

    @ParameterizedTest
    @MethodSource("provideParametersAbout")
    public void testAbout(String command, VerificationMode mode) throws URISyntaxException, IOException {
        telegramBotUpdateListener.process(Collections.singletonList(getUpdate(command, false)));

        verify(messageService, mode).showInfoAboutShelter(chatId);

    }

    // Testing Schedule

    static Stream<Arguments> provideParametersSchedule() {
        return Stream.of(
                Arguments.of(CALLBACK_SCHEDULE_ADDRESS, atLeastOnce()),
                Arguments.of("any other message", never())
        );
    }

    @ParameterizedTest
    @MethodSource("provideParametersSchedule")
    public void testSchedule(String command, VerificationMode mode) throws URISyntaxException, IOException {
        telegramBotUpdateListener.process(Collections.singletonList(getUpdate(command, false)));

        verify(messageService, mode).showShelterSchedule(chatId);

    }

    // Testing Security

    static Stream<Arguments> provideParametersSecurity() {
        return Stream.of(
                Arguments.of(CALLBACK_SHELTER_ADMISSION, atLeastOnce()),
                Arguments.of("any other message", never())
        );
    }

    @ParameterizedTest
    @MethodSource("provideParametersSecurity")
    public void testSecurity(String command, VerificationMode mode) throws URISyntaxException, IOException {
        telegramBotUpdateListener.process(Collections.singletonList(getUpdate(command, false)));

        verify(messageService, mode).showSecurityInfo(chatId);

    }

    // Testing Safety
    static Stream<Arguments> provideParametersSafety() {
        return Stream.of(
                Arguments.of(CALLBACK_SAFETY_RULES, atLeastOnce()),
                Arguments.of("any other message", never())
        );
    }

    @ParameterizedTest
    @MethodSource("provideParametersSafety")
    public void testSafety(String command, VerificationMode mode) throws URISyntaxException, IOException {
        telegramBotUpdateListener.process(Collections.singletonList(getUpdate(command, false)));

        verify(messageService, mode).showSafetyMeasures(chatId);

    }

    // Testing Add_contacts
    @Test
    public void testAddContacts() throws URISyntaxException, IOException {
        telegramBotUpdateListener.process(Collections.singletonList(getUpdate(CALLBACK_SAVE_VISITOR_CONTACTS, false)));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(bot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        assertThat(actual.getParameters().get("chat_id")).isEqualTo(chatId);
        assertThat(actual.getParameters().get("text")).isEqualTo(
                "Напишите одним сообщением Ваш номер телефона и электронную почту:");

    }


    // Testing Volunteer

    static Stream<Arguments> provideParametersVolunteer() {
        return Stream.of(
                Arguments.of(CALLBACK_CALL_VOLUNTEER, atLeastOnce()),
                Arguments.of("any other message", never())
        );
    }
    @ParameterizedTest
    @MethodSource("provideParametersVolunteer")
    public void testVolunteer(String command, VerificationMode mode) throws URISyntaxException, IOException {
        telegramBotUpdateListener.process(Collections.singletonList(getUpdate(command, false)));

        verify(messageService, mode).showFindVolunteerInfo(chatId);
        verify(chat, mode).findVolunteer(chatId);

    }

    // Testing StopChat
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

        verify(chat, mode).stopChat(chatId);

    }

    // Testing Hellopet
    static Stream<Arguments> provideParametersHelloPet() {
        return Stream.of(
                Arguments.of(CALLBACK_HELLO_PET, atLeastOnce()),
                Arguments.of("any other message", never())
        );
    }

    @ParameterizedTest
    @MethodSource("provideParametersHelloPet")
    public void testHelloPet(String command, VerificationMode mode) throws URISyntaxException, IOException {
        telegramBotUpdateListener.process(Collections.singletonList(getUpdate(command, false)));

        verify(messageService, mode).showPetHelloRules(chatId);

    }


    // Testing Transport
    static Stream<Arguments> provideParametersTransport() {
        return Stream.of(
                Arguments.of(CALLBACK_TRANSPORT_ANIMAL_INFO, atLeastOnce()),
                Arguments.of("any other message", never())
        );
    }

    @ParameterizedTest
    @MethodSource("provideParametersTransport")
    public void testTransport(String command, VerificationMode mode) throws URISyntaxException, IOException {
        telegramBotUpdateListener.process(Collections.singletonList(getUpdate(command, false)));

        verify(messageService, mode).showPetTransportRules(chatId);

    }

    // Testing Refuse
    static Stream<Arguments> provideParametersRefuse() {
        return Stream.of(
                Arguments.of(CALLBACK_ADOPTION_REFUSAL_INFO, atLeastOnce()),
                Arguments.of("any other message", never())
        );
    }

    @ParameterizedTest
    @MethodSource("provideParametersRefuse")
    public void testRefuse(String command, VerificationMode mode) throws URISyntaxException, IOException {
        telegramBotUpdateListener.process(Collections.singletonList(getUpdate(command, false)));

        verify(messageService, mode).showRefusePolicy(chatId);

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

        verify(messageService, mode).saveContactsPhoneNumber(chatId,message);
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

        verify(menuService, mode).showShelterMenu(anyLong());
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

    // Testing showVolunteerMenu
    static Stream<Arguments> provideParametersShowVolunteerMenu() {
        return Stream.of(
                Arguments.of(CALLBACK_START_VOLUNTEER_SESSION, atLeastOnce()),
                Arguments.of("something else", never())
        );
    }
    @ParameterizedTest
    @MethodSource("provideParametersShowVolunteerMenu")
    public void testShowVolunteerMenu(String callback, VerificationMode mode) throws URISyntaxException, IOException {

        telegramBotUpdateListener.process(Collections.singletonList(getUpdate(callback,false)));

        verify(menuService, mode).showVolunteerMenu(anyLong());
        verify(volunteerService, mode).setVolunteerFree(anyLong(), anyBoolean());
    }

    // Testing showProbationTermsMenu
    static Stream<Arguments> provideParametersShowProbationTermsMenu() {
        return Stream.of(
                Arguments.of(CALLBACK_CHANGE_PROBATION_TERMS, atLeastOnce()),
                Arguments.of("something else", never())
        );
    }
    @ParameterizedTest
    @MethodSource("provideParametersShowProbationTermsMenu")
    public void testShowProbationTermsMenu(String callback, VerificationMode mode) throws URISyntaxException, IOException {

        telegramBotUpdateListener.process(Collections.singletonList(getUpdate(callback,false)));

        verify(menuService, mode).showProbationTermsMenu(anyLong());
    }

    // Testing showVolunteerMenu
    static Stream<Arguments> provideParametersSetVolunteerFree() {
        return Stream.of(
                Arguments.of(CALLBACK_END_VOLUNTEER_SESSION, atLeastOnce()),
                Arguments.of("something else", never())
        );
    }
    @ParameterizedTest
    @MethodSource("provideParametersSetVolunteerFree")
    public void testSetVolunteerFree(String callback, VerificationMode mode) throws URISyntaxException, IOException {

        telegramBotUpdateListener.process(Collections.singletonList(getUpdate(callback,false)));

        verify(volunteerService, mode).setVolunteerFree(anyLong(),anyBoolean());
        verify(messageService, mode).sendMessage(anyLong(), anyString());
    }

    // Testing showListOfDocuments
    static Stream<Arguments> provideParametersShowListOfDocuments() {
        return Stream.of(
                Arguments.of(CALLBACK_NECESSARY_PAPERS, atLeastOnce()),
                Arguments.of("something else", never())
        );
    }
    @ParameterizedTest
    @MethodSource("provideParametersShowListOfDocuments")
    public void testShowListOfDocuments(String callback, VerificationMode mode) throws URISyntaxException, IOException {

        telegramBotUpdateListener.process(Collections.singletonList(getUpdate(callback,false)));

        verify(messageService, mode).showListOfDocuments(anyLong());
    }

    // Testing showKittenPuppyInfo
    static Stream<Arguments> provideParametersShowKittenPuppyInfo() {
        return Stream.of(
                Arguments.of(CALLBACK_KITTEN_HOUSE_INFO, atLeastOnce()),
                Arguments.of(CALLBACK_PUPPY_HOUSE_INFO, atLeastOnce()),
                Arguments.of("something else", never())
        );
    }
    @ParameterizedTest
    @MethodSource("provideParametersShowKittenPuppyInfo")
    public void testShowKittenPuppyInfo(String callback, VerificationMode mode) throws URISyntaxException, IOException {

        telegramBotUpdateListener.process(Collections.singletonList(getUpdate(callback,false)));

        verify(messageService, mode).showKittenPuppyInfo(anyLong());
    }

    // Testing showAdultAnimalInfo
    static Stream<Arguments> provideParametersShowAdultAnimalInfo() {
        return Stream.of(
                Arguments.of(CALLBACK_CAT_HOUSE_INFO, atLeastOnce()),
                Arguments.of(CALLBACK_DOG_HOUSE_INFO, atLeastOnce()),
                Arguments.of("something else", never())
        );
    }
    @ParameterizedTest
    @MethodSource("provideParametersShowAdultAnimalInfo")
    public void testShowAdultAnimalInfo(String callback, VerificationMode mode) throws URISyntaxException, IOException {

        telegramBotUpdateListener.process(Collections.singletonList(getUpdate(callback,false)));

        verify(messageService, mode).showAdultAnimalInfo(anyLong());
    }

    // Testing doAction
    static Stream<Arguments> provideParametersDoAction() {
        return Stream.of(
                Arguments.of(true, atLeastOnce()),
                Arguments.of(false, never())
        );
    }
    @ParameterizedTest
    @MethodSource("provideParametersDoAction")
    public void testDoAction(boolean b, VerificationMode mode) throws URISyntaxException, IOException {
        when(volunteerService.isVolunteer(chatId)).thenReturn(b);
        if(b){
            when(volunteerService.isAction(chatId)).thenReturn(b);
        }

        telegramBotUpdateListener.process(Collections.singletonList(getUpdate("1", true)));

        verify(chat, mode).doAction(any(), any());
    }

    //testing ProbationTermsMenu & VolunteerMenu

    static Stream<Arguments> provideParametersSaveAction() {
        String message = "Введите айди пользователя";
        return Stream.of(
                Arguments.of(CALLBACK_CONTACT_ADOPTER, atLeastOnce(), CALL_VISITOR, message),
                Arguments.of(CALLBACK_SEND_ADOPTER_WARNING, atLeastOnce(), SEND_WARNING_MESSAGE, message),
                Arguments.of(CALLBACK_ADD_14_DAYS, atLeastOnce(), ADD_14_DAYS, message),
                Arguments.of(CALLBACK_ADD_30_DAYS, atLeastOnce(), ADD_30_DAYS, message),
                Arguments.of(CALLBACK_COMPLETE_PROBATION_TERMS, atLeastOnce(), COMPLETE_PROBATION_TERMS, message),
                Arguments.of(CALLBACK_FAIL_PROBATION_TERMS, atLeastOnce(), FAIL_PROBATION_TERMS, message),
                Arguments.of("something else", never(), ADD_14_DAYS, message)
        );
    }
    @ParameterizedTest
    @MethodSource("provideParametersSaveAction")
    public void testSaveAction(String callback, VerificationMode mode,
                               Action action, String message)
                                throws URISyntaxException, IOException {

        telegramBotUpdateListener.process(Collections.singletonList(getUpdate(callback,false)));

        verify(volunteerService, mode).saveAction(chatId, action);
        verify(messageService, mode).sendMessage(chatId, message);
    }

    // Testing houseForAnimalWithDisabilities
    static Stream<Arguments> provideParametersHouseForAnimalWithDisabilities() {
        return Stream.of(
                Arguments.of(CALLBACK_HANDICAPPED_ANIMAL_HOUSE_INFO, atLeastOnce()),
                Arguments.of("any other message", never())
        );
    }

    @ParameterizedTest
    @MethodSource("provideParametersHouseForAnimalWithDisabilities")
    public void testHouseForAnimalWithDisabilities(String command, VerificationMode mode) throws URISyntaxException, IOException {
        telegramBotUpdateListener.process(Collections.singletonList(getUpdate(command, false)));

        verify(messageService, mode).houseForAnimalWithDisabilities(chatId);

    }
}