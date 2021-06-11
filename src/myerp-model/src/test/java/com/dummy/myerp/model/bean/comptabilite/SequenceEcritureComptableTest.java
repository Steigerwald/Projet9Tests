package com.dummy.myerp.model.bean.comptabilite;

import com.dummy.myerp.technical.exception.NotFoundException;
import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class SequenceEcritureComptableTest {

    @Test
    public void checkGetSequenceEcritureComptableByCodeAndAnnee() throws NotFoundException {
        String code = "OD";
        int annee = 2016;
        JournalComptable journalComptable=new JournalComptable();
        journalComptable.setCode(code);
        SequenceEcritureComptable sequenceEcritureComptable = new SequenceEcritureComptable();
        sequenceEcritureComptable.setAnnee(annee);
        sequenceEcritureComptable.setJournal(journalComptable);
        sequenceEcritureComptable.setDerniereValeur(88);
        String SequenceTest1 = new String("SequenceEcritureComptable{annee=2016, derniereValeur=88}");
        assertThat(sequenceEcritureComptable.toString()).isEqualTo(SequenceTest1);
        assertThat(sequenceEcritureComptable.getAnnee()).isEqualTo(2016);
        assertThat(sequenceEcritureComptable.getDerniereValeur()==(88));
    }

    @Test
    public void checkSequenceEcritureComptableToString(){
        int annee = 2021;
        JournalComptable journalComptable=new JournalComptable();
        int derniereValeur = 80;
        SequenceEcritureComptable sequence1 = new SequenceEcritureComptable(annee,journalComptable,derniereValeur);
        String SequenceDeTest = new String("SequenceEcritureComptable{annee=2021, derniereValeur=80}");
        assertThat(sequence1.toString()).isEqualTo(SequenceDeTest);
    }





}
