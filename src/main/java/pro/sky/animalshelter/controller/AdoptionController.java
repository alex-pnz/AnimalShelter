package pro.sky.animalshelter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import pro.sky.animalshelter.model.Adoption;

import java.util.Collection;
import java.util.Collections;

@Tag(name = "Adoption Controller", description = "Контроллер для управления передачей животных новым хозяевам")
@RestController
@RequestMapping("/api/v1/adoption")
public class AdoptionController {

    @PostMapping("/adopt")
    @Operation(
            summary = "Внесение информации о передаче животного новым хозяевам",
            description = "Внесение информации о передаче животного новым хозяевам"
    )
    public Adoption animalAdoption(@RequestBody @Parameter(description = "Сведения передаче животного хозяину")
                                   Adoption adoption) {
        return adoption;
    }

    @GetMapping("/show-adoptions")
    @Operation(
            summary = "Получение информации о всех передачах животных новым хозяевам",
            description = "Получение информации о всех передачах животных новым хозяевам"
    )
    public Collection<Adoption> getAdoptions() {
        return Collections.emptyList();
    }

    @GetMapping("/show-adoption/{id}")
    @Operation(
            summary = "Получение информации о передаче животного новым хозяевам по идетификатору",
            description = "Получение информации о передаче животного новым хозяевам по идетификатору"
    )
    public Adoption getAdoptionById(@PathVariable @Parameter(description = "Идентификатор") Long id) {
        return new Adoption();
    }
}
