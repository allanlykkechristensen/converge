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
package dk.i2m.converge.ejb.entities.workflow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;


/**
 * Describes the current workflow and state of an entity. Entities using this
 * class will have workflow features
 *
 * @author Allan Lykke Christensen
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Workflowable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "workflow_state_id")
    private WorkflowState currentState;

    @OneToMany(mappedBy = "subject", orphanRemoval=true, cascade= CascadeType.ALL)
    private List<WorkflowStateTransition> history = new ArrayList<WorkflowStateTransition>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WorkflowState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(WorkflowState currentState) {
        this.currentState = currentState;
    }

    public List<WorkflowStateTransition> getHistory() {
        return history;
    }

    public void setHistory(List<WorkflowStateTransition> history) {
        this.history = history;
    }
}
