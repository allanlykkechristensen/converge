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
package com.getconverge.converge.entities;

import org.junit.Test;
import static org.junit.Assert.*;

public class ConfigurationTest {

    @Test
    public void configuration_twoConfigurationsWithSameId_EqualsTrue() {
        // Assign
        Configuration cfg1 = new Configuration();
        cfg1.setId(1L);
        cfg1.setKey(ConfigurationKey.COUNTRY);
        cfg1.setValue("dk");
        Configuration cfg2 = new Configuration();
        cfg2.setId(1L);
        cfg2.setKey(ConfigurationKey.LANGUAGE);
        cfg2.setValue("da");

        // Act
        boolean cfgEquals = cfg1.equals(cfg2);

        // Assert
        assertTrue(cfgEquals);
        assertEquals(cfg1.hashCode(), cfg2.hashCode());
    }

    @Test
    public void configuration_twoConfigurationsWithDifferentId_EqualsFalse() {
        // Assign
        Configuration cfg1 = new Configuration();
        cfg1.setId(1L);
        cfg1.setKey(ConfigurationKey.COUNTRY);
        cfg1.setValue("dk");
        Configuration cfg2 = new Configuration();
        cfg2.setId(2L);
        cfg2.setKey(ConfigurationKey.LANGUAGE);
        cfg2.setValue("da");

        // Act
        boolean cfgEquals = cfg1.equals(cfg2);

        // Assert
        assertFalse(cfgEquals);
        assertNotEquals(cfg1.hashCode(), cfg2.hashCode());
    }

    @Test
    public void configuration_twoConfigurationsWithDifferentIdOneIsNull_EqualsFalse() {
        // Assign
        Configuration cfg1 = new Configuration();
        cfg1.setId(null);
        cfg1.setKey(ConfigurationKey.COUNTRY);
        cfg1.setValue("dk");
        Configuration cfg2 = new Configuration();
        cfg2.setId(2L);
        cfg2.setKey(ConfigurationKey.LANGUAGE);
        cfg2.setValue("da");

        // Act
        boolean cfgEquals = cfg1.equals(cfg2);

        // Assert
        assertFalse(cfgEquals);
        assertNotEquals(cfg1.hashCode(), cfg2.hashCode());
    }

    @Test
    public void configuration_oneConfigurationOneNonConfiguration_EqualsFalse() {
        // Assign
        Configuration cfg1 = new Configuration();
        cfg1.setId(1L);
        cfg1.setKey(ConfigurationKey.COUNTRY);
        cfg1.setValue("dk");
        String cfg2 = "This is not a Configuration object";

        // Act
        boolean cfgEquals = cfg1.equals(cfg2);

        // Assert
        assertFalse(cfgEquals);
        assertNotEquals(cfg1.hashCode(), cfg2.hashCode());
    }

    @Test
    public void configuration_ConfigurationWithId_toStringIsUniform() {
        // Assign
        Configuration cfg = new Configuration();
        cfg.setId(1L);
        cfg.setKey(ConfigurationKey.COUNTRY);
        cfg.setValue("dk");
        String expectedResult = cfg.getClass().getName() + "[id=1]";

        // Act
        String result = cfg.toString();

        // Assert
        assertEquals(expectedResult, result);
    }

}
