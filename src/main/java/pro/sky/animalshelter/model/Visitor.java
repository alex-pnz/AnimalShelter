package pro.sky.animalshelter.model;

import jakarta.persistence.*;

@Entity
@Table(name = "visitors")
public class Visitor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long chatId;

    private String Name;

    private String phoneNumber;

    private String email;
}
