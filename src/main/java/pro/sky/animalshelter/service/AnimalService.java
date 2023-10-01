package pro.sky.animalshelter.service;

import org.springframework.stereotype.Service;
import pro.sky.animalshelter.model.Animal;
import pro.sky.animalshelter.repository.AnimalRepository;

import java.util.Collection;

@Service
public class AnimalService {
    private final AnimalRepository animalRepository;

    public AnimalService(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    public Animal getById(Long id) {
        return animalRepository.findById(id).get();
    }

    public Collection<Animal> getBydShelterId(Long shelterId) {
        return animalRepository.findByShelterId(shelterId);
    }

    public Animal createAnimal(Animal animal) {
        return animalRepository.save(animal);
    }
}
