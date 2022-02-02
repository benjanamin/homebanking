package com.example.homebanking.controllers;

import com.example.homebanking.dtos.LoanApplicationDTO;
import com.example.homebanking.dtos.LoanDTO;
import com.example.homebanking.models.*;
import com.example.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ClientLoanRepository clientLoanRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @GetMapping("/loans")
    public List<LoanDTO> getAll(){
        return loanRepository.findAll().stream().map(LoanDTO::new).collect(Collectors.toList());
    }

    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> createLoan(@RequestBody LoanApplicationDTO loanApplication, Authentication authentication){
        Client client = this.clientRepository.findByEmail(authentication.getName());
        Loan loan = loanRepository.findById(loanApplication.getLoanId());
        Account account = accountRepository.findByNumber(loanApplication.getToAccountNumber());
        boolean isOwnByClient = false;
        boolean paymentsExist = false;


        if(loanApplication == null || loanApplication.getPayments() == 0 || loanApplication.getAmount() == 0 || loanApplication.getToAccountNumber().isEmpty()){
            System.out.println("Missing data");
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        //existe un objeto
        else if(loan == null){
            System.out.println("Loan doesnt exist");
            return new ResponseEntity<>("Loan doesnt exist", HttpStatus.FORBIDDEN);
        }

        for (int payment:loan.getPayments()) {
            if(payment == loanApplication.getPayments()){
                paymentsExist = true;
                break;
            }

        }
        // corresponde la cantidad de cuotas
        if(!paymentsExist){
            System.out.println("Payments doesnt exist");
            return new ResponseEntity<>("Payments doesnt exist", HttpStatus.FORBIDDEN);
        }
        // la cantidad pedida no supera la maxima
        else if(loanApplication.getAmount() > loan.getMaxAmount()){
            System.out.println("Amount exceed maximum");
            return new ResponseEntity<>("Amount exceeds maximum", HttpStatus.FORBIDDEN);
        }

        // la cuenta destino no existe
        else if(account == null){
            System.out.println("Account doesnt exist");
            return new ResponseEntity<>("Account doesnt exist", HttpStatus.FORBIDDEN);
        }
        for(Account aux:client.getAccounts()){
            if (aux.getNumber().equals(loanApplication.getToAccountNumber())){
                isOwnByClient = true;
                break;
            }
        }
        //la cuenta destino no pertenece al usuario
        if(!isOwnByClient){
            System.out.println("Amount doesnt belong to client");
            return new ResponseEntity<>("Amount doesnt belong to client", HttpStatus.FORBIDDEN);
        }

        System.out.println("yeeey");

        ClientLoan clientLoan = new ClientLoan(loanApplication.getAmount(),loanApplication.getPayments(),client,loan);
        clientLoan.setAmount(clientLoan.getAmount()* 1.2);
        Transaction transaction = new Transaction(TransactionType.CREDIT,loanApplication.getAmount(),loan.getName() + " loan approved", LocalDateTime.now(),account);
        double balance = account.getBalance() + loanApplication.getAmount();
        account.setBalance(balance);

        clientLoanRepository.save(clientLoan);
        transactionRepository.save(transaction);

        return new ResponseEntity<>(HttpStatus.CREATED);


    }
}
