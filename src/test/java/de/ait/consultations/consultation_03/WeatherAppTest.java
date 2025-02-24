package de.ait.consultations.consultation_03;

import de.ait.consultations.consuitation_03.Weather;
import de.ait.consultations.consuitation_03.WeatherApp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WeatherAppTest {

    private List<Weather> weatherTestList = Arrays.asList(
            new Weather("New York", 10.5, false),
            new Weather("Los Angeles", 18.2, false),
            new Weather("Chicago", -2.0, true),
            new Weather("Houston", 25.0, false),
            new Weather("Miami", 30.5, true),
            new Weather("Paris", 12.0, false),
            new Weather("London", -7.5, true),
            new Weather("Berlin", 3.0, true),
            new Weather("Sydney", 22.3, false),
            new Weather("Tokyo", 15.6, true)
    );

    private List<Weather> weatherSecondTestList = Arrays.asList(

    );

    private WeatherApp weatherApp;

    @BeforeEach
    void setUp() {
        //Average
        WeatherApp weatherApp = new WeatherApp();
    }

    @Test
    void testFilterByTemperatureTwoWasFound() {


        //Act
        List<Weather> resultList = weatherApp.filterByTemperature(weatherTestList);

        //Assert Утверждение
        assertEquals(2, resultList.size());
        assertEquals("Chicago", resultList.get(0).getCity());
        assertEquals("London", resultList.get(1).getCity());

    }

    @Test
    void testFilterByTemperatureWasNotFound() {


        //Act
        List<Weather> resultList = weatherApp.filterByTemperature(weatherSecondTestList);

        //Assert Утверждение
        assertTrue(resultList.isEmpty());

    }

    @Test
    void testFindByTemperatureMoreTempSuccess() {
        //Average
        //WeatherApp weatherApp = new WeatherApp();

        //Act
        boolean result = weatherApp.findByTemperatureMoreTemp(weatherTestList, 30);

        //Assert Утверждение
        assertTrue(result);
    }
}
