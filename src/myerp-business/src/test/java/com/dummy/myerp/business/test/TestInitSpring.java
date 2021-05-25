package com.dummy.myerp.business.test;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Classe de test de l'initialisation du contexte Spring
 */
public class TestInitSpring extends BusinessTestCase {

    /**
     * Constructeur.
     */
    public TestInitSpring() {
        super();
    }


    /**
     * Teste l'initialisation du contexte Spring
     */
    @Test
    public void testInit() {
        SpringRegistry.init();
        assertThat(SpringRegistry.getBusinessProxy()).isNotNull();
        assertThat(SpringRegistry.getTransactionManager()).isNotNull();
    }
}
