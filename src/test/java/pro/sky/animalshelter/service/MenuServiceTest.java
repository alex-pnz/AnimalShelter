package pro.sky.animalshelter.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.animalshelter.model.enums.AnimalType;

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
    @Mock
    private VolunteerService volunteerService;

    private final Long chatId = 123L;

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
        when(volunteerService.isVolunteer(chatId)).thenReturn(false);
        menuService.showMainMenu(chatId);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(bot, times(1)).execute(argumentCaptor.capture());
        SendMessage actualMessage = argumentCaptor.getValue();

        Assertions.assertThat(actualMessage.getParameters().get("chat_id")).isEqualTo(chatId);
        Assertions.assertThat(actualMessage.getParameters().get("text")).isEqualTo("Правет Друг! Выбери приют:");

    }

    @Test
    public void showMainMenuForVolunteer() {
        when(volunteerService.isVolunteer(chatId)).thenReturn(true);
        menuService.showMainMenu(chatId);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(bot, times(1)).execute(argumentCaptor.capture());
        SendMessage actualMessage = argumentCaptor.getValue();

        Assertions.assertThat(actualMessage.getParameters().get("chat_id")).isEqualTo(chatId);
        Assertions.assertThat(actualMessage.getParameters().get("text")).isEqualTo("Правет Друг! Выбери приют:");

    }

    // Testing showVolunteerMenu
    static Stream<Arguments> provideParametersShowVolunteer() {
        return Stream.of(
                Arguments.of((Long) null),
                Arguments.of(-1L)
        );
    }

    @ParameterizedTest
    @MethodSource("provideParametersShowVolunteer")
    public void showVolunteerMenuNull(Long chatId) {

        assertNull(menuService.showVolunteerMenu(chatId));
    }

    @Test
    public void showVolunteerMenuPass() {

        menuService.showVolunteerMenu(chatId);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(bot, times(1)).execute(argumentCaptor.capture());
        SendMessage actualMessage = argumentCaptor.getValue();

        Assertions.assertThat(actualMessage.getParameters().get("chat_id")).isEqualTo(chatId);
        Assertions.assertThat(actualMessage.getParameters().get("text")).isEqualTo("Меню волонтера");

    }

    // Testing showShelterMenu
    static Stream<Arguments> provideParametersShowShelterMenu() {
        return Stream.of(
                Arguments.of((Long) null),
                Arguments.of(-1L)
        );
    }

    @ParameterizedTest
    @MethodSource("provideParametersShowShelterMenu")
    public void showShelterMenuNull(Long chatId) {

        assertNull(menuService.showShelterMenu(chatId));
    }

    @Test
    public void showShelterMenuPass() {

        menuService.showShelterMenu(chatId);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(bot, times(1)).execute(argumentCaptor.capture());
        SendMessage actualMessage = argumentCaptor.getValue();

        Assertions.assertThat(actualMessage.getParameters().get("chat_id")).isEqualTo(chatId);
        Assertions.assertThat(actualMessage.getParameters().get("text")).isEqualTo("Меню приюта");

    }

    // Testing showShelterInfoMenu
    static Stream<Arguments> provideParametersShowShelterInfoMenu() {
        return Stream.of(
                Arguments.of((Long) null),
                Arguments.of(-1L)
        );
    }

    @ParameterizedTest
    @MethodSource("provideParametersShowShelterInfoMenu")
    public void showShelterInfoMenuNull(Long chatId) {

        assertNull(menuService.showShelterInfoMenu(chatId));
    }

    @Test
    public void showShelterInfoMenuPass() {

        menuService.showShelterInfoMenu(chatId);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(bot, times(1)).execute(argumentCaptor.capture());
        SendMessage actualMessage = argumentCaptor.getValue();

        Assertions.assertThat(actualMessage.getParameters().get("chat_id")).isEqualTo(chatId);
        Assertions.assertThat(actualMessage.getParameters().get("text")).isEqualTo("Информация по приюту");

    }

    // Testing showAnimalAdoptionMenu
    static Stream<Arguments> provideParametersShowAnimalAdoptionMenu() {
        return Stream.of(
                Arguments.of(null, AnimalType.CAT),
                Arguments.of(-1L, AnimalType.CAT),
                Arguments.of(null, AnimalType.DOG),
                Arguments.of(-1L, AnimalType.DOG),
                Arguments.of(-1L, null)
        );
    }

    @ParameterizedTest
    @MethodSource("provideParametersShowAnimalAdoptionMenu")
    public void showAnimalAdoptionMenuNull(Long chatId, AnimalType type) {

        assertNull(menuService.showAnimalAdoptionMenu(chatId, type));
    }

    static Stream<Arguments> provideParametersShowAnimalAdoptionPass() {
        return Stream.of(
                Arguments.of(123L, AnimalType.CAT),
                Arguments.of(123L, AnimalType.DOG)
        );
    }

    @ParameterizedTest
    @MethodSource("provideParametersShowAnimalAdoptionPass")
    public void showAnimalAdoptionMenuPass(Long chatId, AnimalType type) {
        String expectedString = "Информация о том как взять и заботиться о " +
                (type == AnimalType.CAT ? "кошке" : "собаке");

        menuService.showAnimalAdoptionMenu(chatId, type);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(bot, times(1)).execute(argumentCaptor.capture());
        SendMessage actualMessage = argumentCaptor.getValue();

        Assertions.assertThat(actualMessage.getParameters().get("chat_id")).isEqualTo(chatId);
        Assertions.assertThat(actualMessage.getParameters().get("text")).isEqualTo(expectedString);

    }

    static Stream<Arguments> provideParametersProbationTerms() {
        return Stream.of(
                Arguments.of((Long) null),
                Arguments.of(-1L)
        );
    }

    @ParameterizedTest
    @MethodSource("provideParametersProbationTerms")
    public void showProbationTermsMenuNull(Long chatId) {

        assertNull(menuService.showProbationTermsMenu(chatId));
    }

    @Test
    public void showProbationTermsMenuPass() {

        menuService.showProbationTermsMenu(chatId);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(bot, times(1)).execute(argumentCaptor.capture());
        SendMessage actualMessage = argumentCaptor.getValue();

        Assertions.assertThat(actualMessage.getParameters().get("chat_id")).isEqualTo(chatId);
        Assertions.assertThat(actualMessage.getParameters().get("text")).isEqualTo("Выберите действие");

    }

}