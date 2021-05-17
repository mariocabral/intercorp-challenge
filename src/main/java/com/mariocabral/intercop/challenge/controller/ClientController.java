package com.mariocabral.intercop.challenge.controller;

import com.mariocabral.intercop.challenge.model.Client;
import com.mariocabral.intercop.challenge.model.KPIClient;
import com.mariocabral.intercop.challenge.service.ClientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

}
