package com.mariocabral.intercop.challenge.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
@ApiModel(value = "Client", description = "Client data")
@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ApiModelProperty(required = true, allowEmptyValue = false)
    private String name;
    @ApiModelProperty(required = true, allowEmptyValue = false)
    private String lastName;
    @ApiModelProperty(required = true, allowEmptyValue = false)
    private int age;
    @ApiModelProperty(required = true, allowEmptyValue = false)
    private Date birthDate;
    @ApiModelProperty(required = false, allowEmptyValue = true)
    private Date expectedDateOfDeath;

}
