package BusinessProxyImplTest;

import com.dummy.myerp.business.contrat.BusinessProxy;
import com.dummy.myerp.business.impl.BusinessProxyImpl;
import com.dummy.myerp.business.impl.TransactionManager;
import config.BusinessContextBeans;
import com.dummy.myerp.business.util.Constant;
import com.dummy.myerp.consumer.dao.contrat.DaoProxy;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {BusinessContextBeans.class})
public class BusinessProxyImplTest {

    @Autowired
    private DaoProxy daoProxy;

    @Autowired
    private TransactionManager transactionManager;


    private BusinessProxy objectToTest;

    @BeforeEach
    void init() {
        objectToTest = BusinessProxyImpl.getInstance(daoProxy, transactionManager);
    }

    @Test
    void getInstance_configuredBusinessProxy_returnBusinessProxy() {

        objectToTest = BusinessProxyImpl.getInstance();

        Assertions.assertThat(objectToTest).isNotNull();
    }

    @Test
    void getInstance_notConfiguredBusinessProxy_ThrowException() {
        BusinessProxyImpl.getInstance(null, null);
        Assertions.assertThatThrownBy(BusinessProxyImpl::getInstance)
                .isInstanceOf(UnsatisfiedLinkError.class)
                .hasMessageContaining(Constant.BUSINESS_PROXY_NOT_INTIALIZED);
    }

    @Test
    void getComptabiliteManagerTest() {

        Assertions.assertThat(objectToTest.getComptabiliteManager()).isNotNull();
    }


}
