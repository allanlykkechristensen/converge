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
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * SOAP web service for dispatching SMSes.
 *
 * @author Allan Lykke Christensen
 */
@WebService(serviceName = "SmppService")
public class SmppService {

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
        smppDispatcher = new SmppDispatcher(smppHost, smppPort, user, password, systemType, shortCode);
        smppDispatcher.connect();
        String id = smppDispatcher.dispatch(message, recipient, serviceType, destinationPort);
        smppDispatcher.disconnect();
        return id;
    }
}
