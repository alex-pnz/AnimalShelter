package pro.sky.animalshelter.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Table(name = "reports")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "TIMESTAMP")
    @CreationTimestamp
    private LocalDate date;
    private String diet;
    private String behaviour;
    private String overallHealth;
    @ManyToOne()
    @JoinColumn(name="adoption_id")
    private Adoption adoption;

    public Report() {
    }

    public Report(Long id, LocalDate date, String diet, String behaviour, String overallHealth, Adoption adoption) {
        this.id = id;
        this.date = date;
        this.diet = diet;
        this.behaviour = behaviour;
        this.overallHealth = overallHealth;
        this.adoption = adoption;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDiet() {
        return diet;
    }

    public void setDiet(String diet) {
        this.diet = diet;
    }

    public String getBehaviour() {
        return behaviour;
    }

    public void setBehaviour(String behaviour) {
        this.behaviour = behaviour;
    }

    public String getOverallHealth() {
        return overallHealth;
    }

    public void setOverallHealth(String overallHealth) {
        this.overallHealth = overallHealth;
    }

    public Adoption getAdoption() {
        return adoption;
    }

    public void setAdoption(Adoption adoption) {
        this.adoption = adoption;
    }
}
