package com.dummy.myerp.model.bean.comptabilite;

import java.math.BigDecimal;

import org.apache.commons.lang3.ObjectUtils;
import org.junit.Assert;
import org.junit.Test;


public class EcritureComptableTest {

    private LigneEcritureComptable createLigne(Integer pCompteComptableNumero, String pDebit, String pCredit) {
        BigDecimal vDebit = pDebit == null ? null : new BigDecimal(pDebit);
        BigDecimal vCredit = pCredit == null ? null : new BigDecimal(pCredit);
        String vLibelle = ObjectUtils.defaultIfNull(vDebit, BigDecimal.ZERO)
                                     .subtract(ObjectUtils.defaultIfNull(vCredit, BigDecimal.ZERO)).toPlainString();
        LigneEcritureComptable vRetour = new LigneEcritureComptable(new CompteComptable(pCompteComptableNumero),
                                                                    vLibelle,
                                                                    vDebit, vCredit);
        return vRetour;
    }

    @Test
    public void isEquilibree() {
        // arrange
        EcritureComptable vEcriture;
        vEcriture = new EcritureComptable();

        // act
        vEcriture.setLibelle("Equilibrée");
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "200.50", null));
        //vEcriture.getListLigneEcriture().add(this.createLigne(1, "100.50", "33"));
        vEcriture.getListLigneEcriture().add(this.createLigne(1, null, "200.50"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, null, "301"));
        //vEcriture.getListLigneEcriture().add(this.createLigne(2, "40", "7"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "301", null));

        // assert
        Assert.assertTrue(vEcriture.toString(), vEcriture.isEquilibree());
    }


    @Test
    public void isNotEquilibree() {
        // arrange
            EcritureComptable vEcriture;
            vEcriture = new EcritureComptable();

        // act
        vEcriture.getListLigneEcriture().clear();
        vEcriture.setLibelle("Non équilibrée");
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "10", null));
        //vEcriture.getListLigneEcriture().add(this.createLigne(1, "20", "1"));
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "11", "1"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, null, "30"));
        //vEcriture.getListLigneEcriture().add(this.createLigne(2, "1", "2"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "12", "33"));

        //assert
        Assert.assertFalse(vEcriture.toString(), vEcriture.isEquilibree());
    }


    @Test
    public void getTotalDebit(){
        // arrange
        EcritureComptable vEcriture;
        vEcriture = new EcritureComptable();
        BigDecimal vRetourDebit = BigDecimal.ZERO;

        // act
        vEcriture.getListLigneEcriture().clear();
        vEcriture.setLibelle("Non équilibrée");
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "10", null));
        //vEcriture.getListLigneEcriture().add(this.createLigne(1, "20", "1"));
        vEcriture.getListLigneEcriture().add(this.createLigne(1, null, "101"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, null, "30"));
        //vEcriture.getListLigneEcriture().add(this.createLigne(2, "1", "2"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "12", "33"));
        vRetourDebit =vEcriture.getTotalDebit();
        //Integer.parseInt(vRetourDebit)

        // assert
        Assert.assertEquals(22.00,vRetourDebit.longValue(),0.01);
    }

}
