package pro.sky.animalshelter.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class ReportTest {
    private final Long expectedId = 123L;
    private final LocalDate expectedDate = LocalDate.of(2002, 5, 25);
    private final String expectedDiet = "Test Diet";
    private final String expectedBehaviour = "Test Behaviour";
    private final String expectedHealth = "Test Health";
    private final Adoption expectedAdoption = new Adoption();
    private final String expectedImage = "Test/image/path.jpg";

    @Test
    public void testReportConstructor() {
        expectedAdoption.setId(1111L);
        Report actualReport = new Report(123L, expectedDate, expectedDiet, expectedBehaviour, expectedHealth, expectedAdoption, expectedImage);

        Assertions.assertThat(actualReport.getId()).isEqualTo(expectedId);
        Assertions.assertThat(actualReport.getDate()).isEqualTo(expectedDate);
        Assertions.assertThat(actualReport.getDiet()).isEqualTo(expectedDiet);
        Assertions.assertThat(actualReport.getBehaviour()).isEqualTo(expectedBehaviour);
        Assertions.assertThat(actualReport.getOverallHealth()).isEqualTo(expectedHealth);
        Assertions.assertThat(actualReport.getAdoption().getId()).isEqualTo(1111L);
    }

    @Test
    public void testSetterId() {
        Report actualReport = new Report();

        actualReport.setId(expectedId);
        actualReport.setDate(expectedDate);
        actualReport.setDiet(expectedDiet);
        actualReport.setBehaviour(expectedBehaviour);
        actualReport.setOverallHealth(expectedHealth);
        actualReport.setAdoption(expectedAdoption);
        expectedAdoption.setId(1111L);

        Assertions.assertThat(actualReport.getId()).isEqualTo(expectedId);
        Assertions.assertThat(actualReport.getDate()).isEqualTo(expectedDate);
        Assertions.assertThat(actualReport.getDiet()).isEqualTo(expectedDiet);
        Assertions.assertThat(actualReport.getBehaviour()).isEqualTo(expectedBehaviour);
        Assertions.assertThat(actualReport.getOverallHealth()).isEqualTo(expectedHealth);
        Assertions.assertThat(actualReport.getAdoption().getId()).isEqualTo(1111L);
    }
    @Test
    public void getImage() {
        Report actualReport = new Report();
        actualReport.setImage("Test Image");

        Assertions.assertThat(actualReport.getImage()).isEqualTo("Test Image");
    }
    @Test
    public void setImage() {
        Report actualReport = new Report();
        actualReport.setImage("Test Image");

        Assertions.assertThat(actualReport.getImage()).isEqualTo("Test Image");
    }


}