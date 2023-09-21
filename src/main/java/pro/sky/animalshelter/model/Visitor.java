package pro.sky.animalshelter.model;

import jakarta.persistence.*;

/**
 * Класс, описывающий посетителя приюта
 */
@Entity
@Table(name = "visitors")
public class Visitor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long chatId;

    private String visitorName;

    private String phoneNumber;

    private String email;

    @OneToOne
    @JoinColumn(name = "volunteer_id")
    private Volunteer volunteer;

    public Visitor() {}

    public Visitor(Long chatId, String name, String phoneNumber, String email) {
        this.chatId = chatId;
        this.visitorName = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getVisitorName() {
        return visitorName;
    }

    public void setVisitorName(String name) {
        this.visitorName = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public Volunteer getVolunteer() {
        return volunteer;
    }

    public void setVolunteer(Volunteer volunteer) {
        this.volunteer = volunteer;
    }
}
