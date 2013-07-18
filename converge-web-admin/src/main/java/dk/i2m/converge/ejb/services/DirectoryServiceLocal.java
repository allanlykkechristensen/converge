/*
 * Copyright (C) 2009 - 2012 Interactive Media Management
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
package dk.i2m.converge.ejb.services;

import dk.i2m.converge.ejb.entities.security.UserAccount;
import javax.ejb.Local;

/**
 * Local interface for {@link UserServiceBean}.
 *
 * @author Allan Lykke Christensen
 */
@Local
public interface DirectoryServiceLocal {

    /**
     * Finds a {@link UserAccount} in the database or directory. If the 
     * {@link UserAccount} is not found in the database, it will look up the 
     * {@link UserAccount} in the defined directory. If the user is found, a 
     * copy of the profile from the directory is used to create a new 
     * {@link UserAccount} in the database.
     * 
     * @param username
     *          Username of the {@link UserAccount}
     * @return {@link UserAccount} matching the given {@code username}
     * @throws UserNotFoundException
     *          If the {@link UserAccount} was not found in the database nor the
     *          directory
     * @throws DirectoryException 
     *          If the directory could not be reached
     */
    UserAccount findById(String username) throws UserNotFoundException, DirectoryException;

    /**
     * Synchronises a local {@link UserAccount} with a user from the configured
     * directory. Upon synchronisation the given {@link UserAccount} will be
     * updated to include the changes from the directory.
     * 
     * @param userAccount
     *          {@link UserAccount} to synchronise
     * @throws dk.i2m.converge.ejb.services.UserNotFoundException
     *          If a corresponding user does not exist in the director
     * @throws dk.i2m.converge.ejb.services.DirectoryException 
     *          If the directory could not be reached
     */
    void syncWithDirectory(dk.i2m.converge.ejb.entities.security.UserAccount userAccount) throws dk.i2m.converge.ejb.services.UserNotFoundException, dk.i2m.converge.ejb.services.DirectoryException;

    /**
     * Determines if a given user exists in the LDAP directory. The username
     * is located using the field denoting the username of the user 
     * ({@link LdapFieldMapping#USER_MAPPING_USERNAME}).
     *
     * @param uid
     *          Unique identifier of the user
     * @return {@code true} if the {@link UserAccount} exists in the LDAP
     *         directory, otherwise {@code false}
     */
    boolean exists(java.lang.String uid);
}
