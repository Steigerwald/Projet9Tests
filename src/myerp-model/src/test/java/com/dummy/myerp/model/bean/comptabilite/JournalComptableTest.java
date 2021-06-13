package com.dummy.myerp.model.bean.comptabilite;


import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

public class JournalComptableTest {


    @Test
    public void checkGetJournalToString(){
        JournalComptable j1 = new JournalComptable("AC","achat");
        String journalToString="JournalComptable{code='AC', libelle='achat'}";
        assertThat(j1.toString()).isEqualTo(journalToString);
    }


    @Test
    public void checkConstructeurJournalComptable(){
        JournalComptable j1 = new JournalComptable("AC","achat");
        JournalComptable j2=new JournalComptable();
        j2.setCode("AC");
        j2.setLibelle("achat");
        assertThat(j1.toString()).isEqualTo(j2.toString());
    }

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
