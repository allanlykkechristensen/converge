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
package com.getconverge.sms;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsmpp.InvalidResponseException;
import org.jsmpp.PDUException;
import org.jsmpp.bean.*;
import org.jsmpp.bean.OptionalParameter.Tag;
import org.jsmpp.extra.NegativeResponseException;
import org.jsmpp.extra.ProcessRequestException;
import org.jsmpp.extra.ResponseTimeoutException;
import org.jsmpp.session.*;
import org.jsmpp.util.AbsoluteTimeFormatter;
import org.jsmpp.util.InvalidDeliveryReceiptException;
import org.jsmpp.util.TimeFormatter;

/**
 * {@link SmppDispatcher} is an SMPP convenience class for sending SMSes. To
 * send an SMS follow these steps:
 * <ol>
 * <li>create an instance of the {@link SmppDispatcher} with the the settings of
 * the SMPP service.</li>
 * <li>Call {@link SmppDispatcher#connect()} to connect to the SMPP service</li>
 * <li>Send the SMS message using the
 * {@link SmppDispatcher#dispatch(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}
 * method</li>
 * <li>Call {@link SmppDispatcher#disconnect()} to discount from the SMPP
 * service</li>
 * </ol>
 *
 * @author Allan Lykke Christensen
 */
public class SmppDispatcher {

    private static final Logger LOG = Logger.getLogger(SmppDispatcher.class.getName());
    private static TimeFormatter timeFormatter = new AbsoluteTimeFormatter();
    private String smppHost;
    private int smppPort;
    private String user;
    private String password;
    private String systemType;
    private String shortCode;
    private SMPPSession session;

    /**
     * Create an {@link SmppDispatcher}.
     *
     * @param smppHost Host name or IP of the SMPP service
     * @param smppPort Port of the SMPP service
     * @param user Username for accessing the SMPP service
     * @param password Password for accessing the SMPP service
     * @param systemType Type of SMPP service
     * @param shortCode Short code sender, either a number or name, must be
     * recognised by the SMPP service
     */
    public SmppDispatcher(final String smppHost, final int smppPort,
            final String user, final String password, final String systemType,
            final String shortCode) {
        this.smppHost = smppHost;
        this.smppPort = smppPort;
        this.user = user;
        this.password = password;
        this.systemType = systemType;
        this.shortCode = shortCode;
    }

    /**
     * Connects to the SMPP service. Enable {@code WARNING} logging to
     * troubleshoot connections.
     *
     * @return {@code true} if the connection was successful, otherwise
     * {@code false}.
     */
    public boolean connect() {
        this.session = new SMPPSession();
        this.session.setMessageReceiverListener(new MessageReceiverListenerImpl());
        try {
            this.session.connectAndBind(
                    this.smppHost, this.smppPort, new BindParameter(BindType.BIND_TRX,
                    this.user, this.password, this.systemType, TypeOfNumber.UNKNOWN,
                    NumberingPlanIndicator.UNKNOWN, null));
            return true;
        } catch (IOException e) {
            LOG.log(Level.WARNING, e.getMessage(), e);
            return false;
        }
    }

    /**
     * Disconnects from the SMPP service.
     */
    public void disconnect() {
        if (this.session != null) {
            this.session.unbindAndClose();
        }
    }

    /**
     * Dispatch an SMS message. A connection must exist to the SMPP service for
     * a message to be sent. Enable WARNING logging to troubleshoot.
     *
     * @param message Message to send
     * @param recipient Phone number of the recipient (e.g. +4560812405)
     * @param serviceType Type of service
     * @param destinationPort Destination port
     * @return Unique identifier of the delivered message
     */
    public String dispatch(String message, String recipient, String serviceType, String destinationPort) {
        if (this.session == null) {
            return null;
        }

        String messageId = null;
        try {
            org.jsmpp.bean.OptionalParameter.Short s = new org.jsmpp.bean.OptionalParameter.Short(Tag.DESTINATION_PORT, Short.parseShort(destinationPort));

            messageId = session.submitShortMessage(
                    serviceType,
                    TypeOfNumber.NATIONAL, NumberingPlanIndicator.UNKNOWN, shortCode,
                    TypeOfNumber.INTERNATIONAL, NumberingPlanIndicator.UNKNOWN, recipient,
                    new ESMClass(), (byte) 0, (byte) 1, timeFormatter.format(new Date()), null,
                    new RegisteredDelivery(SMSCDeliveryReceipt.SUCCESS_FAILURE), (byte) 0, new GeneralDataCoding(Alphabet.ALPHA_DEFAULT, MessageClass.CLASS1, false),
                    (byte) 0, message.getBytes(), s);
        } catch (PDUException e) {
            // Invalid PDU parameter
            LOG.log(Level.WARNING, e.getMessage(), e);
        } catch (ResponseTimeoutException e) {
            // Response timeout
            LOG.log(Level.WARNING, e.getMessage(), e);
        } catch (InvalidResponseException e) {
            // Invalid response
            LOG.log(Level.WARNING, e.getMessage(), e);
        } catch (NegativeResponseException e) {
            // Receiving negative response (non-zero command_status)
            LOG.log(Level.WARNING, e.getMessage(), e);
        } catch (IOException e) {
            LOG.log(Level.WARNING, e.getMessage(), e);
        }

        return messageId;
    }

    /**
     * Listener for received messages during the connection.
     */
    private static class MessageReceiverListenerImpl implements MessageReceiverListener {

        @Override
        public void onAcceptDeliverSm(DeliverSm deliverSm)
                throws ProcessRequestException {

            if (MessageType.SMSC_DEL_RECEIPT.containedIn(deliverSm.getEsmClass())) {
                // this message is delivery receipt
                try {
                    DeliveryReceipt receipt = deliverSm.getShortMessageAsDeliveryReceipt();

                    String messageId = receipt.getId();
                    LOG.log(Level.FINE, "Receiving delivery receipt for "
                            + "message ''{0} '' from {1} to {2} : {3}",
                            new Object[]{messageId, deliverSm.getSourceAddr(),
                        deliverSm.getDestAddress(), receipt});
                } catch (InvalidDeliveryReceiptException e) {
                    LOG.log(Level.WARNING, e.getMessage(), e);
                }
            } else {
                // this message is regular short message
                LOG.log(Level.FINE, "Receiving message : {0}", new String(deliverSm.getShortMessage()));
            }
        }

        @Override
        public void onAcceptAlertNotification(AlertNotification alertNotification) {
        }

        @Override
        public DataSmResult onAcceptDataSm(DataSm dataSm, Session source) throws ProcessRequestException {
            return null;
        }
    }
}
