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

import org.junit.Test;
import static org.junit.Assert.*;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import java.io.IOException;
import org.apache.http.HttpStatus;
import org.junit.Rule;

public class KannelDispatcherTest {

    // Port used by the mock HTTP server
    private static final int TEST_PORT = 8089;
    @Rule
    public WireMockRule wireMockRule = new WireMockRule(TEST_PORT);

    @Test()
    public void kannelDispatcher_simulateIncorrectHost_exceptionThrown() {
        stubFor(get(urlEqualTo(KannelDispatcher.SEND_SMS_PATH + "?username=&password=&to=%2B4560812405&text=How+are+you%3F"))
                .willReturn(aResponse().withStatus(HttpStatus.SC_NOT_FOUND)));

        KannelDispatcher dispatcher = new KannelDispatcher("localhost", TEST_PORT, "", "");
        try {
            dispatcher.dispatch("+4560812405", "How are you?");
            fail("Dispatcher did not raise an IOException as expected");
        } catch (IOException ex) {
            // pass
        }
    }

    @Test()
    public void kannelDispatcher_simulateCorrectHost_OK() {
        stubFor(get(urlEqualTo(KannelDispatcher.SEND_SMS_PATH + "?username=&password=&to=%2B4560812405&text=How+are+you%3F"))
                .willReturn(aResponse().withStatus(HttpStatus.SC_OK)));

        KannelDispatcher dispatcher = new KannelDispatcher("localhost", TEST_PORT, "", "");
        try {
            dispatcher.dispatch("+4560812405", "How are you?");
        } catch (IOException ex) {
            fail(ex.getMessage());
        }
    }
}