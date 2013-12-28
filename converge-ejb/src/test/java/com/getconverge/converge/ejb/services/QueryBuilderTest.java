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

import org.junit.Test;
import static org.junit.Assert.*;

public class QueryBuilderTest {

    @Test
    public void queryBuilder_emptyWithParameter_oneParameterAvailable() {
        // Arrange
        String parameterKey = "paramkey";
        String parameterValue = "paramvalue";

        // Act
        QueryBuilder result = QueryBuilder.with(parameterKey, parameterValue);

        // Assert
        assertEquals("Expected number of parameters incorrect", 1, result.parameters().size());
        assertTrue("Expected parameter key was not available", result.parameters().containsKey(parameterKey));
        assertTrue("Expected parameter value was not available", result.parameters().containsValue(parameterValue));
    }

    @Test
    public void queryBuilder_emptyWithParameterAndAnotherParameter_twoParametersAvailable() {
        // Arrange
        String parameterKey1 = "paramkey1";
        String parameterValue1 = "paramvalue1";
        String parameterKey2 = "paramkey2";
        String parameterValue2 = "paramvalue2";

        // Act
        QueryBuilder result = QueryBuilder.with(parameterKey1, parameterValue1)
                .and(parameterKey2, parameterValue2);

        // Assert
        assertEquals("Expected number of parameters incorrect", 2, result.parameters().size());
        assertTrue("Expected parameter (1) key was not available", result.parameters().containsKey(parameterKey1));
        assertTrue("Expected parameter (1) value was not available", result.parameters().containsValue(parameterValue1));
        assertTrue("Expected parameter (2) key was not available", result.parameters().containsKey(parameterKey2));
        assertTrue("Expected parameter (2) value was not available", result.parameters().containsValue(parameterValue2));
    }

    @Test
    public void queryBuilder_emptyWithParameterAndAnotherParameterWithAnotherParameter_oneParameterAvailable() {
        // Arrange
        String parameterKey1 = "paramkey1";
        String parameterValue1 = "paramvalue1";
        String parameterKey2 = "paramkey2";
        String parameterValue2 = "paramvalue2";
        String parameterKey3 = "paramkey3";
        String parameterValue3 = "paramvalue3";

        // Act
        QueryBuilder result = QueryBuilder.with(parameterKey1, parameterValue1)
                .and(parameterKey2, parameterValue2)
                .with(parameterKey3, parameterValue3);

        // Assert
        assertEquals("Expected number of parameters incorrect", 1, result.parameters().size());
        assertFalse("Unexpected parameter (1) key was available", result.parameters().containsKey(parameterKey1));
        assertFalse("Unexpected parameter (1) value was available", result.parameters().containsValue(parameterValue1));
        assertFalse("Unexpected parameter (2) key was available", result.parameters().containsKey(parameterKey2));
        assertFalse("Unexpected parameter (2) value was available", result.parameters().containsValue(parameterValue2));
        assertTrue("Expected parameter (3) key was not available", result.parameters().containsKey(parameterKey3));
        assertTrue("Expected parameter (3) value was not available", result.parameters().containsValue(parameterValue3));
    }

}
