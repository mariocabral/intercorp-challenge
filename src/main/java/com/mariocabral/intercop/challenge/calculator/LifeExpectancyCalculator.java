package com.mariocabral.intercop.challenge.calculator;

import lombok.Builder;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public interface LifeExpectancyCalculator {



    default Date calculateLifeExpectancy(GenderCalculator.Gender gender, int age, Date birthDate){
        // source: https://es.wikipedia.org/wiki/Anexo:Pa%C3%ADses_por_esperanza_de_vida
        int expectedLifeWoman = 79;
        int expectedLifeMen = 73;
        int expectedLife = 0;
        if (gender == GenderCalculator.Gender.FEMALE){
            expectedLife = expectedLifeWoman;
        } else {
            expectedLife = expectedLifeMen;
        }
        LocalDate localBirthDate = birthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate expectedLifeCalculated = localBirthDate.plusYears(expectedLife);
        return Date.from(expectedLifeCalculated.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

}
