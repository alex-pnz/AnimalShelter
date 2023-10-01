package pro.sky.animalshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.animalshelter.model.Adoption;
import pro.sky.animalshelter.model.Report;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    Collection<Report> findReportByAdoptionId(Long id);

    List<Report> findReportByDate(LocalDate date);
}
