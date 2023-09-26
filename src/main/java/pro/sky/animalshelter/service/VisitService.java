package pro.sky.animalshelter.service;

import com.pengrad.telegrambot.model.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.animalshelter.model.enums.AnimalType;
import pro.sky.animalshelter.model.Visit;
import pro.sky.animalshelter.model.Visitor;
import pro.sky.animalshelter.repository.VisitRepository;

import java.time.LocalDate;
import java.util.Comparator;

/**
 * Сервис, для работы с визит посетителя в определенный питомник
 */
@Service
public class VisitService {
    private final Logger logger = LoggerFactory.getLogger(VisitService.class);

    private final VisitRepository visitRepository;
    private final VisitorService visitorService;
    private final ShelterService shelterService;

    public VisitService(VisitRepository visitRepository,
                        VisitorService visitorService,
                        ShelterService shelterService) {
        this.visitRepository = visitRepository;
        this.visitorService = visitorService;
        this.shelterService = shelterService;
    }

    /**
     * Сохранение визита с идентификатором посетитля и типом приюта
     * @param update
     */
    public void addVisit(Update update) {
        Visit visit = new Visit();
        visit.setVisitDate(LocalDate.now());
        visit.setVisitor(visitorService.getVisitor(update));
        visit.setShelter(
                shelterService.getShelterByType(
                        AnimalType.valueOf(update.callbackQuery().data().toUpperCase())
                ));
        visitRepository.save(visit);
    }

    public Visit getVisit(Update update) {
        var chatId = update.message() != null
                ? update.message().chat().id()
                : update.callbackQuery().from().id();

        return visitRepository.findAllById(chatId).stream()
                .sorted(Comparator.comparing(v -> ((Visit)v).getVisitDate()).reversed())
                .findFirst().orElse(null);
    }

    public Visit getCurrentVisitByVisitorId(Visitor visitor) {
        return visitRepository.findByVisitor(visitor.getId());
    }
}
