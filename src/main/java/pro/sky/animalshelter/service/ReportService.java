package pro.sky.animalshelter.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.animalshelter.model.Adoption;
import pro.sky.animalshelter.model.Report;
import pro.sky.animalshelter.model.Volunteer;
import pro.sky.animalshelter.model.enums.ProbationTermsStatus;
import pro.sky.animalshelter.repository.AdoptionRepository;
import pro.sky.animalshelter.repository.ReportRepository;
import pro.sky.animalshelter.repository.VolunteerRepository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

/**
 * Сервис для работы с отчетами об усыновлении зверушек
 */
@Service
public class ReportService {
    private final Logger logger = LoggerFactory.getLogger(ReportService.class);
    public static final String REPORT_REMINDER = "Нехороший человек, шли отчет сюда!";
    private final ReportRepository reportRepository;
    private final AdoptionRepository adoptionRepository;
    private final MessageService messageService;
    private final VolunteerRepository volunteerRepository;

    public ReportService(ReportRepository reportRepository,
                         AdoptionRepository adoptionRepository, MessageService messageService, VolunteerRepository volunteerRepository) {
        this.reportRepository = reportRepository;
        this.adoptionRepository = adoptionRepository;
        this.messageService = messageService;
        this.volunteerRepository = volunteerRepository;
    }

    /**
     * Метод напоминания о необходимости выслать отчет о состоянии зверушки
     */
    @Scheduled(cron = "0 0 19 * * ?")
    public void sendNotification() {
        LocalDate today = LocalDate.now();

        List<Adoption> adoptions = adoptionRepository.findByStatus(ProbationTermsStatus.IN_PROGRESS);
        List<Report> reports = reportRepository.findReportByDate(today);

        for (var adoption : adoptions) {
            List<Report> reportsByAdoption = reportRepository.findReportByAdoptionId(adoption.getId()).stream().toList();
            Report lastReport=reportsByAdoption.get(reportsByAdoption.size()-1);
            if(lastReport.getDate().isBefore(LocalDate.now().minusDays(2))){
                Volunteer volunteer = volunteerRepository.getByIsFree(true);
                Long chatId = adoption.getVisitor().getChatId();
                messageService.sendMessage(volunteer.getChatId(),"Усыновитель " + chatId + " давно не оптравлял отчет, узнайте что случилось!");
            }
            boolean found = false;
            for (var report : reports) {
                if (adoption.getId().equals(report.getAdoption().getId())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                messageService.sendMessage(adoption.getVisitor().getChatId(), REPORT_REMINDER);
            }
        }
    }

    public Collection<Report> getTodayReport() {
        return reportRepository.findReportByDate(LocalDate.now());
    }

    public Collection<Report> getReportsByAdoption(Long id) {
        return reportRepository.findReportByAdoptionId(id);
    }
}
