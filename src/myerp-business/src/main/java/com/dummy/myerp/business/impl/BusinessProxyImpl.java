package com.dummy.myerp.business.impl;

import com.dummy.myerp.business.contrat.BusinessProxy;
import com.dummy.myerp.business.contrat.manager.ComptabiliteManager;
import com.dummy.myerp.business.impl.manager.ComptabiliteManagerImpl;
import com.dummy.myerp.business.util.Constant;
import com.dummy.myerp.consumer.dao.contrat.DaoProxy;


/**
 * <p>Implémentation du Proxy d'accès à la couche Business.</p>
 */
public class BusinessProxyImpl implements BusinessProxy {

    // ==================== Attributs Static ====================
    /**
     * Instance unique de la classe (design pattern Singleton)
     */
    private final static BusinessProxyImpl INSTANCE = new BusinessProxyImpl();


    // ==================== Attributs ====================
    /** Le Proxy d'accès à la couche Consumer-DAO */
    private static DaoProxy daoProxy;


    // ==================== Constructeurs ====================
    /**
     * The Comptabilite manager.
     */
    private ComptabiliteManager comptabiliteManager = new ComptabiliteManagerImpl();

    /**
     * Constructeur.
     */
    public BusinessProxyImpl() {
        super();
    }

    /**
     * Renvoie l'instance unique de la classe (design pattern Singleton).
     *
     * @return {@link BusinessProxyImpl}
     */
    public static BusinessProxyImpl getInstance() {
        if (daoProxy == null) {
            throw new UnsatisfiedLinkError(Constant.BUSINESS_PROXY_NOT_INTIALIZED);
        }
        return BusinessProxyImpl.INSTANCE;
    }

    /**
     * Renvoie l'instance unique de la classe (design pattern Singleton).
     *
     * @param pDaoProxy           -
     * @param pTransactionManager -
     * @return {@link BusinessProxyImpl}
     */
    public static BusinessProxyImpl getInstance(DaoProxy pDaoProxy,
                                                TransactionManager pTransactionManager) {
        daoProxy = pDaoProxy;
        AbstractBusinessManager.configure(BusinessProxyImpl.INSTANCE, pDaoProxy, pTransactionManager);
        return BusinessProxyImpl.INSTANCE;
    }

    // ==================== Getters/Setters ====================
    @Override
    public ComptabiliteManager getComptabiliteManager() {
        return comptabiliteManager;
    }
}