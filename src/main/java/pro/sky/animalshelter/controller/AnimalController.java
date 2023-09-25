package pro.sky.animalshelter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import pro.sky.animalshelter.model.Animal;

import java.util.Collection;
import java.util.Collections;

/**
 * Контроллер для реализации веб интерфейса управления животными
 */
@Tag(name = "Animal Controller", description = "Контроллер для реализации веб интерфейса управления животными")
@RestController
@RequestMapping("/api/v1/animals")
public class AnimalController {

    @GetMapping
    @Operation(
            summary = "Приветсвует пользователя",
            description = "Контроль работы"
    )
    public String welcome() {
        return "Welcome to animal management portal";
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Получение информации о животном по его идентификатору",
            description = "Получение информации о животном по его идентификатору"
    )
    public Animal getAnimalById(@PathVariable @Parameter(description = "Идентификатор животного") Long id) {
        return new Animal();
    }

    @GetMapping("/shelter/{id}")
    @Operation(
            summary = "Получение информации о животных, находящихся в определенном приюте по его идентификатору",
            description = "Получение информации о животных, находящихся в определенном приюте по его идентификатору"
    )
    public Collection<Animal> getAnimalsByShelter(
            @PathVariable(name = "id") @Parameter(description = "Идентификатор приюта") Long shelterId) {
        return Collections.emptyList();
    }

    @PostMapping
    @Operation(
            summary = "Регистрация нового животного при его поступлении в приют",
            description = "Регистрация нового животного при его поступлении в приют"
    )
    public Animal addAnimal(@RequestBody @Parameter(description = "Информация о зверюге") Animal animal) {
        return animal;
    }
}
