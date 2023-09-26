package pro.sky.animalshelter.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pro.sky.animalshelter.model.Animal;
import pro.sky.animalshelter.model.Shelter;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ShelterController.class)
class ShelterControllerTest {

    private final String url = "/api/v1/shelter";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testWelcome() throws Exception {
        MvcResult result = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andReturn();

        Assertions.assertThat(result.getResponse().getContentAsString())
                .isEqualTo("Welcome to shelter management portal");
    }

    @Test
    void testGetShelterInfo() throws Exception {
        MvcResult result = mockMvc.perform(get(url + "/{id}", 1))
                .andExpect(status().isOk())
                .andReturn();

        Assertions.assertThat(result.getResponse().getContentAsString())
                .isEqualTo(new ObjectMapper().writeValueAsString(new Shelter()));


    }

    @Test
    void testAddShelter() throws Exception {
        String request = new ObjectMapper().writeValueAsString(new Shelter());

        MvcResult result = mockMvc.perform(post(url + "/add-shelter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(request, result.getResponse().getContentAsString());
    }
}