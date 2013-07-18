/*
 *  Copyright (C) 2010 - 2012 Interactive Media Management
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package dk.i2m.converge.ejb.facades;

import dk.i2m.converge.ejb.entities.Outlet;
import dk.i2m.converge.ejb.services.DataNotFoundException;
import java.util.List;
import javax.ejb.Local;

/**
 * Local interface for the stateless {@link OutletFacade} session bean.
 *
 * @author Allan Lykke Christensen
 */
@Local
public interface OutletFacadeLocal {

    void create(Outlet outlet);

    void update(Outlet outlet);

    void remove(Outlet outlet);

    /**
     * Finds an {@link Outlet} with the given unique identifier.
     * 
     * @param id
     *          Unique identifier of the {@link Outlet}
     * @return {@link Outlet} matching the given {@code id}
     * @throws DataNotFoundException 
     *          If an {@link Outlet} could not be found with the given
     *          {@code id}
     */
    Outlet find(Object id) throws DataNotFoundException;

    List<Outlet> findAll();
    
    /**
     * Finds a {@link List} of {@link Outlet}s by their status.
     * 
     * @param status
     *          {@code true} to find active {@link Outlet}s otherwise
     *          {@code false}
     * @return {@link List} of {@link Outlet}s with the gievn status
     */
    List<Outlet> findByStatus(boolean status);

    List<Outlet> findRange(int[] range);

    int count();
    
}
