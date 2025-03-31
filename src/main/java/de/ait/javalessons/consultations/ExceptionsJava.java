package de.ait.javalessons.consultations;


import de.ait.javalessons.exceptions.MyCustomException;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;

@Slf4j
public class ExceptionsJava {

    public static void main(String[] args) {
        divide(10, 2);
        try {
            divide2(10, 0);
            divide3(23,0);
        }
        catch (SQLException exception) {
            log.error("SQLException: {}", exception.getMessage());
        }
        catch (MyCustomException exception) {
            log.error("MyCustomException: {}", exception.getMessage());
        }

    }


    public static int divide(int a, int b) {
        try {
            return a / b;
        }
        catch (ArithmeticException exception) {
            log.error("ArithmeticException: {}", exception.getMessage());
            return -1;
        }
        finally {
            log.info("Finally block executed!");
        }
    }

    public static int divide2(int a, int b) throws SQLException {
        return a / b;
    }

    public static int divide3(int a, int b) throws MyCustomException {
        return a / b;
    }
}
