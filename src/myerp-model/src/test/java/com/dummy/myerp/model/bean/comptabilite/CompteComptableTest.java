package com.dummy.myerp.model.bean.comptabilite;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

public class CompteComptableTest {

    // test de la méthode getByNumero

    @Test
    public void checkGetByNumeroNotNull() {

        List<CompteComptable> cList = new ArrayList<CompteComptable>();

        CompteComptable c1 = new CompteComptable(1,"libelle");
        CompteComptable c2 = new CompteComptable(2,"libelle");
        CompteComptable c3 = new CompteComptable(3,"libelle");

        cList.add(c1);
        cList.add(c2);
        cList.add(c3);

        CompteComptable compteComptable = CompteComptable.getByNumero(cList, 3);

        assertThat(compteComptable).isEqualTo(c3);
    }

    @Test
    public void checkGetByNumeroNull() {

        List<CompteComptable> cList = new ArrayList<CompteComptable>();

        CompteComptable c1 = new CompteComptable(1,"libelle");
        CompteComptable c2 = new CompteComptable(2,"libelle");
        CompteComptable c3 = new CompteComptable(3,"libelle");

        cList.add(c1);
        cList.add(c2);
        cList.add(c3);

        CompteComptable compteComptable = CompteComptable.getByNumero(cList, 4);

        assertThat(compteComptable).isEqualTo(null);
    }



}
