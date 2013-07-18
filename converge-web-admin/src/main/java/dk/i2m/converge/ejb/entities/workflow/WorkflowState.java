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

import dk.i2m.converge.ejb.entities.security.UserRole;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

/**
 * Workflow state that is part of a {@linkplain WorkflowDefinition}.
 *
 * @author Allan Lykke Christensen
 */
@Entity
@Table(name = "workflow_state")
public class WorkflowState implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "description")
    @Lob
    private String description;

    /** {@link UserRole} allows to access the {@link Workflowable} object in this state. */
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_role_id")
    private UserRole actorRole;

    @Enumerated(EnumType.STRING)
    @Column(name = "permission")
    private WorkflowStatePermission permission = WorkflowStatePermission.GROUP;

    @OneToMany(mappedBy = "fromWorkflowState")
    @OrderBy(value = "displayOrder ASC")
    private List<WorkflowStateOption> options = new ArrayList<WorkflowStateOption>();

    @ManyToOne
    @JoinColumn(name = "default_option")
    private WorkflowStateOption defaultOption;

    @ManyToOne
    @JoinColumn(name = "workflow_definition_id")
    private WorkflowDefinition workflowDefinition;

    @Column(name = "pullback_enabled")
    private boolean pullbackEnabled = false;

    public WorkflowState() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<WorkflowStateOption> getOptions() {
        return options;
    }

    public void setOptions(List<WorkflowStateOption> options) {
        this.options = options;
    }

    /**
     * Gets the default {@link WorkflowStateOption} for this particular state.
     * The default state indicates the most likely selection by the user at
     * this particular state.
     * 
     * @return Most likely selection of the user at this {@link WorkflowState}
     */
    public WorkflowStateOption getDefaultOption() {
        return defaultOption;
    }

    /**
     * Sets the default {@link WorkflowStateOption} for this particular state.
     * The default state indicates the most likely selection by the user at
     * this particular state.
     * 
     * @param defaultOption
     *          Most likely selection of the user at this {@link WorkflowState}
     */
    public void setDefaultOption(WorkflowStateOption defaultOption) {
        this.defaultOption = defaultOption;
    }

    public WorkflowDefinition getWorkflowDefinition() {
        return workflowDefinition;
    }

    public void setWorkflowDefinition(WorkflowDefinition workflowDefinition) {
        this.workflowDefinition = workflowDefinition;
    }

    public UserRole getActorRole() {
        return actorRole;
    }

    public void setActorRole(UserRole actorRole) {
        this.actorRole = actorRole;
    }

    public WorkflowStatePermission getPermission() {
        return permission;
    }

    public void setPermission(WorkflowStatePermission permission) {
        this.permission = permission;
    }

    public boolean isPullbackEnabled() {
        return pullbackEnabled;
    }

    public void setPullbackEnabled(boolean pullbackEnabled) {
        this.pullbackEnabled = pullbackEnabled;
    }

    public int getNumberOfOptions() {
        return getOptions().size();
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof WorkflowState)) {
            return false;
        }
        WorkflowState other = (WorkflowState) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getClass() + "[id=" + id + "]";
    }
}
