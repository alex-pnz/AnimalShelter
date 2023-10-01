package pro.sky.animalshelter.model;

import jakarta.persistence.*;
import pro.sky.animalshelter.model.enums.ProbationTermsStatus;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Collection;

/**
 * Класс, описывающий событие "усыновления" животного из приюта
 */
@Entity
@Table(name = "adoptions")
public class Adoption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "visitor_id")
    private Visitor visitor;

    @OneToOne
    @JoinColumn(name = "animal_id")
    private Animal animal;

    private LocalDate adoptionDate;
    private int reportsCount;
    private int reportsRequired;
    @Enumerated(EnumType.STRING)
    private ProbationTermsStatus status = ProbationTermsStatus.IN_PROGRESS;
    @OneToMany
    private Collection<Report> reports;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Visitor getVisitor() {
        return visitor;
    }

    public void setVisitor(Visitor visitor) {
        this.visitor = visitor;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public LocalDate getAdoptionDate() {
        return adoptionDate;
    }

    public void setAdoptionDate(LocalDate adoptionDate) {
        this.adoptionDate = adoptionDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Adoption adoption)) return false;
        return Objects.equals(id, adoption.id) && Objects.equals(visitor, adoption.visitor) && Objects.equals(animal, adoption.animal) && Objects.equals(adoptionDate, adoption.adoptionDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, visitor, animal, adoptionDate);
    }

    public int getReportsCount() {
        return reportsCount;
    }

    public void setReportsCount(int reportsCount) {
        this.reportsCount = reportsCount;
    }

    public int getReportsRequired() {
        return reportsRequired;
    }

    public void setReportsRequired(int reportsRequired) {
        this.reportsRequired = reportsRequired;
    }

    public ProbationTermsStatus getStatus() {
        return status;
    }

    public void setStatus(ProbationTermsStatus status) {
        this.status = status;
    }

    public Collection<Report> getReports() {
        return reports;
    }

    public void setReports(Collection<Report> reports) {
        this.reports = reports;
    }
}
