package pro.sky.animalshelter.service;

import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.File;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.response.GetFileResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import pro.sky.animalshelter.model.Adoption;
import pro.sky.animalshelter.model.Report;
import pro.sky.animalshelter.model.Visitor;
import pro.sky.animalshelter.model.Volunteer;
import pro.sky.animalshelter.repository.AdoptionRepository;
import pro.sky.animalshelter.repository.ReportRepository;
import pro.sky.animalshelter.repository.VolunteerRepository;


import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static pro.sky.animalshelter.service.ReportService.REPORT_REMINDER;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {
    @Mock
    private AdoptionRepository adoptionRepository;
    @Mock
    private ReportRepository reportRepository;
    @Mock
    private MessageService messageService;
    @Mock
    private VolunteerRepository volunteerRepository;
    @Mock
    private TelegramBot bot;
    @Mock
    private GetFileResponse getFileResponse;
    @Mock
    private File file;
    @Mock
    private VisitorService visitorService;
    @InjectMocks
    private ReportService reportService;
    @Test
    public void sendNotificationFalse() {
        Adoption adoption = new Adoption();
        adoption.setId(1L);
        Visitor visitor = new Visitor();
        visitor.setId(1L);
        visitor.setChatId(123L);
        adoption.setVisitor(visitor);
        Report report = new Report();
        report.setAdoption(adoption);
        report.setDate(LocalDate.of(2023, Month.SEPTEMBER, 22));
        Volunteer volunteer = new Volunteer();
        volunteer.setId(1L);

        List<Report> reports = new ArrayList<>();
        reports.add(report);
        adoption.setReports(reports);

        List<Adoption> adoptions = new ArrayList<>();
        adoptions.add(adoption);

        when(adoptionRepository.findByStatus(any())).thenReturn(adoptions);
        when(reportRepository.findReportByDate(any())).thenReturn(reports);
        when(reportRepository.findReportByAdoptionId(any())).thenReturn(reports);
        when(volunteerRepository.getByIsFree(true)).thenReturn(volunteer);

        reportService.sendNotification();
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        verify(messageService, atLeastOnce()).sendMessage(any(), argumentCaptor.capture());
        String actual = argumentCaptor.getValue();
        assertThat(actual).isEqualTo("Усыновитель " + visitor.getChatId() + " давно не оптравлял отчет, узнайте что случилось!");
    }

    @Test
    public void sendNotificationEmpty() {
        List<Report> reports = new ArrayList<>();
        when(reportRepository.findReportByDate(any())).thenReturn(reports);

        reportService.sendNotification();
        verify(messageService, never()).sendMessage(anyLong(),anyString());
    }

    @Test
    public void sendNotificationTrue() {
        Adoption adoption = new Adoption();
        adoption.setId(1L);
        Visitor visitor = new Visitor();
        visitor.setId(123L);
        adoption.setVisitor(visitor);
        Report report = new Report();
        report.setAdoption(new Adoption());
        report.setDate(LocalDate.of(2023, Month.SEPTEMBER, 22));
        Volunteer volunteer = new Volunteer();
        volunteer.setId(123L);

        List<Report> reports = new ArrayList<>();
        reports.add(report);
        adoption.setReports(reports);

        List<Adoption> adoptions = new ArrayList<>();
        adoptions.add(adoption);

        when(adoptionRepository.findByStatus(any())).thenReturn(adoptions);
        when(reportRepository.findReportByDate(any())).thenReturn(reports);
        when(reportRepository.findReportByAdoptionId(any())).thenReturn(reports);
        when(volunteerRepository.getByIsFree(true)).thenReturn(volunteer);

        reportService.sendNotification();
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        verify(messageService, atLeastOnce()).sendMessage(any(), argumentCaptor.capture());
        String actual = argumentCaptor.getValue();
        assertThat(actual).isEqualTo(REPORT_REMINDER);
    }

    @Test
    public void getTodayReport() {
        List<Report> reports = new ArrayList<>();
        reports.add(new Report());
        reports.add(new Report());
        when(reportRepository.findReportByDate(any())).thenReturn(reports);

        Collection<Report> expectedReports = reportService.getTodayReport();

        assertThat(reports).containsAll(expectedReports);
    }

    @Test
    public void getReportsByAdoption() {
        List<Report> reports = new ArrayList<>();
        reports.add(new Report());
        reports.add(new Report());
        when(reportRepository.findReportByAdoptionId(any())).thenReturn(reports);

        Collection<Report> expectedReports = reportService.getReportsByAdoption(anyLong());

        assertThat(reports).containsAll(expectedReports);
    }

    @Test
    public void checkReportNoPhoto() {
        reportService.checkReport(getUpdate());
        verify(messageService, atLeastOnce()).reportFailureNoPicture(any());
        verify(messageService, never()).reportFailureNoText(any());
    }

    @Test
    public void checkReportNoText() {
        reportService.checkReport(getUpdateCaption("отчет"));
        verify(messageService, never()).reportFailureNoPicture(any());
        verify(messageService, atLeastOnce()).reportFailureNoText(any());
    }

    @Test
    public void checkSaveReport() throws IOException {

        when(bot.execute(any())).thenReturn(getFileResponse);

        when(getFileResponse.file()).thenReturn(file);
        when(file.filePath()).thenReturn("test.jpg");
        ReflectionTestUtils.setField(reportService, "imagePath", "resources/images");
        mockStatic(Files.class);
        OutputStream out = new ByteArrayOutputStream();
        when(visitorService.findVisitor(any())).thenReturn(new Visitor());

        when(Files.newOutputStream(any(), any())).thenReturn(out);
        when(bot.getFileContent(any())).thenReturn(new byte[]{});
        boolean extected = reportService.checkReport(getUpdateCaption("отчет test"));

        verify(messageService, never()).reportFailureNoPicture(any());
        verify(messageService, never()).reportFailureNoText(any());
        Assertions.assertTrue(extected);
    }

    // Вспомогательные методы

    private Update getUpdate() {
        String json = """
                {
                  "message": {
                    "chat": {
                      "id": 123
                    },
                    "text": "%text%"
                  }
                }
                """;
        return BotUtils.fromJson(json, Update.class);
    }
    private Update getUpdateCaption(String text) {
        String json = """
                {
                  "message": {
                    "chat": {
                      "id": 123
                    },
                    "caption": "%text%",
                    "photo": [{}]
                  }
                }
                """;
        return BotUtils.fromJson(json.replace("%text%", text), Update.class);
    }

}