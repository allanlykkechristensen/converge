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

import dk.i2m.converge.ejb.entities.workflow.WorkflowDefinition;
import dk.i2m.converge.ejb.entities.workflow.WorkflowState;
import dk.i2m.converge.ejb.entities.workflow.WorkflowStateOption;
import dk.i2m.converge.ejb.entities.workflow.Workflowable;
import dk.i2m.converge.ejb.services.DataNotFoundException;
import java.util.List;
import javax.ejb.Local;

/**
 * Local interface for the {@link WorkflowFacade} enterprise bean.
 *
 * @author Allan Lykke Christensen
 */
@Local
public interface WorkflowFacadeLocal {

    void step(Workflowable item, WorkflowStateOption option) throws WorkflowTransitionException;

    List<WorkflowDefinition> findWorkflowDefinitions();

    WorkflowDefinition findWorkflowDefinitionById(Long id) throws DataNotFoundException;

    void create(WorkflowDefinition workflowDefinition);

    void update(WorkflowDefinition workflowDefinition);

    WorkflowState findWorkflowStateById(Long workflowStateId) throws DataNotFoundException;
    
    /**
     * Finds a {@link WorkflowStateOption} in the database.
     * 
     * @param workflowStateOptionId
     *          Unique identifier of the {@link WorkflowStateOption}
     * @return {@link WorkflowStateOption} matching the {@code workflowStateOptionId}
     * @throws DataNotFoundException 
     *          If a {@link WorkflowStateOption} could not be found matching {@code workflowStateOptionId}
     */
    WorkflowStateOption findWorkflowStateOptionById(Long workflowStateOptionId) throws DataNotFoundException;
    
    void deleteWorkflowStateById(Long workflowStateId);
    
    void deleteWorkflowStateOptionById(Long workflowStateOptionId);
}
