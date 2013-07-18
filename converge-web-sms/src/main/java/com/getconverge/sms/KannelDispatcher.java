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
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 * {@link KannelDispatcher} is a convenience class for sending SMSes via a
 * <a href="http://www.kannel.org/">Kannel</a> server.
 *
 * @author Allan Lykke Christensen
 */
public class KannelDispatcher {

    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 13013;
    private static final int DEFAULT_TIMEOUT = 30000;
    protected static final String SEND_SMS_PATH = "/cgi-bin/sendsms";
    private int timeout;
    private String host;
    private int port;
    private boolean secure = false;
    private String user;
    private String password;

    /**
     * Creates a new instance of {@link KannelDispatcher}.
     */
    public KannelDispatcher() {
        this(DEFAULT_HOST, DEFAULT_PORT, DEFAULT_TIMEOUT, "", "");
    }

    /**
     * Creates a new instance of {@link KannelDispatcher}.
     *
     * @param host Hostname or IP address of the Kannel sendsms interface
     * @param user Username for connecting to the sendsms interface
     * @param password Password for connecting to the sendsms interface
     */
    public KannelDispatcher(String host, String user, String password) {
        this.host = host;
        this.user = user;
        this.password = password;
    }

    /**
     * Creates a new instance of {@link KannelDispatcher}.
     *
     * @param host Hostname or IP address of the Kannel sendsms interface
     * @param port Port number of the Kannel sendsms interface
     * @param user Username for connecting to the sendsms interface
     * @param password Password for connecting to the sendsms interface
     */
    public KannelDispatcher(String host, int port, String user, String password) {
        this(host, port, DEFAULT_TIMEOUT, user, password);
    }

    /**
     * Creates a new instance of {@link KannelDispatcher}.
     *
     * @param host Hostname or IP address of the Kannel sendsms interface
     * @param port Port number of the Kannel sendsms interface
     * @param timeout Number of ms to wait on the Kannel service before timing
     * out
     * @param user Username for connecting to the sendsms interface
     * @param password Password for connecting to the sendsms interface
     */
    public KannelDispatcher(String host, int port, int timeout, String user, String password) {
        this.timeout = timeout;
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
    }

    /**
     * Dispatch a message to the given recipient.
     *
     * @param recipient Recipient of the message (include country code, e.g.
     * +45)
     * @param message Message to send to the recipient
     * @throws IOException If the Kannel server could not be contacted or the
     * message could not be sent
     */
    public void dispatch(String recipient, String message) throws IOException {
        HttpClient client = new HttpClient();

        // Set the time out
        HttpConnectionManager conMgr = client.getHttpConnectionManager();
        conMgr.getParams().setConnectionTimeout(timeout);

        // Define the URL to call
        String protocol = "http";
        if (secure) {
            protocol = "https";
        }

        String getUrl = protocol + "://" + host + ":" + port;
        
        HttpMethod method = new GetMethod(getUrl);
        method.setPath(SEND_SMS_PATH);
        
        NameValuePair[] queryString = new NameValuePair[]{
            new NameValuePair("username", user),
            new NameValuePair("password", password),
            new NameValuePair("to", recipient),
            new NameValuePair("text", message)};

        method.setQueryString(queryString);
        method.setFollowRedirects(true);

        IOException failedException = null;
        try {
            int responseCode = client.executeMethod(method);

            if (responseCode != HttpStatus.SC_OK) {
                failedException = new IOException("Unexpected response code from [" + method.getURI() + "] [" + responseCode + "]");
            }
        } catch (IOException ex) {
            failedException = ex;
        } finally {
            method.releaseConnection();
        }

        if (failedException != null) {
            throw failedException;
        }
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
