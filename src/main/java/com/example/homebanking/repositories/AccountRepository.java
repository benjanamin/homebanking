package com.example.homebanking.repositories;

import com.example.homebanking.models.Account;
import com.example.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface AccountRepository extends JpaRepository<Account,Long>{

    Account findByNumber(String number);
    Account findById(long id);
    List<Account> findAllByClient(Client client);

}
