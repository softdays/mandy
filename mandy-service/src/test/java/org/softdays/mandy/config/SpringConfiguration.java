/*
 * MANDY is a simple webapp to track man-day consumption on activities.
 * 
 * Copyright 2014, rpatriarche
 *
 * This file is part of MANDY software.
 *
 * MANDY is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * MANDY is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
