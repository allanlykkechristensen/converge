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
package com.getconverge.faces;

import com.getconverge.faces.functions.ListToMap;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ResourceBundle;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextWrapper;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import static org.mockito.Matchers.eq;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(FacesContext.class)
public class JsfUtilsTest {

    @Test
    public void jsfUtils_uninstantiable() {
        try {
            Constructor<JsfUtils> constructor = JsfUtils.class.getDeclaredConstructor(new Class[0]);
            constructor.setAccessible(true);
            JsfUtils l = constructor.newInstance(new Object[0]);
        } catch (NoSuchMethodException ex) {
            fail(ex.getMessage());
        } catch (SecurityException ex) {
            fail(ex.getMessage());
        } catch (InstantiationException ex) {
            fail(ex.getMessage());
        } catch (IllegalAccessException ex) {
            fail(ex.getMessage());
        } catch (IllegalArgumentException ex) {
            fail(ex.getMessage());
        } catch (InvocationTargetException ex) {
            //Expected - as the class is uninstantiable
            return;
        }
        fail("JsfUtils was instantiable");
    }

    @Test
    public void getString_KnownResourceBundleAndMessageKey_ReturnsMatchingMessage() {
        // Arrange
        String resourceBundleId = "com.getconverge.converge.i18n.WebUI";
        String msgKey = "TestMessageKey";
        FacesContext ctx = Mockito.mock(FacesContext.class);
        Application app = Mockito.mock(Application.class);
        Mockito.when(app.getResourceBundle(ctx, resourceBundleId)).thenReturn(ResourceBundle.getBundle(resourceBundleId));
        Mockito.when(ctx.getApplication()).thenReturn(app);
        PowerMockito.mockStatic(FacesContext.class);
        Mockito.when(FacesContext.getCurrentInstance()).thenReturn(ctx);

        // Act
        String result = JsfUtils.getString(resourceBundleId, msgKey);
        String expResult = "Value of TestMessageKey";

        // Assert
        assertEquals(expResult, result);
    }

    @Test
    public void getString_KnownResourceBundleAndUnknownMessageKey_ReturnsMessageKey() {
        // Arrange
        String resourceBundleId = "com.getconverge.converge.i18n.WebUI";
        String msgKey = "TestUnknownMessageKey";
        FacesContext ctx = Mockito.mock(FacesContext.class);
        Application app = Mockito.mock(Application.class);
        Mockito.when(app.getResourceBundle(ctx, resourceBundleId)).thenReturn(ResourceBundle.getBundle(resourceBundleId));
        Mockito.when(ctx.getApplication()).thenReturn(app);
        PowerMockito.mockStatic(FacesContext.class);
        Mockito.when(FacesContext.getCurrentInstance()).thenReturn(ctx);

        // Act
        String result = JsfUtils.getString(resourceBundleId, msgKey);

        // Assert
        assertEquals(msgKey, result);
    }

    @Test
    public void createFacesMessage_KnownMessageKeyWithParams_AddsMessageToContext() {

        //Arrange
        String clientId = "id-of-client";
        FacesMessage.Severity severity = FacesMessage.SEVERITY_ERROR;
        String bundle = "com.getconverge.converge.i18n.WebUI";
        String msgKey = "TestMessageKeyWithOneParam";
        Object[] parameters = new Object[]{"the param"};
        FacesContext ctx = Mockito.mock(FacesContext.class);
        Application app = Mockito.mock(Application.class);
        Mockito.when(app.getResourceBundle(ctx, bundle)).thenReturn(ResourceBundle.getBundle(bundle));
        Mockito.when(ctx.getApplication()).thenReturn(app);
        PowerMockito.mockStatic(FacesContext.class);
        Mockito.when(FacesContext.getCurrentInstance()).thenReturn(ctx);

        //Act
        JsfUtils.createFacesMessage(clientId, severity, bundle, msgKey, parameters);

        //Assert
        Mockito.verify(ctx).addMessage(eq(clientId), Mockito.any(FacesMessage.class));
    }

    @Test
    public void createFacesMessage_KnownMessageKey_AddsMessageToContext() {

        //Arrange
        FacesMessage.Severity severity = FacesMessage.SEVERITY_ERROR;
        String bundle = "com.getconverge.converge.i18n.WebUI";
        String msgKey = "TestMessageKeyWithOneParam";
        FacesContext ctx = Mockito.mock(FacesContext.class);
        Application app = Mockito.mock(Application.class);
        Mockito.when(app.getResourceBundle(ctx, bundle)).thenReturn(ResourceBundle.getBundle(bundle));
        Mockito.when(ctx.getApplication()).thenReturn(app);
        PowerMockito.mockStatic(FacesContext.class);
        Mockito.when(FacesContext.getCurrentInstance()).thenReturn(ctx);

        //Act
        JsfUtils.createFacesMessage(severity, bundle, msgKey);

        //Assert
        Mockito.verify(ctx).addMessage(Mockito.isNull(String.class), Mockito.any(FacesMessage.class));
    }
}
