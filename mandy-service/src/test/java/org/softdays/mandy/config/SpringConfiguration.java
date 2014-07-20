package org.softdays.mandy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
// @ComponentScan({ "org.softdays.mandy" })
@ImportResource("test-context.xml")
public class SpringConfiguration {

    // @Bean
    // public DataSource dataSource() {
    // return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
    // .addScript("schema.sql").build();
    // }
    //
    // @Bean
    // public PlatformTransactionManager transactionManager() {
    // return new DataSourceTransactionManager(dataSource());
    // }
    //
    // @Bean
    // public JdbcTemplate jdbcTemplate() {
    // return new JdbcTemplate(dataSource());
    // }
    //
    // @Bean
    // Destination destination() {
    // return new TransactionAwareDestination(dataSource(),
    // transactionManager());
    // }

}
