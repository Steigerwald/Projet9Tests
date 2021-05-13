package com.dummy.myerp.business.impl.manager;


import com.dummy.myerp.business.contrat.manager.ComptabiliteManager;
import com.dummy.myerp.business.impl.AbstractBusinessManager;
import com.dummy.myerp.business.util.Constant;
import com.dummy.myerp.model.bean.comptabilite.*;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.TransactionStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;


/**
 * Comptabilite manager implementation.
 */
public class ComptabiliteManagerImpl extends AbstractBusinessManager implements ComptabiliteManager {

    // ==================== Attributs ====================


    // ==================== Constructeurs ====================
    /**
     * Instantiates a new Comptabilite manager.
     */
    public ComptabiliteManagerImpl() {
        super();
    }


    // ==================== Getters/Setters ====================
    @Override
    public List<CompteComptable> getListCompteComptable() {
        return getDaoProxy().getComptabiliteDao().getListCompteComptable();
    }


    @Override
    public List<JournalComptable> getListJournalComptable() {
        return getDaoProxy().getComptabiliteDao().getListJournalComptable();
    }

    /**
     * {@inheritDoc}
     */

    @Override
    public List<EcritureComptable> getListEcritureComptable() {
        return getDaoProxy().getComptabiliteDao().getListEcritureComptable();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void addReference(EcritureComptable pEcritureComptable) throws FunctionalException {
        // Bien se réferer à la JavaDoc de cette méthode !
        /* Le principe :
                1.  Remonter depuis la persitance la dernière valeur de la séquence du journal pour l'année de l'écriture
                    (table sequence_ecriture_comptable)
                2.  * S'il n'y a aucun enregistrement pour le journal pour l'année concernée :
                        1. Utiliser le numéro 1.
                    * Sinon :
                        1. Utiliser la dernière valeur + 1
                3.  Mettre à jour la référence de l'écriture avec la référence calculée (RG_Compta_5)
                4.  Enregistrer (insert/update) la valeur de la séquence en persitance
                    (table sequence_ecriture_comptable)
         */
        if(pEcritureComptable.getDate() == null) {
            throw new FunctionalException(Constant.ECRITURE_COMPTABLE_DATE_NULL_FOR_ADD_REFERENCE);
        }
        if(pEcritureComptable.getJournal() == null || pEcritureComptable.getJournal().getCode() == null) {
            throw new FunctionalException(Constant.ECRITURE_COMPTABLE_JOURNAL_NULL_FOR_ADD_REFERENCE);
        }

        LocalDate ecritureDate = pEcritureComptable.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        SequenceEcritureComptable sequenceEcritureComptable;
        boolean isSequenceAlreadyExist = true;
        try {
            sequenceEcritureComptable = getDaoProxy().getComptabiliteDao().getSequenceByYearAndJournalCode(ecritureDate.getYear(),pEcritureComptable.getJournal().getCode());
        } catch (NotFoundException e) {
            sequenceEcritureComptable = new SequenceEcritureComptable(ecritureDate.getYear(), pEcritureComptable.getJournal(), 0);
            isSequenceAlreadyExist = false;
        }

        sequenceEcritureComptable.setDerniereValeur(sequenceEcritureComptable.getDerniereValeur() + 1);
        StringBuilder formattedSequenceNumberBuilder = new StringBuilder(sequenceEcritureComptable.getDerniereValeur().toString());
        while (formattedSequenceNumberBuilder.length() < 5) {
            formattedSequenceNumberBuilder.insert(0, "0");
        }
        String formattedSequenceNumber = formattedSequenceNumberBuilder.toString();

        String reference = sequenceEcritureComptable.getJournal().getCode() + "-" + sequenceEcritureComptable.getAnnee().toString() + "/" + formattedSequenceNumber;
        pEcritureComptable.setReference(reference);

        if(isSequenceAlreadyExist) {
            getDaoProxy().getComptabiliteDao().updateSequenceEcritureComptable(sequenceEcritureComptable);
        } else {
            getDaoProxy().getComptabiliteDao().insertNewSequence(sequenceEcritureComptable);
        }
        pEcritureComptable.setReference(reference);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void checkEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException {
        checkEcritureComptableUnit(pEcritureComptable);
        checkEcritureComptableContext(pEcritureComptable);
    }


    /**
     * Vérifie que l'Ecriture comptable respecte les règles de gestion unitaires,
     * c'est à dire indépendemment du contexte (unicité de la référence, exercie comptable non cloturé...)
     *
     * @param pEcritureComptable -
     * @throws FunctionalException Si l'Ecriture comptable ne respecte pas les règles de gestion
     */
    private void checkEcritureComptableUnit(EcritureComptable pEcritureComptable) throws FunctionalException {
        // ===== Vérification des contraintes unitaires sur les attributs de l'écriture
        Set<ConstraintViolation<EcritureComptable>> vViolations = getConstraintValidator().validate(pEcritureComptable);
        if (!vViolations.isEmpty()) {
            throw new FunctionalException(Constant.ECRITURE_COMPTABLE_MANAGEMENT_RULE_ERRORMSG,
                    new ConstraintViolationException(Constant.ECRITURE_COMPTABLE_VALIDATION_CONSTRAINT_ERRORMSG,
                            vViolations));
        }

        // ===== RG_Compta_3 : une écriture comptable doit avoir au moins 2 lignes d'écriture (1 au débit, 1 au crédit)
        int vNbrCredit = 0;
        int vNbrDebit = 0;
        for (LigneEcritureComptable vLigneEcritureComptable : pEcritureComptable.getListLigneEcriture()) {
            if (BigDecimal.ZERO.compareTo(ObjectUtils.defaultIfNull(vLigneEcritureComptable.getCredit(),
                    BigDecimal.ZERO)) != 0) {
                vNbrCredit++;
            }
            if (BigDecimal.ZERO.compareTo(ObjectUtils.defaultIfNull(vLigneEcritureComptable.getDebit(),
                    BigDecimal.ZERO)) != 0) {
                vNbrDebit++;
            }
        }
        // On test le nombre de lignes car si l'écriture à une seule ligne
        //      avec un montant au débit et un montant au crédit ce n'est pas valable
        if (vNbrCredit < 1 || vNbrDebit < 1) {
            throw new FunctionalException(
                    Constant.RG_COMPTA_3_VIOLATION_ERRORMSG);
        }

        // ===== RG_Compta_2 : Pour qu'une écriture comptable soit valide, elle doit être équilibrée
        if (!pEcritureComptable.isEquilibree()) {
            throw new FunctionalException(Constant.RG_COMPTA_2_VIOLATION_ERRORMSG);
        }

        // ===== RG_Compta_5 : vérifier que l'année dans la référence correspond bien à la date de l'écriture, idem pour le code journal...
        String refJournalCode = pEcritureComptable.getReference().substring(0, 2);
        String refDateYear = pEcritureComptable.getReference().substring(3, 7);
        LocalDate ecritureDate = pEcritureComptable.getDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        int ecritureDateYear = ecritureDate.getYear();
        if (!refJournalCode.equals(pEcritureComptable.getJournal().getCode()) || !refDateYear.equals(Integer.toString(ecritureDateYear))) {
            throw new FunctionalException(Constant.RG_COMPTA_5_VIOLATION_ERRORMSG);
        }
    }


    /**
     * Vérifie que l'Ecriture comptable respecte les règles de gestion liées au contexte
     * (unicité de la référence, année comptable non cloturé...)
     *
     * @param pEcritureComptable -
     * @throws FunctionalException Si l'Ecriture comptable ne respecte pas les règles de gestion
     */
    private void checkEcritureComptableContext(EcritureComptable pEcritureComptable) throws FunctionalException {
        // ===== RG_Compta_6 : La référence d'une écriture comptable doit être unique
        if (StringUtils.isNoneEmpty(pEcritureComptable.getReference())) {
            try {
                // Recherche d'une écriture ayant la même référence
                EcritureComptable vECRef = getDaoProxy().getComptabiliteDao().getEcritureComptableByRef(
                        pEcritureComptable.getReference());

                // Si l'écriture à vérifier est une nouvelle écriture (id == null),
                // ou si elle ne correspond pas à l'écriture trouvée (id != idECRef),
                // c'est qu'il y a déjà une autre écriture avec la même référence
                if (pEcritureComptable.getId() == null
                        || !pEcritureComptable.getId().equals(vECRef.getId())) {
                    throw new FunctionalException(Constant.RG_COMPTA_6_VIOLATION_ERRORMSG);
                }
            } catch (NotFoundException vEx) {
                // Dans ce cas, c'est bon, ça veut dire qu'on n'a aucune autre écriture avec la même référence.
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException {
        this.checkEcritureComptable(pEcritureComptable);
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        try {
            getDaoProxy().getComptabiliteDao().insertEcritureComptable(pEcritureComptable);
            TransactionStatus vTSCommit = vTS;
            vTS = null;
            getTransactionManager().commitMyERP(vTSCommit);
        } finally {
            if (vTS != null) {
                getTransactionManager().rollbackMyERP(vTS);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException {
        this.checkEcritureComptable(pEcritureComptable);
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        try {
            getDaoProxy().getComptabiliteDao().updateEcritureComptable(pEcritureComptable);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteEcritureComptable(Integer pId) {
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        try {
            getDaoProxy().getComptabiliteDao().deleteEcritureComptable(pId);
            TransactionStatus vTSCommit = vTS;
            vTS = null;
            getTransactionManager().commitMyERP(vTSCommit);
        } finally {
            if (vTS != null) {
                getTransactionManager().rollbackMyERP(vTS);
            }
        }
    }

}