package org.example;

import jdk.jfr.Description;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DeliveryCostCalculationTests {

    @Description("Calculate cost of delivery for small cargo size")
    @Tag("Positive")
    @DisplayName("Checking cost of delivery for Small cargo")
    @ParameterizedTest
    @CsvSource({
            "3, SMALL, false, HIGH, 400",
            "1, SMALL, true, NORMAL, 450",
            "31, SMALL, false, VERY_HIGH, 640",
            "3, SMALL, true, INCREASED, 600",
            "11, SMALL, true, NORMAL, 600",
            "11, SMALL, false, INCREASED, 400",
            "31, SMALL, false, INCREASED, 480"
    })
    public void calculateSmallCargoDeliveryCost(int distance, Size size, boolean fragile, Load load, int expected) {

        double actual = CostCalculation.FinalCostCalculation(distance, size, fragile, load);
        assertEquals(expected, actual);
    }

    @Description("Calculate cost of delivery for big cargo size")
    @Tag("Positive")
    @DisplayName("Checking cost of delivery for Big cargo")
    @ParameterizedTest
    @CsvSource({
            "4, BIG, true, NORMAL, 600",
            "31, BIG, false, HIGH, 700",
            "1, BIG, false, INCREASED, 400",
            "1, BIG, true, HIGH, 770",
            "11, BIG, false, INCREASED, 480",
            "1, BIG, true, VERY_HIGH, 880",
            "3, BIG, false, VERY_HIGH, 480",
            "11, BIG, true, HIGH, 980",
            "100, BIG, false, NORMAL, 500",
    })
    public void calculateBigCargoDeliveryCost(int distance, Size size, boolean fragile, Load load, double expected) {

        double actual = CostCalculation.FinalCostCalculation(distance, size, fragile, load);
        assertEquals(expected, actual);
    }

    @Description("Calculate cost of delivery for fragile cargo to more than 30 km")
    @Tag("Negative")
    @DisplayName("Verify error message 'Fragile goods can't be transported for more than 30 km'")
    @ParameterizedTest
    @CsvSource({
            "31, BIG, true, NORMAL"

    })
    public void calculateFragileCostDelivery(int distance, Size size, boolean fragile, Load load) {

        Exception e = assertThrows(
                IllegalArgumentException.class,
                () -> CostCalculation.FinalCostCalculation(distance, size, fragile, load)
        );
        assertEquals(ExceptionMessage.LONG_DISTANCE_FOR_FRAGILE_CARGO, e.getMessage());
    }

    @Description("Calculate cost of delivery for zero distance")
    @Tag("Negative")
    @DisplayName("Verify error message 'Distance should be greater than 0'")
    @ParameterizedTest
    @CsvSource({
            "0, BIG, false, NORMAL"

    })
    public void calculateZeroDistanceDelivery(int distance, Size size, boolean fragile, Load load) {

        Exception e = assertThrows(
                IllegalArgumentException.class,
                () -> CostCalculation.FinalCostCalculation(distance, size, fragile, load)
        );
        assertEquals(ExceptionMessage.SHORT_DISTANCE, e.getMessage());
    }


    @Description("Calculate cost of delivery for negative distance value")
    @Tag("Negative")
    @DisplayName("Verify error message 'Distance should be greater than 0'")
    @ParameterizedTest
    @CsvSource({
            "-1, SMALL, false, HIGH"

    })
    public void calculateNegativeDistanceDelivery(int distance, Size size, boolean fragile, Load load) {

        Exception e = assertThrows(
                IllegalArgumentException.class,
                () -> CostCalculation.FinalCostCalculation(distance, size, fragile, load)
        );
        assertEquals(ExceptionMessage.SHORT_DISTANCE, e.getMessage());
    }

}
