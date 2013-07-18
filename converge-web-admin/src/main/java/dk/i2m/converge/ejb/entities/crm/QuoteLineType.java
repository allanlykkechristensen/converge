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
 * Type of {@link QuoteLine} with a reference to a serialiser that serialise
 * and deserialise the content of a single {@link QuoteLine}.
 *
 * @author Allan Lykke Christensen
 */
@Entity
@Table(name = "quote_line_type")
public class QuoteLineType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    @Lob
    private String description;

    @Column(name = "serialiser")
    private String serialiser;

    public QuoteLineType() {
        this("", "", "");
    }

    public QuoteLineType(String name, String description, String serialiser) {
        this.name = name;
        this.description = description;
        this.serialiser = serialiser;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSerialiser() {
        return serialiser;
    }

    public void setSerialiser(String serialiser) {
        this.serialiser = serialiser;
    }
    
    /**
     * Creates an instance of the serialiser specified in {@link #getSerialiser()}.
     *
     * @return Instance of the serialiser
     * @throws PluginInstantiationException
     *          If the serialiser could not be instantiated
     */
    public QuoteLineSerializer getSerialiserInstance() throws PluginInstantiationException {
        try {
            Class c = Class.forName(getSerialiser());
            QuoteLineSerializer s = (QuoteLineSerializer) c.newInstance();
            return s;
        } catch (ClassNotFoundException ex) {
            throw new PluginInstantiationException("Could not find serialiser: " + getSerialiser(), ex);
        } catch (InstantiationException ex) {
            throw new PluginInstantiationException("Could not instantiate serialiser [" + getSerialiser() + "]. Check to ensure that the serialiser has a public contructor with no arguments", ex);
        } catch (IllegalAccessException ex) {
            throw new PluginInstantiationException("Could not access serialiser: " + getSerialiser(), ex);
        }
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof QuoteLineType)) {
            return false;
        }
        QuoteLineType other = (QuoteLineType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dk.i2m.converge.ejb.entities.crm.QuoteLineType[ id=" + id + " ]";
    }
}
