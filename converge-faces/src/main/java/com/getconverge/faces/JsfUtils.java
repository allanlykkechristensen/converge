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
package com.getconverge.faces;

import java.text.MessageFormat;
import java.util.ResourceBundle;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * Utility library for common JSF tasks.
 *
 * @author Allan Lykke Christensen
 */
public class JsfUtils {

    private JsfUtils() {
        // Hide constructor, shouldn't be instantiated
        throw new UnsupportedOperationException();
    }

    /**
     * Obtain a message from a {@link ResourceBundle} defined in
     * {@code faces-config.xml}.
     *
     * @param resourceBundleId Unique identifier of the {@link ResourceBundle}
     * @param msgKey Key of the message to retrieve in the
     * {@link ResourceBundle}
     * @return The message from the given {@link ResourceBundle}, or if the
     * message was not found the {@code msgKey} is returned
     */
    public static String getString(String resourceBundleId, String msgKey) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        Application app = ctx.getApplication();
        ResourceBundle bundle = app.getResourceBundle(ctx, resourceBundleId);

        if (bundle.containsKey(msgKey)) {
            return bundle.getString(msgKey);
        } else {
            return msgKey;
        }
    }

    /**
     * Creates a {@link FacesMessage} using the given {@link ResourceBundle} and
     * message key.
     *
     * @param severity Severity of the {@link FacesMessage}
     * @param bundle Unique identifier of the {@link ResourceBundle}
     * @param msgKey Key of the message to retrieve in the
     * {@link ResourceBundle}
     */
    public static void createFacesMessage(FacesMessage.Severity severity, String bundle, String msgKey) {
        createFacesMessage(severity, bundle, msgKey, new Object[]{});
    }

    /**
     * Creates a {@link FacesMessage} using the given {@link ResourceBundle} and
     * message key.
     *
     * @param severity Severity of the {@link FacesMessage}
     * @param bundle Unique identifier of the {@link ResourceBundle}
     * @param msgKey Key of the message to retrieve in the
     * {@link ResourceBundle}
     * @param parameters Parameters to merge into the message
     */
    public static void createFacesMessage(FacesMessage.Severity severity, String bundle, String msgKey, Object[] parameters) {
        createFacesMessage(null, severity, bundle, msgKey, parameters);
    }

    /**
     * Creates a {@link FacesMessage} using the given {@link ResourceBundle} and
     * message key.
     *
     * @param clientId Client for which to attach the message
     * @param severity Severity of the {@link FacesMessage}
     * @param bundle Unique identifier of the {@link ResourceBundle}
     * @param msgKey Key of the message to retrieve in the
     * {@link ResourceBundle}
     * @param parameters Parameters to merge into the message
     */
    public static void createFacesMessage(String clientId, FacesMessage.Severity severity, String bundle, String msgKey, Object[] parameters) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        String pattern = getString(bundle, msgKey);
        String message = MessageFormat.format(pattern, parameters);
        FacesMessage msg = new FacesMessage(message);
        msg.setSeverity(severity);
        ctx.addMessage(clientId, msg);
    }
}
