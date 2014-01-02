/*
 * Copyright (C) 2007 - 2014 Converge Consulting Limited
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
package com.getconverge.converge.entities.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

/**
 * {@link UserRole} contains {@link Permission}s which a {@link UserAccount} can
 * be granted by being a member.
 *
 * @author Allan Lykke Christensen
 */
@Entity
@Table(name = "user_role")
public class UserRole implements Serializable {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "role_name", length = 255)
    private String name;

    @Column(name = "description")
    @Lob
    private String description;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_role_has_permission",
            joinColumns = {
                @JoinColumn(referencedColumnName = "id", name = "user_role_id", nullable = false)},
            inverseJoinColumns = {
                @JoinColumn(referencedColumnName = "id", name = "permission_id", nullable = false)})
    private List<Permission> permissions = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_account_has_user_role",
            joinColumns = {
                @JoinColumn(referencedColumnName = "id", name = "user_account_id", nullable = false)},
            inverseJoinColumns = {
                @JoinColumn(referencedColumnName = "id", name = "user_role_id", nullable = false)})
    private List<UserAccount> userAccounts = new ArrayList<>();

    /**
     * Creates a new instance of {@link UserRole}.
     */
    public UserRole() {
        this("");
    }

    /**
     * Creates a new instance of {@link UserRole}.
     *
     * @param name Unique name of the role
     */
    public UserRole(String name) {
        this.name = name;
    }

    /**
     * Gets the unique identifier of the {@link UserRole}.
     *
     * @return Unique identifier of the {@link UserRole}
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the {@link UserRole}.
     *
     * @param id Unique identifier of the {@link UserRole}
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the name of the role.
     *
     * @return Name of the role.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the role.
     *
     * @param name Name of the role
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the description of the role.
     *
     * @return Description of the role.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the role.
     *
     * @param description Description of the role.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets a {@link List} of the {@link Permission}s granted to the
     * {@link UserRole}.
     *
     * @return {@link List} of the {@link Permission}s granted to the
     * {@link UserRole}
     */
    public List<Permission> getPermissions() {
        return permissions;
    }

    /**
     * Sets the {@link List} of {@link Permission}s granted to the
     * {@link UserRole}.
     *
     * @param permissions {@link List} of the {@link Permission}s granted to the
     * {@link UserRole}
     */
    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    /**
     * Gets the members of the {@link UserRole}.
     *
     * @return {@link List} of {@link UserAccount}s that are members of the
     * {@link UserAccount}
     */
    public List<UserAccount> getUserAccounts() {
        return userAccounts;
    }

    /**
     * Sets the members of the {@link UserRole}.
     *
     * @param userAccounts {@link List} of {@link UserAccount}s that are members
     * of the {@link UserAccount}
     */
    public void setUserAccounts(List<UserAccount> userAccounts) {
        this.userAccounts = userAccounts;
    }

    /**
     * A {@link UserRole} ({@code a}) is equal to another object ({@code b})
     * only if {@code b} is a {@link UserRole} and
     * {@code a.getId().equals(b.getId())}.
     *
     * @param obj Object to determine if this object is equal
     * @return {@code true} if this {@link UserRole} is equal to the given
     * object, otherwise {@code false}
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UserRole other = (UserRole) obj;
        return this.id == other.id || (this.id != null && this.id.equals(other.id));
    }

    /**
     * {@inheritDoc }.
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    /**
     * {@inheritDoc }.
     */
    @Override
    public String toString() {
        return getClass().getName() + "[id=" + id + "]";
    }
}
