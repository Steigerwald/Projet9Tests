package com.dummy.myerp.business.manager;

import com.dummy.myerp.business.contrat.BusinessProxy;
import com.dummy.myerp.business.impl.TransactionManager;
import com.dummy.myerp.business.impl.manager.ComptabiliteManagerImpl;
import com.dummy.myerp.business.util.Constant;
import com.dummy.myerp.consumer.dao.contrat.ComptabiliteDao;
import com.dummy.myerp.consumer.dao.contrat.DaoProxy;
import com.dummy.myerp.model.bean.comptabilite.*;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.TransactionStatus;

import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@RunWith(MockitoJUnitRunner.class)
//@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
public class ComptabiliteManagerImplTest {

    Logger logger = (Logger) LoggerFactory.getLogger(ComptabiliteManagerImplTest.class);

    @Mock
    private BusinessProxy businessProxy;

    @Mock
    private DaoProxy daoProxy;

    @Mock
    private TransactionManager transactionManager;

    @Mock
    private ComptabiliteDao comptabiliteDao;

    @InjectMocks
    private ComptabiliteManagerImpl objectToTest;

    private EcritureComptable sampleEcritureComptable;

    @Before
    public void init() throws ParseException {
        ComptabiliteManagerImpl.configure(businessProxy, daoProxy, transactionManager);
        sampleEcritureComptable = new EcritureComptable();
        sampleEcritureComptable.setId(1);
        sampleEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        sampleEcritureComptable.setDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-02-11"));
        //sampleEcritureComptable.setDate(new GregorianCalendar(2020, Calendar.FEBRUARY, 11).getTime());
        sampleEcritureComptable.setLibelle("Libelle");
        sampleEcritureComptable.setReference("AC-2020/00001");
        LigneEcritureComptable l1 = new LigneEcritureComptable(new CompteComptable(1,"Achats"),"Cartouches", new BigDecimal("123.00"),null);
        LigneEcritureComptable l2 = new LigneEcritureComptable(new CompteComptable(2,"Banque"),"Cartouches", null,new BigDecimal("123.00"));
        sampleEcritureComptable.getListLigneEcriture().add(l1);
        sampleEcritureComptable.getListLigneEcriture().add(l2);

    }

    @After
    public void reset() {
        Mockito.reset(daoProxy);
        Mockito.reset(comptabiliteDao);
        Mockito.reset(transactionManager);
    }

    @Test
    public void getListCompteComptable_shouldGetListByCallingDao(){
        logger.error(" la valeur de sampleEcritureComptable "+sampleEcritureComptable);
        List<CompteComptable> compteComptables = new ArrayList<>();
        compteComptables.add(new CompteComptable(1));
        compteComptables.add(new CompteComptable(2));

        logger.error(" la valeur de comptabiliteDao "+comptabiliteDao);
        Mockito.when(comptabiliteDao.getListCompteComptable()).thenReturn(compteComptables);
        Mockito.when(daoProxy.getComptabiliteDao()).thenReturn(comptabiliteDao);
        logger.error(" la valeur de daProxy getComptabiliteDao "+daoProxy.getComptabiliteDao());

        //comptabiliteDao.insertEcritureComptable(sampleEcritureComptable);
        //logger.error(" la valeur de getListEcritureComptable de sampleEcritureComptable "+sampleEcritureComptable.getListLigneEcriture());

        List<CompteComptable> result = objectToTest.getListCompteComptable();
        logger.error(" la valeur de result "+result);
        logger.error(" la valeur de compteComptables"+compteComptables);

        Assertions.assertThat(result).isEqualTo(compteComptables);
        Mockito.verify(comptabiliteDao).getListCompteComptable();
    }

    @Test
    public void getListJournalComptable_shouldGetListByCallingDao() {
        List<JournalComptable> journalComptables = new ArrayList<>();
        journalComptables.add(new JournalComptable("BQ", "Banque"));
        journalComptables.add(new JournalComptable("FO", "Fournisseur"));
        Mockito.when(daoProxy.getComptabiliteDao()).thenReturn(comptabiliteDao);
        Mockito.when(comptabiliteDao.getListJournalComptable()).thenReturn(journalComptables);

        List<JournalComptable> result = objectToTest.getListJournalComptable();

        Assertions.assertThat(result).isEqualTo(journalComptables);
        Mockito.verify(comptabiliteDao).getListJournalComptable();
    }

