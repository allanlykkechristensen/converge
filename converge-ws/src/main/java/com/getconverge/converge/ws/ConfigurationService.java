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

import com.getconverge.converge.ejb.services.ConfigurationServiceBean;
import com.getconverge.converge.entities.ConfigurationKey;
import javax.ejb.EJB;
import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 *
 * @author Allan Lykke Christensen
 */
@WebService(serviceName = "ConfigurationService")
public class ConfigurationService implements ConfigurationWebService {
    
    @EJB
    private ConfigurationServiceBean cfgService;
    
    @WebMethod(operationName = "get")
    @Override
    public <T> T get(@WebParam(name = "type") Class<T> type, @WebParam(name = "key") String key) {
        return cfgService.get(type, ConfigurationKey.valueOf(key));
    }
    
    @WebMethod(operationName = "set")
    @Oneway
    @Override
    public void set(@WebParam(name = "key") String key, @WebParam(name = "value") String value) {
        cfgService.set(ConfigurationKey.valueOf(key), value);
    }
    
    @WebMethod(operationName = "getApplicationLocale")
    @Override
    public String getApplicationLocale() {
        return cfgService.getApplicationLocale().toString();
    }
    
    @WebMethod(operationName = "getVersion")
    @Override
    public String getVersion() {
        return cfgService.getVersion();
    }
    
    @WebMethod(operationName = "getBuildTime")
    @Override
    public String getBuildTime() {
        return cfgService.getBuildTime();
    }
    
    @WebMethod(operationName = "getLongVersion")
    @Override
    public String getLongVersion() {
        return cfgService.getLongVersion();
    }
    
    @WebMethod(operationName = "reset")
    @Oneway
    @Override
    public void reset(@WebParam(name = "key") String key) {
        cfgService.reset(ConfigurationKey.valueOf(key));
    }
    
}
