package com.mariocabral.intercop.challenge.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "KpiClient", description = "Stats of clients")
public class KPIClient {
    private int avgAges;
    private BigDecimal standardDeviationAges;
}
