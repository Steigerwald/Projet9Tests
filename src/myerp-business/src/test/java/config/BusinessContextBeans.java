package config;

import com.dummy.myerp.business.contrat.BusinessProxy;
import com.dummy.myerp.business.impl.BusinessProxyImpl;
import com.dummy.myerp.business.impl.TransactionManager;
import com.dummy.myerp.consumer.dao.contrat.ComptabiliteDao;
import com.dummy.myerp.consumer.dao.contrat.DaoProxy;
import com.dummy.myerp.consumer.dao.impl.DaoProxyImpl;
import com.dummy.myerp.consumer.dao.impl.db.dao.ComptabiliteDaoImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;


@Configuration
public class BusinessContextBeans {

    @Bean
    public BusinessProxy businessProxy() {
        return Mockito.mock(BusinessProxyImpl.class);
    }

    @Bean
    public DaoProxy daoProxy() {
        return Mockito.mock(DaoProxyImpl.class);
    }

    @Bean
    public TransactionManager transactionManager() {
        return Mockito.mock(TransactionManager.class);
    }

    @Bean
    public ComptabiliteDao comptabiliteDao() {
        return Mockito.mock(ComptabiliteDaoImpl.class);
    }
}
