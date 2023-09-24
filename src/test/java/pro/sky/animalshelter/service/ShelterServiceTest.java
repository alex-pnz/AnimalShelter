package pro.sky.animalshelter.service;

import jdk.dynalink.linker.LinkerServices;
import org.assertj.core.api.Assertions;
import org.hibernate.mapping.Selectable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.animalshelter.model.AnimalType;
import pro.sky.animalshelter.model.Shelter;
import pro.sky.animalshelter.repository.ShelterRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShelterServiceTest {

    @Mock
    private ShelterRepository shelterRepository;

    @InjectMocks
    private ShelterService shelterService;

    @Test
    void testGetShelterById() {
        Shelter shelter = new Shelter();
        shelter.setId(1L);
        shelter.setShelterType(AnimalType.DOG);
        shelter.setShelterName("Test Dog");
        shelter.setCityAddress("Test City");
        shelter.setStreetAddress("Test Street");
        shelter.setHouseNumber(11);

        when(shelterRepository.findById(1L)).thenReturn(Optional.of(shelter)).thenReturn(null);

        Shelter actualShelter = shelterService.getShelterById(1L);

        Assertions.assertThat(actualShelter.getId()).isEqualTo(1L);
        Assertions.assertThat(actualShelter.getShelterType()).isEqualTo(AnimalType.DOG);
        Assertions.assertThat(actualShelter.getShelterName()).isEqualTo("Test Dog");
        Assertions.assertThat(actualShelter.getCityAddress()).isEqualTo("Test City");
        Assertions.assertThat(actualShelter.getStreetAddress()).isEqualTo("Test Street");
        Assertions.assertThat(actualShelter.getHouseNumber()).isEqualTo(11);
    }

    @Test
    void testGetShelterByIdNull() {

        when(shelterRepository.findById(1L)).thenReturn(Optional.empty()).thenReturn(null);

        Shelter actualShelter = shelterService.getShelterById(1L);

        assertNull(actualShelter);
    }

    @Test
    void getShelterByTypeDog() {
        Shelter shelter = new Shelter();
        shelter.setId(1L);
        shelter.setShelterType(AnimalType.DOG);
        shelter.setShelterName("Test Dog");
        shelter.setCityAddress("Test City");
        shelter.setStreetAddress("Test Street");
        shelter.setHouseNumber(11);

        List<Shelter> shelterList = new ArrayList<>();
        shelterList.add(shelter);

        when(shelterRepository.findByShelterType("DOG")).thenReturn(shelterList);

        Shelter actualShelter = shelterService.getShelterByType(AnimalType.DOG);

        Assertions.assertThat(actualShelter.getId()).isEqualTo(1L);
        Assertions.assertThat(actualShelter.getShelterType()).isEqualTo(AnimalType.DOG);
        Assertions.assertThat(actualShelter.getShelterName()).isEqualTo("Test Dog");
        Assertions.assertThat(actualShelter.getCityAddress()).isEqualTo("Test City");
        Assertions.assertThat(actualShelter.getStreetAddress()).isEqualTo("Test Street");
        Assertions.assertThat(actualShelter.getHouseNumber()).isEqualTo(11);
    }

    @Test
    void getShelterByTypeCat() {
        Shelter shelter = new Shelter();
        shelter.setId(1L);
        shelter.setShelterType(AnimalType.CAT);
        shelter.setShelterName("Test Cat");
        shelter.setCityAddress("Test City");
        shelter.setStreetAddress("Test Street");
        shelter.setHouseNumber(11);

        List<Shelter> shelterList = new ArrayList<>();
        shelterList.add(shelter);

        when(shelterRepository.findByShelterType("CAT")).thenReturn(shelterList);

        Shelter actualShelter = shelterService.getShelterByType(AnimalType.CAT);

        Assertions.assertThat(actualShelter.getId()).isEqualTo(1L);
        Assertions.assertThat(actualShelter.getShelterType()).isEqualTo(AnimalType.CAT);
        Assertions.assertThat(actualShelter.getShelterName()).isEqualTo("Test Cat");
        Assertions.assertThat(actualShelter.getCityAddress()).isEqualTo("Test City");
        Assertions.assertThat(actualShelter.getStreetAddress()).isEqualTo("Test Street");
        Assertions.assertThat(actualShelter.getHouseNumber()).isEqualTo(11);
    }
}