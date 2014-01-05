/*
 * Copyright (C) 2014 Converge Consulting Limited
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
package com.getconverge.converge.ws;

import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Interface for the web service used for accessing the Converge configuration.
 *
 * @author Allan Lykke Christensen
 */
@WebService
public interface ConfigurationWebService {

    /**
     * Gets a configuration value as a given type.
     *
     * @param <T> Type of entity to retrieve and return
     * @param type Type of entity to retrieve and return
     * @param key Key of the configuration value
     * @return Value matching the given key
     */
    @WebMethod(operationName = "get")
    <T> T get(@WebParam(name = "type") Class<T> type, @WebParam(name = "key") String key);

    /**
     * Gets the locale identifier of the application.
     *
     * @return Locale identifier of the application
     */
    @WebMethod(operationName = "getApplicationLocale")
    String getApplicationLocale();

    /**
     * Gets the build time of the current version.
     *
     * @return Current build time of the system
     */
    @WebMethod(operationName = "getBuildTime")
    String getBuildTime();

    /**
     * Gets the long display of the current version of the system. The short
     * display only contains the version number, whereas the long display also
     * includes the date when the application was built.
     *
     * @return Current version of the system
     */
    @WebMethod(operationName = "getLongVersion")
    String getLongVersion();

    /**
     * Gets the short display of the current version of the system. The short
     * display only contains the version number, whereas the long display also
     * includes the date when the application was built.
     *
     * @return Current version of the system
     */
    @WebMethod(operationName = "getVersion")
    String getVersion();

    /**
     * Updates the value of a configuration. The updated value is stored in the
     * persisted storage.
     *
     * @param key Key of the configuration
     * @param value Value of the configuration
     */
    @WebMethod(operationName = "set")
    @Oneway
    void set(@WebParam(name = "key") String key, @WebParam(name = "value") String value);

    /**
     * Resets the value in the given configuration key.
     *
     * @param key Configuration key for which to reset the value
     */
    @WebMethod(operationName = "reset")
    @Oneway
    void reset(@WebParam(name = "key") String key);

}
