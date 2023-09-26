package pro.sky.animalshelter;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ConfigurableApplicationContext;
import pro.sky.animalshelter.config.TelegramBotConfiguration;
import pro.sky.animalshelter.controller.AdoptionController;
import pro.sky.animalshelter.controller.AnimalController;
import pro.sky.animalshelter.controller.ShelterController;
import pro.sky.animalshelter.controller.VolunteerController;
import pro.sky.animalshelter.listener.ChatWithVolunteer;
import pro.sky.animalshelter.listener.TelegramBotUpdateListener;
import pro.sky.animalshelter.service.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class AnimalShelterApplicationTest {
    @MockBean
    private AdoptionController adoptionController;
    @MockBean
    private AnimalController animalController;
    @MockBean
    private ShelterController shelterController;
    @MockBean
    private VolunteerController volunteerController;
    @MockBean
    private TelegramBotConfiguration telegramBotConfiguration;
    @MockBean
    private ChatWithVolunteer chatWithVolunteer;
    @MockBean
    private TelegramBotUpdateListener telegramBotUpdateListener;
    @MockBean
    private MenuService menuService;
    @MockBean
    private MessageService messageService;
    @MockBean
    private ShelterService shelterService;
    @MockBean
    private VisitorService visitorService;
    @MockBean
    private VisitService visitService;
    @MockBean
    private VolunteerService volunteerService;
    @InjectMocks
    private AnimalShelterApplication animalShelterApplication;

    @Test
    public void testMain() {
        try (MockedStatic<SpringApplication> mocked = mockStatic(SpringApplication.class)) {

            mocked.when(() -> { SpringApplication.run(AnimalShelterApplication.class,
                            new String[] {"cat", "dog"});})
                    .thenReturn(mock(ConfigurableApplicationContext.class));

            AnimalShelterApplication.main(new String[] {"cat", "dog"});

            mocked.verify(() -> { SpringApplication.run(AnimalShelterApplication.class,
                    new String[] {"cat", "dog"}); });

        }
    }

}