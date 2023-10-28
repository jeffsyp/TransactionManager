package banking;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the functionality of the Date class, focusing on the validity of date
 * values.
 *
 * @author Jeffery Sypytkowski
 */

public class DateTest {

    /**
     * Tests the handling of invalid date values by the Date class.
     * This includes days or months out of valid range and negative years.
     */
    @Test
    public void testInvalidDates() {

        assertFalse(new Date("01/00/2000").isValid()); // day is 0
        assertFalse(new Date("01/32/2000").isValid()); // day is 32 for January
        assertFalse(new Date("00/12/2000").isValid()); // month is 0
        assertFalse(new Date("13/12/2000").isValid()); // month is 13
        assertFalse(new Date("02/12/-1").isValid()); // year is negative
    }

    /**
     * Tests the handling of valid date values by the Date class.
     */
    @Test
    public void testValidDates() {
        assertTrue(new Date("05/15/2020").isValid());
        assertTrue(new Date("01/01/0001").isValid());
    }

    /**
     * Tests the handling of dates in February for leap years and non-leap years.
     */
    @Test
    public void testLeapYearDates() {
        assertTrue(new Date("02/29/2020").isValid()); // 2020 is a leap year
        assertFalse(new Date("02/29/2021").isValid()); // 2021 is not a leap year
    }
}