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
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * {@link OutletFacade} is responsible for exposing {@link Outlet} operations
 * for clients.
 *
 * @author Allan Lykke Christensen
 */
@Stateless
public class OutletFacade extends AbstractEntityFacade<Outlet> implements OutletFacadeLocal {

    @PersistenceContext(unitName = "convergePlatformPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public OutletFacade() {
        super(Outlet.class);
    }

    /** {@inheritDoc } */
    @Override
    public List<Outlet> findByStatus(boolean status) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Outlet> cq = cb.createQuery(Outlet.class);
        Root<Outlet> o = cq.from(Outlet.class);

        Predicate condition = cb.equal(o.get("active"), status);
        cq.where(condition);

        return getEntityManager().createQuery(cq).getResultList();
    }
}
