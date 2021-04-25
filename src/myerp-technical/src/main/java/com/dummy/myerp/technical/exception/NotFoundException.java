package com.dummy.myerp.technical.exception;


/**
 * Classe des Exception de type "Donnée non trouvée"
 */
public class NotFoundException extends Exception {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur.
     */
    public NotFoundException() {
        super();
    }

    /**
     * Constructeur.
     *
     * @param pMessage -
     */
    public NotFoundException(String pMessage) {
        super(pMessage);
    }
}
