package com.luv2code.junitdemo;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class DemoUtilsTest {

    DemoUtils demoUtils;

    @BeforeAll
    static void setupBeforeAll(){
        System.out.println("@BeforeAll executed");
    }

//    @AfterAll
//    static void tearDownAfterAll(){
//        System.out.println("@AfterAll executed");
//    }
//
//    @BeforeEach
//    void setupBeforeEach(){
//        demoUtils = new DemoUtils();
//        System.out.println("@BeforeEach executed");
//    }
//
//    @AfterEach
//    void tearDownAfterEach(){
//        System.out.println("@AfterEach executed");
//    }

    @Test
    //@DisplayName("Equals and Not Equals")
    void test_Equals_And_Not_Equals(){

        System.out.println("Running test: testEqualsAndNotEquals");
        DemoUtils demoUtils = new DemoUtils();

        assertEquals(6, demoUtils.add(2, 4), "2+4 must be 6");
        assertNotEquals(6, demoUtils.add(1, 9), "1+9 must not be 6");
    }

    @Test
    //@DisplayName("Null and Not Null")
    void test_Null_And_Not_Null(){

        System.out.println("Running test: testNullAndNotNull");
        DemoUtils demoUtils = new DemoUtils();

        assertNull(demoUtils.checkNull(null), "Object should be null");
        assertNotNull(demoUtils.checkNull("Mourat"), "Object should not be null");
    }
}
