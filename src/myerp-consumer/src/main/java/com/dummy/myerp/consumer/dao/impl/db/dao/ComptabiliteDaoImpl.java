package com.dummy.myerp.consumer.dao.impl.db.dao;

import com.dummy.myerp.consumer.dao.contrat.ComptabiliteDao;
import com.dummy.myerp.consumer.dao.impl.db.rowmapper.comptabilite.*;
import com.dummy.myerp.consumer.db.AbstractDbConsumer;
import com.dummy.myerp.consumer.db.DataSourcesEnum;
import com.dummy.myerp.model.bean.comptabilite.*;
import com.dummy.myerp.technical.exception.NotFoundException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.Types;
import java.util.List;


/**
 * Implémentation de l'interface {@link ComptabiliteDao}
 */
public class ComptabiliteDaoImpl extends AbstractDbConsumer implements ComptabiliteDao {

    // ==================== Constructeurs ====================
    /**
     * Instance unique de la classe (design pattern Singleton)
     */
    private static final String REFERENCE_FIELD = "reference";
    private static final String JOURNAL_CODE_FIELD = "journal_code";
    private static final String DATE_FIELD = "date";
    private static final String LIBELLE_FIELD = "libelle";
    private static final String ECRITURE_ID_FIELD = "ecriture_id";
    private static final String LIGNE_ID_FIELD = "ligne_id";
    private static final String COMPTE_COMPTABLE_NUMERO_FIELD = "compte_comptable_numero";
    private static final String DEBIT_FIELD = "debit";
    private static final String CREDIT_FIELD = "credit";
    private static final String ID_FIELD = "id";
    private static final String ANNEE_FIELD = "annee";
    private static final String DERNIERE_VALEUR_FIELD = "derniere_valeur";

    private static final ComptabiliteDaoImpl INSTANCE = new ComptabiliteDaoImpl();
    /**
     * SQLgetListCompteComptable
     */

    private static String sqlGetListCompteComptable;
    /**
     * SQLgetListJournalComptable
     */
    private static String sqlGetListJournalComptable;


    // ==================== Méthodes ====================
    /**
     * SQLgetListEcritureComptable
     */
    private static String sqlGetListEcritureComptable;
    /**
     * SQLgetEcritureComptable
     */
    private static String sqlGetEcritureComptable;
    /**
     * SQLgetEcritureComptableByRef
     */
    private static String sqlGetEcritureComptableByRef;
    /**
     * SQLloadListLigneEcriture
     */
    private static String sqlLoadListLigneEcriture;
    /**
     * SQLinsertEcritureComptable
     */
    private static String sqlInsertEcritureComptable;
    /**
     * SQLinsertListLigneEcritureComptable
     */
    private static String sqlInsertListLigneEcritureComptable;

    // ==================== EcritureComptable - GET ====================
    /**
     * SQLupdateEcritureComptable
     */
    private static String sqlUpdateEcritureComptable;
    /**
     * SQLdeleteEcritureComptable
     */
    private static String sqlDeleteEcritureComptable;
    /**
     * SQLdeleteListLigneEcritureComptable
     */
    private static String sqlDeleteListLigneEcritureComptable;

    private static String sqlGetSequenceEcritureComptableByAnneeAndJournalCode;

    private static String sqlInsertSequenceEcritureComptable;

    private static String sqlUpdateSequenceEcritureComptable;

    /**
     * Constructeur.
     */
    protected ComptabiliteDaoImpl() {
        super();
    }

    /**
     * Renvoie l'instance unique de la classe (design pattern Singleton).
     *
     * @return {@link ComptabiliteDaoImpl}
     */
    public static ComptabiliteDaoImpl getInstance() {
        return ComptabiliteDaoImpl.INSTANCE;
    }

    public void setSqlGetListCompteComptable(String pSQLgetListCompteComptable) {
        sqlGetListCompteComptable = pSQLgetListCompteComptable;
    }

    @Override
    public List<CompteComptable> getListCompteComptable() {
        JdbcTemplate vJdbcTemplate = new JdbcTemplate(this.getDataSource(DataSourcesEnum.MYERP));
        CompteComptableRM vRM = new CompteComptableRM();
        return vJdbcTemplate.query(sqlGetListCompteComptable, vRM);
    }

    public void setSqlGetListJournalComptable(String pSQLgetListJournalComptable) {
        sqlGetListJournalComptable = pSQLgetListJournalComptable;
    }

