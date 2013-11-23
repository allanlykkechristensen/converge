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

import java.util.HashMap;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;

public class AbbreviationDisplayConverterTest {

    FacesContext facesContext = null;

    @Test
    public void testGetAsObject_ReturnsNull() {
        // Arrange
        String value = "";
        UIComponent uiComponent = null;

        // Act
        AbbreviationDisplayConverter instance = new AbbreviationDisplayConverter();
        Object result = instance.getAsObject(facesContext, uiComponent, value);

        // Assert
        assertNull(result);
    }

    /**
     * Given that a long string is passed to the converter<br/>
     * When no abbreviation is set<br/>
     * Then the same long string is returned.
     */
    @Test
    public void getAsString_LongStringWithNoAbbreviation_ReturnsLongString() {
        // Arrange
        UIComponent uiComponent = Mockito.mock(UIComponent.class);
        String longString = "This is a long string without abbreviation";

        // Act
        AbbreviationDisplayConverter instance = new AbbreviationDisplayConverter();
        String result = instance.getAsString(facesContext, uiComponent, longString);

        // Assert
        assertEquals(longString, result);
    }

    /**
     * Given that a long string is passed to the converter<br/>
     * When abbreviation is set<br/>
     * Then an abbreviated string is returned of the given length.
     */
    @Test
    public void getAsString_LongStringWithAbbreviation_ReturnsAbbreviatedString() {
        // Arrange
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put(AbbreviationDisplayConverter.ATTRIBUTE_MAX_LENGTH, "10");
        UIComponent uiComponent = Mockito.mock(UIComponent.class);
        Mockito.when(uiComponent.getAttributes()).thenReturn(attributes);
        String longString = "This is a long string without abbreviation";
        String expectedString = "This is...";

        // Act
        AbbreviationDisplayConverter instance = new AbbreviationDisplayConverter();
        String result = instance.getAsString(facesContext, uiComponent, longString);

        // Assert
        assertEquals(expectedString, result);
    }

    /**
     * Given that a long string is passed to the converter<br/>
     * When abbreviation is set and the abbreviation type is incorrect<br/>
     * Then the long string is returned
     */
    @Test
    public void getAsString_LongStringWithIncorrectAbbreviationType_ReturnsLongString() {
        // Arrange
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put(AbbreviationDisplayConverter.ATTRIBUTE_MAX_LENGTH, 10);
        UIComponent uiComponent = Mockito.mock(UIComponent.class);
        Mockito.when(uiComponent.getAttributes()).thenReturn(attributes);
        String longString = "This is a long string without abbreviation";

        // Act
        AbbreviationDisplayConverter instance = new AbbreviationDisplayConverter();
        String result = instance.getAsString(facesContext, uiComponent, longString);

        // Assert
        assertEquals(longString, result);
    }

    /**
     * Given that a long string is passed to the converter<br/>
     * When abbreviation is set and the abbreviation type is not a number<br/>
     * Then the long string is returned
     */
    @Test
    public void getAsString_LongStringWithAbbreviationNotANumber_ReturnsLongString() {
        // Arrange
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put(AbbreviationDisplayConverter.ATTRIBUTE_MAX_LENGTH, "10d");
        UIComponent uiComponent = Mockito.mock(UIComponent.class);
        Mockito.when(uiComponent.getAttributes()).thenReturn(attributes);
        String longString = "This is a long string without abbreviation";

        // Act
        AbbreviationDisplayConverter instance = new AbbreviationDisplayConverter();
        String result = instance.getAsString(facesContext, uiComponent, longString);

        // Assert
        assertEquals(longString, result);
    }

    /**
     * Given that a non-String object is passed to the converter<br/>
     * Then an empty String is returned.
     */
    @Test
    public void getAsString_NonString_ReturnsEmptyString() {
        // Arrange
        UIComponent uiComponent = null;
        Integer nonString = 11241;

        // Act
        AbbreviationDisplayConverter instance = new AbbreviationDisplayConverter();
        String result = instance.getAsString(facesContext, uiComponent, nonString);

        // Assert
        assertEquals("", result);
    }

}
