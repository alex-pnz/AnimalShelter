package pro.sky.animalshelter;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import pro.sky.animalshelter.controller.VolunteerController;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AnimalShelterApplicationTest {
    @LocalServerPort
    private int port;

    @Autowired
    private VolunteerController volunteerController;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void testLoadContext() {
//        Assertions.assertThat(volunteerController).isNotNull();
    }

}