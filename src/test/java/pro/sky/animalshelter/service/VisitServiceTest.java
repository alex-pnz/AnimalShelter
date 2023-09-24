package pro.sky.animalshelter.service;

import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.animalshelter.model.Shelter;
import pro.sky.animalshelter.model.Visit;
import pro.sky.animalshelter.model.Visitor;
import pro.sky.animalshelter.repository.VisitRepository;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VisitServiceTest {
    @Mock
    private VisitRepository visitRepository;
    @Mock
    private VisitorService visitorService;
    @Mock
    private ShelterService shelterService;
    @InjectMocks
    private VisitService visitService;

    @Test
    void addVisit() {

        Visitor visitor = new Visitor();
        visitor.setVisitorName("Visitor Test Name");
        Shelter shelter = new Shelter();
        shelter.setShelterName("Shelter Test Name");
        when(visitorService.getVisitor(any())).thenReturn(visitor);
        when(shelterService.getShelterByType(any())).thenReturn(shelter);

        visitService.addVisit(getUpdate("dog", false));

        ArgumentCaptor<Visit> argumentCaptor = ArgumentCaptor.forClass(Visit.class);
        verify(visitRepository, atLeastOnce()).save(argumentCaptor.capture());
        Visit actual = argumentCaptor.getValue();

        Assertions.assertThat(actual.getVisitor().getVisitorName()).isEqualTo("Visitor Test Name");
        Assertions.assertThat(actual.getShelter().getShelterName()).isEqualTo("Shelter Test Name");

    }

    static Stream<Arguments> provideParametersGetVisit() {
        return Stream.of(
                Arguments.of(true),
                Arguments.of(false)
        );
    }

    @ParameterizedTest
    @MethodSource("provideParametersGetVisit")
    void testGetVisit(boolean message) {

        Visit visit1 = new Visit();
        visit1.setVisitDate(LocalDate.of(2023, Month.SEPTEMBER, 22));
        Visit visit2 = new Visit();
        visit2.setVisitDate(LocalDate.of(2023, Month.SEPTEMBER, 12));

        List<Visit> visits = new ArrayList<>();
        visits.add(visit1);
        visits.add(visit2);
        when(visitRepository.findAllById(anyLong())).thenReturn(visits);

        Visit actual = visitService.getVisit(getUpdate("test", message));

        assertNotNull(actual);
        verify(visitRepository, atLeastOnce()).findAllById(123L);
        Assertions.assertThat(actual.getVisitDate()).isEqualTo(LocalDate.of(2023, Month.SEPTEMBER, 22));
    }

    static Stream<Arguments> provideParametersGetVisitNull() {
        return Stream.of(
                Arguments.of(true),
                Arguments.of(false)
        );
    }

    @ParameterizedTest
    @MethodSource("provideParametersGetVisitNull")
    void testGetVisitNull(boolean message) {

        List<Visit> visits = new ArrayList<>();
        when(visitRepository.findAllById(anyLong())).thenReturn(visits);

        Visit actual = visitService.getVisit(getUpdate("test", message));

        assertNull(actual);
        verify(visitRepository, atLeastOnce()).findAllById(123L);

    }

    @Test
    void testGetCurrentVisitByVisitorId() {
        Visitor visitor = new Visitor();
        visitor.setId(123L);
        when(visitRepository.findByVisitor(visitor.getId())).thenReturn(new Visit());

        Visit actual = visitService.getCurrentVisitByVisitorId(visitor);

        assertNotNull(actual);
        verify(visitRepository, atLeastOnce()).findByVisitor(any());
    }

    @Test
    void testGetCurrentVisitByVisitorIdNull() {
        Visitor visitor = new Visitor();
        visitor.setId(123L);
        when(visitRepository.findByVisitor(visitor.getId())).thenReturn(null);

        Visit actual = visitService.getCurrentVisitByVisitorId(visitor);

        assertNull(actual);
        verify(visitRepository, atLeastOnce()).findByVisitor(any());
    }

    // Вспомогательный метод
    private Update getUpdate(String command, boolean message) {
        String json = null;

        if (message) {
            json = """
                    {
                      "message": {
                        "chat": {
                          "id": 123
                        },
                        "text": "%text%"
                      }
                    }
                        """;
        } else {
            json = """
                    {
                      "callback_query": {
                        "from": {
                          "id": 123
                        },
                        "data": "%text%"
                      }
                    }
                        """;
        }

        return BotUtils.fromJson(json.replace("%text%", command), Update.class);
    }
}