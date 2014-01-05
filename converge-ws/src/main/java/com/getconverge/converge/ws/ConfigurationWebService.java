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

    @WebMethod(operationName = "get")
    <T> T get(@WebParam(name = "type") Class<T> type, @WebParam(name = "key") String key);

    @WebMethod(operationName = "getApplicationLocale")
    String getApplicationLocale();

    @WebMethod(operationName = "getBuildTime")
    String getBuildTime();

    @WebMethod(operationName = "getLongVersion")
    String getLongVersion();

    @WebMethod(operationName = "getVersion")
    String getVersion();

    @WebMethod(operationName = "set")
    @Oneway
    void set(@WebParam(name = "key") String key, @WebParam(name = "value") String value);

    @WebMethod(operationName = "reset")
    @Oneway
    void reset(@WebParam(name = "key") String key);

}
