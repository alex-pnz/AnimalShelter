package pro.sky.animalshelter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pro.sky.animalshelter.model.Animal;
import pro.sky.animalshelter.service.AnimalService;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AnimalController.class)
public class AnimalControllerTest {
    private String url = "/api/v1/animals";
    @MockBean
    private AnimalService animalService;
    @InjectMocks
    private AnimalController animalController;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getGreetingTest() throws Exception {
        MvcResult result = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals("Welcome to animal management portal", result.getResponse().getContentAsString());
    }

    @Test
    public void getAnimalByIdTest() throws Exception {
        when(animalService.getById(any())).thenReturn(new Animal());
        MvcResult result = mockMvc.perform(get(url + "/{id}", 1))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(new ObjectMapper().writeValueAsString(new Animal()), result.getResponse().getContentAsString());
        verify(animalService, atLeastOnce()).getById(any());
    }

    @Test
    public void getAnimalsByShelterTest() throws Exception {
        MvcResult result = mockMvc.perform(get(url + "/shelter/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertEquals(new ObjectMapper().writeValueAsString(Collections.emptyList()),
                result.getResponse().getContentAsString());
    }

    @Test
    public void postAddAnimalTest() throws Exception {
        when(animalService.createAnimal(any())).thenReturn(new Animal());
        String request = new ObjectMapper().writeValueAsString(new Animal());
        MvcResult result = mockMvc.perform(post(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(request, result.getResponse().getContentAsString());
    }
}
