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
package dk.i2m.converge.admin.jsf.beans;

import dk.i2m.converge.ejb.entities.Outlet;
import dk.i2m.converge.ejb.facades.OutletFacadeLocal;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

/**
 * Backing bean for {@link /Outlets.xhtml}.
 *
 * @author Allan Lykke Christensen
 */
@ManagedBean
@ViewScoped
public class Outlets implements Serializable {

    /** Interface to the OutletFacade EJB. */
    @EJB
    private OutletFacadeLocal outletFacade;

    /** DataModel containing the outlets to show. */
    private DataModel<Outlet> outlets;

    /** 
     * Creates a new instance of {@link Outlets}.
     */
    public Outlets() {
    }

    // --- Properties ---------------------------------------------------------
    /**
     * Gets the {@link DataModel} of {@link Outlet}s to display.
     * 
     * @return {@link DataModel} of {@link Outlet}s to display
     */
    public DataModel<Outlet> getOutlets() {
        if (outlets == null) {
            onShowActiveOutlets();
        }
        return outlets;
    }

    // --- Event handlers -----------------------------------------------------
    /**
     * Event handler for showing active {@link Outlet}s. Upon invoking the 
     * quotes {@link DataModel} will contain only active {@link Outlet}s.
     */
    public void onShowActiveOutlets() {
        List<Outlet> activeOutlets = outletFacade.findByStatus(true);
        this.outlets = new ListDataModel<Outlet>(activeOutlets);
    }

    /**
     * Event handler for showing inactive {@link Outlet}s. Upon invoking the 
     * quotes {@link DataModel} will contain only inactive {@link Outlet}s.
     */
    public void onShowInactiveOutlets() {
        List<Outlet> inactiveOutlets = outletFacade.findByStatus(false);
        this.outlets = new ListDataModel<Outlet>(inactiveOutlets);
    }
}
