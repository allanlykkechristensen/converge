/*
 * Copyright (C) 2013 Converge Consulting
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.getconverge.converge.entities;

import com.getconverge.converge.ejb.services.*;
import javax.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class ConfigurationDaoIT {

    @Inject
    private DaoServiceBean daoService;

    @Deployment
    public static EnterpriseArchive prepareDeploymentForTesting() {
        EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "converge.ear");
        JavaArchive jar = ShrinkWrap.create(JavaArchive.class, "converge-ejb.jar");
        jar.addClasses(Configuration.class,
                DaoServiceBean.class,
                DataNotFoundException.class,
                QueryBuilder.class);
        jar.addAsManifestResource("META-INF/beans.xml", "beans.xml");
        jar.addAsManifestResource("META-INF/test-persistence.xml", "persistence.xml");
        ear.addAsModule(jar);
        return ear;
    }

    @Before
    public void initData() {
        daoService.executeQuery("DELETE FROM Configuration");
    }

    @Test
    public void configuration_queryFindByExistingKey_entityFound() throws Exception {
        // Arrange
        Configuration firstEntity = daoService.create(new Configuration(ConfigurationKey.LANGUAGE, "da"));
        daoService.create(new Configuration(ConfigurationKey.COUNTRY, "dk"));
        daoService.create(new Configuration(ConfigurationKey.TIME_ZONE, "CET"));
        QueryBuilder parameters = QueryBuilder.with(Configuration.PARAM_FIND_BY_KEY_KEY, firstEntity.getKey());

        // Act
        Configuration result = daoService.findObjectWithNamedQuery(Configuration.class, Configuration.FIND_BY_KEY, parameters);

        // Assert
        assertEquals(firstEntity.getId(), result.getId());
        assertEquals(firstEntity.getKey(), result.getKey());
        assertEquals(firstEntity.getValue(), result.getValue());
    }

}
