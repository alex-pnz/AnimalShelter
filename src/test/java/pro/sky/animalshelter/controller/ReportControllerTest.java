package pro.sky.animalshelter.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pro.sky.animalshelter.model.Report;
import pro.sky.animalshelter.service.ReportService;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReportController.class)
class ReportControllerTest {
    private String url = "/api/v1/reports";
    @MockBean
    private ReportService reportService;
    @InjectMocks
    private ReportController reportController;
    @Autowired
    private MockMvc mockMvc;
    @Test
    void getTodayReports() throws Exception {
        MvcResult result = mockMvc.perform(get(url + "/today"))
                .andExpect(status().isOk())
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();
        List<Report> actual = mapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<Report>>() {});

        assertIterableEquals(Collections.emptyList(), actual);

        verify(reportService, atLeastOnce()).getTodayReport();
    }

    @Test
    void getReportsByAdoption() throws Exception {
        MvcResult result = mockMvc.perform(get(url + "/adoption/1"))
                .andExpect(status().isOk())
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();
        List<Report> actual = mapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<Report>>() {});

        assertIterableEquals(Collections.emptyList(), actual);

        verify(reportService, atLeastOnce()).getReportsByAdoption(1L);
    }
}