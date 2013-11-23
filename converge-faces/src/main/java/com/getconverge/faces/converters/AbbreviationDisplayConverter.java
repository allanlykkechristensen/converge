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

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.apache.commons.lang3.StringUtils;

/**
 * JSF {@link Converter} for abbreviating a {@link String}. The abbreviation is
 * a one-way conversion where only the
 * {@link Converter#getAsString(FacesContext, UIComponent, Object)} is
 * supported.<br/>
 * <br/>
 * <b>Example</b>
 * <code>
 * &lt;h:outputText value="A long long text"&gt;
 *     &lt;f:converter converterId="abbreviationDisplay" /&gt;
 *     &lt;f:attribute name="maxLength" value="5" /&gt;
 * &lt;/h:outputText&gt;
 * </code>
 *
 * @author Allan Lykke Christensen
 */
@FacesConverter(value = "abbreviationDisplay")
public class AbbreviationDisplayConverter implements Converter {

    private static final Logger LOG = Logger.getLogger(AbbreviationDisplayConverter.class.getName());
    /**
     * Name of the attribute where the max length is stored.
     */
    public static final String ATTRIBUTE_MAX_LENGTH = "maxLength";

    public Object getAsObject(FacesContext ctx, UIComponent comp, String value) {
        return null;
    }

    public String getAsString(FacesContext ctx, UIComponent comp, Object value) {
        if (value instanceof String) {
            String originalValue = (String) value;
            Map<String, Object> attrs = comp.getAttributes();
            if (attrs.containsKey(ATTRIBUTE_MAX_LENGTH)) {
                Object objMaxLength = attrs.get(ATTRIBUTE_MAX_LENGTH);
                if (objMaxLength instanceof String) {
                    String maxLength = (String) objMaxLength;

                    try {
                        return StringUtils.abbreviate(originalValue,
                                Integer.valueOf(maxLength));
                    } catch (NumberFormatException ex) {
                        LOG.warning("Attribute '" + ATTRIBUTE_MAX_LENGTH
                                + "' must contain a String with a numeric value");
                        return originalValue;
                    }
                } else {
                    LOG.warning("Attribute '" + ATTRIBUTE_MAX_LENGTH
                            + "' must contain a String with a numeric value");
                    return originalValue;
                }
            } else {
                LOG.warning("Attribute '" + ATTRIBUTE_MAX_LENGTH + "' is missing");
                return originalValue;
            }
        } else {
            LOG.log(Level.WARNING, "Value provided to {0} was not a String but a {1}", new Object[]{getClass().getName(), value.getClass()});
            return "";
        }
    }
}
