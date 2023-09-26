package pro.sky.animalshelter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.animalshelter.model.Animal;
import pro.sky.animalshelter.model.Volunteer;
import pro.sky.animalshelter.service.VolunteerService;

/**
 * Контроллер для управления волонтерами
 */
@Tag(name = "Volunteer Controller", description = "Контроллер для реализации веб интерфейса управления приютами")
@RestController
@RequestMapping("/api/v1/volunteer")
public class VolunteerController {
    private final VolunteerService volunteerService;

    public VolunteerController(VolunteerService volunteerService) {
        this.volunteerService = volunteerService;
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Получение информации о волонтере по его идентификатору",
            description = "Получение информации о волонтере по его идентификатору"
    )
    public ResponseEntity<Volunteer> getVolunteer(@PathVariable Long id) {
        Volunteer volunteer = volunteerService.findVolunteer(id);
        if (volunteer == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(volunteer);
    }

    @PostMapping
    @Operation(
            summary = "Добавление волонтера",
            description = "Добавление волонтера"
    )
    public Volunteer createVolunteer(@RequestBody Volunteer volunteer) {
        return volunteerService.createVolunteer(volunteer);
    }

    @PutMapping
    @Operation(
            summary = "Редактирование волонтера",
            description = "Редактирование волонтера"
    )
    public ResponseEntity<Volunteer> editVolunteer(@RequestBody Volunteer volunteer) {
        Volunteer foundVolunteer = volunteerService.editVolunteer(volunteer);
        if (foundVolunteer == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundVolunteer);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Удаление волонтера по его идентификатору",
            description = "Удаление волонтера по его идентификатору"
    )
    public ResponseEntity deleteVolunteer(@PathVariable Long id) {
        volunteerService.deleteVolunteer(id);
        return ResponseEntity.ok().build();
    }
}
