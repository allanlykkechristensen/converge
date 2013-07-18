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
 * Definition of a workflow containing states and options available in the
 * workflow.
 *
 * @author Allan Lykke Christensen
 */
@Entity
@Table(name = "workflow_definition")
public class WorkflowDefinition implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToMany(mappedBy = "workflowDefinition", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<WorkflowState> states = new ArrayList<WorkflowState>();

    @ManyToOne
    @JoinColumn(name = "state_start")
    private WorkflowState start;

    @OneToMany
    @JoinTable(name = "workflow_end_state", joinColumns =
    @JoinColumn(name = "workflow_definition_id"), inverseJoinColumns =
    @JoinColumn(name = "workflow_state_id"))
    private List<WorkflowState> endStates = new ArrayList<WorkflowState>();

    @ManyToOne
    @JoinColumn(name = "state_trash")
    private WorkflowState trash;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    @Lob
    private String description;

    public WorkflowDefinition() {
        this.name = "";
        this.description = "";
        this.start = null;
        this.trash = null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public WorkflowState getStart() {
        return start;
    }

    public void setStart(WorkflowState start) {
        this.start = start;
    }

    public WorkflowState getTrash() {
        return trash;
    }

    public void setTrash(WorkflowState trash) {
        this.trash = trash;
    }

    public List<WorkflowState> getStates() {
        return states;
    }

    public void setStates(List<WorkflowState> states) {
        this.states = states;
    }

    /**
     * Gets a {@link List} of {@link WorkflowState}s that represent end states
     * of the {@link WorkflowDefinition}.
     * 
     * @return {@link List} of end {@link WorkflowState}s
     */
    public List<WorkflowState> getEndStates() {
        return endStates;
    }

    /**
     * Sets a {@link List} of {@link WorkflowState}s that represent end states
     * of the {@link WorkflowDefinition}.
     * 
     * @param endStates
     *          {@link List} of end {@link WorkflowState}s
     */
    public void setEndStates(List<WorkflowState> endStates) {
        this.endStates = endStates;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof WorkflowDefinition)) {
            return false;
        }
        WorkflowDefinition other = (WorkflowDefinition) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dk.i2m.converge.ejb.entities.workflow.WorkflowDefinition[ id=" + id + " ]";
    }
}
