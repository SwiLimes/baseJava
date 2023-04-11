package com.topjava.webapp;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MainStream {
    public static void main(String[] args) {
        System.out.println(minValue(new int[]{1, 2, 3, 3, 2, 3}));
        System.out.println(minValue(new int[]{9, 8}));
        System.out.println(oddOrEven(Arrays.asList(1, 1, 1, 2, 2, 1)));
        System.out.println(oddOrEven(Arrays.asList(1, 1, 1, 2, 2)));
    }

    public static int minValue(int[] values) {
        return Arrays.stream(values)
                .distinct()
                .sorted()
                .reduce(0, (accum, elem) -> accum * 10 + elem);
    }

    public static List<Integer> oddOrEven(List<Integer> integers) {
        Map<Boolean, List<Integer>> map = integers.stream()
                .collect(Collectors.partitioningBy(value -> value % 2 == 0, Collectors.toList()));

        return map.get(map.get(false).size() % 2 != 0);
    }
}