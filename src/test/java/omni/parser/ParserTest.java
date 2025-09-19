package omni.parser;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import omni.exceptions.InvalidArgumentException;

/**
 * Test class for the Parser component.
 * Contains unit tests to verify the functionality of date string validation.
 *
 * @author Brandon Tan
 */
public class ParserTest {

    /**
     * Tests the checkValidDateString method with valid date formats.
     * Verifies that both "dd-MM-yyyy" and "dd-MM-yyyy HHmm" formats are accepted.
     *
     * @throws Exception If any unexpected error occurs during testing.
     */
    @Test
    public void checkValidDateString_success() throws Exception {
        assertTrue(Parser.checkValidDateString("01-01-2025"));
        assertTrue(Parser.checkValidDateString("01-01-2025 1400"));
    }

    /**
     * Tests the checkValidDateString method with invalid date formats.
     * Verifies that InvalidArgumentException is thrown for incorrect formats.
     */
    @Test
    public void checkInvalidDateString_success() {
        InvalidArgumentException e1 = assertThrows(InvalidArgumentException.class, () ->
                Parser.checkValidDateString("01/01/2025"));
        InvalidArgumentException e2 = assertThrows(InvalidArgumentException.class, () ->
                Parser.checkValidDateString("01-01-2025 14:00"));

        assertTrue(e1.getMessage().contains("Invalid date format!"));
        assertTrue(e2.getMessage().contains("Invalid date format!"));
    }
}
