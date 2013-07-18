/*
 * Copyright (C) 2010 - 2012 Interactive Media Management
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

import dk.i2m.converge.ejb.services.ConfigurationKey;
import java.io.Serializable;
import javax.persistence.*;

/**
 * Representation of a {@link Configuration} stored in the database.
 *
 * @author Allan Lykke Christensen
 */
@Entity
@Table(name = "config", uniqueConstraints =
@UniqueConstraint(columnNames = {"config_key"}))
@NamedQueries({
    @NamedQuery(name = Configuration.FIND_BY_KEY, query = "SELECT c FROM Configuration c WHERE c.key=:" + Configuration.PARAM_KEY)
})
public class Configuration implements Serializable {

    /** Query for finding a configuration setting by its unique key. Use the {@link Configuration#PARAM_KEY} parameter to specific the key. */
    public static final String FIND_BY_KEY = "Configuration.findByKey";
    
    /** Parameter used for locating a {@link Configuration}. */
    public static final String PARAM_KEY = "cfgKey";

    /** Unique identifier of the {@link Configuration}. */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Key used for locating the {@link Configuration}. */
    @Column(name = "config_key")
    @Enumerated(EnumType.STRING)
    private ConfigurationKey key;

    /** Value of the {@link Configuration}. */
    @Column(name = "config_value") @Lob
    private String value;

    /**
     * Creates a new instance of {@link Configuration}.
     */
    public Configuration() {
    }

    /**
     * Gets the unique identifier of the configuration. The unique identifier is
     * automatically generated and does not have any meaning in relation to the
     * key and value.
     *
     * @return Unique identifier of the configuration
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the configuration.
     *
     * @param id
     *          Unique identifier of the configuration
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the key of the configuration setting. The key describes the value of
     * the setting.
     *
     * @return Key of the configuration setting
     */
    public ConfigurationKey getKey() {
        return key;
    }

    /**
     * Sets the key of the configuration setting.
     *
     * @param key
     *          Key of the configuration setting
     */
    public void setKey(ConfigurationKey key) {
        this.key = key;
    }

    /**
     * Gets the value of the configuration setting.
     *
     * @return Value of the configuration setting
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the configuration setting.
     *
     * @param value
     *          Value of the configuration setting
     */
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Configuration)) {
            return false;
        }
        Configuration other = (Configuration) object;
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
