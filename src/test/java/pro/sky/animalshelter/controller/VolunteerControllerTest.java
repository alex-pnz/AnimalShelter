package pro.sky.animalshelter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pro.sky.animalshelter.model.Shelter;
import pro.sky.animalshelter.model.Volunteer;
import pro.sky.animalshelter.service.VolunteerService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VolunteerController.class)
class VolunteerControllerTest {
    private final String url = "/api/v1/volunteer";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private VolunteerService volunteerService;
    @InjectMocks
    private VolunteerController volunteerController;
    private final Long id = 1L;

    @Test
    void testGetVolunteerNotFound() throws Exception {

        when(volunteerService.findVolunteer(id)).thenReturn(null);
        MvcResult result = mockMvc.perform(get(url + "/{id}", id))
                .andExpect(status().isNotFound())
                .andReturn();

        Assertions.assertThat(result.getResponse().getContentAsString()).isEmpty();
    }
    @Test
    void testGetVolunteerFound() throws Exception {
        String request = new ObjectMapper().writeValueAsString(new Volunteer(1L, 123L, "Test Volunteer Name", true, null));
        when(volunteerService.findVolunteer(id)).thenReturn(new Volunteer(1L, 123L, "Test Volunteer Name", true, null));

        MvcResult result = mockMvc.perform(get(url + "/{id}", id))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(request, result.getResponse().getContentAsString());
    }

    @Test
    void testCreateVolunteer() throws Exception {
        String request = new ObjectMapper().writeValueAsString(new Volunteer(1L, 123L, "Test Volunteer Name", true, null));

        mockMvc.perform(MockMvcRequestBuilders.post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk());
    }

    @Test
    void testEditVolunteerNotFound() throws Exception {
        when(volunteerService.editVolunteer(any())).thenReturn(null);
        MvcResult result = mockMvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(new Volunteer())))
                .andExpect(status().isNotFound())
                .andReturn();

        Assertions.assertThat(result.getResponse().getContentAsString()).isEmpty();
    }

    @Test
    void testEditVolunteerFound() throws Exception {
        Volunteer volunteer = new Volunteer(1L, 123L, "Test Volunteer Name", true, null);

        String request = new ObjectMapper().writeValueAsString(volunteer);

        when(volunteerService.editVolunteer(any())).thenReturn(volunteer);
        MvcResult result = mockMvc.perform(put(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(request, result.getResponse().getContentAsString());
    }

    @Test
    void testDeleteVolunteer() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(url + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}