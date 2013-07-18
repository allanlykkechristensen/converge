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
package dk.i2m.converge.ejb.entities.crm;

/**
 * Type of {@link Activity}.
 *
 * @author Allan Lykke Christensen
 */
public enum ActivityType {

    /** Virtual meeting such as Skype or WebEx. */
    VIRTUAL_MEETING,
    /** Physical meeting held face-to-face .*/
    PHYSICAL_METTING,
    /** E-mail between one or many parties. */
    EMAIL,
    /** Phone call between one or many parties. */
    PHONE_CALL,
    /** A note or thought. */
    NOTE

}
