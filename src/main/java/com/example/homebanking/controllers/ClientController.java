package com.example.homebanking.controllers;

import com.example.homebanking.dtos.ClientDTO;

import com.example.homebanking.models.Client;
import com.example.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


import java.util.List;


import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping("/clients")
    public List<ClientDTO> getAll(){
        return this.clientRepository.findAll().stream().map(ClientDTO::new).collect(toList());
    }
    @RequestMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){
        return this.clientRepository.findById(id).map(ClientDTO::new).orElse(null);
    }

    @RequestMapping("/clients/find/{email}")
    public ClientDTO getClientByMail(@PathVariable String email){
        if(this.clientRepository.findClientByEmail(email) == null){
            return null;
        }
        return new ClientDTO(this.clientRepository.findClientByEmail(email)) ;

    }

    @RequestMapping("/clients/findBy/{firstName}")
    public List<ClientDTO> getClientByName(@PathVariable String firstName){
        firstName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1);
        return this.clientRepository.findAllByFirstNameIsLikeOrderByLastNameAsc(firstName).stream().map(ClientDTO::new).collect(toList());
    }

    @RequestMapping(path = "/clients", method = RequestMethod.POST)
    public ResponseEntity<Object> register(@RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password) {
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {

            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);

        }

        if (clientRepository.findByEmail(email) !=  null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }
        clientRepository.save(new Client(firstName, lastName, email, passwordEncoder.encode(password)));
        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @RequestMapping("/clients/current")
    public ClientDTO getClients(Authentication authentication){
        Client client = this.clientRepository.findByEmail(authentication.getName());
        return new ClientDTO(client);
    }



}
