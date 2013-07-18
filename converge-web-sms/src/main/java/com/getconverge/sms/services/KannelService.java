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
package com.getconverge.sms.services;

import com.getconverge.sms.KannelDispatcher;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebParam;
import javax.jws.WebService;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 * SOAP web service for dispatching SMSes via <a
 * href="http://www.kannel.org/">Kannel</a>.
 *
 * @author Allan Lykke Christensen
 */
@WebService(serviceName = "KannelService")
public class KannelService {

    private static final Logger LOG = Logger.getLogger(KannelService.class.getName());

    /**
     * Sends a message using a Kannel server.
     *
     * @param host Hostname or IP of server
     * @param port Port of server
     * @param user Username to use for sending
     * @param password Password to use for sending
     * @param recipient Receipient of the message (include country code, e.g.
     * +45)
     * @param message Message to send
     * @param timeout Timeout in ms
     * @return {@code true} if the message was sent, otherwise {@code false}
     */
    public boolean kannelSendSms(
            @WebParam(name = "host") String host,
            @WebParam(name = "port") int port,
            @WebParam(name = "user") String user,
            @WebParam(name = "password") String password,
            @WebParam(name = "recipient") String recipient,
            @WebParam(name = "message") String message,
            @WebParam(name = "timeout") int timeout) {

        KannelDispatcher dispatcher = new KannelDispatcher(host, port, timeout, user, password);
        try {
            dispatcher.dispatch(recipient, message);
        } catch (HttpException ex) {
            LOG.log(Level.WARNING, "Could not execute URL. {0}", ex.getMessage());
            LOG.log(Level.FINEST, "", ex);
            return false;
        } catch (IOException ex) {
            LOG.log(Level.WARNING, "Could not execute URL. {0}", ex.getMessage());
            LOG.log(Level.FINEST, "", ex);
            return false;
        }
        return true;
    }
}