    @Test
    public void getListEcritureComptable_shouldGetListByCallingDao() {
        List<EcritureComptable> ecritureComptables = new ArrayList<>();
        ecritureComptables.add(new EcritureComptable());
        ecritureComptables.add(new EcritureComptable());
        Mockito.when(daoProxy.getComptabiliteDao()).thenReturn(comptabiliteDao);
        Mockito.when(comptabiliteDao.getListEcritureComptable()).thenReturn(ecritureComptables);

        List<EcritureComptable> result = objectToTest.getListEcritureComptable();

        Assertions.assertThat(result).isEqualTo(ecritureComptables);
        Mockito.verify(comptabiliteDao).getListEcritureComptable();
    }

    @Test
    public void addReference_shouldcreateNewSequenceInDBAndConstructRef_whenSequenceNotFound() throws NotFoundException, FunctionalException {
        LocalDate ecritureDate = sampleEcritureComptable.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        String expectedRef = sampleEcritureComptable.getReference();
        sampleEcritureComptable.setReference("");
        Mockito.when(daoProxy.getComptabiliteDao()).thenReturn(comptabiliteDao);
        Mockito.when(comptabiliteDao.getSequenceByYearAndJournalCode(ecritureDate.getYear(), sampleEcritureComptable.getJournal().getCode())).thenThrow(NotFoundException.class);

        objectToTest.addReference(sampleEcritureComptable);

        Mockito.verify(comptabiliteDao).insertNewSequence(Mockito.any(SequenceEcritureComptable.class));
        Assertions.assertThat(sampleEcritureComptable.getReference()).isEqualTo(expectedRef);
    }

    @Test
    public void addReference_shouldUpdateSequenceDerniereValeurInDBAndConstructRef_whenSequenceFound() throws NotFoundException, FunctionalException {
        String expectedRef = "AC-2020/00006";
        SequenceEcritureComptable sequenceEcritureComptableFound = new SequenceEcritureComptable(2020, new JournalComptable("AC", "Achat"), 5);
        LocalDate ecritureDate = sampleEcritureComptable.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        sampleEcritureComptable.setReference("");
        Mockito.when(daoProxy.getComptabiliteDao()).thenReturn(comptabiliteDao);
        Mockito.when(comptabiliteDao.getSequenceByYearAndJournalCode(ecritureDate.getYear(), sampleEcritureComptable.getJournal().getCode())).thenReturn(sequenceEcritureComptableFound);

        objectToTest.addReference(sampleEcritureComptable);

        Mockito.verify(comptabiliteDao).updateSequenceEcritureComptable(sequenceEcritureComptableFound);
        Assertions.assertThat(sampleEcritureComptable.getReference()).isEqualTo(expectedRef);
    }

    @Test
    public void addReference_shouldThrowFunctionalException_whenEcritureComptableDateIsNull() {
        sampleEcritureComptable.setDate(null);

        Assertions.assertThatThrownBy(() -> objectToTest.addReference(sampleEcritureComptable))
                .isInstanceOf(FunctionalException.class)
                .hasMessageContaining(Constant.ECRITURE_COMPTABLE_DATE_NULL_FOR_ADD_REFERENCE);
    }

    @Test
    public void addReference_shouldThrowFunctionalException_whenEcritureComptableJournalIsNull() {
        sampleEcritureComptable.setJournal(null);

        Assertions.assertThatThrownBy(() -> objectToTest.addReference(sampleEcritureComptable))
                .isInstanceOf(FunctionalException.class)
                .hasMessageContaining(Constant.ECRITURE_COMPTABLE_JOURNAL_NULL_FOR_ADD_REFERENCE);
    }

    @Test
    public void addReference_shouldThrowFunctionalException_whenEcritureComptableJournalCodeIsNull() {
        JournalComptable brokenJournalComptable = new JournalComptable();
        brokenJournalComptable.setCode(null);
        sampleEcritureComptable.setJournal(brokenJournalComptable);

        Assertions.assertThatThrownBy(() -> objectToTest.addReference(sampleEcritureComptable))
                .isInstanceOf(FunctionalException.class)
                .hasMessageContaining(Constant.ECRITURE_COMPTABLE_JOURNAL_NULL_FOR_ADD_REFERENCE);
    }

