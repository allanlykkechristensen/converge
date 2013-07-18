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
import dk.i2m.converge.ejb.entities.workflow.WorkflowDefinition;
import dk.i2m.converge.ejb.facades.WorkflowFacadeLocal;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

/**
 *
 * @author Allan Lykke Christensen
 */
@ManagedBean
@ViewScoped
public class Workflows implements Serializable {

    @EJB
    private WorkflowFacadeLocal workflowFacade;
    
    private DataModel<WorkflowDefinition> workflows = null;

    /** Creates a new instance of Workflows. */
    public Workflows() {
    }
    
    
    // --- Properties ---------------------------------------------------------
    /**
     * Gets the {@link DataModel} of {@link Outlet}s to display.
     * 
     * @return {@link DataModel} of {@link Outlet}s to display
     */
    public DataModel<WorkflowDefinition> getWorkflows() {
        if (this.workflows == null) {
            List<WorkflowDefinition> definitions = workflowFacade.findWorkflowDefinitions();
            this.workflows = new ListDataModel<WorkflowDefinition>(definitions);
        }
        return this.workflows;
    }
}
