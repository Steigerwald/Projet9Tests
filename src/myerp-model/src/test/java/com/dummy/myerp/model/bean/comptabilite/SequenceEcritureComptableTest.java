package com.dummy.myerp.model.bean.comptabilite;

import com.dummy.myerp.technical.exception.NotFoundException;
import org.junit.Test;

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
    }

}
