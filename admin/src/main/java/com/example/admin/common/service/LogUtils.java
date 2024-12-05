package com.example.admin.common.service;

import java.util.Random;

public class LogUtils {

    // 고유 TrxNo를 생성한다.
    public static String generateTrxNo() {
        long millis = System.currentTimeMillis();
        String randomNumber = generate3RandomNumber();

        return millis + randomNumber;
    }


    // 3자리의 랜덤 숫자를 생성한다.
    private static String generate3RandomNumber() {
        Random random = new Random();
        return String.valueOf(random.nextInt(900) + 100);
    }

}