    @Test
    public void checkEcritureComptable_correctNewEcritureComptable_shouldNotThrowException() throws NotFoundException, ParseException {

        sampleEcritureComptable.getListLigneEcriture().clear();
        sampleEcritureComptable = new EcritureComptable();
        sampleEcritureComptable.setId(1);
        sampleEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        sampleEcritureComptable.setDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-02-11"));
        sampleEcritureComptable.setLibelle("Libelle");
        sampleEcritureComptable.setReference("AC-2020/00001");
        LigneEcritureComptable l1 = new LigneEcritureComptable(new CompteComptable(1),"Cartouches", new BigDecimal(123),null);
        LigneEcritureComptable l2 = new LigneEcritureComptable(new CompteComptable(2),"Cartouches", null,new BigDecimal(123));
        sampleEcritureComptable.getListLigneEcriture().add(l1);
        sampleEcritureComptable.getListLigneEcriture().add(l2);
        logger.error(" la valeur de comptabiliteDao "+comptabiliteDao);
        Mockito.when(daoProxy.getComptabiliteDao()).thenReturn(comptabiliteDao);
        Mockito.when(comptabiliteDao.getEcritureComptableByRef(sampleEcritureComptable.getReference())).thenThrow(new NotFoundException());
        logger.error(" la ligne ecriture "+sampleEcritureComptable.getListLigneEcriture());
        logger.error(" date la ligne ecriture "+sampleEcritureComptable.getDate());
        Assertions.assertThatCode(() -> objectToTest.checkEcritureComptable(sampleEcritureComptable))
               .doesNotThrowAnyException();

    }

    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitViolation() throws Exception {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        objectToTest.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitRG2() throws Exception {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(123),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                null, null,
                new BigDecimal(1234)));
        vEcritureComptable.setReference("AC-2020/00001");
        objectToTest.checkEcritureComptableUnit(vEcritureComptable);
    }


    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitRG3() throws Exception {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, null,
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, null,
                null));
        vEcritureComptable.setReference("AC-2020/00001");
        objectToTest.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitRG3_() throws Exception {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(123),
                null));
        vEcritureComptable.setReference("AC-2020/00001");
        objectToTest.checkEcritureComptableUnit(vEcritureComptable);
    }

    //expected = FunctionalException.class
    @Test
    public void chekcEcritureComptableUnitRG5Format() throws Exception {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(123),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                null, null,
                new BigDecimal(123)));
        vEcritureComptable.setReference("AC-2020/000010");
        Assertions.assertThatThrownBy(() -> objectToTest.checkEcritureComptableUnit(vEcritureComptable))
                .isInstanceOf(FunctionalException.class)
                .hasMessageContaining(Constant.ECRITURE_COMPTABLE_MANAGEMENT_RULE_ERRORMSG);
    }

    @Test(expected = FunctionalException.class)
    public void chekcEcritureComptableUnitRG5Code() throws Exception {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(123),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                null, null,
                new BigDecimal(123)));
        vEcritureComptable.setReference("AQ-2020/00001");
        objectToTest.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void chekcEcrutiureComptableUnitRG5Annee() throws Exception {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(123),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                null, null,
                new BigDecimal(123)));
        vEcritureComptable.setReference("AC-2019/00001");
        objectToTest.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void chekcEcrutiureComptableUnitRG5RefVide() throws Exception {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(123),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                null, null,
                new BigDecimal(123)));
        vEcritureComptable.setReference("");
        objectToTest.checkEcritureComptableUnit(vEcritureComptable);
    }


    @Test
    public void checkEcritureComptable_ecritureComptableDoesNotRespectValidationConstraint_returnFunctionalExceptionWithCorrectMessage() {
        //Add only one LigneEcritureComptable, validation contraints expect at least two lines.
        sampleEcritureComptable.getListLigneEcriture().clear();
        sampleEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1,"Achats"),"Cartouches", new BigDecimal("123.00"),null));
        Assertions.assertThatThrownBy(() -> objectToTest.checkEcritureComptable(sampleEcritureComptable))
                .isInstanceOf(FunctionalException.class)
                .hasMessageContaining(Constant.ECRITURE_COMPTABLE_MANAGEMENT_RULE_ERRORMSG);
                //.hasMessageContaining(Constant.RG_COMPTA_3_VIOLATION_ERRORMSG);
    }

    @Test
    public void insertEcritureComptable_commitThrowException_shouldNotRollback() throws FunctionalException, NotFoundException {
        TransactionStatus transactionStatus = Mockito.mock(TransactionStatus.class);
        Mockito.when(daoProxy.getComptabiliteDao()).thenReturn(comptabiliteDao);
        Mockito.when(comptabiliteDao.getEcritureComptableByRef(sampleEcritureComptable.getReference())).thenThrow(new NotFoundException());
        Mockito.when(transactionManager.beginTransactionMyERP()).thenReturn(transactionStatus);
        Mockito.doThrow(RuntimeException.class).when(transactionManager).commitMyERP(transactionStatus);

        Assertions.assertThatThrownBy(() -> objectToTest.insertEcritureComptable(sampleEcritureComptable));

        Mockito.verify(transactionManager, Mockito.never()).rollbackMyERP(Mockito.any());
    }

    /*
    @Test
    public void updateEcritureComptable_commitThrowException_shouldNotRollback() throws FunctionalException, NotFoundException {
        TransactionStatus transactionStatus = Mockito.mock(TransactionStatus.class);
        Mockito.when(daoProxy.getComptabiliteDao()).thenReturn(comptabiliteDao);
        Mockito.when(comptabiliteDao.getEcritureComptableByRef(sampleEcritureComptable.getReference())).thenThrow(new NotFoundException());
        Mockito.when(transactionManager.beginTransactionMyERP()).thenReturn(transactionStatus);
        Mockito.doThrow(RuntimeException.class).when(transactionManager).commitMyERP(transactionStatus);

        Assertions.assertThatThrownBy(() -> objectToTest.updateEcritureComptable(sampleEcritureComptable));

        Mockito.verify(transactionManager, Mockito.never()).rollbackMyERP(Mockito.any());
    }

     */


    @Test
    public void deleteEcritureComptable_insertSuccess_shouldCallTransactionManagerAndDao() throws NotFoundException {
        TransactionStatus transactionStatus = Mockito.mock(TransactionStatus.class);
        Mockito.when(daoProxy.getComptabiliteDao()).thenReturn(comptabiliteDao);
        Mockito.when(comptabiliteDao.getEcritureComptableByRef(sampleEcritureComptable.getReference())).thenThrow(new NotFoundException());
        Mockito.when(transactionManager.beginTransactionMyERP()).thenReturn(transactionStatus);

        objectToTest.deleteEcritureComptable(sampleEcritureComptable.getId());

        Mockito.verify(transactionManager).beginTransactionMyERP();
        Mockito.verify(comptabiliteDao).deleteEcritureComptable(sampleEcritureComptable.getId());
        Mockito.verify(transactionManager).commitMyERP(Mockito.any());
        Mockito.verify(transactionManager, Mockito.never()).rollbackMyERP(Mockito.any());
    }

    @Test
    public void deleteEcritureComptable_insertThrowException_shouldNotCommitAndRollback() throws NotFoundException {
        TransactionStatus transactionStatus = Mockito.mock(TransactionStatus.class);
        Mockito.when(daoProxy.getComptabiliteDao()).thenReturn(comptabiliteDao);
        Mockito.when(comptabiliteDao.getEcritureComptableByRef(sampleEcritureComptable.getReference())).thenThrow(new NotFoundException());
        Mockito.when(transactionManager.beginTransactionMyERP()).thenReturn(transactionStatus);
        Mockito.doThrow(RuntimeException.class).when(comptabiliteDao).deleteEcritureComptable(sampleEcritureComptable.getId());

        Assertions.assertThatThrownBy(() -> objectToTest.deleteEcritureComptable(sampleEcritureComptable.getId()));

        Mockito.verify(transactionManager, Mockito.never()).commitMyERP(Mockito.any());
        Mockito.verify(transactionManager).rollbackMyERP(Mockito.any());
    }

    @Test
    public void deleteEcritureComptable_commitThrowException_shouldNotRollback() throws NotFoundException {
        TransactionStatus transactionStatus = Mockito.mock(TransactionStatus.class);
        Mockito.when(daoProxy.getComptabiliteDao()).thenReturn(comptabiliteDao);
        Mockito.when(comptabiliteDao.getEcritureComptableByRef(sampleEcritureComptable.getReference())).thenThrow(new NotFoundException());
        Mockito.when(transactionManager.beginTransactionMyERP()).thenReturn(transactionStatus);
        Mockito.doThrow(RuntimeException.class).when(transactionManager).commitMyERP(transactionStatus);

        Assertions.assertThatThrownBy(() -> objectToTest.deleteEcritureComptable(sampleEcritureComptable.getId()));

        Mockito.verify(transactionManager, Mockito.never()).rollbackMyERP(Mockito.any());
    }

    @Test
    public void getInstanceTransactionStatus(){
        TransactionStatus transactionStatus = Mockito.mock(TransactionStatus.class);
        Mockito.when(transactionManager.beginTransactionMyERP()).thenReturn(transactionStatus);
        transactionManager.commitMyERP(transactionStatus);
        transactionManager.rollbackMyERP(transactionStatus);
        logger.error(" la valeur de transactionStatus "+transactionStatus);
        logger.error(" la valeur de transactionStatus "+transactionManager.beginTransactionMyERP());
        Assertions.assertThat(transactionManager.beginTransactionMyERP()).isNotNull();
    }


}
