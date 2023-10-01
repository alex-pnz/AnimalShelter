package pro.sky.animalshelter.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.animalshelter.model.Adoption;
import pro.sky.animalshelter.model.Animal;
import pro.sky.animalshelter.model.Report;
import pro.sky.animalshelter.model.enums.AnimalType;
import pro.sky.animalshelter.model.enums.ProbationTermsStatus;
import pro.sky.animalshelter.repository.AdoptionRepository;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdoptionServiceTest {
    @Mock
    private AdoptionRepository adoptionRepository;
    @InjectMocks
    private AdoptionService adoptionService;

    private Adoption adoption;

    @BeforeEach
    public void setAdoption() {
        Animal animal = new Animal();
        animal.setId(1L);
        animal.setName("Animal name");
        animal.setAnimalType(AnimalType.CAT);

        adoption = new Adoption();
        adoption.setId(1L);
        adoption.setAnimal(animal);
        adoption.setAdoptionDate(LocalDate.of(2023, Month.SEPTEMBER,22));
        adoption.setReportsCount(1);
        Collection<Report> reports = new ArrayList<>();
        reports.add(new Report());
        adoption.setReports(reports);
    }
    @Test
    void createAdoption() {
        when(adoptionRepository.save(any())).thenReturn(adoption);

        Adoption expectedAdoption = adoptionService.createAdoption(any());

        assertEquals(expectedAdoption.getAdoptionDate(), adoption.getAdoptionDate());
        assertEquals(expectedAdoption.getAnimal().getName(), "Animal name");
        assertEquals(expectedAdoption.getReportsCount(), 1);
        assertEquals(expectedAdoption.getReports().size(), 1);
        assertEquals(expectedAdoption.getStatus(), ProbationTermsStatus.IN_PROGRESS);
    }

    @Test
    void allAdoptions() {
        List<Adoption> adoptions = new ArrayList<>();
        adoptions.add(adoption);
        when(adoptionRepository.findAll()).thenReturn(adoptions);

        Collection<Adoption> expectedAdoptions = adoptionService.allAdoptions();
        assertEquals(expectedAdoptions.size(), 1);
        assertTrue(expectedAdoptions.contains(adoption));
    }

    @Test
    void findById() {
        when(adoptionRepository.findById(any())).thenReturn(Optional.of(adoption));

        Adoption expectedAdoption = adoptionService.findById(any());

        assertEquals(expectedAdoption.getAdoptionDate(), adoption.getAdoptionDate());
        assertEquals(expectedAdoption.getAnimal().getName(), "Animal name");
    }
}