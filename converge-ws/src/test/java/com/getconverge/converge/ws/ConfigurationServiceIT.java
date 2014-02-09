/*
 * Copyright (C) 2014 Converge Consulting Limited
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
package com.getconverge.converge.ws;

import com.getconverge.converge.ejb.services.ConfigurationServiceBean;
import com.getconverge.converge.ejb.services.DaoServiceBean;
import com.getconverge.converge.ejb.services.DataNotFoundException;
import com.getconverge.converge.ejb.services.QueryBuilder;
import com.getconverge.converge.entities.Configuration;
import com.getconverge.converge.entities.ConfigurationKey;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class ConfigurationServiceIT {

    @Deployment
    public static EnterpriseArchive prepareDeploymentForTesting() {
        EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "converge.ear");
        WebArchive webServiceWar = ShrinkWrap.create(WebArchive.class, "converge-ws.war");
        webServiceWar.addClasses(
                ConfigurationService.class
        );
        webServiceWar.addManifest();

        JavaArchive ejb = ShrinkWrap.create(JavaArchive.class, "converge-ejb.jar");
        ejb.addClasses(Configuration.class,
                ConfigurationServiceBean.class,
                DaoServiceBean.class,
                DataNotFoundException.class,
                QueryBuilder.class);
        ejb.addAsManifestResource("META-INF/beans.xml", "beans.xml");
        ejb.addAsManifestResource("META-INF/test-persistence.xml", "persistence.xml");
        ejb.addManifest();

        ear.addAsModule(ejb);
        ear.addAsModule(webServiceWar);

        return ear;
    }

    @Before
    public void initData() throws Exception {
        getWebService().reset(ConfigurationKey.VERSION.toString());
    }

    @Test
    public void configurationService_getVersion_returnVersion() throws Exception {
        // Arrange
        String expVersion = "Injected Version Number";
        ConfigurationWebService ws = getWebService();
        ws.set(ConfigurationKey.VERSION.toString(), expVersion);

        // Act
        String version = ws.getVersion();

        // Assert
        assertEquals(expVersion, version);
    }

    @Test
    public void configurationService_setConfiguration_customConfigCreated() throws Exception {
        // Arrange
        String expBuildTime = "2014-01-01 12:34:56";
        ConfigurationWebService ws = getWebService();

        // Act
        ws.set(ConfigurationKey.BUILD_TIME.toString(), expBuildTime);
        String buildTime = ws.get(String.class, ConfigurationKey.BUILD_TIME.toString());

        // Assert
        assertEquals(expBuildTime, buildTime);
    }

    @Test
    public void configurationService_getApplicationLocale_defaultLocaleReturned() throws Exception {
        // Arrange
        ConfigurationWebService ws = getWebService();
        String expLocale = new Locale("en", "gb").toString();

        // Act
        String locale = ws.getApplicationLocale();

        // Assert
        assertEquals(expLocale, locale);
    }

    @Test
    public void configurationService_getBuildTime_returnBuildTime() throws Exception {
        // Arrange
        String expBuildTime = "Injected Build Time";
        ConfigurationWebService ws = getWebService();
        ws.set(ConfigurationKey.BUILD_TIME.toString(), expBuildTime);

        // Act
        String buildTime = ws.getBuildTime();

        // Assert
        assertEquals(expBuildTime, buildTime);
    }

    @Test
    public void configurationService_getLongVersion_returnLongVersion() throws Exception {
        // Arrange
        String expVersion = "Injected Version";
        String expBuildTime = "Injected Build Time";
        String expLongVersion = expVersion + " " + expBuildTime;
        ConfigurationWebService ws = getWebService();
        ws.set(ConfigurationKey.VERSION.toString(), expVersion);
        ws.set(ConfigurationKey.BUILD_TIME.toString(), expBuildTime);

        // Act
        String longVersion = ws.getLongVersion();

        // Assert
        assertEquals(expLongVersion, longVersion);
    }

    private ConfigurationWebService getWebService() throws MalformedURLException {
        URL wsdlDocumentLocation = new URL("http://localhost:9999/converge-ws/ConfigurationService?wsdl");
        String namespaceURI = "http://ws.converge.getconverge.com/";
        String servicePart = "ConfigurationService";
        String portName = "ConfigurationServicePort";
        QName serviceQN = new QName(namespaceURI, servicePart);
        QName portQN = new QName(namespaceURI, portName);
        Service service = Service.create(wsdlDocumentLocation, serviceQN);
        return service.getPort(portQN, ConfigurationWebService.class);
    }

}
