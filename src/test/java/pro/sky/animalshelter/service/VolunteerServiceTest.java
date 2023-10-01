package pro.sky.animalshelter.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.animalshelter.model.Volunteer;
import pro.sky.animalshelter.model.enums.Action;
import pro.sky.animalshelter.repository.VolunteerRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VolunteerServiceTest {
    @Mock
    private VolunteerRepository volunteerRepository;

    @InjectMocks
    private VolunteerService volunteerService;

    private final Long volunteerId = 1L;
    private final Long chatId = 123L;


    @Test
    void testCreateVolunteer() {
        Volunteer volunteer = new Volunteer(volunteerId,chatId,"Volunteer Test Name", true, null);
        when(volunteerRepository.save(any())).thenReturn(volunteer);

        Volunteer actualVolunteer = volunteerService.createVolunteer(volunteer);

        verify(volunteerRepository, atLeastOnce()).save(volunteer);

        Assertions.assertThat(actualVolunteer.getId()).isEqualTo(volunteerId);
        Assertions.assertThat(actualVolunteer.getChatId()).isEqualTo(chatId);
        Assertions.assertThat(actualVolunteer.getFirstName()).isEqualTo("Volunteer Test Name");
        assertTrue(actualVolunteer.isFree());
    }

    @Test
    void testFindVolunteer() {
        Volunteer volunteer = new Volunteer(volunteerId,chatId,"Volunteer Test Name", true, null);
        when(volunteerRepository.findById(volunteerId)).thenReturn(Optional.of(volunteer));

        Volunteer actualVolunteer = volunteerService.findVolunteer(volunteerId);

        verify(volunteerRepository, atLeastOnce()).findById(volunteerId);

        Assertions.assertThat(actualVolunteer.getId()).isEqualTo(volunteerId);
        Assertions.assertThat(actualVolunteer.getChatId()).isEqualTo(chatId);
        Assertions.assertThat(actualVolunteer.getFirstName()).isEqualTo("Volunteer Test Name");
        assertTrue(actualVolunteer.isFree());
    }

    @Test
    void testFindVolunteerNull() {

        when(volunteerRepository.findById(volunteerId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> volunteerService.findVolunteer(volunteerId));

        verify(volunteerRepository, atLeastOnce()).findById(volunteerId);

    }

    @Test
    void editVolunteer() {
        Volunteer volunteer = new Volunteer(volunteerId,chatId,"Volunteer Test Name", true, null);
        when(volunteerRepository.save(any())).thenReturn(volunteer);

        Volunteer actualVolunteer = volunteerService.editVolunteer(volunteer);

        verify(volunteerRepository, atLeastOnce()).save(volunteer);

        Assertions.assertThat(actualVolunteer.getId()).isEqualTo(volunteerId);
        Assertions.assertThat(actualVolunteer.getChatId()).isEqualTo(chatId);
        Assertions.assertThat(actualVolunteer.getFirstName()).isEqualTo("Volunteer Test Name");
        assertTrue(actualVolunteer.isFree());
    }

    @Test
    void deleteVolunteer() {
        doNothing().when(volunteerRepository).deleteById(volunteerId);

        volunteerService.deleteVolunteer(volunteerId);

        verify(volunteerRepository, atLeastOnce()).deleteById(volunteerId);

    }
    @Test
    public void setVolunteerFree() {
        Volunteer volunteer = new Volunteer(volunteerId,chatId,"Volunteer Test Name", false, null);
        when(volunteerRepository.findByChatId(chatId)).thenReturn(volunteer);
        volunteerService.setVolunteerFree(chatId,true);
        assertTrue(volunteer.isFree());
    }
    @Test
    public void isVolunteerTrue() {
        Volunteer volunteer = new Volunteer(volunteerId,chatId,"Volunteer Test Name", false, null);
        when(volunteerRepository.findByChatId(chatId)).thenReturn(volunteer);
        assertTrue(volunteerService.isVolunteer(chatId));
    }

    @Test
    public void saveAction() {
        Volunteer volunteer = new Volunteer(volunteerId,chatId,"Volunteer Test Name", false, null);
        when(volunteerRepository.findByChatId(chatId)).thenReturn(volunteer);

        volunteerService.saveAction(chatId, Action.ADD_14_DAYS);
        verify(volunteerRepository, atLeastOnce()).save(volunteer);
    }
    @Test
    public void isActionTrue(){
        Volunteer volunteer = new Volunteer(volunteerId,chatId,"Volunteer Test Name", false, null);
        volunteer.setAction(Action.ADD_14_DAYS);
        when(volunteerRepository.findByChatId(chatId)).thenReturn(volunteer);
        assertTrue(volunteerService.isAction(chatId));
    }
    @Test
    public void isActionFalse(){
        Volunteer volunteer = new Volunteer(volunteerId,chatId,"Volunteer Test Name", false, null);
        when(volunteerRepository.findByChatId(chatId)).thenReturn(volunteer);
        assertFalse(volunteerService.isAction(chatId));
    }


}