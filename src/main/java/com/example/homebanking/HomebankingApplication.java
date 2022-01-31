package com.example.homebanking;

import com.example.homebanking.models.*;
import com.example.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {
	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);

	}

	@Bean
	public CommandLineRunner initData(ClientRepository repository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository
	, ClientLoanRepository clientLoanRepository, CardRepository cardRepository) {
		return (args) -> {
			// save a couple of customers

			Client cliente1 = new Client("Melba", "Morel","melba@mindhub.com",passwordEncoder.encode("melba"));
			Client cliente2 = new Client("Pepe","Peposo","pepe@pepe.pepe",passwordEncoder.encode("def"));
			Client cliente3 = new Client("Melba", "Norel","a@b.com",passwordEncoder.encode("ccc"));
			repository.save(cliente1);
			repository.save(cliente2);
			repository.save(cliente3);

			Account cuenta1 = new Account("0000", LocalDateTime.now(),5000,cliente1);
			Account cuenta2 = new Account("0001", LocalDateTime.now().plusDays(1),7500,cliente1);
			accountRepository.save(cuenta1);
			accountRepository.save(cuenta2);

			Transaction transaction1 = new Transaction(TransactionType.CREDIT,2000,"abc",LocalDateTime.now(),cuenta1);
			Transaction transaction2 = new Transaction(TransactionType.DEBIT,-1000,"xyz",LocalDateTime.now(),cuenta1);
			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);

			Loan loan1 = new Loan("Hipotecario",500000, List.of(12,24,36,48,60));
			Loan loan2 = new Loan("Personal",100000, List.of(6,12,24));
			Loan loan3 = new Loan("Automotriz",300000, List.of(12,24,36));
			loanRepository.save(loan1);
			loanRepository.save(loan2);
			loanRepository.save(loan3);

			ClientLoan clientLoan1 = new ClientLoan(400000,60,cliente1,loan1);
			ClientLoan clientLoan2 = new ClientLoan(50000,12,cliente1,loan2);
			ClientLoan clientLoan3 = new ClientLoan(100000,24,cliente2,loan2);
			ClientLoan clientLoan4 = new ClientLoan(200000,36,cliente2,loan3);
			clientLoanRepository.save(clientLoan1);
			clientLoanRepository.save(clientLoan2);
			clientLoanRepository.save(clientLoan3);
			clientLoanRepository.save(clientLoan4);

			Card card1 = new Card(cliente1.getFirstName() + " " + cliente1.getLastName(),CardType.DEBIT,CardColor.GOLD,"24500-03",111,LocalDateTime.now(),LocalDateTime.now().plusYears(5),cliente1);
			Card card2 = new Card(cliente1.getFirstName() + " " + cliente1.getLastName(),CardType.CREDIT,CardColor.TITANIUM,"52146-12",222,LocalDateTime.now(),LocalDateTime.now().plusYears(5),cliente1);
			Card card3 = new Card(cliente2.getFirstName() + " " + cliente2.getLastName(),CardType.CREDIT,CardColor.SILVER,"12345-56",333,LocalDateTime.now(),LocalDateTime.now().plusYears(5),cliente2);
			cardRepository.save(card1);
			cardRepository.save(card2);
			cardRepository.save(card3);
		};
	}

}
