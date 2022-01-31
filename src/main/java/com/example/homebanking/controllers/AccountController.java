package com.example.homebanking.controllers;

import com.example.homebanking.dtos.AccountDTO;
import com.example.homebanking.models.Account;
import com.example.homebanking.models.Client;
import com.example.homebanking.repositories.AccountRepository;
import com.example.homebanking.repositories.ClientRepository;
import com.example.homebanking.utils.Generator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping("/accounts")
    public List<AccountDTO> getAll(){
        return this.accountRepository.findAll().stream().map(AccountDTO::new).collect(Collectors.toList());
    }


    @GetMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id, Authentication authentication){

        Client client = this.clientRepository.findByEmail(authentication.getName());
        Set<Account> accounts = client.getAccounts();

        for (Account account : accounts) {
            if (account.getId() == id) {
                return this.accountRepository.findById(id).map(AccountDTO::new).orElse(null);
            }
        }
        return null;
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication){
        Client client = this.clientRepository.findByEmail(authentication.getName());
        Set<Account> accounts = client.getAccounts();
        if(accounts.size() < 3){
            Account account = new Account(Generator.randomAccountNumber(), LocalDateTime.now(),0);
            account.setClient(client);
            accountRepository.save(account);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
    }


}
