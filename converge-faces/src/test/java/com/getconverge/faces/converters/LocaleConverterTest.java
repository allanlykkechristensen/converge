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
package com.getconverge.faces.converters;

import java.util.Locale;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleConverterTest {

    /**
     * Given that a valid locale string is passed to the converter<br/>
     * Then the corresponding Locale object should be returned.
     */
    @Test
    public void getAsObject_ValidLocaleString_ReturnsCorrespondingLocaleObject() {
        // Arrange
        FacesContext ctx = null;
        UIComponent comp = null;
        String value = "en";

        // Act
        LocaleConverter instance = new LocaleConverter();
        Locale expResult = Locale.ENGLISH;
        Object result = instance.getAsObject(ctx, comp, value);

        // Assert
        assertEquals(Locale.class, result.getClass());
        assertEquals(expResult, result);
    }

    /**
     * Given that a Locale object is passed to the converter<br/>
     * Then the corresponding locale string should be returned.
     */
    @Test
    public void getAsString_LocaleObject_ReturnsCorrespondingLocaleString() {
        // Arrange
        FacesContext ctx = null;
        UIComponent comp = null;
        Locale locale = Locale.ENGLISH;

        // Act
        LocaleConverter instance = new LocaleConverter();
        String expResult = "en";
        String result = instance.getAsString(ctx, comp, locale);

        // Assert
        assertEquals(expResult, result);
    }

}
