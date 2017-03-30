package com.kt.giga.home.b2b.validator;

/**
 * Created by 민우 on 2016-11-30.
 */
public class ImeiValidator {

    public static boolean validateImei(String str) {

        long n = Long.parseLong(str);
        int d;
        int sum = 0;

        for (int i = 15; i >= 1; i--) {
            d = (int) (n % 10);
            if (i % 2 == 0) {
                d = 2 * d;
            }
            sum = sum + sumDig(d);
            n = n / 10;
        }

        return sum % 10 == 0;

    }

    private static int sumDig(int n) {
        int a = 0;
        while (n > 0) {
            a = a + n % 10;
            n = n / 10;
        }
        return a;
    }
}
