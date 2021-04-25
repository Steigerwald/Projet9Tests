package com.dummy.myerp.business.impl.manager;

import com.dummy.myerp.business.contrat.manager.ComptabiliteManager;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:/com/dummy/myerp/business/businessBootstrapContext.xml"})
public class ComptabiliteManagerImplIT {

    private ComptabiliteManager objectToTest;

    private EcritureComptable sampleEcritureComptable;

    @BeforeEach
    void init() {
        objectToTest = new ComptabiliteManagerImpl();

        sampleEcritureComptable = new EcritureComptable();
        sampleEcritureComptable.setId(1);
        sampleEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        sampleEcritureComptable.setDate(new GregorianCalendar(2020, Calendar.FEBRUARY, 11).getTime());
        sampleEcritureComptable.setLibelle("Libelle");
        sampleEcritureComptable.setReference("AC-2020/00001");
        sampleEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(123),
                null));
        sampleEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                null, null,
                new BigDecimal(123)));
    }

    @Test
    void getListCompteComptableTest() {
        List<CompteComptable> compteComptablesList = objectToTest.getListCompteComptable();

        Assertions.assertThat(compteComptablesList.size()).isEqualTo(7);
    }

    @Test
    void getListJournalComptableTest() {
        List<JournalComptable> journalComptableList = objectToTest.getListJournalComptable();

        Assertions.assertThat(journalComptableList.size()).isEqualTo(4);
    }

    @Test
    void getListEcritureComptableTest() {
        List<EcritureComptable> ecritureComptableList = objectToTest.getListEcritureComptable();

        Assertions.assertThat(ecritureComptableList.size()).isEqualTo(5);
    }

    @Test
    @Transactional
    @Rollback
    void addReferenceTest() throws NotFoundException, FunctionalException {
        String expectedReference = sampleEcritureComptable.getReference();
        sampleEcritureComptable.setReference("");

        objectToTest.addReference(sampleEcritureComptable);

        Assertions.assertThat(sampleEcritureComptable.getReference()).isEqualTo(expectedReference);
    }

    @Test
    @Transactional
    @Rollback
    void insertEcritureComptableTest() throws ParseException, FunctionalException {
        EcritureComptable ecritureComptableToInsert = new EcritureComptable();
        ecritureComptableToInsert.setId(-10);
        ecritureComptableToInsert.setJournal(new JournalComptable("OD", "Opérations Diverses"));
        ecritureComptableToInsert.setReference("OD-2016/00006");
        ecritureComptableToInsert.setDate(new SimpleDateFormat("yyyy-MM-dd").parse("2016-12-31"));
        ecritureComptableToInsert.setLibelle("Remboursement");
        ecritureComptableToInsert.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(706,	"Prestations de services"), "TMA Appli Xxx", new BigDecimal("500.00"), null));
        ecritureComptableToInsert.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(706,	"Prestations de services"), "TMA LOGICIEL", null, new BigDecimal("500.00")));

        objectToTest.insertEcritureComptable(ecritureComptableToInsert);
        Integer ecritureComptableCount = objectToTest.getListEcritureComptable().size();

        Assertions.assertThat(ecritureComptableCount).isEqualTo(6);
    }
}