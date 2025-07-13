package com.luv2code.tdd;

public class FizzBuzz {

    public static String compute(int number){
        if(number % 15 == 0) return "FizzBuzz";
        if(number % 3 == 0) return "Fizz";
        if(number % 5 == 0) return "Buzz";
        return Integer.toString(number);
    }
}
