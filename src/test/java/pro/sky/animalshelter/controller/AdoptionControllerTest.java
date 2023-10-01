package pro.sky.animalshelter.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pro.sky.animalshelter.model.Adoption;
import pro.sky.animalshelter.service.AdoptionService;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdoptionController.class)
public class AdoptionControllerTest {
    private String url = "/api/v1/adoption";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AdoptionService adoptionService;
    @InjectMocks
    private AdoptionController adoptionController;

    @Test
    public void getAdoptionTest() throws Exception {
        MvcResult result = mockMvc.perform(get(url + "/show-adoptions"))
                .andExpect(status().isOk())
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();
        List<Adoption> actual = mapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<Adoption>>() {});

        assertIterableEquals(Collections.emptyList(), actual);
        verify(adoptionService, atLeastOnce()).allAdoptions();
    }

    @Test
    public void getAdoptionByIdTest() throws Exception {
        when(adoptionService.findById(any())).thenReturn(new Adoption());
        MvcResult result = mockMvc.perform(get(url + "/show-adoption/1", 0))
                .andExpect(status().isOk())
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();
        Adoption adoption = mapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<Adoption>() {});

        assertEquals(new Adoption(), adoption);

        verify(adoptionService, atLeastOnce()).findById(any());
    }

    @Test
    public void postAnimalAdoptionTest() throws Exception {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String request = ow.writeValueAsString(new Adoption());
        when(adoptionService.createAdoption(any())).thenReturn(new Adoption());

        MvcResult result = mockMvc.perform(post(url + "/adopt")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();

        Adoption adoption = mapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<Adoption>() {});

        assertEquals(new Adoption(), adoption);
        verify(adoptionService, atLeastOnce()).createAdoption(any());
    }

}
