package pro.sky.animalshelter.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.animalshelter.model.Adoption;
import pro.sky.animalshelter.model.Report;
import pro.sky.animalshelter.model.Visitor;
import pro.sky.animalshelter.repository.AdoptionRepository;
import pro.sky.animalshelter.repository.ReportRepository;

import java.util.ArrayList;
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
    @InjectMocks
    private ReportService reportService;
    @Test
    public void sendNotificationFalse() {
        Adoption adoption = new Adoption();
        adoption.setId(1L);
        Visitor visitor = new Visitor();
        visitor.setId(123L);
        adoption.setVisitor(visitor);
        Report report = new Report();
        report.setAdoption(adoption);

        List<Report> reports = new ArrayList<>();
        reports.add(report);
        adoption.setReports(reports);

        List<Adoption> adoptions = new ArrayList<>();
        adoptions.add(adoption);

        when(adoptionRepository.findByStatus(any())).thenReturn(adoptions);
        when(reportRepository.findReportByDate(any())).thenReturn(reports);

        reportService.sendNotification();
        verify(messageService, never()).sendMessage(anyLong(),anyString());
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

        List<Report> reports = new ArrayList<>();
        reports.add(report);
        adoption.setReports(reports);

        List<Adoption> adoptions = new ArrayList<>();
        adoptions.add(adoption);

        when(adoptionRepository.findByStatus(any())).thenReturn(adoptions);
        when(reportRepository.findReportByDate(any())).thenReturn(reports);

        reportService.sendNotification();
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        verify(messageService, atLeastOnce()).sendMessage(any(), argumentCaptor.capture());
        String actual = argumentCaptor.getValue();
        assertThat(actual).isEqualTo(REPORT_REMINDER);

    }

}