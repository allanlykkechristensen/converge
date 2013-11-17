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
import org.junit.Test;
import static org.junit.Assert.*;

public class StringLineBreakConverterTest {

    FacesContext facesContext = null;
    UIComponent uiComponent = null;

    @Test
    public void testGetAsObject() {
        String value = "Hello<br/>How are you?";
        String expResult = "Hello" + System.getProperty("line.separator") + "How are you?";

        StringLineBreakConverter instance = new StringLineBreakConverter();
        Object result = instance.getAsObject(facesContext, uiComponent, value);
        assertEquals(expResult, result);
    }

    @Test
    public void testGetAsString() {
        String value = "Hello" + System.getProperty("line.separator") + "How are you?";
        String expResult = "Hello<br/>How are you?";

        StringLineBreakConverter instance = new StringLineBreakConverter();
        Object result = instance.getAsString(facesContext, uiComponent, value);
        assertEquals(expResult, result);
    }

}
