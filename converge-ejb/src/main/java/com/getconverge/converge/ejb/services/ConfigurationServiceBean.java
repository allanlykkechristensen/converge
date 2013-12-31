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
package com.getconverge.converge.ejb.services;

import com.getconverge.converge.entities.Configuration;
import com.getconverge.converge.entities.ConfigurationKey;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;

/**
 * Stateless session bean providing a service for obtaining the application
 * configuration.
 *
 * @author Allan Lykke Christensen
 */
@Stateless
@LocalBean
public class ConfigurationServiceBean {

    private static final String CONFIGURATION_BUNDLE = "Converge";

    @EJB
    private DaoServiceBean daoService;

    private final ResourceBundle defaultConfigurations = ResourceBundle.getBundle(CONFIGURATION_BUNDLE);

    /**
     * Gets a configuration value as a given type.
     *
     * @param <T> Type of entity to retrieve and return
     * @param type Type of entity to retrieve and return
     * @param key Key of the configuration value
     * @return Value matching the given key
     */
    public <T> T get(Class<T> type, ConfigurationKey key) {
        return get(type, key, null);
    }

    /**
     * Gets a configuration value as a given type.
     *
     * @param <T> Type of entity to retrieve and return
     * @param type Type of entity to retrieve and return
     * @param key Key of the configuration value
     * @param defaultValue Default value if the configuration could not be found
     * @return Value matching the given key or {@code defaultValue} if the key
     * could not be matched
     */
    public <T> T get(Class<T> type, ConfigurationKey key, T defaultValue) throws NumberFormatException, UnsupportedOperationException {

        try {
            Configuration configuration = daoService.findObjectWithNamedQuery(Configuration.class,
                    Configuration.FIND_BY_KEY,
                    QueryBuilder.with(Configuration.PARAM_FIND_BY_KEY_KEY, key));
            Object cfgValue = convertStringToType(configuration.getValue(), type);
            return (T) cfgValue;
        } catch (DataNotFoundException ex) {
            String cfgStringValue = defaultConfigurations.getString(key.name());
            Object cfgValue = convertStringToType(cfgStringValue, type);
            return (T) cfgValue;
        }
    }

    // TODO: Find out if there is a commons library that can do the same
    private Object convertStringToType(String value, Class type) throws NumberFormatException, UnsupportedOperationException {
        Object cfgValue = value;
        if (type == String.class) {
            cfgValue = value;
        } else if (type == Integer.class) {
            cfgValue = Integer.valueOf((String) cfgValue);
        } else if (type == Long.class) {
            cfgValue = Long.valueOf((String) cfgValue);
        } else {
            throw new UnsupportedOperationException("Convertion from String to " + type.getName() + " is not supported");
        }
        return cfgValue;
    }

    /**
     * Updates the value of a configuration. The updated value is stored in the
     * persisted storage.
     *
     * @param key Key of the configuration
     * @param value Value of the configuration
     */
    public void set(ConfigurationKey key, String value) {
        List results = daoService.findWithNamedQuery(Configuration.FIND_BY_KEY,
                QueryBuilder.with("cfgKey", key).parameters());
        if (results.size() == 1) {
            Configuration entry = (Configuration) results.get(0);
            entry.setValue(value);
            daoService.update(entry);
        } else {
            Configuration entry = new Configuration();
            entry.setKey(key);
            entry.setValue(value);
            daoService.create(entry);
        }
    }

    /**
     * Gets the {@link Locale} of the application.
     *
     * @return {@link Locale} of the application
     */
    public Locale getApplicationLocale() {
        String language = get(String.class, ConfigurationKey.LANGUAGE);
        String country = get(String.class, ConfigurationKey.COUNTRY);
        return new Locale(language, country);
    }

    /**
     * Gets the short display of the current version of the system. The short
     * display only contains the version number, whereas the long display also
     * includes the date when the application was built.
     *
     * @return Current version of the system
     */
    public String getVersion() {
        return get(String.class, ConfigurationKey.VERSION);
    }

    /**
     * Gets the build time of the current version.
     *
     * @return Current build time of the system
     */
    public String getBuildTime() {
        return get(String.class, ConfigurationKey.BUILD_TIME);
    }

    /**
     * Gets the long display of the current version of the system. The short
     * display only contains the version number, whereas the long display also
     * includes the date when the application was built.
     *
     * @return Current version of the system
     */
    public String getLongVersion() {
        return getVersion() + " " + getBuildTime();
    }
}
