package com.dummy.myerp.business.manager;

import BusinessProxyImplTest.BusinessProxyImplTest;
import com.dummy.myerp.business.contrat.BusinessProxy;
import com.dummy.myerp.business.impl.TransactionManager;
import com.dummy.myerp.business.impl.manager.ComptabiliteManagerImpl;
import com.dummy.myerp.business.util.Constant;
import com.dummy.myerp.consumer.dao.contrat.ComptabiliteDao;
import com.dummy.myerp.consumer.dao.contrat.DaoProxy;
import com.dummy.myerp.model.bean.comptabilite.*;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;
import config.BusinessContextBeans;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.TransactionStatus;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = {BusinessContextBeans.class})
public class ComptabiliteManagerImplTest {
/*
    Logger logger = (Logger) LoggerFactory.getLogger(ComptabiliteManagerImplTest.class);

    @Mock
    private BusinessProxy businessProxy;

    @Mock
    private DaoProxy daoProxy;

    @Mock
    private TransactionManager transactionManager;

    @Mock
    private ComptabiliteDao comptabiliteDao;

    @Mock
    public ComptabiliteManagerImpl objectToTest;

    @Mock
    public EcritureComptable sampleEcritureComptable;

    @BeforeEach
    public void init() throws FunctionalException {
        //objectToTest.configure(businessProxy, daoProxy, transactionManager);
        objectToTest = new ComptabiliteManagerImpl();
        logger.error(" la valeur de getListComptable de objectToTest  "+objectToTest.getListCompteComptable());
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
        objectToTest.insertEcritureComptable(sampleEcritureComptable);
        logger.error(" la valeur de ligneEcritureComptable de sampleEcritureComptable  "+sampleEcritureComptable.getListLigneEcriture());

    }


    @AfterEach
    public void reset() {
        Mockito.reset(daoProxy);
        Mockito.reset(comptabiliteDao);
        Mockito.reset(transactionManager);
        //Mockito.reset(objectToTest);
        //Mockito.reset(sampleEcritureComptable);
    }

    @Test
    public void getListCompteComptable_shouldGetListByCallingDao(){


        logger.error(" la valeur de getListCompteComptable de objectToTest  "+objectToTest.getListCompteComptable());
        List<CompteComptable> compteComptables = new ArrayList<>();
        compteComptables.add(new CompteComptable(1));
        compteComptables.add(new CompteComptable(2));
        logger.error(" la valeur de comptabiliteDao "+comptabiliteDao);
        logger.error(" la valeur de daProxy getComptabiliteDao "+daoProxy.getComptabiliteDao());

        Mockito.when(daoProxy.getComptabiliteDao()).thenReturn(comptabiliteDao);
        Mockito.when(comptabiliteDao.getListCompteComptable()).thenReturn(compteComptables);
        sampleEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(123),
                null));
        sampleEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                null, null,
                new BigDecimal(123)));
        comptabiliteDao.insertEcritureComptable(sampleEcritureComptable);
        logger.error(" la valeur de getListEcritureComptable de sampleEcritureComptable "+sampleEcritureComptable.getListLigneEcriture());

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
    public void checkEcritureComptable_correctNewEcritureComptable_shouldNotThrowException() throws NotFoundException {
        Mockito.when(daoProxy.getComptabiliteDao()).thenReturn(comptabiliteDao);
        Mockito.when(comptabiliteDao.getEcritureComptableByRef(sampleEcritureComptable.getReference())).thenThrow(new NotFoundException());

        Assertions.assertThatCode(() -> objectToTest.checkEcritureComptable(sampleEcritureComptable))
                .doesNotThrowAnyException();
    }

    @Test
    public void checkEcritureComptable_alreadySavedEcritureComptable_shouldNotThrowException() throws NotFoundException {
        Mockito.when(daoProxy.getComptabiliteDao()).thenReturn(comptabiliteDao);
        Mockito.when(comptabiliteDao.getEcritureComptableByRef(sampleEcritureComptable.getReference())).thenReturn(sampleEcritureComptable);

        Assertions.assertThatCode(() -> objectToTest.checkEcritureComptable(sampleEcritureComptable))
                .doesNotThrowAnyException();
    }

    @Test
    public void checkEcritureComptable_ecritureComptableDoesNotRespectValidationConstraint_returnFunctionalExceptionWithCorrectMessage() {
        //Add only one LigneEcritureComptable, validation contraints expect at least two lines.
        sampleEcritureComptable.getListLigneEcriture().clear();
        sampleEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(123),
                null));

        Assertions.assertThatThrownBy(() -> objectToTest.checkEcritureComptable(sampleEcritureComptable))
                .isInstanceOf(FunctionalException.class)
                .hasMessageContaining(Constant.ECRITURE_COMPTABLE_MANAGEMENT_RULE_ERRORMSG);
    }

    @ParameterizedTest(name = "{0} is not a valid reference")
    @ValueSource(strings = {"BQ-201/00002", "B1-2019/00002", "BC2019/00002", "BC-201900002", "BC-2019/000028", "BC-201a/00002"})
    public void checkEcritureComptable_invalidReferenceFormat_returnFunctionalExceptionWithCorrectMessage(String arg) {
        sampleEcritureComptable.setReference(arg);

        Assertions.assertThatThrownBy(() -> objectToTest.checkEcritureComptable(sampleEcritureComptable))
                .isInstanceOf(FunctionalException.class)
                .hasMessageContaining(Constant.ECRITURE_COMPTABLE_MANAGEMENT_RULE_ERRORMSG);
    }

    @Test
    @Tag("RG")
    @Tag("RG_Compta_2")
    public void checkEcritureComptable_unbalancedEcritureComptable_returnFunctionalExceptionForRGCompta2Violation() {
        sampleEcritureComptable.getListLigneEcriture().clear();
        sampleEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(123),
                null));
        sampleEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                null, null,
                new BigDecimal(1234)));

        Assertions.assertThatThrownBy(() -> objectToTest.checkEcritureComptable(sampleEcritureComptable))
                .isInstanceOf(FunctionalException.class)
                .hasMessageContaining(Constant.RG_COMPTA_2_VIOLATION_ERRORMSG);
    }

    @Test
    @Tag("RG")
    @Tag("RG_Compta_3")
    public void checkEcritureComptable_noCreditValue_returnFunctionalExceptionForRGCompta3Violation() {
        sampleEcritureComptable.getListLigneEcriture().clear();
        sampleEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(0),
                null));
        sampleEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(0),
                null));

        Assertions.assertThatThrownBy(() -> objectToTest.checkEcritureComptable(sampleEcritureComptable))
                .isInstanceOf(FunctionalException.class)
                .hasMessageContaining(Constant.RG_COMPTA_3_VIOLATION_ERRORMSG);
    }

    @Test
    @Tag("RG")
    @Tag("RG_Compta_3")
    public void checkEcritureComptable_noDebitValue_returnFunctionalExceptionForRGCompta3Violation() {
        sampleEcritureComptable.getListLigneEcriture().clear();
        sampleEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, null,
                new BigDecimal(0)));
        sampleEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, null,
                new BigDecimal(0)));

        Assertions.assertThatThrownBy(() -> objectToTest.checkEcritureComptable(sampleEcritureComptable))
                .isInstanceOf(FunctionalException.class)
                .hasMessageContaining(Constant.RG_COMPTA_3_VIOLATION_ERRORMSG);
    }

    @ParameterizedTest
    @ValueSource(strings = {"BQ-2020/00001", "AC-2019/00001"})
    @Tag("RG")
    @Tag("RG_Compta_5")
    public void checkEcritureComptable_invalidReference_returnFunctionalExceptionForRGCompta5Violation(String arg1) {
        sampleEcritureComptable.setReference(arg1);

        Assertions.assertThatThrownBy(() -> objectToTest.checkEcritureComptable(sampleEcritureComptable))
                .isInstanceOf(FunctionalException.class)
                .hasMessageContaining(Constant.RG_COMPTA_5_VIOLATION_ERRORMSG);
    }

    @Test
    @Tag("RG")
    @Tag("RG_Compta_6")
    public void checkEcritureComptable_notUniqueReference_returnFunctionalExceptionForRGCompta6Violation() throws NotFoundException {
        EcritureComptable ecritureComptableWithSameReference = new EcritureComptable();
        ecritureComptableWithSameReference.setReference("AC-2020/00001");
        //getEcritureComptableByRef return an EcritureComptable, that mean the reference is already used by an other EcritureComptable
        Mockito.when(daoProxy.getComptabiliteDao()).thenReturn(comptabiliteDao);
        Mockito.when(comptabiliteDao.getEcritureComptableByRef(sampleEcritureComptable.getReference())).thenReturn(ecritureComptableWithSameReference);

        Assertions.assertThatThrownBy(() -> objectToTest.checkEcritureComptable(sampleEcritureComptable))
                .isInstanceOf(FunctionalException.class)
                .hasMessageContaining(Constant.RG_COMPTA_6_VIOLATION_ERRORMSG);
    }

    @Test
    public void insertEcritureComptable_insertSuccess_shouldCallTransactionManagerAndDao() throws FunctionalException, NotFoundException {
        TransactionStatus transactionStatus = Mockito.mock(TransactionStatus.class);
        Mockito.when(daoProxy.getComptabiliteDao()).thenReturn(comptabiliteDao);
        Mockito.when(comptabiliteDao.getEcritureComptableByRef(sampleEcritureComptable.getReference())).thenThrow(new NotFoundException());
        Mockito.when(transactionManager.beginTransactionMyERP()).thenReturn(transactionStatus);

        objectToTest.insertEcritureComptable(sampleEcritureComptable);

        Mockito.verify(transactionManager).beginTransactionMyERP();
        Mockito.verify(comptabiliteDao).insertEcritureComptable(sampleEcritureComptable);
        Mockito.verify(transactionManager).commitMyERP(Mockito.any());
        Mockito.verify(transactionManager, Mockito.never()).rollbackMyERP(Mockito.any());
    }

    @Test
    public void insertEcritureComptable_insertThrowException_shouldNotCommitAndRollback() throws FunctionalException, NotFoundException {
        TransactionStatus transactionStatus = Mockito.mock(TransactionStatus.class);
        Mockito.when(daoProxy.getComptabiliteDao()).thenReturn(comptabiliteDao);
        Mockito.when(comptabiliteDao.getEcritureComptableByRef(sampleEcritureComptable.getReference())).thenThrow(new NotFoundException());
        Mockito.when(transactionManager.beginTransactionMyERP()).thenReturn(transactionStatus);
        Mockito.doThrow(RuntimeException.class).when(comptabiliteDao).insertEcritureComptable(sampleEcritureComptable);

        Assertions.assertThatThrownBy(() -> objectToTest.insertEcritureComptable(sampleEcritureComptable));

        Mockito.verify(transactionManager, Mockito.never()).commitMyERP(Mockito.any());
        Mockito.verify(transactionManager).rollbackMyERP(Mockito.any());
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

    @Test
    public void updateEcritureComptable_insertSuccess_shouldCallTransactionManagerAndDao() throws FunctionalException, NotFoundException {
        TransactionStatus transactionStatus = Mockito.mock(TransactionStatus.class);
        Mockito.when(daoProxy.getComptabiliteDao()).thenReturn(comptabiliteDao);
        Mockito.when(comptabiliteDao.getEcritureComptableByRef(sampleEcritureComptable.getReference())).thenThrow(new NotFoundException());
        Mockito.when(transactionManager.beginTransactionMyERP()).thenReturn(transactionStatus);

        objectToTest.updateEcritureComptable(sampleEcritureComptable);

        Mockito.verify(transactionManager).beginTransactionMyERP();
        Mockito.verify(comptabiliteDao).updateEcritureComptable(sampleEcritureComptable);
        Mockito.verify(transactionManager).commitMyERP(Mockito.any());
        Mockito.verify(transactionManager, Mockito.never()).rollbackMyERP(Mockito.any());
    }

    @Test
    public void updateEcritureComptable_insertThrowException_shouldNotCommitAndRollback() throws FunctionalException, NotFoundException {
        TransactionStatus transactionStatus = Mockito.mock(TransactionStatus.class);
        Mockito.when(daoProxy.getComptabiliteDao()).thenReturn(comptabiliteDao);
        Mockito.when(comptabiliteDao.getEcritureComptableByRef(sampleEcritureComptable.getReference())).thenThrow(new NotFoundException());
        Mockito.when(transactionManager.beginTransactionMyERP()).thenReturn(transactionStatus);
        Mockito.doThrow(RuntimeException.class).when(comptabiliteDao).updateEcritureComptable(sampleEcritureComptable);

        Assertions.assertThatThrownBy(() -> objectToTest.updateEcritureComptable(sampleEcritureComptable));

        Mockito.verify(transactionManager, Mockito.never()).commitMyERP(Mockito.any());
        Mockito.verify(transactionManager).rollbackMyERP(Mockito.any());
    }

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
*/
}
