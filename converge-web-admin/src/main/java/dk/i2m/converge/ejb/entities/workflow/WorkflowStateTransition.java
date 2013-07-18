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

import dk.i2m.converge.ejb.entities.security.UserAccount;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 * Transition from one workflow state to another.
 *
 * @author Allan Lykke Christensen
 */
@Entity
@Table(name = "workflow_state_transition")
public class WorkflowStateTransition implements Serializable {

    
    /**
     * Unique identifier of the transition.
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "transition_timestamp")
    private Date timestamp;

    @ManyToOne
    @JoinColumn(name = "user_account_id")
    private UserAccount user;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Workflowable subject;

    @ManyToOne
    @JoinColumn(name = "workflow_state_option_id")
    private WorkflowStateOption option;
    
    @ManyToOne
    @JoinColumn(name = "workflow_state_id")
    private WorkflowState state;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Workflowable getSubject() {
        return subject;
    }

    public void setSubject(Workflowable subject) {
        this.subject = subject;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public UserAccount getUser() {
        return user;
    }

    public void setUser(UserAccount user) {
        this.user = user;
    }

    public WorkflowStateOption getOption() {
        return option;
    }

    public void setOption(WorkflowStateOption option) {
        this.option = option;
    }

    public WorkflowState getState() {
        return state;
    }

    public void setState(WorkflowState state) {
        this.state = state;
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
        if (!(object instanceof WorkflowStateTransition)) {
            return false;
        }
        WorkflowStateTransition other = (WorkflowStateTransition) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dk.i2m.converge.ejb.entities.workflow.WorkflowStateTransition[ id=" + id + " ]";
    }
}
