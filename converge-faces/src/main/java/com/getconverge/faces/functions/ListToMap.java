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
package com.getconverge.faces.functions;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.beanutils.BeanUtils;

/**
 * EL function for turning a {@link List} into a {@link Map}. The EL function
 * can be used to turning a {@link List} of entity objects into a {@link Map} at
 * runtime for use with select tags.
 *
 * @author Allan Lykke Christensen
 */
public class ListToMap {

    private static final Logger LOG = Logger.getLogger(ListToMap.class.getName());

    private ListToMap() {
        // Hide constructor, shouldn't be instantiated
        throw new UnsupportedOperationException();
    }

    /**
     * Converts a {@link List} of {@link Object}s into a {@link Map} of
     * {@link Object}s with a given {@code property} of the {@link Object} as
     * the key of the {@link Map}.
     *
     * @param list {@link List} of {@link Object}s to convert
     * @param property Name of the field to use as the key of the output
     * {@link Map}
     * @return {@link Map} containing the {@code property} as the key for each
     * of the {@link Object}s in the {@code list}
     */
    public static Map listToMap(List list, String property) {
        Map<String, Object> output = new LinkedHashMap<String, Object>();

        if (list == null) {
            return output;
        }

        for (Object o : list) {
            String propertyValue = o.toString();
            try {
                propertyValue = BeanUtils.getProperty(o, property);
            } catch (ReflectiveOperationException ex) {
                LOG.log(Level.FINEST, ex.getMessage());
            }

            output.put(propertyValue, o);
        }

        return output;
    }
}
