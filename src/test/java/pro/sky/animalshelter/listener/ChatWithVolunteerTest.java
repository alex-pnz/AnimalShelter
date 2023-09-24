package pro.sky.animalshelter.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.animalshelter.model.Visitor;
import pro.sky.animalshelter.model.Volunteer;
import pro.sky.animalshelter.repository.VisitorRepository;
import pro.sky.animalshelter.repository.VolunteerRepository;
import pro.sky.animalshelter.service.MessageService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static pro.sky.animalshelter.utils.Constants.SECURITY_CONTACT_INFO;

@ExtendWith(MockitoExtension.class)
class ChatWithVolunteerTest {

    @Mock
    private VolunteerRepository volunteerRepository;
    @Mock
    private VisitorRepository visitorRepository;
    @Mock
    private MessageService messageService;
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
}