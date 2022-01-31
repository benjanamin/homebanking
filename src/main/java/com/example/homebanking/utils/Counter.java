package com.example.homebanking.utils;

import com.example.homebanking.models.Card;
import com.example.homebanking.models.CardType;

import java.util.Set;

public class Counter {
    public static int[] cardTypeCounter(Set<Card> cards){
        int[] type = new int[2];
        int debit = 0;
        int credit = 0;
        for(Card card:cards){
            if(card.getType() == CardType.CREDIT){
                credit +=1;
            }
            else if(card.getType() == CardType.DEBIT){
                debit +=1;
            }
        }
        type[0] = credit;
        type[1] = debit;
        return type;
    }
}
