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
package dk.i2m.converge.ejb.entities.crm;

import dk.i2m.converge.ejb.entities.security.UserAccount;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

/**
 * {@link Activity} that has taken places. An {@link Activity} typically relates
 * to one or more {@link Account}s and/or {@link Contact}s.
 *
 * @author Allan Lykke Christensen
 */
@Entity
@Table(name = "activity")
public class Activity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "subject")
    private String subject;

    @Column(name = "details")
    @Lob
    private String details;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "activity_date")
    private Date date;

    @Enumerated(EnumType.STRING)
    private ActivityType type = ActivityType.NOTE;

    @ManyToMany()
    @JoinTable(name = "activity_account", joinColumns =
    @JoinColumn(name = "activity_id"),
    inverseJoinColumns =
    @JoinColumn(name = "account_id"))
    private List<Account> accounts = new ArrayList<Account>();

    @ManyToMany()
    @JoinTable(name = "activity_contact", joinColumns =
    @JoinColumn(name = "activity_id"),
    inverseJoinColumns =
    @JoinColumn(name = "contact_id"))
    private List<Contact> contact = new ArrayList<Contact>();

    @ManyToMany()
    @JoinTable(name = "activity_project", joinColumns =
    @JoinColumn(name = "activity_id"),
    inverseJoinColumns =
    @JoinColumn(name = "project_id"))
    private List<Project> projects = new ArrayList<Project>();

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "created")
    private Date created;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "updated")
    private Date updated;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private UserAccount createdBy;

    public Activity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public List<Contact> getContact() {
        return contact;
    }

    public void setContact(List<Contact> contact) {
        this.contact = contact;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public ActivityType getType() {
        return type;
    }

    public void setType(ActivityType type) {
        this.type = type;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Activity)) {
            return false;
        }
        Activity other = (Activity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dk.i2m.converge.ejb.entities.crm.Activity[ id=" + id + " ]";
    }
}
