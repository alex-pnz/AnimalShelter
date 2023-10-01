package pro.sky.animalshelter.model;

import jakarta.persistence.*;
import org.aspectj.util.GenericSignature;
import pro.sky.animalshelter.model.enums.AnimalType;

import java.time.LocalDate;

/**
 * Класс, описывающий животное в приюте
 */
@Entity
@Table(name = "animals")
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private AnimalType animalType;

    private String name;

    private int age;
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDate admissionDate;
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDate leaveDate;
    @ManyToOne()
    @JoinColumn(name="shelter_id")
    private Shelter shelter;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AnimalType getAnimalType() {
        return animalType;
    }

    public void setAnimalType(AnimalType animalType) {
        this.animalType = animalType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public LocalDate getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(LocalDate admissionDate) {
        this.admissionDate = admissionDate;
    }

    public LocalDate getLeaveDate() {
        return leaveDate;
    }

    public void setLeaveDate(LocalDate leaveDate) {
        this.leaveDate = leaveDate;
    }

    public Shelter getShelter() {
        return shelter;
    }

    public void setShelter(Shelter shelter) {
        this.shelter = shelter;
    }
}
