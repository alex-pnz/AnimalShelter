package pro.sky.animalshelter.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.animalshelter.model.Animal;
import pro.sky.animalshelter.model.Shelter;
import pro.sky.animalshelter.model.enums.AnimalType;
import pro.sky.animalshelter.repository.AnimalRepository;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnimalServiceTest {
    @Mock
    private AnimalRepository animalRepository;
    @InjectMocks
    private AnimalService animalService;

    private Animal animal;

    @BeforeEach
    public void setAnimal() {
        animal = new Animal();
        animal.setId(1L);
        animal.setName("Animal name");
        animal.setAnimalType(AnimalType.CAT);
        animal.setAge(11);
        animal.setAdmissionDate(LocalDate.of(2023, Month.AUGUST,11));
        animal.setLeaveDate(LocalDate.of(2023, Month.SEPTEMBER,20));
        Shelter shelter = new Shelter();
        shelter.setShelterName("Shelter Test");
        animal.setShelter(shelter);
    }

    @Test
    void getById() {

        when(animalRepository.findById(any())).thenReturn(Optional.of(animal));

        Animal expectedAnimal = animalService.getById(any());

        assertEquals(expectedAnimal.getId(), 1L);
        assertEquals(expectedAnimal.getName(), "Animal name");
        assertEquals(expectedAnimal.getAnimalType(), AnimalType.CAT);
        assertEquals(expectedAnimal.getAge(), 11);
        assertEquals(expectedAnimal.getAdmissionDate(), LocalDate.of(2023, Month.AUGUST,11));
        assertEquals(expectedAnimal.getLeaveDate(), LocalDate.of(2023, Month.SEPTEMBER,20));
        assertEquals(expectedAnimal.getAnimalType(), animal.getAnimalType());
        assertEquals(expectedAnimal.getShelter().getShelterName(), "Shelter Test");
    }

    @Test
    void getBydShelterId() {
        List<Animal> animals = new ArrayList<>();
        animals.add(animal);
        when(animalRepository.findByShelterId(any())).thenReturn(animals);

        Collection<Animal> expectedAnimals = animalService.getBydShelterId(any());
        assertEquals(expectedAnimals.size(), 1);
        assertTrue(expectedAnimals.contains(animal));
    }

    @Test
    void createAnimal() {
        when(animalRepository.save(any())).thenReturn(animal);

        Animal expectedAnimal = animalService.createAnimal(any());

        assertEquals(expectedAnimal.getId(), animal.getId());
        assertEquals(expectedAnimal.getName(), animal.getName());
        assertEquals(expectedAnimal.getAnimalType(), animal.getAnimalType());
    }
}