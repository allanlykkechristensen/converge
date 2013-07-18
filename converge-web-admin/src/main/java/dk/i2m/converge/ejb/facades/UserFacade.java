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
import dk.i2m.converge.ejb.services.*;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * Implementation of the {@link UserFacade} stateless session bean.
 *
 * @author Allan Lykke Christensen
 */
@Stateless
public class UserFacade implements UserFacadeLocal {

    @EJB
    private DaoServiceLocal daoService;

    @EJB
    private DirectoryServiceLocal directoryService;

    /** {@inheritDoc } */
    @Override
    public UserAccount findUserAccountByUsername(String username) throws DirectoryException, UserNotFoundException {
        return directoryService.findById(username);
    }

    /** {@inheritDoc } */
    @Override
    public List<UserRole> findUserRoles() {
        return daoService.findAll(UserRole.class);
    }

    /** {@inheritDoc } */
    @Override
    public UserRole findUserRoleById(Long id) throws DataNotFoundException {
        return daoService.findById(UserRole.class, id);
    }
}
