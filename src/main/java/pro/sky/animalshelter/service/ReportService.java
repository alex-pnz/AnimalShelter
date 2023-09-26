package pro.sky.animalshelter.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.animalshelter.listener.TelegramBotUpdateListener;
import pro.sky.animalshelter.model.Adoption;
import pro.sky.animalshelter.model.Report;
import pro.sky.animalshelter.repository.AdoptionRepository;
import pro.sky.animalshelter.repository.ReportRepository;

import java.time.LocalDate;
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
    private final TelegramBotUpdateListener listener;

    public ReportService(ReportRepository reportRepository,
                         AdoptionRepository adoptionRepository,
                         TelegramBotUpdateListener listener) {
        this.reportRepository = reportRepository;
        this.adoptionRepository = adoptionRepository;
        this.listener = listener;
    }

    /**
     * Метод напоминания о необходимости выслать отчет о состоянии зверушки
     */
    @Scheduled(cron = "0 0 19 * * ?")
    public void sendNotification() {
        LocalDate today = LocalDate.now();
        LocalDate fromDate = today.minusDays(30);

        List<Adoption> adoptions = adoptionRepository.findByAdoptionDateLessThanEqual(fromDate);
        List<Report> reports = reportRepository.findReportByDate(today);

        for (var adoption : adoptions) {
            boolean found = false;
            for (var report : reports) {
                if (adoption.getId().equals(report.getAdoption().getId())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                listener.sendGenericMessage(adoption.getVisitor().getChatId(), REPORT_REMINDER);
            }
        }
    }
}
