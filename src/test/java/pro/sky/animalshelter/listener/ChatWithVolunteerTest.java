package pro.sky.animalshelter.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.animalshelter.model.Adoption;
import pro.sky.animalshelter.model.Visitor;
import pro.sky.animalshelter.model.Volunteer;
import pro.sky.animalshelter.model.enums.Action;
import pro.sky.animalshelter.repository.AdoptionRepository;
import pro.sky.animalshelter.repository.VisitorRepository;
import pro.sky.animalshelter.repository.VolunteerRepository;
import pro.sky.animalshelter.service.MessageService;
import pro.sky.animalshelter.service.VolunteerService;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static pro.sky.animalshelter.model.enums.Action.*;
import static pro.sky.animalshelter.utils.Constants.*;

@ExtendWith(MockitoExtension.class)
class ChatWithVolunteerTest {

    @Mock
    private VolunteerRepository volunteerRepository;
    @Mock
    private VisitorRepository visitorRepository;
    @Mock
    private MessageService messageService;
    @Mock
    private AdoptionRepository adoptionRepository;
    @Mock
    private VolunteerService volunteerService;
    @Mock
    private TelegramBot bot;
    @InjectMocks
    private ChatWithVolunteer chatWithVolunteer;
    private final Long chatId = 123L;
    @Test
    void testFindVolunteer() {
        Visitor visitor = new Visitor();
        visitor.setVolunteer(new Volunteer());
        when(visitorRepository.findByChatId(chatId)).thenReturn(visitor);

        chatWithVolunteer.findVolunteer(chatId);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(messageService).sendMessage(anyLong(), argumentCaptor.capture());
        String actual = argumentCaptor.getValue();

        assertThat(actual).isEqualTo("Вы уже находитесь в чате с волонтером!");
    }

    @Test
    void testFindVolunteerNoVolunteer() {
        Visitor visitor = new Visitor();
        visitor.setVisitorName("Visitor Test Name");
        when(visitorRepository.findByChatId(chatId)).thenReturn(visitor);

        when(volunteerRepository.getByIsFree(true)).thenReturn(new Volunteer(1L, chatId, "Volunteer Test Name", true, null));

        chatWithVolunteer.findVolunteer(chatId);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(messageService, times(2)).sendMessage(anyLong(), argumentCaptor.capture());
        String actual = argumentCaptor.getValue();

        assertThat(actual).isEqualTo("Посетитель нуждается в вашей помощи! Его имя - Visitor Test Name");
    }

    @Test
    void testFindVolunteerNullVolunteer() {
        Visitor visitor = new Visitor();
        when(visitorRepository.findByChatId(chatId)).thenReturn(visitor);

        when(volunteerRepository.getByIsFree(true)).thenReturn(null);

        chatWithVolunteer.findVolunteer(chatId);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(messageService).sendMessage(anyLong(), argumentCaptor.capture());
        String actual = argumentCaptor.getValue();

        assertThat(actual).isEqualTo("К сожалению сейчас нет свободных волонтеров.");
    }

    @Test
    void stopChatCheckVolunteer() {
        when(volunteerRepository.findByChatId(chatId)).thenReturn(new Volunteer(1L, chatId, "Volunteer Test Name", false, new Visitor()));

        chatWithVolunteer.stopChat(chatId);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(messageService).sendMessage(anyLong(), argumentCaptor.capture());
        String actual = argumentCaptor.getValue();

        assertThat(actual).isEqualTo("Чат с посетителем завершен");
    }

    @Test
    void stopChatCheckVisitor() {
        Visitor visitor = new Visitor();
        visitor.setVolunteer(new Volunteer());
        when(visitorRepository.findByChatId(chatId)).thenReturn(visitor);

        chatWithVolunteer.stopChat(chatId);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(messageService,times(2)).sendMessage(any(), argumentCaptor.capture());
        String actual = argumentCaptor.getValue();

        assertThat(actual).isEqualTo("Чат с посетителем завершен");
    }

    @Test
    void stopChatNullVolunteerNullVisitor() {
        when(visitorRepository.findByChatId(chatId)).thenReturn(null);

        when(volunteerRepository.findByChatId(chatId)).thenReturn(null);

        chatWithVolunteer.stopChat(chatId);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(messageService).sendMessage(anyLong(), argumentCaptor.capture());
        String actual = argumentCaptor.getValue();

        assertThat(actual).isEqualTo("Вы не находитесь в чате с волонтером.");
    }

    @Test
    void testCheckVolunteerTrue() {
        when(volunteerRepository.findByChatId(chatId)).thenReturn(new Volunteer(1L, chatId, "Volunteer Test Name", false, new Visitor()));

        assertTrue(chatWithVolunteer.checkVolunteer(chatId));
    }

    @Test
    void testCheckVolunteerFalse() {
        when(volunteerRepository.findByChatId(chatId)).thenReturn(new Volunteer(1L, chatId, "Volunteer Test Name", true, new Visitor()));

        assertFalse(chatWithVolunteer.checkVolunteer(chatId));
    }

