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

import dk.i2m.converge.ejb.entities.workflow.WorkflowDefinition;
import dk.i2m.converge.ejb.entities.workflow.WorkflowState;
import dk.i2m.converge.ejb.facades.WorkflowFacadeLocal;
import dk.i2m.converge.ejb.services.DataNotFoundException;
import dk.i2m.converge.faces.JsfUtils;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Allan Lykke Christensen
 */
@ManagedBean
@ViewScoped
public class WorkflowDefinitionDetails implements Serializable {

    private static final Logger LOG = Logger.getLogger(WorkflowDefinitionDetails.class.getName());

    @EJB
    private WorkflowFacadeLocal workflowFacade;

    private Long id;

    private WorkflowDefinition workflowDefinition;

    private WorkflowState workflowState;

    private Map<String, WorkflowState> workflowStates = new LinkedHashMap<String, WorkflowState>();

    /** 
     * Creates a new instance of WorkflowDefinitionDetails.
     */
    public WorkflowDefinitionDetails() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.log(Level.FINEST, "Setting the ID of the WorkflowDefinition: {0}", id);
        }
        this.id = id;
        if (this.id == null) {
            this.workflowDefinition = new WorkflowDefinition();
        } else {
            loadWorkflowDefinition(this.id);
        }
    }

    public WorkflowDefinition getWorkflowDefinition() {
        if (workflowDefinition == null) {
            this.workflowDefinition = new WorkflowDefinition();
        }
        return workflowDefinition;
    }

    public void setWorkflowDefinition(WorkflowDefinition workflowDefinition) {
        this.workflowDefinition = workflowDefinition;
    }

    public WorkflowState getWorkflowState() {
        return workflowState;
    }

    public void setWorkflowState(WorkflowState workflowState) {
        this.workflowState = workflowState;
    }

    public void onSaveWorkflowState() {
    }

    /**
     * Determine if a new {@link WorkflowDefinition} is being created.
     * 
     * @return {@code true} if a new {@link WorkflowDefinition} is being
     *         created, otherwise {@code false}
     */
    public boolean isNew() {
        if (this.id == null) {
            return true;
        } else {
            return false;
        }
    }

    private void loadWorkflowDefinition(Long id) {
        try {
            this.workflowDefinition = workflowFacade.findWorkflowDefinitionById(this.id);
        } catch (DataNotFoundException ex) {
            if (LOG.isLoggable(Level.WARNING)) {
                LOG.log(Level.WARNING, "Outlet could not be opened for editing", ex);
            }
        }
    }

    /**
     * Event handler for saving the {@link Outlet} details.
     */
    public String onSave() {
        if (isNew()) {
            workflowFacade.create(getWorkflowDefinition());
            if (LOG.isLoggable(Level.FINEST)) {
                LOG.log(Level.FINEST, "Workflow Definition {0} created with ID {1}",
                        new Object[]{getWorkflowDefinition().getName(), getWorkflowDefinition().getId()});
            }
            JsfUtils.createFacesMessage(FacesMessage.SEVERITY_INFO, "msgs",
                    "WorkflowDefinitionDetails_WORKFLOW_X_CREATED",
                    new Object[]{getWorkflowDefinition().getName()});
        } else {
            workflowFacade.update(this.workflowDefinition);
            JsfUtils.createFacesMessage(FacesMessage.SEVERITY_INFO, "msgs",
                    "WorkflowDefinitionDetails_WORKFLOW_X_SAVED",
                    new Object[]{getWorkflowDefinition().getName()});
        }

        return "Workflows?faces-redirect=true";
    }
    
    public void onNewWorkflowState() {
        this.workflowState = new WorkflowState();
        this.workflowState.setWorkflowDefinition(this.workflowDefinition);
        this.workflowDefinition.getStates().add(this.workflowState);
    }
    
    public void onDeleteWorkflowState(WorkflowState workflowState) {
        workflowFacade.deleteWorkflowStateById(id);
    }

    public Map<String, WorkflowState> getWorkflowStates() {
        this.workflowStates.clear();
        for (WorkflowState ws : getWorkflowDefinition().getStates()) {
            this.workflowStates.put(ws.getName(), ws);
        }
        return workflowStates;
    }
}
