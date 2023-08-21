package org.example.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.example.model.User;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement //означает, что классы, помеченные @Transactional, должны быть обернуты аспектом транзакций.
@ComponentScan("org.example")
public class DatabaseConfig {

    //DataSource используется для получения физического соединения с БД. Это альтернатива DriverManager
    @Bean
    public DataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/spring_hiber?verifyServerCertificate=false&useSSL=false&requireSSL=false&useLegacyDatetimeCode=false&amp&serverTimezone=UTC");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        return dataSource;
    }

    //Создание SessionFactory - фабрика по производству сессий
    @Bean
    public LocalSessionFactoryBean getSessionFactory() {
        LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();
        factoryBean.setDataSource(getDataSource());

        Properties properties = new Properties();
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");

        factoryBean.setHibernateProperties(properties);
        factoryBean.setAnnotatedClasses(User.class);
        return factoryBean;
    }

    //HibernateTransactionManager автоматически открывает транзакцию Hibernate
    @Bean
    public HibernateTransactionManager getTransactionManager() {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(getSessionFactory().getObject());
        return transactionManager;
    }
}