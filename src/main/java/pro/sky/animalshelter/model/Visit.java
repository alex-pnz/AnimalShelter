package pro.sky.animalshelter.model;

import jakarta.persistence.*;

import java.time.LocalDate;

/**
 * Класс, описывающий событие посещения приюта возможным
 * "усыновителем" какого-либо животного
 */
@Entity
@Table(name = "visitors_per_shelter")
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "shelter_id")
    private Shelter shelter;

    @OneToOne
    @JoinColumn(name = "visitor_id")
    private Visitor visitor;

    private LocalDate visitDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Shelter getShelter() {
        return shelter;
    }

    public void setShelter(Shelter shelter) {
        this.shelter = shelter;
    }

    public Visitor getVisitor() {
        return visitor;
    }

    public void setVisitor(Visitor visitor) {
        this.visitor = visitor;
    }

    public LocalDate getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(LocalDate visitDate) {
        this.visitDate = visitDate;
    }
}
