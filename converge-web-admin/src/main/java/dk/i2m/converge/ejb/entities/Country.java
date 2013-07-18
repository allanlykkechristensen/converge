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
package dk.i2m.converge.ejb.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Reference to a {@link Country} where something is physically located.
 *
 * @author Allan Lykke Christensen
 */
@Entity
@Table(name = "country")
public class Country implements Serializable {

    /** Unique identifier of the {@link Country}. */
    @Id
    @Column(name = "code")
    private String code;

    /** Official name of the {@link Country}. */
    @Column(name = "name")
    private String name;

    /**
     * Creates a new instance of {@link Country}.
     */
    public Country() {
        this("", "");
    }

    /**
     * Creates a new instance of {@link Country}.
     * 
     * @param code
     *          Unique identifier of the {@link Country}
     * @param name 
     *          Name of the {@link Country}
     */
    public Country(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Country other = (Country) obj;
        if ((this.code == null) ? (other.code != null) : !this.code.equals(other.code)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (this.code != null ? this.code.hashCode() : 0);
        return hash;
    }
}
