/*
 * Copyright (C) 2009 - 2013 Converge Consulting
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
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 * Generic converter for enumeration types.
 *
 * @author Allan Lykke Christensen
 */
@FacesConverter(value = "enumTypeConverter")
public class EnumTypeConverter implements Converter {

    /**
     * Converts the string representation of the enumeration to the enumeration
     * type.
     *
     * @param ctx Faces context
     * @param comp Component using the converter
     * @param value Value to convert
     * @return Enumeration type matching the <code>value</code>
     * @throws javax.faces.convert.ConverterException If the conversion failed
     */
    public Object getAsObject(FacesContext ctx, UIComponent comp, String value) throws ConverterException {
        Class enumType = comp.getValueBinding("value").getType(ctx);
        return Enum.valueOf(enumType, value);
    }

    /**
     * Converts the enumeration type to a unique string representation.
     *
     * @param ctx Faces context
     * @param comp Component using the converter
     * @param obj Object to convert
     * @return String representation of the enumeration type
     * @throws javax.faces.convert.ConverterException If the conversion failed
     */
    public String getAsString(FacesContext ctx, UIComponent comp, Object obj) throws ConverterException {
        if (obj == null) {
            return null;
        }
        Enum type = (Enum) obj;
        return type.toString();
    }
}
