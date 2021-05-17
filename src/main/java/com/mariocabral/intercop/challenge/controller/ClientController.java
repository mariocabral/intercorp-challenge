package com.mariocabral.intercop.challenge.controller;

import com.mariocabral.intercop.challenge.model.Client;
import com.mariocabral.intercop.challenge.service.ClientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Api(value = "Users microservice")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping(value = "/creacliente")
    @ApiOperation(value = "Add new user", notes = "Return a user stored" )
    public Client newClient(@RequestBody(required = true) Client client){
        return clientService.newClient(client);
    }

}
