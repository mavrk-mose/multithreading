package com.mkippe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Streaming {
    public static IntStream factorize (int value) {
        List<Integer> factors = new ArrayList<>();
        for (int i = 2; i <= value; i++){
            while (value % i ==0) {
                factors.add(i);
                value /= i; // what are the f*ck is this /= ??? its divide and assign the result of division to value
            }
        }
        return factors.stream()
                .mapToInt(Integer::intValue);
    }
    // I can trace the current stream through debugging tools
    public static void main(String[] args) {

        int[] result = IntStream.of(10, 87, 97, 43, 121, 20)
                            .flatMap(Streaming::factorize)
                            .distinct()
                            .sorted()
                            .toArray();

        System.out.println(Arrays.toString(result));

    }

    public static class Something {
        public static void main(String[] args) {

            String[] strArray = {"me", "mo", "mi", "adasffa","asfasdf","afsdfsadf"};
            System.out.println(Arrays.toString(strArray));
            // stream through the array
            // perform map operation to change to uppercase
            // then filter through the mapped objects to remove chars
            //finally it is sorted then converted to array

            Object[] objects = Stream.of(Arrays.stream(strArray)
                    .map(String::toUpperCase) // how do method references work
                    .filter(elem -> elem.length() == 1)).toArray();
            System.out.println(Arrays.toString(objects));
        }
    }

    enum CardType {SILVER, GOLD, PLATINUM, DIAMOND}
    private static BiFunction<Double, Integer, Double> getOrderDiscountFormula(CardType cardType) {
        return switch (cardType) {
            case SILVER -> (a, b) -> (a * 0) + b;
            case GOLD -> (a, b) -> (a * .05) + b;
            case PLATINUM -> (a, b) -> (a * 0.1) + b * 2;
            case DIAMOND -> (a, b) -> (a * 0.15) + b * 3;
        };
    }

    //using the streams API to iterate over characters of a string & swtch expression as an arg
    private static StringBuilder handleSpecialCharacters(StringBuilder sb, String segment) {
        segment.chars()
                .mapToObj(c -> (char) c)
                .map(c -> switch(c){
                    case '+' -> "~2";
                    case '-' -> "~9";
                    case '*' -> "~5";
                    default -> String.valueOf(c);
                })
                .forEach(sb::append);
        return sb;
    }

    //it's preferred not to nest lambdas so its better to
    private static StringBuilder handleSpecialCharacters2(StringBuilder sb, String segment) {
        segment.chars()
                .mapToObj(c -> (char) c)
                .map(Streaming::mapCharacter)
                .forEach(sb::append);
        return sb;
    }

    private static String mapCharacter(Character c) {
        return switch (c) {
            case '/' -> "~1";
            case '~' -> "~0";
            case '*' -> "~5";
            default -> String.valueOf(c);
        };
    }


}

