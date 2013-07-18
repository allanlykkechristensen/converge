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

import dk.i2m.converge.ejb.entities.workflow.*;
import dk.i2m.converge.ejb.services.DaoServiceLocal;
import dk.i2m.converge.ejb.services.DataNotFoundException;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * Facade for accessing the workflow engine.
 *
 * @author Allan Lykke Christensen
 */
@Stateless
public class WorkflowFacade implements WorkflowFacadeLocal {

    @EJB
    private DaoServiceLocal daoService;

    @Override
    public void step(Workflowable item, WorkflowStateOption option) throws WorkflowTransitionException {
        if (!item.getCurrentState().getOptions().contains(option) && item.getCurrentState() != null) {
            throw new WorkflowTransitionException(option.toString() + " is not a valid option for " + item.toString());
        }

        WorkflowStateTransition transition = new WorkflowStateTransition();
        transition.setSubject(item);
        transition.setTimestamp(Calendar.getInstance().getTime());
        transition.setOption(option);
        transition.setState(option.getToWorkflowState());
        daoService.create(transition);

        item.getHistory().add(transition);
        item.setCurrentState(option.getToWorkflowState());
        daoService.update(item);
    }

    @Override
    public List<WorkflowDefinition> findWorkflowDefinitions() {
        return daoService.findAll(WorkflowDefinition.class);
    }

    @Override
    public WorkflowDefinition findWorkflowDefinitionById(Long id) throws DataNotFoundException {
        return daoService.findById(WorkflowDefinition.class, id);
    }

    @Override
    public void create(WorkflowDefinition workflowDefinition) {
        daoService.create(workflowDefinition);
    }

    @Override
    public void update(WorkflowDefinition workflowDefinition) {
        daoService.update(workflowDefinition);
    }

    @Override
    public WorkflowState findWorkflowStateById(Long workflowStateId) throws DataNotFoundException {
        return daoService.findById(WorkflowState.class, workflowStateId);
    }

    /** {@inheritDoc} */
    @Override
    public WorkflowStateOption findWorkflowStateOptionById(Long workflowStateOptionId) throws DataNotFoundException {
        return daoService.findById(WorkflowStateOption.class, workflowStateOptionId);
    }

    @Override
    public void deleteWorkflowStateOptionById(Long workflowStateOptionId) {
        daoService.delete(WorkflowStateOption.class, workflowStateOptionId);
    }

    @Override
    public void deleteWorkflowStateById(Long workflowStateId) {
        daoService.delete(WorkflowState.class, workflowStateId);
    }
}
