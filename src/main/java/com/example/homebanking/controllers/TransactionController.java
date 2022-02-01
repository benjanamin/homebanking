package com.example.homebanking.controllers;
import com.example.homebanking.dtos.TransactionDTO;
import com.example.homebanking.models.Account;
import com.example.homebanking.models.Client;
import com.example.homebanking.models.Transaction;
import com.example.homebanking.models.TransactionType;
import com.example.homebanking.repositories.AccountRepository;
import com.example.homebanking.repositories.ClientRepository;
import com.example.homebanking.repositories.TransactionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ClientRepository clientRepository;

    @RequestMapping(value = "/transactions", method = RequestMethod.GET)
    public List<Transaction> getTransactions(){
        return transactionRepository.findAll();
    }
    @RequestMapping(value = "/transactions/{id}", method= RequestMethod.GET)
    public Optional<TransactionDTO> getTransaction(@PathVariable long id){
        return transactionRepository.findById(id).map(TransactionDTO::new);
    }

    @Transactional
    @PostMapping("/clients/current/transactions")
    public ResponseEntity<Object> createTransaction(@RequestParam String fromAccountNumber, @RequestParam String toAccountNumber, @RequestParam double amount, @RequestParam String description

                                                    , Authentication authentication){
        Client client = this.clientRepository.findByEmail(authentication.getName());
        boolean isOwnByClient = false;
        for(Account account:client.getAccounts()){
            if (account.getNumber().equals(fromAccountNumber)){
                isOwnByClient = true;
                break;
            }
        }

        Account fromAccount = accountRepository.findByNumber(fromAccountNumber);
        Account toAccount = accountRepository.findByNumber(toAccountNumber);
        if(amount == 0 || description.isEmpty() || fromAccountNumber.isEmpty() || toAccountNumber.isEmpty()){
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        else if(fromAccountNumber.equals(toAccountNumber)){
            return new ResponseEntity<>("Same account numbers", HttpStatus.FORBIDDEN);
        }
        else if(fromAccount == null ){
            return new ResponseEntity<>("First account doesnt exist", HttpStatus.FORBIDDEN);
        }
        else if(!isOwnByClient){
            return new ResponseEntity<>("Client doesnt own account", HttpStatus.FORBIDDEN);
        }
        else if(toAccount == null){
            return new ResponseEntity<>("Seconds account doesnt exist", HttpStatus.FORBIDDEN);
        }
        else if(fromAccount.getBalance() < amount){
            return new ResponseEntity<>("Not enought money", HttpStatus.FORBIDDEN);
        }
        else{

            double dinero1 = fromAccount.getBalance() - amount;
            double dinero2 = toAccount.getBalance() + amount;
            fromAccount.setBalance(dinero1);
            toAccount.setBalance(dinero2);
            /*
            accountRepository.save(fromAccount);
            accountRepository.save(toAccount);
            */
            Transaction fromTransaction = new Transaction(TransactionType.CREDIT,-amount,description, LocalDateTime.now(),fromAccount);
            Transaction toTransaction = new Transaction(TransactionType.DEBIT, amount,description,LocalDateTime.now(),toAccount);
            transactionRepository.save(fromTransaction);
            transactionRepository.save(toTransaction);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }

    }
}