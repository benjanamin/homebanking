package com.example.homebanking.repositories;

import com.example.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;


@RepositoryRestResource
public interface ClientRepository extends JpaRepository<Client, Long> {
    Client findClientByEmail(String email);
    Client findByEmail(String email);
    Client findClientByEmailAndPassword(String email, String password);
    List<Client> findAllByFirstNameIsLike(String firstName);
    List<Client> findAllByFirstNameIsLikeOrderByLastNameAsc(String firstName);


}
