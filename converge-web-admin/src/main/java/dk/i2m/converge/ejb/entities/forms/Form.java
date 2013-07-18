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
 *
 * @author Allan Lykke Christensen
 */
@Entity
@Table(name = "form_entry")
public class Form implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "form_definition_id")
    private FormDefinition formDefinition;
    @ManyToOne
    @JoinColumn(name = "submitter_id")
    private UserAccount submitter;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "updated")
    private Date updated;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "created")
    private Date created;
    @OneToMany(mappedBy = "form")
    private List<FormFieldEntry> entries = new ArrayList<FormFieldEntry>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public List<FormFieldEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<FormFieldEntry> entries) {
        this.entries = entries;
    }

    public FormDefinition getFormDefinition() {
        return formDefinition;
    }

    public void setFormDefinition(FormDefinition formDefinition) {
        this.formDefinition = formDefinition;
    }

    public UserAccount getSubmitter() {
        return submitter;
    }

    public void setSubmitter(UserAccount submitter) {
        this.submitter = submitter;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Form)) {
            return false;
        }
        Form other = (Form) object;
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
