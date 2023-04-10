package com.topjava.webapp;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MainStream {
    public static void main(String[] args) {
        System.out.println(minValue(new int[]{1, 2, 3, 3, 2, 3}));
        System.out.println(minValue(new int[]{9, 8}));
        System.out.println(oddOrEven(Arrays.asList(1, 1, 1, 2, 2, 1)));
        System.out.println(oddOrEven(Arrays.asList(1, 1, 1, 2, 2)));
    }

    public static int minValue(int[] values) {
        int[] ints = Arrays.stream(values)
                .distinct()
                .sorted()
                .toArray();

        return IntStream.range(0, ints.length)
                .map(i -> (int) (ints[i] * Math.pow(10, ints.length - i - 1)))
                .sum();
    }

    public static List<Integer> oddOrEven(List<Integer> integers) {
        Map<Boolean, List<Integer>> map = integers.stream()
                .collect(Collectors.partitioningBy(value -> value % 2 == 0, Collectors.toList()));

        return map.get(map.get(false).size() % 2 != 0);
    }
}