    @Override
    public List<JournalComptable> getListJournalComptable() {
        JdbcTemplate vJdbcTemplate = new JdbcTemplate(this.getDataSource(DataSourcesEnum.MYERP));
        JournalComptableRM vRM = new JournalComptableRM();
        return vJdbcTemplate.query(sqlGetListJournalComptable, vRM);
    }

    public void setSqlGetListEcritureComptable(String pSQLgetListEcritureComptable) {
        sqlGetListEcritureComptable = pSQLgetListEcritureComptable;
    }

    @Override
    public List<EcritureComptable> getListEcritureComptable() {
        JdbcTemplate vJdbcTemplate = new JdbcTemplate(this.getDataSource(DataSourcesEnum.MYERP));
        EcritureComptableRM vRM = new EcritureComptableRM();
        return vJdbcTemplate.query(sqlGetListEcritureComptable, vRM);
    }

    public void setSqlGetEcritureComptable(String pSQLgetEcritureComptable) {
        sqlGetEcritureComptable = pSQLgetEcritureComptable;
    }


    // ==================== EcritureComptable - INSERT ====================

    @Override
    public EcritureComptable getEcritureComptable(Integer pId) throws NotFoundException {
        NamedParameterJdbcTemplate vJdbcTemplate = new NamedParameterJdbcTemplate(getDataSource(DataSourcesEnum.MYERP));
        MapSqlParameterSource vSqlParams = new MapSqlParameterSource();
        vSqlParams.addValue(ID_FIELD, pId);
        EcritureComptableRM vRM = new EcritureComptableRM();
        EcritureComptable vBean;
        try {
            vBean = vJdbcTemplate.queryForObject(sqlGetEcritureComptable, vSqlParams, vRM);
        } catch (EmptyResultDataAccessException vEx) {
            throw new NotFoundException("EcritureComptable non trouvée : id=" + pId);
        }
        return vBean;
    }

    public void setSqlGetEcritureComptableByRef(String pSQLgetEcritureComptableByRef) {
        sqlGetEcritureComptableByRef = pSQLgetEcritureComptableByRef;
    }

    @Override
    public EcritureComptable getEcritureComptableByRef(String pReference) throws NotFoundException {
        NamedParameterJdbcTemplate vJdbcTemplate = new NamedParameterJdbcTemplate(getDataSource(DataSourcesEnum.MYERP));
        MapSqlParameterSource vSqlParams = new MapSqlParameterSource();
        vSqlParams.addValue(REFERENCE_FIELD, pReference);
        EcritureComptableRM vRM = new EcritureComptableRM();
        EcritureComptable vBean;
        try {
            vBean = vJdbcTemplate.queryForObject(sqlGetEcritureComptableByRef, vSqlParams, vRM);
        } catch (EmptyResultDataAccessException vEx) {
            throw new NotFoundException("EcritureComptable non trouvée : reference=" + pReference);
        }
        return vBean;
    }

    public void setSqlLoadListLigneEcriture(String pSQLloadListLigneEcriture) {
        sqlLoadListLigneEcriture = pSQLloadListLigneEcriture;
    }

    @Override
    public void loadListLigneEcriture(EcritureComptable pEcritureComptable) {
        NamedParameterJdbcTemplate vJdbcTemplate = new NamedParameterJdbcTemplate(getDataSource(DataSourcesEnum.MYERP));
        MapSqlParameterSource vSqlParams = new MapSqlParameterSource();
        vSqlParams.addValue(ECRITURE_ID_FIELD, pEcritureComptable.getId());
        LigneEcritureComptableRM vRM = new LigneEcritureComptableRM();
        List<LigneEcritureComptable> vList = vJdbcTemplate.query(sqlLoadListLigneEcriture, vSqlParams, vRM);
        pEcritureComptable.getListLigneEcriture().clear();
        pEcritureComptable.getListLigneEcriture().addAll(vList);
    }

    public void setSqlInsertEcritureComptable(String pSQLinsertEcritureComptable) {
        sqlInsertEcritureComptable = pSQLinsertEcritureComptable;
    }


    // ==================== EcritureComptable - UPDATE ====================

