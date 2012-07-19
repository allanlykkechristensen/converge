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
package dk.i2m.converge.sms.services;

import dk.i2m.converge.sms.SmppDispatcher;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 * SOAP web service for dispatching SMSes.
 *
 * @author Allan Lykke Christensen
 */
@WebService(serviceName = "SmppService")
public class SmppService {

    private static final Logger LOG = Logger.getLogger(SmppService.class.
            getName());

    private SmppDispatcher smppDispatcher;

    /**
     * Sends an SMS through the specified SMPP service.
     *
     * @param smppHost
     *          SMPP host or IP address
     * @param smppPort 
     *          SMPP port
     * @param user 
     *          SMPP username
     * @param password 
     *          SMPP password
     * @param systemType
     *          Type of SMPP service
     * @param shortCode 
     *          Sender of the SMS
     * @param message 
     *          Message to send
     * @param recipient 
     *          Recipient of the SMS
     * @param serviceType 
     *          Type of service used for sending the SMS
     * @param destinationPort 
     *          Port used to send the SMS
     * @return Unique identifier of the sent message or {@code null} if sending
     *         failed
     */
    @WebMethod(operationName = "send")
    public String send(
            @WebParam(name = "smppHost") String smppHost,
            @WebParam(name = "smppPort") int smppPort,
            @WebParam(name = "user") String user,
            @WebParam(name = "password") String password,
            @WebParam(name = "systemType") String systemType,
            @WebParam(name = "shortCode") String shortCode,
            @WebParam(name = "message") String message,
            @WebParam(name = "recipient") String recipient,
            @WebParam(name = "serviceType") String serviceType,
            @WebParam(name = "destinationPort") String destinationPort) {
        smppDispatcher = new SmppDispatcher(smppHost, smppPort, user, password,
                systemType, shortCode);
        smppDispatcher.connect();
        String id = smppDispatcher.dispatch(message, recipient, serviceType,
                destinationPort);
        smppDispatcher.disconnect();
        return id;
    }

    /**
     * Sends a message using a Kannel server.
     *
     * @param host
     *          Hostname or IP of server
     * @param port
     *          Port of server
     * @param user
     *          Username to use for sending
     * @param password
     *          Password to use for sending
     * @param recipient
     *          Receipient of the message (include country code, e.g. +45)
     * @param message
     *          Message to send
     * @param timeout
     *          Timeout in ms
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

        HttpClient client = new HttpClient();

        // Set the time out
        HttpConnectionManager conMgr = client.getHttpConnectionManager();
        conMgr.getParams().setConnectionTimeout(timeout);

        // Define the URL to call
        HttpMethod method = new GetMethod(host + ":" + port);
        method.setPath("/cgi-bin/sendsms");

        NameValuePair[] queryString = new NameValuePair[]{
            new NameValuePair("username", user),
            new NameValuePair("password", password),
            new NameValuePair("to", recipient),
            new NameValuePair("text", message)};

        method.setQueryString(queryString);
        method.setFollowRedirects(true);

        boolean failed = false;
        try {
            client.executeMethod(method);
        } catch (HttpException ex) {
            LOG.log(Level.WARNING, "Could not execute URL. " + ex.getMessage(),
                    ex);
            failed = true;
        } catch (IOException ex) {
            LOG.log(Level.WARNING, "Could not execute URL. " + ex.getMessage(),
                    ex);
        } finally {
            method.releaseConnection();
        }

        return !failed;
    }
}
