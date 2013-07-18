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
package dk.i2m.converge.ejb.entities.forms;

import dk.i2m.converge.ejb.entities.security.UserAccount;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

/**
 * Form definition that can be used to fill in and take part of a workflow.
 *
 * @author Allan Lykke Christensen
 */
@Entity
@Table(name = "form_definition")
public class FormDefinition implements Serializable {

    
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
    @Column(name="created")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date created;
    @Column(name="updated")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date updated;
    @ManyToOne
    @JoinColumn(name = "created_by")
    private UserAccount createdBy;
    @ManyToOne
    @JoinColumn(name = "updated_by")
    private UserAccount updatedBy;
    @Column(name = "enabled")
    private boolean enabled;
    @OneToMany(mappedBy = "form")
    private List<FormField> fields = new ArrayList<FormField>();

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

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public UserAccount getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserAccount createdBy) {
        this.createdBy = createdBy;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public UserAccount getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(UserAccount updatedBy) {
        this.updatedBy = updatedBy;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<FormField> getFields() {
        return fields;
    }

    public void setFields(List<FormField> fields) {
        this.fields = fields;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof FormDefinition)) {
            return false;
        }
        FormDefinition other = (FormDefinition) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dk.i2m.converge.ejb.entities.forms.Form[ id=" + id + " ]";
    }
}
