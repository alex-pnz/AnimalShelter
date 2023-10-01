package pro.sky.animalshelter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.sky.animalshelter.model.Report;
import pro.sky.animalshelter.service.ReportService;

import java.util.Collection;

/**
 * Контроллер для реализации веб интерфейса управления отчетами
 */
@Tag(name = "Report Controller", description = "Контроллер для реализации веб интерфейса управления отчетами")
@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {
    private final ReportService reportService;
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/today")
    @Operation(
            summary = "Получение информации об отчетах полученных сегодня",
            description = "Получение информации об отчетах полученных сегодня"
    )
    public Collection<Report> getTodayReports(){
         return reportService.getTodayReport();
    }

    @GetMapping("/adoption/{id}")
    @Operation(
            summary = "Получение информации об отчетах по конкретному усыновлению",
            description = "Получение информации об отчетах по конкретному усыновлению"
    )
    public Collection<Report> getReportsByAdoption(
            @PathVariable(name = "id") @Parameter(description = "Идентификатор усыновления") Long id){
        return reportService.getReportsByAdoption(id);
    }

}
