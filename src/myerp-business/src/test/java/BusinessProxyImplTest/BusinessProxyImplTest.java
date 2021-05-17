package BusinessProxyImplTest;

import com.dummy.myerp.business.contrat.BusinessProxy;
import com.dummy.myerp.business.contrat.manager.ComptabiliteManager;
import com.dummy.myerp.business.impl.BusinessProxyImpl;
import com.dummy.myerp.business.impl.TransactionManager;
import config.BusinessContextBeans;
import com.dummy.myerp.business.util.Constant;
import com.dummy.myerp.consumer.dao.contrat.DaoProxy;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {BusinessContextBeans.class})
public class BusinessProxyImplTest {

    Logger logger = (Logger) LoggerFactory.getLogger(BusinessProxyImplTest.class);

    @Autowired
    private DaoProxy daoProxy;

    @Autowired
    private TransactionManager transactionManager;

    @Mock
    //@Autowired
    private BusinessProxy objectToTest;

    @BeforeEach
    public void init() {
        objectToTest = BusinessProxyImpl.getInstance(daoProxy, transactionManager);
    }

    @Test
    public void getInstance_configuredBusinessProxy_returnBusinessProxy() {

        //objectToTest = BusinessProxyImpl.getInstance();
        logger.error(" la valeur de objectToTest  "+objectToTest);
        Assertions.assertThat(objectToTest).isNotNull();
    }

    @Test
    public void getInstance_notConfiguredBusinessProxy_ThrowException() {
        BusinessProxyImpl.getInstance(null,null);
        Assertions.assertThatThrownBy(BusinessProxyImpl::getInstance)
                .isInstanceOf(UnsatisfiedLinkError.class)
                .hasMessageContaining(Constant.BUSINESS_PROXY_NOT_INTIALIZED);
    }

   @Test
    public void getComptabiliteManagerTest() {
        objectToTest=BusinessProxyImpl.getInstance(daoProxy,transactionManager);
        logger.info(" la valeur de transactionManager  "+transactionManager);
        logger.error(" la valeur de daoProxy  "+daoProxy);
        logger.error(" la valeur de objectToTest  "+objectToTest);
        logger.error(" la valeur de de getComptabiliteManager de objectToTest  "+objectToTest.getComptabiliteManager());
        Assertions.assertThat(objectToTest.getComptabiliteManager()).isNotNull();
    }

}
