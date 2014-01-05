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
import java.util.Locale;
import java.util.ResourceBundle;
import javax.ejb.EJBException;
import javax.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class ConfigurationServiceBeanTest {

    @Inject
    private ConfigurationServiceBean cfgService;
    @Inject
    private DaoServiceBean daoService;

    @Deployment
    public static EnterpriseArchive prepareDeploymentForTesting() {
        EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "converge.ear");
        JavaArchive jar = ShrinkWrap.create(JavaArchive.class, "converge-ejb.jar");
        jar.addClasses(Configuration.class,
                ConfigurationServiceBean.class,
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
    public void configurationServiceBean_getConfigurationWithoutCustomizedValue_returnDefaultConfigurationValue() {
        // Arrange
        String defaultLanguageInConvergeProperties = "en";

        // Act
        String value = cfgService.get(String.class, ConfigurationKey.LANGUAGE);

        // Assert
        assertEquals(defaultLanguageInConvergeProperties, value);
    }

    @Test
    public void configurationServiceBean_setConfigurationGetConfiguration_returnCustomizedConfigurationValue() {
        // Arrange
        String customLanguage = "da";

        // Act
        cfgService.set(ConfigurationKey.LANGUAGE, customLanguage);
        String value = cfgService.get(String.class, ConfigurationKey.LANGUAGE);

        // Assert
        assertEquals(customLanguage, value);
    }

    @Test
    public void configurationServiceBean_twoTimesSetConfigurationGetConfiguration_returnLatestCustomizedConfigurationValue() {
        // Arrange
        String firstCustomLanguage = "da";
        String secondCustomLanguage = "de";

        // Act
        cfgService.set(ConfigurationKey.LANGUAGE, firstCustomLanguage);
        cfgService.set(ConfigurationKey.LANGUAGE, secondCustomLanguage);
        String value = cfgService.get(String.class, ConfigurationKey.LANGUAGE);

        // Assert
        assertEquals(secondCustomLanguage, value);
    }

    @Test
    public void configurationServiceBean_getConfigurationAsIntegerWithoutCustomizedValue_returnDefaultConfigurationValueAsInteger() {
        // Arrange
        Integer defaultNewswireBasketInterval = 60;

        // Act
        Integer value = cfgService.get(Integer.class, ConfigurationKey.NEWSWIRE_BASKET_INTERVAL);

        // Assert
        assertEquals(defaultNewswireBasketInterval, value);
    }

    @Test
    public void configurationServiceBean_getConfigurationAsLongWithoutCustomizedValue_returnDefaultConfigurationValueAsLong() {
        // Arrange
        Long defaultNewswireBasketInterval = 60L;

        // Act
        Long value = cfgService.get(Long.class, ConfigurationKey.NEWSWIRE_BASKET_INTERVAL);

        // Assert
        assertEquals(defaultNewswireBasketInterval, value);
    }

    @Test
    public void configurationServiceBean_getStringConfigurationAsLong_throwNumberFormatException() {
        // Arrange
        ConfigurationKey nonNumberConfiguration = ConfigurationKey.LANGUAGE;

        // Act
        try {
            cfgService.get(Long.class, nonNumberConfiguration);
            // Assert
            fail("Expected NumberFormatException as value cannot be converted");
        } catch (EJBException ex) {

        }
    }

    @Test
    public void configurationServiceBean_getStringConfigurationAsDouble_throwUnsupportedOperationException() {
        // Arrange
        ConfigurationKey nonNumberConfiguration = ConfigurationKey.LANGUAGE;

        // Act
        try {
            cfgService.get(Double.class, nonNumberConfiguration);
            // Assert
            fail("Expected UnsupportedOperationException as Double conversion is not supported");
        } catch (EJBException ex) {

        }
    }

    @Test
    public void configurationServiceBean_getApplicationLocaleWithoutCutomizedValue_returnDefaultApplicationLocale() {
        // Arrange
        String defaultLanguage = "en";
        String defaultCountry = "gb";
        Locale defaultLocale = new Locale(defaultLanguage, defaultCountry);

        // Act
        Locale applicationLocale = cfgService.getApplicationLocale();

        // Assert
        assertEquals(defaultLocale, applicationLocale);
    }

    @Test
    public void configurationServiceBean_getCustomizedApplicationLocale_returnCustomizedApplicationLocale() {
        // Arrange
        String customLanguage = "fr";
        String customCountry = "be";
        Locale customLocale = new Locale(customLanguage, customCountry);
        cfgService.set(ConfigurationKey.LANGUAGE, customLanguage);
        cfgService.set(ConfigurationKey.COUNTRY, customCountry);

        // Act
        Locale applicationLocale = cfgService.getApplicationLocale();

        // Assert
        assertEquals(customLocale, applicationLocale);
    }

    @Test
    public void configurationServiceBean_getVersion_returnVersionFromConvergeProperties() {
        // Arrange
        ResourceBundle convergeProperties = ResourceBundle.getBundle("Converge");
        String expectedVersion = convergeProperties.getString(ConfigurationKey.VERSION.toString());

        // Act
        String actualVersion = cfgService.getVersion();

        // Assert
        assertEquals(expectedVersion, actualVersion);
    }

    @Test
    public void configurationServiceBean_getBuildTime_returnBuildTimeFromConvergeProperties() {
        // Arrange
        ResourceBundle convergeProperties = ResourceBundle.getBundle("Converge");
        String expectedBuildTime = convergeProperties.getString(ConfigurationKey.BUILD_TIME.toString());

        // Act
        String actualBuildTime = cfgService.getBuildTime();

        // Assert
        assertEquals(expectedBuildTime, actualBuildTime);
    }

    @Test
    public void configurationServiceBean_getLongVersion_returnBuildTimeAndVersionFromConvergeProperties() {
        // Arrange
        ResourceBundle convergeProperties = ResourceBundle.getBundle("Converge");
        String buildTime = convergeProperties.getString(ConfigurationKey.BUILD_TIME.toString());
        String version = convergeProperties.getString(ConfigurationKey.VERSION.toString());
        String expectedLongVersion = version + " " + buildTime;

        // Act
        String actualLongVersion = cfgService.getLongVersion();

        // Assert
        assertEquals(expectedLongVersion, actualLongVersion);
    }

    @Test
    public void configurationServiceBean_resetCustomConfiguration_customConfigRemoved() {
        // Arrange
        String expVersion = cfgService.get(String.class, ConfigurationKey.VERSION);
        cfgService.set(ConfigurationKey.VERSION, "Some custom version");
        String updatedVersion = cfgService.get(String.class, ConfigurationKey.VERSION);

        // Act
        cfgService.reset(ConfigurationKey.VERSION);
        String version = cfgService.get(String.class, ConfigurationKey.VERSION);

        // Assert
        assertNotEquals(version, updatedVersion);
        assertEquals(expVersion, version);
    }

    @Test
    public void configurationServiceBean_resetDefaultConfiguration_configRemainsWithDefaultValue() {
        // Arrange
        String expLanguage = cfgService.get(String.class, ConfigurationKey.LANGUAGE);

        // Act
        cfgService.reset(ConfigurationKey.LANGUAGE);
        String resetLanguage = cfgService.get(String.class, ConfigurationKey.LANGUAGE);

        // Assert
        assertEquals(expLanguage, resetLanguage);
    }
}
