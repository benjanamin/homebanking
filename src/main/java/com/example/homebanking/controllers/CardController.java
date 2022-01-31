package com.example.homebanking.controllers;

import com.example.homebanking.models.Card;
import com.example.homebanking.models.CardColor;
import com.example.homebanking.models.CardType;
import com.example.homebanking.models.Client;
import com.example.homebanking.repositories.CardRepository;
import com.example.homebanking.repositories.ClientRepository;
import com.example.homebanking.utils.Counter;
import com.example.homebanking.utils.Generator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class CardController {
    @Autowired
    CardRepository cardRepository;
    @Autowired
    private ClientRepository clientRepository;

    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> createCard(@RequestParam CardColor cardColor, @RequestParam CardType cardType, Authentication authentication){
        Client client = this.clientRepository.findByEmail(authentication.getName());
        Set<Card> cards = client.getCards();
        String cardHolder = client.getFirstName() + " " + client.getLastName();
        if(cardColor == null || cardType == null ){
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        else if(cardType == CardType.CREDIT && Counter.cardTypeCounter(cards)[0] < 3){

            Card card = new Card(cardHolder,cardType,cardColor, Generator.randomCardNumber(),Generator.randomCVV(), LocalDateTime.now(),LocalDateTime.now().plusYears(5),client);
            cardRepository.save(card);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        else if(cardType == CardType.DEBIT && Counter.cardTypeCounter(cards)[1] < 3){
            Card card = new Card(cardHolder,cardType,cardColor, Generator.randomCardNumber(),Generator.randomCVV(), LocalDateTime.now(),LocalDateTime.now().plusYears(5),client);
            cardRepository.save(card);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Cannot create", HttpStatus.FORBIDDEN);
    }
}
