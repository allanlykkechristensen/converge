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

import java.io.Serializable;
import javax.persistence.*;

/**
 * Property of a {@link QuoteLine}. A property contains some relevant
 * information regarding a {@link QuoteLine}.
 *
 * @author Allan Lykke Christensen
 */
@Entity
@Table(name = "quote_line_property")
public class QuoteLineProperty implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Unique identifier of the {@link QuoteLineProperty}. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /** Named identifier used to identify the property. */
    @Column(name = "property_named_identifier")
    private String namedIdentifier;

    /** Qualifier of the property, used for grouping related properties. */
    @Column(name = "property_qualifier")
    private String qualifier;

    /** Name of the property - this is user-friendly value of the property. */
    @Column(name = "property_name")
    private String name;

    /** Label of the property, typically used as the column header. */
    @Column(name = "property_label")
    private String label;

    /** Actual value of the property. */
    @Column(name = "property_value")
    @Lob
    private String value;

    @ManyToOne
    @JoinColumn(name = "quote_line_id")
    private QuoteLine quoteLine;

    public QuoteLineProperty() {
        this("", "", "", "", "", null);
    }

    public QuoteLineProperty(String namedIdentifier, String name, String label, String qualifier, String value, QuoteLine quoteLine) {
        this.namedIdentifier = namedIdentifier;
        this.name = name;
        this.qualifier = qualifier;
        this.value = value;
        this.quoteLine = quoteLine;
        this.label = label;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getNamedIdentifier() {
        return namedIdentifier;
    }

    public void setNamedIdentifier(String namedIdentifier) {
        this.namedIdentifier = namedIdentifier;
    }

    public String getQualifier() {
        return qualifier;
    }

    public void setQualifier(String qualifier) {
        this.qualifier = qualifier;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public QuoteLine getQuoteLine() {
        return quoteLine;
    }

    public void setQuoteLine(QuoteLine quoteLine) {
        this.quoteLine = quoteLine;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof QuoteLineProperty)) {
            return false;
        }
        QuoteLineProperty other = (QuoteLineProperty) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getClass().getName() + "[id=" + id + "]";
    }
}