    @Test
    void testCheckVolunteerNull() {
        when(volunteerRepository.findByChatId(chatId)).thenReturn(null);

        assertFalse(chatWithVolunteer.checkVolunteer(chatId));
    }

    @Test
    void checkVisitorTrue() {
        Visitor visitor = new Visitor();
        visitor.setVolunteer(new Volunteer());
        when(visitorRepository.findByChatId(chatId)).thenReturn(visitor);

        assertTrue(chatWithVolunteer.checkVisitor(chatId));
    }

    @Test
    void checkVisitorFalse() {
        Visitor visitor = new Visitor();
        when(visitorRepository.findByChatId(chatId)).thenReturn(visitor);

        assertFalse(chatWithVolunteer.checkVisitor(chatId));
    }

    @Test
    void checkVisitorNull() {
        when(visitorRepository.findByChatId(chatId)).thenReturn(null);

        assertFalse(chatWithVolunteer.checkVisitor(chatId));
    }

    @Test
    void continueChatVolunteer() {
        Visitor visitor = new Visitor();
        visitor.setVolunteer(new Volunteer());
        when(visitorRepository.findByChatId(chatId)).thenReturn(visitor);

        chatWithVolunteer.continueChat(chatId, null, "Test Message");

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(messageService).sendMessage(any(), argumentCaptor.capture());
        String actual = argumentCaptor.getValue();

        assertThat(actual).isEqualTo("Test Message");
    }

    @Test
    void continueChatVisitor() {
        when(volunteerRepository.findByChatId(chatId)).thenReturn(new Volunteer(1L, chatId, "Volunteer Test Name", false, new Visitor()));

        chatWithVolunteer.continueChat(null, chatId, "Test Message");

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(messageService).sendMessage(any(), argumentCaptor.capture());
        String actual = argumentCaptor.getValue();

        assertThat(actual).isEqualTo("Test Message");
    }

    static Stream<Arguments> provideParametersDoAction() {
        return Stream.of(
                Arguments.of(ADD_30_DAYS, "К вашему испытательному сроку было добавлено 30 дней."),
                Arguments.of(ADD_14_DAYS, "К вашему испытательному сроку было добавлено 14 дней."),
                Arguments.of(FAIL_PROBATION_TERMS, FAIL_PROBATION_TERMS_MESSAGE),
                Arguments.of(COMPLETE_PROBATION_TERMS, "Поздравляем! Вы успешно завершили испытательный срок"),
                Arguments.of(SEND_WARNING_MESSAGE, WARNING_MESSAGE)

        );
    }

    @ParameterizedTest
    @MethodSource("provideParametersDoAction")
    void doAction(Action action, String text) {
        Volunteer volunteer = new Volunteer();
        volunteer.setAction(action);
        when(volunteerRepository.findByChatId(anyLong())).thenReturn(volunteer);
        Visitor visitor = new Visitor();
        visitor.setId(1L);
        when(visitorRepository.findByChatId(anyLong())).thenReturn(visitor);
        when(adoptionRepository.findByVisitorId(anyLong())).thenReturn(new Adoption());

        chatWithVolunteer.doAction(1L,"1");
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(messageService, atLeastOnce()).sendMessage(any(), argumentCaptor.capture());
        String actualText = argumentCaptor.getValue();
        assertThat(actualText).isEqualTo(text);
    }

    @Test
    void doActionCallVisitor() {
        Volunteer volunteer = new Volunteer();
        volunteer.setAction(CALL_VISITOR);
        when(volunteerRepository.findByChatId(anyLong())).thenReturn(volunteer);
        Visitor visitor = new Visitor();
        visitor.setId(1L);
        when(visitorRepository.findByChatId(anyLong())).thenReturn(visitor);
        when(adoptionRepository.findByVisitorId(anyLong())).thenReturn(new Adoption());

        chatWithVolunteer.doAction(1L,"1");
        verify(adoptionRepository, atLeastOnce()).save(any());
    }

    @Test
    void doAction() {
        chatWithVolunteer.doAction(1L,"a");
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(messageService, atLeastOnce()).sendMessage(any(), argumentCaptor.capture());
        String actualText = argumentCaptor.getValue();
        assertThat(actualText).isEqualTo("Некорректный id пользователя, попробуйте еще раз.");
    }

    @Test
    void findAdoption() {
        Visitor visitor = new Visitor();
        visitor.setId(1L);

        when(visitorRepository.findByChatId(chatId)).thenReturn(visitor);
        when(adoptionRepository.findByVisitorId(any())).thenReturn(new Adoption());

        chatWithVolunteer.findAdoption(chatId);
        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(adoptionRepository, atLeastOnce()).findByVisitorId(argumentCaptor.capture());
        Long actualId = argumentCaptor.getValue();
        assertThat(actualId).isEqualTo(1L);
    }

    @Test
    void isNumericTrue() {
        assertTrue(chatWithVolunteer.isNumeric("7"));
    }

    @Test
    void isNumericFalse() {
        assertFalse(chatWithVolunteer.isNumeric("a"));
    }
}