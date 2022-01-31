package com.example.homebanking.controllers;

import com.example.homebanking.dtos.LoanDTO;
import com.example.homebanking.models.Loan;
import com.example.homebanking.repositories.ClientRepository;
import com.example.homebanking.repositories.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    ClientRepository clientRepository;

    @RequestMapping("/loans")
    public List<LoanDTO> getAll(){
        return loanRepository.findAll().stream().map(LoanDTO::new).collect(Collectors.toList());
    }

    @Transactional
    @PostMapping("/clients/current/loans")
    public ResponseEntity<Object> createLoan(@RequestParam Loan loan, Authentication authentication){
        if(loan == null){
            return new ResponseEntity<>("Not enought money", HttpStatus.FORBIDDEN);
        }
        else {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }
}
