package pro.sky.animalshelter.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pro.sky.animalshelter.model.Adoption;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdoptionController.class)
public class AdoptionControllerTest {
    private String url = "/api/v1/adoption";

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getAdoptionTest() throws Exception {
        MvcResult result = mockMvc.perform(get(url + "/show-adoptions"))
                .andExpect(status().isOk())
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();
        List<Adoption> actual = mapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<Adoption>>() {});

        assertIterableEquals(Collections.emptyList(), actual);
    }

    @Test
    public void getAdoptionByIdTest() throws Exception {
        MvcResult result = mockMvc.perform(get(url + "/show-adoption/{id}", 0))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();
        Adoption adoption = mapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<Adoption>() {});

        assertEquals(new Adoption(), adoption);
    }

    @Test
    public void postAnimalAdoptionTest() throws Exception {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String request = ow.writeValueAsString(new Adoption());

        MvcResult result = mockMvc.perform(post(url + "/adopt")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();
        Adoption adoption = mapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<Adoption>() {});

        assertEquals(new Adoption(), adoption);
    }

}
