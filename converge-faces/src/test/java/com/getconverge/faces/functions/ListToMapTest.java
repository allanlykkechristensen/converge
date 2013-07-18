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

import dk.i2m.converge.faces.functions.ListToMap;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;

public class ListToMapTest {

    @Test
    public void listToMap_nullList_zeroElements() {
        List list = null;
        Map result = ListToMap.listToMap(list, "id");
        assertEquals(0, result.size());
    }

    @Test
    public void listToMap_populatedList_correctElementCount() {
        List list = new ArrayList<DomainTestObject>();
        list.add(new DomainTestObject("1", "1st Title", "1st Description"));
        list.add(new DomainTestObject("2", "2nd Title", "2nd Description"));
        list.add(new DomainTestObject("3", "3rd Title", "3rd Description"));

        Map result = ListToMap.listToMap(list, "id");

        assertEquals(3, result.size());
    }

    @Test
    public void listToMap_populatedList_correctKeyValuePairs() {
        List list = new ArrayList<DomainTestObject>();
        list.add(new DomainTestObject("1", "1st Title", "1st Description"));
        list.add(new DomainTestObject("2", "2nd Title", "2nd Description"));
        list.add(new DomainTestObject("3", "3rd Title", "3rd Description"));

        Map result = ListToMap.listToMap(list, "id");

        assertTrue(result.containsKey("1"));
        assertTrue(result.containsKey("2"));
        assertTrue(result.containsKey("3"));
        assertEquals(((DomainTestObject) result.get("1")).getTitle(), "1st Title");
        assertEquals(((DomainTestObject) result.get("1")).getDescription(), "1st Description");
        assertEquals(((DomainTestObject) result.get("2")).getTitle(), "2nd Title");
        assertEquals(((DomainTestObject) result.get("2")).getDescription(), "2nd Description");
        assertEquals(((DomainTestObject) result.get("3")).getTitle(), "3rd Title");
        assertEquals(((DomainTestObject) result.get("3")).getDescription(), "3rd Description");
    }

    @Test
    public void listToMap_extractIncorrectKey_DefaultKeyValues() {
        List list = new ArrayList<DomainTestObject>();
        list.add(new DomainTestObject("1", "1st Title", "1st Description"));
        list.add(new DomainTestObject("2", "2nd Title", "2nd Description"));
        list.add(new DomainTestObject("3", "3rd Title", "3rd Description"));

        Map result = ListToMap.listToMap(list, "nonexistingproperty");

        assertEquals(3, result.size());
        assertFalse(result.containsKey("1"));
        assertFalse(result.containsKey("2"));
        assertFalse(result.containsKey("3"));
        assertTrue(result.containsKey("DomainTestObject: #1"));
        assertTrue(result.containsKey("DomainTestObject: #2"));
        assertTrue(result.containsKey("DomainTestObject: #3"));
    }

    @Test()
    public void listToMap_utilityClass_uninstantiable() {
        final Constructor<?>[] c = ListToMap.class.getDeclaredConstructors();

        for (Constructor<?> constructor : c) {
            assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        }
    }

    public class DomainTestObject {

        private String id;
        private String title;
        private String description;

        public DomainTestObject(String id, String title, String description) {
            this.id = id;
            this.title = title;
            this.description = description;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return "DomainTestObject: #" + getId();
        }
    }
}