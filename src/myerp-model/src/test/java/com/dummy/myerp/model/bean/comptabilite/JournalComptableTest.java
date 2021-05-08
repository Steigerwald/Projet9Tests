package com.dummy.myerp.model.bean.comptabilite;

import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

public class JournalComptableTest {

    // test de la méthode getByCode

    @Test
    public void checkGetByCodeNotNull() {

        List<JournalComptable> jList = new ArrayList<JournalComptable>();

        JournalComptable j1 = new JournalComptable("AC","libelle");
        JournalComptable j2 = new JournalComptable("BE","libelle");
        JournalComptable j3 = new JournalComptable("VQ","libelle");

        jList.add(j1);
        jList.add(j2);
        jList.add(j3);

        JournalComptable journalComptable = JournalComptable.getByCode(jList, "AC");

        assertThat(journalComptable).isEqualTo(j1);
    }

    @Test
    public void checkGetByCodeNull() {

        List<JournalComptable> jList = new ArrayList<JournalComptable>();

        JournalComptable j1 = new JournalComptable("AC","libelle");
        JournalComptable j2 = new JournalComptable("BE","libelle");
        JournalComptable j3 = new JournalComptable("VQ","libelle");

        jList.add(j1);
        jList.add(j2);
        jList.add(j3);

        JournalComptable journalComptable = JournalComptable.getByCode(jList, "AD");

        assertThat(journalComptable).isEqualTo(null);
    }

}
