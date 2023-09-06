package pro.sky.animalshelter.controller;

import org.springframework.web.bind.annotation.*;
import pro.sky.animalshelter.model.Animal;

/**
 * Контроллер для реализации веб интерфейса управления животными
 */
@RestController
@RequestMapping("/api/v1/animals")
public class AnimalController {

    @GetMapping
    public String welcome() {
        return "Welcome to animal management portal";
    }

    @GetMapping("/{id}")
    public Animal getAnimalById(@PathVariable Long id) {
        return new Animal();
    }

    @PostMapping("/")
    public Animal addAnimal(@RequestBody Animal animal) {
        return animal;
    }
}
