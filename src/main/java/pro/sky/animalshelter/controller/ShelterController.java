package pro.sky.animalshelter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import pro.sky.animalshelter.model.Shelter;

/**
 * Контроллер для управления приютами
 */
@Tag(name = "Shelter Controller", description = "Контроллер для реализации веб интерфейса управления приютами")
@RestController
@RequestMapping("/api/v1/shelter")
public class ShelterController {

    @GetMapping
    @Operation(
            summary = "Приветсвует пользователя",
            description = "Контроль работы"
    )
    public String welcome() {
        return "Welcome to shelter management portal";
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Получение информации о приюте по его идентификатору",
            description = "Получение информации о приюте по его идентификатору"
    )
    public Shelter getShelterInfo(@PathVariable(name = "id") @Parameter(description = "Идентификатор приюта") Long shelterId) {
        return new Shelter();
    }

    @PostMapping("/add-shelter")
    @Operation(
            summary = "Регистрация нового приюте при его открытии",
            description = "Регистрация нового приюте при его открытии"
    )
    public Shelter addShelter(@RequestBody @Parameter(description = "Информация о приюте") Shelter shelter) {
        return shelter;
    }
}
