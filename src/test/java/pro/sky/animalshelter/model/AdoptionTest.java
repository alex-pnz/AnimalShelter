package pro.sky.animalshelter.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class AdoptionTest {

    @Test
    void testHashCode() {
        Visitor visitor = new Visitor(123L, "Test Name", null, null);
        Adoption adoption1 = new Adoption();
        adoption1.setId(1L);
        adoption1.setVisitor(visitor);
        adoption1.setAdoptionDate(LocalDate.of(2002, 5, 25));
        Adoption adoption2 = new Adoption();
        adoption2.setId(1L);
        adoption2.setVisitor(visitor);
        adoption2.setAdoptionDate(LocalDate.of(2002, 5, 25));

        assertTrue(adoption1.equals(adoption2) && adoption2.equals(adoption1));
        assertEquals(adoption1.hashCode(), adoption2.hashCode());

    }
}