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

    @Test
    public void checkConstructeurLigneEcritureComptable(){
        CompteComptable compte1 = new CompteComptable(1,"TD");
        LigneEcritureComptable ligneATester1 = new LigneEcritureComptable(compte1,"Voiture",new BigDecimal(100),null);
        LigneEcritureComptable ligneATester2 = new LigneEcritureComptable();
        ligneATester2.setCompteComptable(compte1);
        ligneATester2.setLibelle("Voiture");
        ligneATester2.setDebit(new BigDecimal(100));
        ligneATester2.setCredit(null);
        assertThat(ligneATester1.toString()).isEqualTo(ligneATester2.toString());
    }
}
