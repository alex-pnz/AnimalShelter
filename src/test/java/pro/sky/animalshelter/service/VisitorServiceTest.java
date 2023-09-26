package pro.sky.animalshelter.service;

import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.model.Update;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.animalshelter.model.Visitor;
import pro.sky.animalshelter.repository.VisitorRepository;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VisitorServiceTest {

    @Mock
    private VisitorRepository visitorRepository;

    @InjectMocks
    private VisitorService visitorService;

    private final Long chatId = 123L;

    @Test
    void getVisitorMessageNewVisitor() {
        Visitor visitor = new Visitor();
        visitor.setVisitorName("Test First Name Test Last Name");
        when(visitorRepository.findByChatId(any())).thenReturn(null);
        when(visitorRepository.save(any())).thenReturn(visitor);

        Visitor actualVisitor = visitorService.getVisitor(getUpdate(true));

        Assertions.assertThat(actualVisitor.getVisitorName()).isEqualTo("Test First Name Test Last Name");
        verify(visitorRepository, atLeastOnce()).findByChatId(chatId);
        verify(visitorRepository, atLeastOnce()).save(any());
    }

    @Test
    void getVisitorMessageExistingVisitor() {
        Visitor visitor = new Visitor();
        visitor.setVisitorName("Test First Name Test Last Name");
        when(visitorRepository.findByChatId(any())).thenReturn(visitor);

        Visitor actualVisitor = visitorService.getVisitor(getUpdate(true));

        Assertions.assertThat(actualVisitor.getVisitorName()).isEqualTo("Test First Name Test Last Name");
        verify(visitorRepository, atLeastOnce()).findByChatId(chatId);
    }

    @Test
    void getVisitorCallbackNewVisitor() {
        Visitor visitor = new Visitor();
        visitor.setVisitorName("Test First Name Test Last Name");
        when(visitorRepository.findByChatId(any())).thenReturn(null);
        when(visitorRepository.save(any())).thenReturn(visitor);

        Visitor actualVisitor = visitorService.getVisitor(getUpdate(false));

        Assertions.assertThat(actualVisitor.getVisitorName()).isEqualTo("Test First Name Test Last Name");
        verify(visitorRepository, atLeastOnce()).findByChatId(chatId);
        verify(visitorRepository, atLeastOnce()).save(any());
    }

    @Test
    void getVisitorCallbackExistingVisitor() {
        Visitor visitor = new Visitor();
        visitor.setVisitorName("Test First Name Test Last Name");
        when(visitorRepository.findByChatId(any())).thenReturn(visitor);

        Visitor actualVisitor = visitorService.getVisitor(getUpdate(false));

        Assertions.assertThat(actualVisitor.getVisitorName()).isEqualTo("Test First Name Test Last Name");
        verify(visitorRepository, atLeastOnce()).findByChatId(chatId);
    }

    static Stream<Arguments> provideParametersFindVisitorNewUser() {
        return Stream.of(
                Arguments.of(true),
                Arguments.of(false)
        );
    }

    @ParameterizedTest
    @MethodSource("provideParametersFindVisitorNewUser")
    void findVisitorNewUser(boolean message) {
        when(visitorRepository.findByChatId(any())).thenReturn(null);

        assertNull(visitorService.findVisitor(getUpdate(message)));
    }

    static Stream<Arguments> provideParametersFindVisitorMessageExistingUser() {
        return Stream.of(
                Arguments.of(true),
                Arguments.of(false)
        );
    }

    @ParameterizedTest
    @MethodSource("provideParametersFindVisitorMessageExistingUser")
    void findVisitorMessageExistingUser(boolean message) {
        Visitor visitor = new Visitor();
        visitor.setVisitorName("Test First Name Test Last Name");
        when(visitorRepository.findByChatId(anyLong())).thenReturn(visitor);

        Visitor actualVisitor = visitorService.findVisitor(getUpdate(message));
        Assertions.assertThat(actualVisitor.getVisitorName()).isEqualTo("Test First Name Test Last Name");
        verify(visitorRepository, atLeastOnce()).findByChatId(chatId);
    }

    static Stream<Arguments> provideParametersCreateVisitor() {
        return Stream.of(
                Arguments.of(true),
                Arguments.of(false)
        );
    }

    @ParameterizedTest
    @MethodSource("provideParametersCreateVisitor")
    void createVisitor(boolean message) {
        Visitor visitor = new Visitor();
        visitor.setVisitorName("Test First Name Test Last Name");
        when(visitorRepository.save(any())).thenReturn(visitor);

        Visitor actualVisitor = visitorService.createVisitor(getUpdate(message));
        Assertions.assertThat(actualVisitor.getVisitorName()).isEqualTo("Test First Name Test Last Name");
        verify(visitorRepository, atLeastOnce()).save(any());
    }

    // Вспомогательный метод
    private Update getUpdate(boolean message) {
        String json = null;

        if(message){
            json = """
                {
                  "message": {
                    "chat": {
                      "id": 123,
                      "first_name": "Test First Name",
                      "last_name": "Test Last Name"
                    }
                  }
                }
                    """;
        } else {
            json = """
                {
                  "callback_query": {
                    "from": {
                      "id": 123,
                      "first_name": "Test First Name",
                      "last_name": "Test Last Name"
                    }
                  }
                }
                """;
        }

        return BotUtils.fromJson(json, Update.class);
    }
}