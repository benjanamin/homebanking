package com.example.homebanking.repositories;

import com.example.homebanking.models.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface LoanRepository extends JpaRepository<Loan,Long> {
    List<Loan> findAll();
    Loan findById(long id);
}
