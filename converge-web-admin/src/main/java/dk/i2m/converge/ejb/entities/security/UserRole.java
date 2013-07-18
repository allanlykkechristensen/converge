/*
 * Copyright (C) 2007 - 2012 Interactive Media Management
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
package dk.i2m.converge.ejb.entities.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

/**
 * {@link UserRole} represents a role that a user can be a member of.
 * <p/>
 * @author Allan Lykke Christensen
 */
@Entity
@Table(name = "user_role")
public class UserRole implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "role_name", length = 255)
    private String name;
    @Column(name = "description")
    @Lob
    private String description;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_role_privilege",
    joinColumns = {
        @JoinColumn(referencedColumnName = "id", name = "role_id", nullable = false)},
    inverseJoinColumns = {
        @JoinColumn(referencedColumnName = "id", name = "privilege_id", nullable = false)})
    private List<Privilege> privileges = new ArrayList<Privilege>();
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_account_membership", 
            joinColumns = { @JoinColumn(referencedColumnName = "id", name = "user_account_id", nullable = false)},
            inverseJoinColumns = { @JoinColumn(referencedColumnName = "id", name = "user_role_id", nullable = false)})
    private List<UserAccount> userAccounts = new ArrayList<UserAccount>();

    /**
     * Creates a new instance of {@link UserRole}.
     */
    public UserRole() {
    }

    /**
     * Creates a new instance of {@link UserRole}.
     * <p/>
     * @param name
*          Unique name of the role
     */
    public UserRole(String name) {
        this.name = name;
    }

    /**
     * Gets the unique identifier of the {@link UserRole}.
     * <p/>
     * @return Unique identifier of the {@link UserRole}
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the {@link UserRole}.
     * <p/>
     * @param id
*          Unique identifier of the {@link UserRole}
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the name of the role.
     * <p/>
     * @return Name of the role.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the role.
     * <p/>
     * @param name
*          Name of the role
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the description of the role.
     * <p/>
     * @return Description of the role.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the role.
     * <p/>
     * @param description
*          Description of the role.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets a {@link List} of privileges when a user has this role.
     * <p/>
     * @return {@link List} of privileges when a user has this role
     */
    public List<Privilege> getPrivileges() {
        return privileges;
    }

    /**
     * Sets a {@link List} of privileges when a user has this role.
     * <p/>
     * @param privileges
*          {@link List} of privileges when a user has this role
     */
    public void setPrivileges(List<Privilege> privileges) {
        this.privileges = privileges;
    }

    /**
     * Gets the {@link UserAccount}s that are part of this {@link UserRole}.
     * <p/>
     * @return {@link List} of {@link UserAccount}s belonging to this {@link UserRole}
     */
    public List<UserAccount> getUserAccounts() {
        return userAccounts;
    }

    /**
     * Sets the {@link UserAccount}s that are part of this {@link UserRole}.
     * <p/>
     * @param userAccounts
*          {@link List} of {@link UserAccount}s belonging to
     * this {@link UserRole}
     */
    public void setUserAccounts(List<UserAccount> userAccounts) {
        this.userAccounts = userAccounts;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UserRole other = (UserRole) obj;
        if (this.id != other.id
                && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return getClass().getName() + "[id=" + id + "]";
    }
}