    @Override
    public void insertEcritureComptable(EcritureComptable pEcritureComptable) {
        // ===== Ecriture Comptable
        NamedParameterJdbcTemplate vJdbcTemplate = new NamedParameterJdbcTemplate(getDataSource(DataSourcesEnum.MYERP));
        MapSqlParameterSource vSqlParams = new MapSqlParameterSource();
        vSqlParams.addValue(JOURNAL_CODE_FIELD, pEcritureComptable.getJournal().getCode());
        vSqlParams.addValue(REFERENCE_FIELD, pEcritureComptable.getReference());
        vSqlParams.addValue(DATE_FIELD, pEcritureComptable.getDate(), Types.DATE);
        vSqlParams.addValue(LIBELLE_FIELD, pEcritureComptable.getLibelle());

        vJdbcTemplate.update(sqlInsertEcritureComptable, vSqlParams);

        // ----- Récupération de l'id
        Integer vId = this.queryGetSequenceValuePostgreSQL(DataSourcesEnum.MYERP, "myerp.ecriture_comptable_id_seq",
                Integer.class);
        pEcritureComptable.setId(vId);

        // ===== Liste des lignes d'écriture
        this.insertListLigneEcritureComptable(pEcritureComptable);
    }

    public void setSqlInsertListLigneEcritureComptable(String pSQLinsertListLigneEcritureComptable) {
        sqlInsertListLigneEcritureComptable = pSQLinsertListLigneEcritureComptable;
    }

    /**
     * Insert les lignes d'écriture de l'écriture comptable
     *
     * @param pEcritureComptable l'écriture comptable
     */
    private void insertListLigneEcritureComptable(EcritureComptable pEcritureComptable) {
        NamedParameterJdbcTemplate vJdbcTemplate = new NamedParameterJdbcTemplate(getDataSource(DataSourcesEnum.MYERP));
        MapSqlParameterSource vSqlParams = new MapSqlParameterSource();
        vSqlParams.addValue(ECRITURE_ID_FIELD, pEcritureComptable.getId());

        int vLigneId = 0;
        for (LigneEcritureComptable vLigne : pEcritureComptable.getListLigneEcriture()) {
            vLigneId++;
            vSqlParams.addValue(LIGNE_ID_FIELD, vLigneId);
            vSqlParams.addValue(COMPTE_COMPTABLE_NUMERO_FIELD, vLigne.getCompteComptable().getNumero());
            vSqlParams.addValue(LIBELLE_FIELD, vLigne.getLibelle());
            vSqlParams.addValue(DEBIT_FIELD, vLigne.getDebit());

            vSqlParams.addValue(CREDIT_FIELD, vLigne.getCredit());

            vJdbcTemplate.update(sqlInsertListLigneEcritureComptable, vSqlParams);
        }
    }


    // ==================== EcritureComptable - DELETE ====================

    public void setSqlUpdateEcritureComptable(String pSQLupdateEcritureComptable) {
        sqlUpdateEcritureComptable = pSQLupdateEcritureComptable;
    }

    @Override
    public void updateEcritureComptable(EcritureComptable pEcritureComptable) {
        // ===== Ecriture Comptable
        NamedParameterJdbcTemplate vJdbcTemplate = new NamedParameterJdbcTemplate(getDataSource(DataSourcesEnum.MYERP));
        MapSqlParameterSource vSqlParams = new MapSqlParameterSource();
        vSqlParams.addValue(ID_FIELD, pEcritureComptable.getId());
        vSqlParams.addValue(JOURNAL_CODE_FIELD, pEcritureComptable.getJournal().getCode());
        vSqlParams.addValue(REFERENCE_FIELD, pEcritureComptable.getReference());
        vSqlParams.addValue(DATE_FIELD, pEcritureComptable.getDate(), Types.DATE);
        vSqlParams.addValue(LIBELLE_FIELD, pEcritureComptable.getLibelle());

        vJdbcTemplate.update(sqlUpdateEcritureComptable, vSqlParams);

        // ===== Liste des lignes d'écriture
        this.deleteListLigneEcritureComptable(pEcritureComptable.getId());
        this.insertListLigneEcritureComptable(pEcritureComptable);
    }

    public void setSqlDeleteEcritureComptable(String pSQLdeleteEcritureComptable) {
        sqlDeleteEcritureComptable = pSQLdeleteEcritureComptable;
    }

    @Override
    public void deleteEcritureComptable(Integer pId) {
        // ===== Suppression des lignes d'écriture
        this.deleteListLigneEcritureComptable(pId);

        // ===== Suppression de l'écriture
        NamedParameterJdbcTemplate vJdbcTemplate = new NamedParameterJdbcTemplate(getDataSource(DataSourcesEnum.MYERP));
        MapSqlParameterSource vSqlParams = new MapSqlParameterSource();
        vSqlParams.addValue(ID_FIELD, pId);
        vJdbcTemplate.update(sqlDeleteEcritureComptable, vSqlParams);
    }

