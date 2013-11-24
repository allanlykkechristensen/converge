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

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;

public class EnumTypeConverterTest {

    /**
     * Given that a valid enumeration string is passed to the converter<br/>
     * Then return the corresponding Enum object.
     */
    @Test
    public void getAsObject_ValidEnumString_ReturnsEnumObject() {
        // Arrange
        FacesContext ctx = Mockito.mock(FacesContext.class);
        UIComponent comp = Mockito.mock(UIComponent.class);
        ValueBinding vb = Mockito.mock(ValueBinding.class);
        Mockito.when(vb.getType(ctx)).thenReturn(TestEnum.class);
        Mockito.when(comp.getValueBinding("value")).thenReturn(vb);
        String value = "VAL1";

        // Act
        EnumTypeConverter instance = new EnumTypeConverter();
        TestEnum expResult = TestEnum.VAL1;
        Object result = instance.getAsObject(ctx, comp, value);

        // Assert
        assertEquals(expResult, result);
    }

    /**
     * Given that an Enum object is passed to the converter<br/>
     * Then return the corresponding enumeration string.
     */
    @Test
    public void getAsString_EnumObject_ReturnsEnumString() {
        // Arrange
        FacesContext ctx = null;
        UIComponent comp = null;

        // Act
        EnumTypeConverter instance = new EnumTypeConverter();
        String expResult = "VAL2";
        String result = instance.getAsString(ctx, comp, TestEnum.VAL2);

        // Assert
        assertEquals(expResult, result);
    }

    /**
     * Given that null is passed to the converter<br/>
     * Then return null.
     */
    @Test
    public void getAsString_NullObject_ReturnsNullObject() {
        // Arrange
        FacesContext ctx = null;
        UIComponent comp = null;

        // Act
        EnumTypeConverter instance = new EnumTypeConverter();
        String result = instance.getAsString(ctx, comp, null);

        // Assert
        assertNull(result);
    }

    /**
     * Enumeration used for testing.
     */
    enum TestEnum {

        VAL1, VAL2, VAL3
    }

}
