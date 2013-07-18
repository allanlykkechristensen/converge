/*
 * Copyright (C) 2012 Interactive Media Management
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
package dk.i2m.converge.ejb.entities.crm;

import java.util.HashMap;
import java.util.Map;

/**
 * Field to display in a {@link QuoteLine}.
 *
 * @author Allan Lykke Christensen
 */
public class QuoteLineField {

    /** Identifier of the field used for recognising the field in the databas after it has been saved. */
    private String id;

    /** Order in which the field is displayed (ascending order). */
    private int displayOrder;

    /** Name of the field. */
    private String name;

    /** Label of the field - similar to name but may be localised. */
    private String label;

    /** Category of field - used for identifying a group of fields containing related information. */
    private String qualifier;

    /** Default value stored in the field. */
    private String value;

    /** Type of field. TODO: Should be split into a enum for rendering purposes. */
    private String type;

    /** CSS style class to use for displaying the field type. */
    private String styleClass;

    /** Should a summary field be displayed. */
    private boolean summary = false;

    /** Should the field be frozen in the display. */
    private boolean freeze = false;

    /** Is the field read-only (for display only). */
    private boolean readOnly = false;
    
    public QuoteLineField() {
        this("", "", "", "", "", "", 0, false, false, false);
    }

    public QuoteLineField(String id, String name, String label, String value, String type, String styleClass, int displayOrder, boolean summary, boolean freeze, boolean readOnly) {
        this(id, name, label, value, type, styleClass, "", displayOrder, summary, freeze, readOnly);
    }

    public QuoteLineField(String id, String name, String label, String value, String type, String styleClass, String qualifier, int displayOrder, boolean summary, boolean freeze, boolean readOnly) {
        this.id = id;
        this.name = name;
        this.label = label;
        this.value = value;
        this.type = type;
        this.styleClass = styleClass;
        this.qualifier = qualifier;
        this.displayOrder = displayOrder;
        this.summary = summary;
        this.freeze = freeze;
        this.readOnly = readOnly;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getStyleClass() {
        return styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public boolean isSummary() {
        return summary;
    }

    public void setSummary(boolean summary) {
        this.summary = summary;
    }

    public boolean isFreeze() {
        return freeze;
    }

    public void setFreeze(boolean freeze) {
        this.freeze = freeze;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public String getQualifier() {
        return qualifier;
    }

    public void setQualifier(String qualifier) {
        this.qualifier = qualifier;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final QuoteLineField other = (QuoteLineField) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }
}
