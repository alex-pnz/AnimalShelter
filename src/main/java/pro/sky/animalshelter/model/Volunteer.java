package pro.sky.animalshelter.model;

import jakarta.persistence.*;
import pro.sky.animalshelter.model.enums.Action;

import java.util.Objects;

/**
 * Класс, описывающий волонтера
 */
@Entity
@Table(name = "volunteers")
public class Volunteer {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "is_free")
    private boolean isFree;

    @OneToOne
    @JoinColumn(name = "visitor_id")
    private Visitor visitor;
    @Enumerated(EnumType.STRING)
    private Action action;

    public Volunteer() {}

    public Volunteer(Long id, Long chatId, String firstName, boolean isFree, Visitor visitor) {
        this.id = id;
        this.chatId = chatId;
        this.firstName = firstName;
        this.isFree = isFree;
        this.visitor = visitor;
    }

    public Long getId() {
        return id;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public boolean isFree() {
        return isFree;
    }

    public void setFree(boolean free) {
        isFree = free;
    }

    public Visitor getVisitor() {
        return visitor;
    }

    public void setVisitor(Visitor visitor) {
        this.visitor = visitor;
    }
}
