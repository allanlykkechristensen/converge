/*
 * Copyright (C) 2012 Allan Lykke Christensen
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
import javax.persistence.*;

/**
 *
 * @author Allan Lykke Christensen
 */
@Entity
@Table(name = "workflow_state_option")
public class WorkflowStateOption implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    @Lob
    private String description;

    @ManyToOne
    @JoinColumn(name = "from_workflow_state")
    private WorkflowState fromWorkflowState;

    @ManyToOne
    @JoinColumn(name = "to_workflow_state")
    private WorkflowState toWorkflowState;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "display_order")
    private Integer displayOrder;

    public WorkflowStateOption() {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public WorkflowState getFromWorkflowState() {
        return fromWorkflowState;
    }

    public void setFromWorkflowState(WorkflowState fromWorkflowState) {
        this.fromWorkflowState = fromWorkflowState;
    }

    public WorkflowState getToWorkflowState() {
        return toWorkflowState;
    }

    public void setToWorkflowState(WorkflowState toWorkflowState) {
        this.toWorkflowState = toWorkflowState;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WorkflowStateOption)) {
            return false;
        }
        WorkflowStateOption other = (WorkflowStateOption) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dk.i2m.converge.ejb.entities.workflow.WorkflowStateOption[ id=" + id + " ]";
    }
}
