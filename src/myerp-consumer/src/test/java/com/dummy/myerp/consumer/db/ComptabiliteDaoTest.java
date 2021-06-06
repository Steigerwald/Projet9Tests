package com.dummy.myerp.consumer.db;

//import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertTrue;

//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.Matchers.is;
//import static org.hamcrest.Matchers.equalTo;

//import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.*;

import org.hamcrest.CoreMatchers.*;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;

import com.dummy.myerp.consumer.config.SpringRegistry;
import com.dummy.myerp.consumer.dao.contrat.ComptabiliteDao;
import com.dummy.myerp.consumer.dao.impl.db.dao.ComptabiliteDaoImpl;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.SequenceEcritureComptable;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;

import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.xml.crypto.Data;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ComptabiliteDaoTest {

	Logger logger = (Logger) LoggerFactory.getLogger(ComptabiliteDaoTest.class);


	private ComptabiliteDao comptabiliteDao;

	public ComptabiliteDaoTest() {
		// TODO Auto-generated constructor stub
		SpringRegistry.init();
		comptabiliteDao = ComptabiliteDaoImpl.getInstance();
	}


	@Test
	public void checkGetListCompteComptable() {
		List<CompteComptable> cList = comptabiliteDao.getListCompteComptable();
		
		CompteComptable c1 = new CompteComptable(401,"Fournisseurs");
		CompteComptable c2 = new CompteComptable(411,"Clients");
		CompteComptable c3 = new CompteComptable(4456,"Taxes sur le chiffre d'affaires déductibles");
		CompteComptable c4 = new CompteComptable(4457,"Taxes sur le chiffre d'affaires collectées par l'entreprise");
		CompteComptable c5 = new CompteComptable(512,"Banque");
		CompteComptable c6 = new CompteComptable(606,"Achats non stockés de matières et fournitures");
		CompteComptable c7 = new CompteComptable(706,"Prestations de services");
		
		assertThat(cList).usingFieldByFieldElementComparator().containsExactly(c1,c2,c3,c4,c5,c6,c7);
	}


	@Test
	public void checkGetListJournalComptable() {
		List<JournalComptable> jList = comptabiliteDao.getListJournalComptable();
		
		JournalComptable j1 = new JournalComptable("AC","Achat");
		JournalComptable j2 = new JournalComptable("VE","Vente");
		JournalComptable j3 = new JournalComptable("BQ","Banque");
		JournalComptable j4 = new JournalComptable("OD","Opérations Diverses");
		
		assertThat(jList).usingFieldByFieldElementComparator().containsExactly(j1,j2,j3,j4);
	}

	/*
	@Test
	public void checkAGetListEcritureComptable() throws ParseException {
		List<EcritureComptable> eList = comptabiliteDao.getListEcritureComptable();
		
		//EcritureComptable e1 = new EcritureComptable("AC","AC-2016/00001",new SimpleDateFormat("yyyy-MM-dd").parse("2016-12-28"),"Cartouches d’imprimante");
		EcritureComptable e1 = new EcritureComptable();
		e1.setId(-1);
		e1.setJournal(new JournalComptable("AC","Achat"));
		e1.setReference("AC-2016/00001");
		e1.setLibelle("Cartouches d’imprimante");
		e1.setDate(new SimpleDateFormat("yyyy-MM-dd").parse("2016-12-31"));
		
		LigneEcritureComptable l1 = new LigneEcritureComptable(new CompteComptable(606,"Achats non stockés de matières et fournitures"),"Cartouches d’imprimante", new BigDecimal("43.95"),null);
		LigneEcritureComptable l2 = new LigneEcritureComptable(new CompteComptable(4456,"Taxes sur le chiffre d'affaires déductibles"),"TVA 20%", new BigDecimal("8.79"),null);
		LigneEcritureComptable l3 = new LigneEcritureComptable(new CompteComptable(401,"Fournisseurs"),"Facture F110001", null, new BigDecimal("52.74"));
		e1.getListLigneEcriture().add(l1);
		e1.getListLigneEcriture().add(l2);
		e1.getListLigneEcriture().add(l3);
		
		EcritureComptable e2 = new EcritureComptable();
		e2.setId(-2);
		e2.setJournal(new JournalComptable("VE","Vente"));
		e2.setReference("VE-2016/00002");
		e2.setLibelle("TMA Appli Xxx");
		e2.setDate(new SimpleDateFormat("yyyy-MM-dd").parse("2016-12-30"));
		
		l1 = new LigneEcritureComptable(new CompteComptable(411,"Clients"),"Facture C110002", new BigDecimal("3000"),null);
		l2 = new LigneEcritureComptable(new CompteComptable(706,"Prestations de services"),"TMA Appli Xxx", null, new BigDecimal("2500"));
		l3 = new LigneEcritureComptable(new CompteComptable(4457,"Taxes sur le chiffre d'affaires collectées par l'entreprise"),"TVA 20%", null, new BigDecimal("500"));
		e2.getListLigneEcriture().add(l1);
		e2.getListLigneEcriture().add(l2);
		e2.getListLigneEcriture().add(l3);
		
		EcritureComptable e3 = new EcritureComptable();
		e3.setId(-3);
		e3.setJournal(new JournalComptable("BQ","Banque"));
		e3.setReference("BQ-2016/00003");
		e3.setLibelle("Paiement Facture F110001");
		e3.setDate(new SimpleDateFormat("yyyy-MM-dd").parse("2016-12-29"));
		
		l1 = new LigneEcritureComptable(new CompteComptable(401,"Fournisseurs"),null, new BigDecimal("52.74"),null);
		l2 = new LigneEcritureComptable(new CompteComptable(512,"Banque"),null, null, new BigDecimal("52.74"));
		e3.getListLigneEcriture().add(l1);
		e3.getListLigneEcriture().add(l2);
		
		EcritureComptable e4 = new EcritureComptable();
		e4.setId(-4);
		e4.setJournal(new JournalComptable("VE","Vente"));
		e4.setReference("VE-2016/00004");
		e4.setLibelle("TMA Appli Yyy");
		e4.setDate(new SimpleDateFormat("yyyy-MM-dd").parse("2016-12-28"));
		
		l1 = new LigneEcritureComptable(new CompteComptable(411,"Clients"),"Facture C110004", new BigDecimal("5700"),null);
		l2 = new LigneEcritureComptable(new CompteComptable(706,"Prestations de services"),"TMA Appli Xxx", null, new BigDecimal("4750"));
		l3 = new LigneEcritureComptable(new CompteComptable(4457,"Taxes sur le chiffre d'affaires collectées par l'entreprise"),"TVA 20%", null, new BigDecimal("950"));
		e4.getListLigneEcriture().add(l1);
		e4.getListLigneEcriture().add(l2);
		e4.getListLigneEcriture().add(l3);
		
		EcritureComptable e5 = new EcritureComptable();
		e5.setId(-5);
		e5.setJournal(new JournalComptable("BQ","Banque"));
		e5.setReference("BQ-2016/00005");
		e5.setLibelle("Paiement Facture C110002");
		e5.setDate(new SimpleDateFormat("yyyy-MM-dd").parse("2016-12-27"));
				
		l1 = new LigneEcritureComptable(new CompteComptable(512,"Banque"),null, new BigDecimal("3000"), null);
		l2 = new LigneEcritureComptable(new CompteComptable(411,"Clients"),null, null, new BigDecimal("3000"));
		e5.getListLigneEcriture().add(l1);
		e5.getListLigneEcriture().add(l2);
		
		assertThat(eList).usingRecursiveFieldByFieldElementComparator().containsExactly(e1,e2,e3,e4,e5);
	}*/


	@Test
	public void checkGetEcritureComptableById() throws ParseException, NotFoundException {
		int id = -4;
		EcritureComptable ecritureComptable = new EcritureComptable();
		ecritureComptable.setId(-4);
		ecritureComptable.setJournal(new JournalComptable("VE","Vente"));
		ecritureComptable.setLibelle("TMA Appli Yyy");
		ecritureComptable.setReference("VE-2016/00004");
		//ecritureComptable.setDate(new SimpleDateFormat("yyyy-MM-dd").parse("2016-12-28"));
		Date simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd").parse("2016-12-28");
		logger.error(" date simpleformat"+(simpleDateFormat));
		ecritureComptable.setDate(simpleDateFormat);
		logger.error(" la date de ecritureComptable "+((ecritureComptable.getDate())));
		//ecritureComptable.setDate(new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US).parse("Wed Jul 13 00:00:00 CEST 2011"));

		LigneEcritureComptable ligne1 = new LigneEcritureComptable(new CompteComptable(411,"Clients"),"Facture C110004",new BigDecimal("5700.00"),null);
		LigneEcritureComptable ligne2 = new LigneEcritureComptable(new CompteComptable(706,"Prestations de services"),"TMA Appli Xxx",null,new BigDecimal("4750.00"));
		LigneEcritureComptable ligne3 = new LigneEcritureComptable(new CompteComptable(4457,"Taxes sur le chiffre d'affaires collectées par l'entreprise"),"TVA 20%",null,new BigDecimal("950.00"));

		ecritureComptable.getListLigneEcriture().add(ligne1);
		ecritureComptable.getListLigneEcriture().add(ligne2);
		ecritureComptable.getListLigneEcriture().add(ligne3);
		logger.error(" l'écriture ecritureComptable"+((ecritureComptable.toString())));

		EcritureComptable eComptableTest = comptabiliteDao.getEcritureComptable(-4);
		logger.error(" l'écriture eComptableTest "+((eComptableTest.toString())));
		logger.error(" date de eComptableTest "+((eComptableTest.getDate())));

		assertThat(eComptableTest.toString()).isEqualTo(ecritureComptable.toString());

	}

	/*
	@Test(expected = NotFoundException.class)
	public void checkGetEcritureComptableByIdNotFound() throws NotFoundException {
		comptabiliteDao.getEcritureComptable(28);
	}

	 */
/*
	@Test
	public void checkGetEcritureComptableByRef() throws ParseException, NotFoundException {
		String ref = "BQ-2016/00005";
		
		EcritureComptable ecritureComptable = new EcritureComptable();
		ecritureComptable.setId(-5);
		ecritureComptable.setJournal(new JournalComptable("BQ","Banque"));
		ecritureComptable.setLibelle("Paiement Facture C110002");
		ecritureComptable.setReference("BQ-2016/00005");
		ecritureComptable.setDate(new SimpleDateFormat("yyyy-MM-dd").parse("2016-12-27"));
		
		LigneEcritureComptable ligne1 = new LigneEcritureComptable(new CompteComptable(512,"Banque"),null,new BigDecimal("3000.00"),null);
		LigneEcritureComptable ligne2 = new LigneEcritureComptable(new CompteComptable(411,"Clients"),null,null,new BigDecimal("3000.00"));

		ecritureComptable.getListLigneEcriture().add(ligne1);
		ecritureComptable.getListLigneEcriture().add(ligne2);
		
		EcritureComptable eComptableTest = comptabiliteDao.getEcritureComptableByRef(ref);
		
		assertThat(eComptableTest).isEqualTo(ecritureComptable);
	}

 */
	/*
	@Test(expected = NotFoundException.class)
	public void checkGetEcritureComptableByRefNotFound() throws NotFoundException {
		comptabiliteDao.getEcritureComptableByRef("AD-2018/00041");
	}
	
	@Test
	public void checkGetSequenceEcritureComptableByCodeAndAnnee() throws NotFoundException {
		String code = "OD";
		int annee = 2016;
		
		SequenceEcritureComptable sequenceEcritureComptable = new SequenceEcritureComptable();
		sequenceEcritureComptable.setAnnee(2016);
		sequenceEcritureComptable.setJournalCode(code);
		sequenceEcritureComptable.setDerniereValeur(88);
		
		SequenceEcritureComptable sComptableTest = comptabiliteDao.getSequenceEcritureComptableByCodeAndByAnnee(code, annee);
		
		assertThat(sComptableTest).isEqualToComparingFieldByField(sequenceEcritureComptable);
	}
	
	@Test(expected = NotFoundException.class)
	public void checkGetSequenceEcritureComptableByCodeAndAnneeNotFound() throws NotFoundException {
		String code = "JS";
		int annee = 2014;
		
		comptabiliteDao.getSequenceEcritureComptableByCodeAndByAnnee(code, annee);
	} 

/*
	@Test
	@Transactional
	@Rollback
	public void checkInsertSequenceEcritureComptable() throws NotFoundException {

		List<SequenceEcritureComptable> sList = new ArrayList<SequenceEcritureComptable>();
		sList = comptabiliteDao.getListSequenceEcritureComptable();
		int sizeBefore = sList.size();

		SequenceEcritureComptable sequenceEcritureComptable = new SequenceEcritureComptable();
		sequenceEcritureComptable.setJournalCode("VE");
		sequenceEcritureComptable.setAnnee(2020);
		sequenceEcritureComptable.setDerniereValeur(1);
		comptabiliteDao.insertSequenceEcritureComptable(sequenceEcritureComptable);

		sList = comptabiliteDao.getListSequenceEcritureComptable();

		int sizeExpected = sizeBefore + 1;

		assertThat(sList).hasSize(sizeExpected);

		SequenceEcritureComptable sequenceEcritureComptable2 = comptabiliteDao
				.getSequenceEcritureComptableByCodeAndByAnnee("VE", 2020);
		assertThat(sequenceEcritureComptable2).isEqualToComparingFieldByField(sequenceEcritureComptable);
	}

	@Test
	@Transactional
	@Rollback
	public void checkUpdateSequenceEcritureComptable() throws NotFoundException {
		SequenceEcritureComptable sequenceEcritureComptable = new SequenceEcritureComptable("BQ", 2016, 2);
		comptabiliteDao.updateSequenceEcritureComptable(sequenceEcritureComptable);
		
		sequenceEcritureComptable = comptabiliteDao.getSequenceEcritureComptableByCodeAndByAnnee("BQ", 2016);
		assertThat(sequenceEcritureComptable).extracting("derniereValeur").isEqualTo(2);
	}

	@Test
	@Transactional
	@Rollback
	public void checkInsertEcritureComptable() throws NotFoundException, ParseException {

		List<EcritureComptable> eList = new ArrayList<EcritureComptable>();
		eList = comptabiliteDao.getListEcritureComptable();
		int sizeBefore = eList.size();

		EcritureComptable vEcritureComptable;
		vEcritureComptable = new EcritureComptable();
		vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
		vEcritureComptable.setDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-12-31"));
		vEcritureComptable.setLibelle("Achat");
		
		LigneEcritureComptable ligneEcritureComptable1 = new LigneEcritureComptable(new CompteComptable(401, "Fournisseurs"), null, new BigDecimal(123), null);
		LigneEcritureComptable ligneEcritureComptable2 = new LigneEcritureComptable(new CompteComptable(411, "Clients"), null, null, new BigDecimal(123));
		
		vEcritureComptable.getListLigneEcriture().add(ligneEcritureComptable1);
		vEcritureComptable.getListLigneEcriture().add(ligneEcritureComptable2);
		vEcritureComptable.setReference("AC-2019/00001");
		
		

		comptabiliteDao.insertEcritureComptable(vEcritureComptable);

		eList = comptabiliteDao.getListEcritureComptable();

		int sizeExpected = sizeBefore + 1;

		assertThat(eList).hasSize(sizeExpected);

		EcritureComptable ecritureComptable = comptabiliteDao.getEcritureComptableByRef("AC-2019/00001");
		assertThat(ecritureComptable).isEqualToComparingFieldByFieldRecursively(vEcritureComptable);
		//assertThat(ecritureComptable.getListLigneEcriture()).usingRecursiveFieldByFieldElementComparator().contains(ligneEcritureComptable1,ligneEcritureComptable2);
	}
	
	@Test
	@Transactional
	@Rollback
	public void checkUpdateEcritureComptable() throws ParseException, NotFoundException {
		EcritureComptable ecritureComptable = new EcritureComptable();
		ecritureComptable.setId(-3);
		ecritureComptable.setJournal(new JournalComptable("BQ","Banque"));
		ecritureComptable.setDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-12-29"));
		ecritureComptable.setLibelle("Paiement Facture F110001 changement");
		ecritureComptable.setReference("BQ-2016/00006");
		
		LigneEcritureComptable ligneEcritureComptable1 = new LigneEcritureComptable(new CompteComptable(401, "Fournisseurs"), null, new BigDecimal(52.74), null);
		LigneEcritureComptable ligneEcritureComptable2 = new LigneEcritureComptable(new CompteComptable(512, "Banque"), null, null, new BigDecimal(52.74));
		
		ecritureComptable.getListLigneEcriture().add(ligneEcritureComptable1);
		ecritureComptable.getListLigneEcriture().add(ligneEcritureComptable2);
		
		comptabiliteDao.updateEcritureComptable(ecritureComptable);
		
		EcritureComptable eComptableTest =comptabiliteDao.getEcritureComptableByRef("BQ-2016/00006");
		
//		assertThat(eComptableTest).usingRecursiveComparison().isEqualTo(ecritureComptable);
		assertThat(eComptableTest).isEqualToComparingFieldByField(ecritureComptable);
	}

	@Test 
	@Transactional
	@Rollback
	public void checkDeleteEcritureComptable() throws NotFoundException {
		
		int id = -2;
		 
		EcritureComptable ecritureComptable = comptabiliteDao.getEcritureComptable(id);
		
		List<EcritureComptable> eList = comptabiliteDao.getListEcritureComptable();
		int sizeEListBefore = eList.size();
		
		//comptabiliteDao.loadListLigneEcriture(ecritureComptable);
		
		//int sizeListLigneBefore = ecritureComptable.getListLigneEcriture().size();
		
		comptabiliteDao.deleteEcritureComptable(id);
		
		eList = comptabiliteDao.getListEcritureComptable();
		int sizeExpected = sizeEListBefore - 1;
		
		assertThat(eList.size()).isEqualTo(sizeExpected);
		assertThat(eList).usingRecursiveFieldByFieldElementComparator().doesNotContain(ecritureComptable);
	}*/
}
