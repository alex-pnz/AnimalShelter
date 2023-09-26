package pro.sky.animalshelter.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.animalshelter.model.enums.AnimalType;
import pro.sky.animalshelter.model.Shelter;
import pro.sky.animalshelter.repository.ShelterRepository;

/**
 * Класс для работы с приютами
 */
@Service
public class ShelterService {
    private final Logger logger = LoggerFactory.getLogger(ShelterService.class);
    private final ShelterRepository shelterRepository;

    public ShelterService(ShelterRepository shelterRepository) {
        this.shelterRepository = shelterRepository;
    }

    /**
     * Метод поиска приюта по идентификатору
     * @param shelterId
     * @return Shelter
     */
    public Shelter getShelterById(Long shelterId) {
        return shelterRepository.findById(shelterId).orElse(null);
    }

    /**
     * Метод поиска приюта по типу
     * TODO: Необходимо переписать при появлении нескольких приютов одного типа
     * @param type
     * @return
     */
    public Shelter getShelterByType(AnimalType type) {
        logger.info("Animal type received {}", type);
        String key = type.toString();
        return shelterRepository.findByShelterType(key).stream()
                .findFirst()
                .orElse(null);
    }
}
