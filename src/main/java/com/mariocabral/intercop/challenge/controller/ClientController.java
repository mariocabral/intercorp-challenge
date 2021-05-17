package com.mariocabral.intercop.challenge.controller;

import com.mariocabral.intercop.challenge.model.Client;
import com.mariocabral.intercop.challenge.model.KPIClient;
import com.mariocabral.intercop.challenge.service.ClientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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


    @GetMapping(value = "/kpideclientes")
    @ApiOperation(value = "Stats from Client data", notes = "Return the avg and standard deviation of the ages froms clients" )
    public KPIClient kpiClient(){
        return clientService.kpiClient();
    }

    @GetMapping(value = "/listclientes")
    @ApiOperation(value = "Get List of clients", notes = "Return the list of clients with expected death date." )
    public Page<Client> listClient(@RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size){
        return clientService.listClient(PageRequest.of(page, size));
    }

}
