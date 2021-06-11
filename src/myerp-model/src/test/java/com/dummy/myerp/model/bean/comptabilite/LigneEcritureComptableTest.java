package com.dummy.myerp.model.bean.comptabilite;

import org.junit.Test;
import org.junit.Assert;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class LigneEcritureComptableTest {

    @Test
    public void checkLigneEcritureComptableToString(){
        CompteComptable compte1 = new CompteComptable(1,"TD");
        LigneEcritureComptable ligneATester = new LigneEcritureComptable(compte1,"Voiture",new BigDecimal(100),null);
        String LigneDeTest = new String("LigneEcritureComptable{compteComptable=CompteComptable{numero=1, libelle='TD'}, libelle='Voiture', debit=100, credit=null}");
        assertThat(ligneATester.toString()).isEqualTo(LigneDeTest);
    }



}
