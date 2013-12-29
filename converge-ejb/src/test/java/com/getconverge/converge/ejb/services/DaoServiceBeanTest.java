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
package com.getconverge.converge.ejb.services;

import com.getconverge.converge.entities.Configuration;
import com.getconverge.converge.entities.ConfigurationKey;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class DaoServiceBeanTest {

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
    public void daoService_createNewEntity_entityCreated() throws Exception {
        // Arrange
        ConfigurationKey key = ConfigurationKey.CONVERGE_HOME_URL;
        String value = "http://localhost/converge";
        Configuration cfg = new Configuration();
        cfg.setKey(key);
        cfg.setValue(value);

        // Act
        Configuration result = daoService.create(cfg);

        // Assert
        assertNotNull(result.getId());
        assertEquals(key, result.getKey());
        assertEquals(value, result.getValue());
    }

    @Test
    public void daoService_findExistingEntity_entityFound() throws Exception {
        // Arrange
        Configuration firstEntity = daoService.create(new Configuration(ConfigurationKey.LANGUAGE, "da"));
        daoService.create(new Configuration(ConfigurationKey.COUNTRY, "dk"));
        daoService.create(new Configuration(ConfigurationKey.TIME_ZONE, "CET"));

        // Act
        Configuration result = daoService.findById(Configuration.class, firstEntity.getId());

        // Assert
        assertEquals(firstEntity.getId(), result.getId());
        assertEquals(firstEntity.getKey(), result.getKey());
        assertEquals(firstEntity.getValue(), result.getValue());
    }

    @Test
    public void daoService_deleteExistingEntity_entityDeleted() throws Exception {
        // Arrange
        Configuration addedEntity = daoService.create(new Configuration(ConfigurationKey.LANGUAGE, "da"));
        addedEntity = daoService.findById(Configuration.class, addedEntity.getId());

        // Act
        daoService.delete(Configuration.class, addedEntity.getId());

        // Assert
        try {
            daoService.findById(Configuration.class, addedEntity.getId());
            fail("Entity was not deleted as expected");
        } catch (DataNotFoundException ex) {

        }
    }

    @Test
    public void daoService_deleteEntityWithNullId_noChange() throws Exception {
        // Arrange
        Object nullId = null;

        // Act & Assert
        try {
            daoService.delete(Configuration.class, nullId);
            fail("Expected a DataNotFoundException upon deleting entity with null id");
        } catch (DataNotFoundException ex) {

        }
    }

    @Test
    public void daoService_deleteEntityWithUnknownId_noChange() {
        // Arrange
        Long unknownId = 999L;

        // Act & Assert
        try {
            daoService.delete(Configuration.class, unknownId);
            fail("Expected a DataNotFoundException upon deleting non-existing entity");
        } catch (DataNotFoundException ex) {

        }
    }

    @Test
    public void daoService_findNonExistingEntity_dataNotFoundException() throws Exception {
        // Arrange
        Long unknownId = 999L;

        // Act & Assert
        try {
            daoService.findById(Configuration.class, unknownId);
            fail("DataNotFoundException was expected for non-existing configuration");
        } catch (DataNotFoundException ex) {

        }
    }

    @Test
    public void daoService_findEntityWithNullId_dataNotFoundException() throws Exception {
        // Arrange
        Long nullId = null;

        // Act & Assert
        try {
            daoService.findById(Configuration.class, nullId);
            fail("DataNotFoundException was expected for null id");
        } catch (DataNotFoundException ex) {

        }
    }

    @Test
    public void daoService_getEntityManager_entityManagerReturned() throws Exception {
        // Arrange

        // Act
        EntityManager entityManager = daoService.getEntityManager();

        // Assert
        assertNotNull(entityManager);
    }

    @Test
    public void daoService_createAndUpdateEntity_entityUpdated() throws Exception {
        // Arrange
        Configuration newEntity = daoService.create(new Configuration(ConfigurationKey.LANGUAGE, "da"));
        newEntity = daoService.findById(Configuration.class, newEntity.getId());
        String newValue = "en";
        newEntity.setValue(newValue);

        // Act
        Configuration updatedEntity = daoService.update(newEntity);

        // Assert
        assertEquals(newEntity.getId(), updatedEntity.getId());
        assertEquals(newEntity.getKey(), updatedEntity.getKey());
        assertEquals(newValue, updatedEntity.getValue());
    }

    @Test
    public void daoService_noEntities_countIsZero() throws Exception {
        // Arrange

        // Act
        Number count = daoService.count(Configuration.class, "id");

        // Assert
        assertEquals(0L, count);
    }

    @Test
    public void daoService_oneEntity_countIsOne() throws Exception {
        // Arrange
        daoService.create(new Configuration(ConfigurationKey.LANGUAGE, "da"));

        // Act
        Number count = daoService.count(Configuration.class, "id");

        // Assert
        assertEquals(1L, count);
    }

    @Test
    public void daoService_threeEntities_countIsThree() throws Exception {
        // Assign
        daoService.create(new Configuration(ConfigurationKey.LANGUAGE, "da"));
        daoService.create(new Configuration(ConfigurationKey.COUNTRY, "dk"));
        daoService.create(new Configuration(ConfigurationKey.TIME_ZONE, "CET"));

        // Act
        Number count = daoService.count(Configuration.class, "id");

        // Assert
        assertEquals(3L, count);
    }

    @Test
    public void daoService_threeEntitiesOneDeleted_countIsTwo() throws Exception {
        // Arrange
        Configuration toBeDeleted = daoService.create(new Configuration(ConfigurationKey.LANGUAGE, "da"));
        daoService.create(new Configuration(ConfigurationKey.COUNTRY, "dk"));
        daoService.create(new Configuration(ConfigurationKey.TIME_ZONE, "CET"));
        daoService.delete(Configuration.class, toBeDeleted.getId());

        // Act
        Number count = daoService.count(Configuration.class, "id");

        // Assert
        assertEquals(2L, count);
    }

    @Test
    public void daoService_findAllWithZeroEntities_emptyList() throws Exception {
        // Arrange

        // Act
        List<Configuration> entities = daoService.findAll(Configuration.class);

        // Assert
        assertEquals(0L, entities.size());
    }

    @Test
    public void daoService_findAllOneEntities_listWithOneItem() throws Exception {
        // Arrange
        daoService.create(new Configuration(ConfigurationKey.COUNTRY, "dk"));

        // Act
        List<Configuration> entities = daoService.findAll(Configuration.class);

        // Assert
        assertEquals(1L, entities.size());
    }

    @Test
    public void daoService_findAllThreeEntities_listWithThreeItems() throws Exception {
        // Arrange
        daoService.create(new Configuration(ConfigurationKey.COUNTRY, "dk"));
        daoService.create(new Configuration(ConfigurationKey.TIME_ZONE, "CET"));
        daoService.create(new Configuration(ConfigurationKey.LANGUAGE, "da"));

        // Act
        List<Configuration> entities = daoService.findAll(Configuration.class);

        // Assert
        assertEquals(3L, entities.size());
    }

    @Test
    public void daoService_findAllAscendingThreeEntities_listWithThreeItemsInAscendingOrder() throws Exception {
        // Arrange
        daoService.create(new Configuration(ConfigurationKey.COUNTRY, "dk"));
        daoService.create(new Configuration(ConfigurationKey.TIME_ZONE, "CET"));
        daoService.create(new Configuration(ConfigurationKey.LANGUAGE, "da"));

        // Act
        List<Configuration> entities = daoService.findAll(Configuration.class, "key", true);

        // Assert
        assertEquals(3L, entities.size());
        assertEquals(ConfigurationKey.COUNTRY, entities.get(0).getKey());
        assertEquals("dk", entities.get(0).getValue());
        assertEquals(ConfigurationKey.LANGUAGE, entities.get(1).getKey());
        assertEquals("da", entities.get(1).getValue());
        assertEquals(ConfigurationKey.TIME_ZONE, entities.get(2).getKey());
        assertEquals("CET", entities.get(2).getValue());
    }

    @Test
    public void daoService_findAllDescendingThreeEntities_listWithThreeItemsInDescendingOrder() throws Exception {
        // Arrange
        daoService.create(new Configuration(ConfigurationKey.COUNTRY, "dk"));
        daoService.create(new Configuration(ConfigurationKey.TIME_ZONE, "CET"));
        daoService.create(new Configuration(ConfigurationKey.LANGUAGE, "da"));

        // Act
        List<Configuration> entities = daoService.findAll(Configuration.class, "key", false);

        // Assert
        assertEquals(3L, entities.size());
        assertEquals(ConfigurationKey.TIME_ZONE, entities.get(0).getKey());
        assertEquals("CET", entities.get(0).getValue());
        assertEquals(ConfigurationKey.LANGUAGE, entities.get(1).getKey());
        assertEquals("da", entities.get(1).getValue());
        assertEquals(ConfigurationKey.COUNTRY, entities.get(2).getKey());
        assertEquals("dk", entities.get(2).getValue());
    }

    @Test
    public void daoService_findAllPagingOneRecordAscendingThreeEntities_listWithOneItemInAscendingOrder() throws Exception {
        // Arrange
        daoService.create(new Configuration(ConfigurationKey.COUNTRY, "dk"));
        daoService.create(new Configuration(ConfigurationKey.TIME_ZONE, "CET"));
        daoService.create(new Configuration(ConfigurationKey.LANGUAGE, "da"));

        // Act
        List<Configuration> entities = daoService.findAll(Configuration.class, 0, 1, "key", true);

        // Assert
        assertEquals(1L, entities.size());
        assertEquals(ConfigurationKey.COUNTRY, entities.get(0).getKey());
        assertEquals("dk", entities.get(0).getValue());
    }

    @Test
    public void daoService_findAllPagingOneRecordPageTwoAscendingThreeEntities_listWithOneItemInAscendingOrder() throws Exception {
        // Arrange
        daoService.create(new Configuration(ConfigurationKey.COUNTRY, "dk"));
        daoService.create(new Configuration(ConfigurationKey.TIME_ZONE, "CET"));
        daoService.create(new Configuration(ConfigurationKey.LANGUAGE, "da"));

        // Act
        List<Configuration> entities = daoService.findAll(Configuration.class, 1, 1, "key", true);

        // Assert
        assertEquals(1L, entities.size());
        assertEquals(ConfigurationKey.LANGUAGE, entities.get(0).getKey());
        assertEquals("da", entities.get(0).getValue());
    }

}
