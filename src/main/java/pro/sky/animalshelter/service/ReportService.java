package pro.sky.animalshelter.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.File;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.response.GetFileResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.animalshelter.model.Adoption;
import pro.sky.animalshelter.model.Report;
import pro.sky.animalshelter.model.Visitor;
import pro.sky.animalshelter.model.Volunteer;
import pro.sky.animalshelter.model.enums.ProbationTermsStatus;
import pro.sky.animalshelter.repository.AdoptionRepository;
import pro.sky.animalshelter.repository.ReportRepository;
import pro.sky.animalshelter.repository.VolunteerRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Arrays;
import java.util.List;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

/**
 * Сервис для работы с отчетами об усыновлении зверушек
 */
@Service
public class ReportService {
    private final Logger logger = LoggerFactory.getLogger(ReportService.class);

    @Value("${animal.photo.dir.path}")
    private String imagePath;

    public static final String REPORT_REMINDER = "Нехороший человек, шли отчет сюда!";
    private final ReportRepository reportRepository;
    private final AdoptionRepository adoptionRepository;
    private final MessageService messageService;
    private final VisitorService visitorService;
    private final TelegramBot bot;
    private final VolunteerRepository volunteerRepository;

    public ReportService(ReportRepository reportRepository, TelegramBot bot,
                         AdoptionRepository adoptionRepository, MessageService messageService,
                         VisitorService visitorService, VolunteerRepository volunteerRepository) {
        this.reportRepository = reportRepository;
        this.bot = bot;
        this.adoptionRepository = adoptionRepository;
        this.messageService = messageService;
        this.visitorService = visitorService;
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

    /**
     * Метод проверки отчета
     */
    public boolean checkReport(Update update) {
        if (update.message().photo() == null) {
            messageService.reportFailureNoPicture(update.message().chat().id());
            return false;
        } else if ("".equals(update.message().caption().substring(5))) {
            messageService.reportFailureNoText(update.message().chat().id());
            return false;
        }
        saveReport(update);
        return true;
    }

    /**
     * Метод сохранения отчета
     * @param update
     */
    private void saveReport(Update update) {
        // saving image to file system
        String imageLocation = "";
        try {
            imageLocation = getImage(update);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // getting data from caption
        String diet = getDietFromCaption(update.message().caption());
        String behaviour = getBehaviourFromCaption(update.message().caption());
        String overallHealth = getOverallHealthFromCaption(update.message().caption());

        // saving report
        Visitor visitor = visitorService.findVisitor(update);
        Adoption adoption = adoptionRepository.findByVisitorId(visitor.getId());
        Report report = new Report();
        report.setImage(imageLocation);
        report.setAdoption(adoption);
        report.setDate(LocalDate.now());
        report.setDiet(diet);
        report.setBehaviour(behaviour);
        report.setOverallHealth(overallHealth);
        reportRepository.save(report);
    }

    private String getImage(Update update) throws IOException {
        LocalDate currentDate = LocalDate.now();
        // getting file from bot
        var photoSizes = update.message().photo();
        var fileId = photoSizes[0].fileId();
        GetFile request = new GetFile(fileId);
        GetFileResponse response = bot.execute(request);
        File file = response.file();
        var img = bot.getFileContent(file);
        // writing file to disk
        Path filePath = Path.of(imagePath, update.message().chat().id() + "-" + currentDate.toString()
                + "." + file.filePath().substring(file.filePath().lastIndexOf(".") + 1));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        var out = Files.newOutputStream(filePath, CREATE_NEW);
        out.write(img);
        out.close();
        return filePath.toString();
    }

    private String getDietFromCaption(String cap) {
        String[] contents = cap.split("\\n");
        String result = Arrays.stream(contents)
                .filter(s -> s.startsWith("Диета"))
                .findFirst()
                .orElse("");
        if (!result.isBlank()) {
            result = result.substring("Диета".length() + 1).trim();
        }
        return result;
    }

    private String getBehaviourFromCaption(String cap) {
        String[] contents = cap.split("\\n");
        String result = Arrays.stream(contents)
                .filter(s -> s.startsWith("Поведение"))
                .findFirst()
                .orElse("");
        if (!result.isBlank()) {
            result = result.substring("Поведение".length() + 1).trim();
        }
        return result;
    }

    private String getOverallHealthFromCaption(String cap) {
        String[] contents = cap.split("\\n");
        String result = Arrays.stream(contents)
                .filter(s -> s.startsWith("Общее состояние"))
                .findFirst()
                .orElse("");
        if (!result.isBlank()) {
            result = result.substring("Общее состояние".length() + 1).trim();
        } else {
            result = cap;
        }
        return result;
    }
}
