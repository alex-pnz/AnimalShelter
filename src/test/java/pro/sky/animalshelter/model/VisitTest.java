package pro.sky.animalshelter.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import pro.sky.animalshelter.model.enums.AnimalType;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class VisitTest {

    @Test
    public void testVisitConstructor1() {
        Long expectedId = 123L;
        Shelter expectedShelter = new Shelter();
        expectedShelter.setShelterType(AnimalType.CAT);
        expectedShelter.setShelterName("Cat Shelter");
        Visitor expectedVisitor = new Visitor(1L, "Test Name", null, null);
        LocalDate visitDate = LocalDate.of(2002, 5, 25);

        Visit actualVisit = new Visit(123L, expectedShelter, expectedVisitor, visitDate);

        Assertions.assertThat(actualVisit.getId()).isEqualTo(expectedId);
        Assertions.assertThat(actualVisit.getShelter()).isEqualTo(expectedShelter);
        Assertions.assertThat(actualVisit.getVisitor()).isEqualTo(expectedVisitor);
        Assertions.assertThat(actualVisit.getVisitDate()).isEqualTo(visitDate);
    }

    @Test
    public void testVisitConstructor2() {
        Shelter expectedShelter = new Shelter();
        expectedShelter.setShelterType(AnimalType.CAT);
        expectedShelter.setShelterName("Cat Shelter");
        Visitor expectedVisitor = new Visitor(1L, "Test Name", null, null);


        Visit actualVisit = new Visit(expectedShelter, expectedVisitor);

        Assertions.assertThat(actualVisit.getShelter()).isEqualTo(expectedShelter);
        Assertions.assertThat(actualVisit.getVisitor()).isEqualTo(expectedVisitor);

    }

    @Test
    public void testSetId() {
        Visit actualVisit = new Visit();
        actualVisit.setId(1L);

        Assertions.assertThat(actualVisit.getId()).isEqualTo(1L);

    }
}