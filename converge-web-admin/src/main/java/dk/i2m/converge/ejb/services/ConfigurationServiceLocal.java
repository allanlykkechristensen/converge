/*
 *  Copyright (C) 2010 - 2012 Interactive Media Management
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package dk.i2m.converge.ejb.services;

import java.util.Locale;
import javax.ejb.Local;

/**
 * Local interface for the Configuration Service bean.
 *
 * @author Allan Lykke Christensen
 */
@Local
public interface ConfigurationServiceLocal {

    /**
     * Gets a configuration value as a String. Note: If a configuration value
     * could not be found, the string representation of the configuration key
     * will be returned.
     *
     * @param key
     *          Key of the configuration value
     * @return Value matching the given key
     */
    String getString(ConfigurationKey key);

    /**
     * Gets a configuration value as an Integer. Note: If a configuration value
     * could not be found, -1 is returned.
     *
     * @param key
     *          Key of the configuration value
     * @return Value matching the given key
     */
    Integer getInteger(ConfigurationKey key);

    /**
     * Gets a configuration value as a Boolean. Note: If a configuration value
     * could not be found, false is returned.
     *
     * @param key
     *          Key of the configuration value
     * @return Value matching the given key
     */
    Boolean getBoolean(ConfigurationKey key);

    /**
     * Updates the value of a configuration. The updated value is stored in the
     * persisted storage.
     *
     * @param key
     *          Key of the configuration
     * @param value
     *          Value of the configuration
     */
    void set(ConfigurationKey key, String value);

    /**
     * Gets the {@link Locale} of the application.
     *
     * @return {@link Locale} of the application
     */
    Locale getApplicationLocale();

    /**
     * Gets the short display of the current version of the system. The short
     * display only contains the version number, whereas the long display also
     * includes the date when the application was built.
     *
     * @return Current version of the system
     */
    String getVersion();

    /**
     * Gets the build number of the current version.
     *
     * @return Current build number of the system
     */
    String getBuildNumber();

    /**
     * Gets the long display of the current version of the system. The short
     * display only contains the version number, whereas the long display also
     * includes the date when the application was built.
     *
     * @return Current version of the system
     */
    String getLongVersion();
}
