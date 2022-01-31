package com.example.homebanking.utils;

import java.util.Random;

public class Generator {

    public static String randomAccountNumber(){
        Random random = new Random();
        int number = random.nextInt(100000000);
        return "VIN-" + number;
    }

    public static String randomCardNumber(){
        Random random = new Random();
        StringBuilder cardNumber = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            int number = random.nextInt(10000);
            cardNumber.append(number).append("-");
        }
        cardNumber.append(random.nextInt(10000));
        return cardNumber.toString();
    }

    public static int randomCVV(){
        Random random = new Random();
        return random.nextInt(1000);
    }

}
