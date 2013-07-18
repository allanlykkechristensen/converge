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
package dk.i2m.converge.ejb.facades;

import dk.i2m.converge.ejb.entities.security.UserAccount;
import dk.i2m.converge.ejb.entities.security.UserRole;
import dk.i2m.converge.ejb.services.DataNotFoundException;
import dk.i2m.converge.ejb.services.DirectoryException;
import dk.i2m.converge.ejb.services.UserNotFoundException;
import java.util.List;
import javax.ejb.Local;

/**
 * Local interface of the {@link UserFacade}.
 *
 * @author Allan Lykke Christensen
 */
@Local
public interface UserFacadeLocal {

    /**
     * Finds a {@link UserAccount} by its username.
     * 
     * @param username
     *          Username of the {@link UserAccount}
     * @return {@link UserAccount} matching the {@code username}
     * @throws UserNotFoundException
     *          If a {@link UserAccount} with the given {@code username} could 
     *          not be found in the database nor directory
     * @throws DirectoryException
     *          If the directory could not be reached
     */
    UserAccount findUserAccountByUsername(String username) throws UserNotFoundException, DirectoryException;

    /**
     * Finds all the available {@link UserRole}s.
     * 
     * @return {@link List} of available {@link UserRole}s
     */
    List<UserRole> findUserRoles();
    
    /**
     * Finds a {@link UserRole} by its unique identifier.
     * 
     * @param id
     *          Unique identifier of the {@link UserRole}
     * @return {@link UserRole} matching the given {@code id}
     * @throws DataNotFoundException 
     *          If a {@link UserRole} could not be found with the given
     *          {@code id}
     */
    UserRole findUserRoleById(Long id) throws DataNotFoundException;
}
