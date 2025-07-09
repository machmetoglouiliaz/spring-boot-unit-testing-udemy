package com.luv2code.junitdemo;

import org.junit.jupiter.api.*;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestMethodOrder(MethodOrderer.DisplayName.class)
class DemoUtilsTest {

    DemoUtils demoUtils;

   /* @BeforeAll
    static void setupBeforeAll(){
        System.out.println("@BeforeAll executed");
    }

    @AfterAll
    static void tearDownAfterAll(){
        System.out.println("@AfterAll executed");
    }*/

    @BeforeEach
    void setupBeforeEach() {
        demoUtils = new DemoUtils();
    }

    /*@AfterEach
    void tearDownAfterEach(){
        System.out.println("@AfterEach executed");
    }*/

    @Test
    @DisplayName("Equals and Not Equals")
    //@Order(1)
    void testEqualsAndNotEquals() {

        DemoUtils demoUtils = new DemoUtils();

        assertEquals(6, demoUtils.add(2, 4), "2+4 must be 6");
        assertNotEquals(6, demoUtils.add(1, 9), "1+9 must not be 6");
    }

    @Test
    @DisplayName("Null and Not Null")
    //@Order(3)
    void testNullAndNotNull() {

        DemoUtils demoUtils = new DemoUtils();

        assertNull(demoUtils.checkNull(null), "Object should be null");
        assertNotNull(demoUtils.checkNull("Mourat"), "Object should not be null");
    }

    @Test
    @DisplayName("Same and Not Same")
    //@Order(1)
    void testSameAndNotSame() {

        String str = "luv2code";

        assertSame(demoUtils.getAcademy(), demoUtils.getAcademyDuplicate(), "Object should refer to same object");
        assertNotSame(str, demoUtils.getAcademy(), "Object should not refer to same object");
    }

    @Test
    @DisplayName("True and False")
    //@Order(-1)
    void testTrueFalse() {

        int gradeOne = 10;
        int gradeTwo = 5;

        assertTrue(demoUtils.isGreater(gradeOne, gradeTwo), "This should return true");
        assertFalse(demoUtils.isGreater(gradeTwo, gradeOne), "This should return false");
    }

    @Test
    @DisplayName("Array Equals")
    void testArrayEquals() {
        String[] stringArray = {"A", "B", "C"};

        assertArrayEquals(stringArray, demoUtils.getFirstThreeLettersOfAlphabet(), "Array should be the same");
    }

    @Test
    @DisplayName("Iterable Equals")
    void testIterableEquals() {
        List<String> list = List.of("luv", "2", "code");

        assertIterableEquals(list, demoUtils.getAcademyInList(), "Expected list should be same as actual list");
    }

    @Test
    @DisplayName("Lines match")
    void testLineMatch() {
        List<String> list = List.of("luv", "2", "code");

        assertLinesMatch(list, demoUtils.getAcademyInList(), "Lines should match");
    }

    @Test
    @DisplayName("Throws and Does Not Throw")
    void testThrowsAndDoesNotThrow(){
        assertThrows(Exception.class, () -> {demoUtils.throwException(-1);}, "Should throw exception");

        assertDoesNotThrow(() -> {demoUtils.throwException(1);}, "Should not throw exception");
    }

    @Test
    @DisplayName("Timeout")
    void testTimeout(){
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> {demoUtils.checkTimeout();}, "Method should execute in 3 seconds");
    }

    @Test
    @DisplayName("Multiply")
    void testMultiply(){
        assertEquals(12, demoUtils.multiply(4, 3), "4*3 should be 12");
    }
}
