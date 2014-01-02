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
package com.getconverge.converge.entities.security;

import java.util.Locale;
import java.util.TimeZone;
import org.junit.Test;
import static org.junit.Assert.*;

public class UserAccountTest {

    @Test
    public void userAccount_getTimeZoneWithValidTimeZoneString_returnTimeZone() {
        // Arrange
        String userTimeZone = "cet";
        UserAccount user = new UserAccount();
        user.setTimeZoneAsString(userTimeZone);
        TimeZone expectedTimeZone = TimeZone.getTimeZone(userTimeZone);

        // Act
        TimeZone timeZone = user.getTimeZone();

        // Assert
        assertEquals(expectedTimeZone, timeZone);
    }

    @Test
    public void userAccount_getTimeZoneWithNullTimeZoneString_returnDefaultTimeZone() {
        // Arrange
        String userTimeZone = null;
        UserAccount user = new UserAccount();
        user.setTimeZoneAsString(userTimeZone);
        TimeZone expectedTimeZone = TimeZone.getDefault();

        // Act
        TimeZone timeZone = user.getTimeZone();

        // Assert
        assertEquals(expectedTimeZone, timeZone);
    }

    @Test
    public void userAccount_getPreferredLocaleWithLanguageSet_returnLocaleForLanguage() {
        // Arrange
        String language = "en";
        UserAccount user = new UserAccount();
        user.setLanguage(language);
        Locale expectedLocale = new Locale("en");

        // Act
        Locale locale = user.getPreferredLocale();

        // Assert
        assertEquals(expectedLocale, locale);
    }

    @Test
    public void userAccount_getPreferredLocaleWithLanguageAndCountrySet_returnLocaleForLanguageAndCountry() {
        // Arrange
        String language = "en_us";
        UserAccount user = new UserAccount();
        user.setLanguage(language);
        Locale expectedLocale = new Locale("en", "us");

        // Act
        Locale locale = user.getPreferredLocale();

        // Assert
        assertEquals(expectedLocale, locale);
    }

    @Test
    public void userAccount_isRightToLeftWithEnglishLanguageSet_returnFalse() {
        // Arrange
        String language = "en";
        UserAccount user = new UserAccount();
        user.setLanguage(language);
        boolean expectedResult = false;

        // Act
        boolean result = user.isRightToLeft();

        // Assert
        assertEquals(expectedResult, result);
    }

    @Test
    public void userAccount_isRightToLeftWithArabicLanguageSet_returnTrue() {
        // Arrange
        String language = "ar";
        UserAccount user = new UserAccount();
        user.setLanguage(language);
        boolean expectedResult = true;

        // Act
        boolean result = user.isRightToLeft();

        // Assert
        assertEquals(expectedResult, result);
    }

    @Test
    public void userAccount_userAccountWithIdAndUsername_toStringIsUniform() {
        // Arrange
        UserAccount user = new UserAccount();
        user.setId(12L);
        user.setUsername("allan");

        String expectedResult = user.getClass().getName() + "[id=12/username=allan]";

        // Act
        String result = user.toString();

        // Assert
        assertEquals(expectedResult, result);
    }
}
