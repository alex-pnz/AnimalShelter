package pro.sky.animalshelter.model;

import jakarta.persistence.*;

import java.time.LocalDate;

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

    private LocalDate adpotionDate;

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

    public LocalDate getAdpotionDate() {
        return adpotionDate;
    }

    public void setAdpotionDate(LocalDate adpotionDate) {
        this.adpotionDate = adpotionDate;
    }
}
