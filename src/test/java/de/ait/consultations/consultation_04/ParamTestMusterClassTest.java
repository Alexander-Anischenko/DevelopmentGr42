package de.ait.consultations.consultation_04;

import de.ait.javalessons.consultations.consultation_04.ParamTestMusterClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ParamTestMusterClassTest {

    private ParamTestMusterClass paramTestMusterClass;

    @BeforeEach
    void setUp() {
        paramTestMusterClass = new ParamTestMusterClass();
    }

    @ParameterizedTest
    @ValueSource  (ints = {2, 4, 6, 8, 10, 12})
    @DisplayName("Все числа четные")
    void testIsEven(int number) {
        assertTrue(paramTestMusterClass.isEven(number));
    }

    @ParameterizedTest
    @ValueSource  (ints = {3, 5, 7, 9, 11, 13})
    void testIsNotEven(int number) {
        assertFalse(paramTestMusterClass.isEven(number));
    }

    @ParameterizedTest
    @CsvSource({
            "1, 2, 3",
            "5, 5, 10",
            "10, -5, 5",
            "0, 0, 0"
    })
    void testAdd(int a, int b, int expected) {
        assertEquals(expected, paramTestMusterClass.add(a, b));
    }

    @ParameterizedTest
    //анотация говорит о том, что мы берем csv файл из папки resources, файл test.csv,
    // второй параметр после запятой говорит о пропуске одной строчки сверху
    @CsvFileSource(resources = "/testData/test.csv", numLinesToSkip = 1)
    void testAddFromFile(int a, int b, int expected) {
        assertEquals(expected, paramTestMusterClass.add(a, b));
    }
}