    public void setSqlDeleteListLigneEcritureComptable(String pSQLdeleteListLigneEcritureComptable) {
        sqlDeleteListLigneEcritureComptable = pSQLdeleteListLigneEcritureComptable;
    }

    /**
     * Supprime les lignes d'écriture de l'écriture comptable d'id {@code pEcritureId}
     *
     * @param pEcritureId id de l'écriture comptable
     */
    private void deleteListLigneEcritureComptable(Integer pEcritureId) {
        NamedParameterJdbcTemplate vJdbcTemplate = new NamedParameterJdbcTemplate(getDataSource(DataSourcesEnum.MYERP));
        MapSqlParameterSource vSqlParams = new MapSqlParameterSource();
        vSqlParams.addValue(ECRITURE_ID_FIELD, pEcritureId);
        vJdbcTemplate.update(sqlDeleteListLigneEcritureComptable, vSqlParams);
    }

    public void setSqlGetSequenceEcritureComptableByAnneeAndJournalCode(String pSQLgetSequenceEcritureComptableByAnneeAndJournalCode) {
        sqlGetSequenceEcritureComptableByAnneeAndJournalCode = pSQLgetSequenceEcritureComptableByAnneeAndJournalCode;
    }

    @Override
    public SequenceEcritureComptable getSequenceByYearAndJournalCode(Integer year, String code) throws NotFoundException {
        NamedParameterJdbcTemplate vJdbcTemplate = new NamedParameterJdbcTemplate(getDataSource(DataSourcesEnum.MYERP));
        MapSqlParameterSource vSqlParams = new MapSqlParameterSource();
        vSqlParams.addValue(ANNEE_FIELD, year);
        vSqlParams.addValue(JOURNAL_CODE_FIELD, code);

        SequenceEcritureComptableRM vRM = new SequenceEcritureComptableRM();
        SequenceEcritureComptable vBean;
        try {
            vBean = vJdbcTemplate.queryForObject(sqlGetSequenceEcritureComptableByAnneeAndJournalCode, vSqlParams, vRM);
        } catch (EmptyResultDataAccessException vEx) {
            throw new NotFoundException("SequenceEcriture non trouvée : journal_code=" + code + " annee=" + year);
        }
        return vBean;
    }

    public void setSqlInsertSequenceEcritureComptable(String pSQLinsertSequenceEcritureComptable) {
        sqlInsertSequenceEcritureComptable = pSQLinsertSequenceEcritureComptable;
    }

    @Override
    public void insertNewSequence(SequenceEcritureComptable sequenceEcritureComptable) {
        NamedParameterJdbcTemplate vJdbcTemplate = new NamedParameterJdbcTemplate(getDataSource(DataSourcesEnum.MYERP));
        MapSqlParameterSource vSqlParams = new MapSqlParameterSource();
        vSqlParams.addValue(ANNEE_FIELD, sequenceEcritureComptable.getAnnee());
        vSqlParams.addValue(JOURNAL_CODE_FIELD, sequenceEcritureComptable.getJournal().getCode());

        vSqlParams.addValue(DERNIERE_VALEUR_FIELD, sequenceEcritureComptable.getDerniereValeur());

        vJdbcTemplate.update(sqlInsertSequenceEcritureComptable, vSqlParams);
    }

    public void setSqlUpdateSequenceEcritureComptable(String pSQLupdateSequenceEcritureComptable) {
        sqlUpdateSequenceEcritureComptable = pSQLupdateSequenceEcritureComptable;
    }

    @Override
    public void updateSequenceEcritureComptable(SequenceEcritureComptable sequenceEcritureComptable) {
        // ===== Ecriture Comptable
        NamedParameterJdbcTemplate vJdbcTemplate = new NamedParameterJdbcTemplate(getDataSource(DataSourcesEnum.MYERP));
        MapSqlParameterSource vSqlParams = new MapSqlParameterSource();
        vSqlParams.addValue(ANNEE_FIELD, sequenceEcritureComptable.getAnnee());
        vSqlParams.addValue(JOURNAL_CODE_FIELD, sequenceEcritureComptable.getJournal().getCode());

        vSqlParams.addValue(DERNIERE_VALEUR_FIELD, sequenceEcritureComptable.getDerniereValeur());

        vJdbcTemplate.update(sqlUpdateSequenceEcritureComptable, vSqlParams);

    }


}
