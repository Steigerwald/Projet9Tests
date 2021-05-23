package com.dummy.myerp.business.manager;

import BusinessProxyImplTest.BusinessProxyImplTest;
import com.dummy.myerp.business.contrat.BusinessProxy;
import com.dummy.myerp.business.impl.TransactionManager;
import com.dummy.myerp.business.impl.manager.ComptabiliteManagerImpl;
import com.dummy.myerp.business.util.Constant;
import com.dummy.myerp.consumer.dao.contrat.ComptabiliteDao;
import com.dummy.myerp.consumer.dao.contrat.DaoProxy;
import com.dummy.myerp.consumer.dao.impl.db.dao.ComptabiliteDaoImpl;
import com.dummy.myerp.model.bean.comptabilite.*;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;
import config.BusinessContextBeans;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
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
import java.util.*;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {BusinessContextBeans.class})
public class ComptabiliteManagerImplTest {

    Logger logger = (Logger) LoggerFactory.getLogger(ComptabiliteManagerImplTest.class);

    @Autowired
    private BusinessProxy businessProxy;

    @Autowired
    private DaoProxy daoProxy;

    @Autowired
    private TransactionManager transactionManager;

    @Autowired
    private ComptabiliteDao comptabiliteDao;

    public ComptabiliteManagerImpl objectToTest;

    public EcritureComptable sampleEcritureComptable;

    @Before
    public void init() throws FunctionalException {
        //ComptabiliteManagerImpl.configure(businessProxy, daoProxy, transactionManager);
        objectToTest = new ComptabiliteManagerImpl();
        //logger.error(" la valeur de objectToTest  "+objectToTest);
        sampleEcritureComptable = new EcritureComptable();
        //sampleEcritureComptable.setId(1);
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
        //objectToTest.insertEcritureComptable(sampleEcritureComptable);
        //logger.error(" la valeur de objectToTest  "+objectToTest.getListCompteComptable());
        //logger.error(" la valeur de ligneEcritureComptable de sampleEcritureComptable  "+sampleEcritureComptable.getListLigneEcriture());
    }

    @After
    public void reset() {
        Mockito.reset(daoProxy);
        Mockito.reset(comptabiliteDao);
        Mockito.reset(transactionManager);
        //Mockito.reset(objectToTest);
        //Mockito.reset(sampleEcritureComptable);
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

        comptabiliteDao.insertEcritureComptable(sampleEcritureComptable);
        logger.error(" la valeur de getListEcritureComptable de sampleEcritureComptable "+sampleEcritureComptable.getListLigneEcriture());

        List<CompteComptable> result = objectToTest.getListCompteComptable();
        logger.error(" la valeur de result "+result);
        logger.error(" la valeur de compteComptables"+compteComptables);

        Assertions.assertThat(result).isEqualTo(compteComptables);
        Mockito.verify(comptabiliteDao).getListCompteComptable();
    }
}
