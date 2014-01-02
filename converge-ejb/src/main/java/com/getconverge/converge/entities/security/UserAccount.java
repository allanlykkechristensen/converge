/*
 * Copyright (C) 2010 - 2014 Converge Consulting Limited
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

import java.awt.ComponentOrientation;
import java.io.Serializable;
import java.util.*;
import javax.persistence.*;

/**
 * {@link UserAccount} with access to the system and relation news items,
 * outlets, photos, and so forth. The majority of the {@link UserAccount}
 * properties are not managed by the JPA persistence framework. Instead the are
 * retrieved from an LDAP directory upon fetching an account through the data
 * access object. The mapping of the LDAP user account to the
 * {@link UserAccount} object is defined in the application configuration.
 *
 * @author Allan Lykke Christensen
 */
@Entity
@Table(name = "user_account", uniqueConstraints = @UniqueConstraint(columnNames = {"username"}))
@NamedQueries({
    @NamedQuery(name = UserAccount.FIND_BY_UID, query = "SELECT u FROM UserAccount u WHERE u.username=:" + UserAccount.FIND_BY_UID_PARAM_USERNAME),
    @NamedQuery(name = UserAccount.FIND_BY_USER_ROLE, query = "SELECT u FROM UserRole r JOIN r.userAccounts u WHERE r.name=:" + UserAccount.FIND_BY_USER_ROLE_PARAM_ROLENAME)
})
public class UserAccount implements Serializable {

    /**
     * Query for finding a user account by its unique user identifier.
     */
    public static final String FIND_BY_UID = "UserAccount.findByUid";
    /**
     * {@code Username} parameter for the {@link UserAccount#FIND_BY_UID} query.
     */
    public static final String FIND_BY_UID_PARAM_USERNAME = "username";

    /**
     * Query for finding a user accounts who are members of a given role.
     */
    public static final String FIND_BY_USER_ROLE = "UserAccount.findByUserRole";
    /**
     * {@code Rolename} parameter for the {@link UserAccount#FIND_BY_USER_ROLE}
     * query.
     */
    public static final String FIND_BY_USER_ROLE_PARAM_ROLENAME = "rolename";

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "username", length = 255)
    private String username = "";

    @Column(name = "time_zone")
    private String timeZone;

    @ManyToMany(mappedBy = "userAccounts")
    private List<UserRole> userRoles = new ArrayList<>();

    @Column(name = "preferred_language")
    private String language = "";

    @Column(name = "display_name")
    private String displayName = "";

    @Column(name = "given_name")
    private String givenName = "";

    @Column(name = "surname")
    private String surname = "";

    @Column(name = "job_title")
    private String jobTitle = "";

    @Column(name = "organization")
    @Lob
    private String organization = "";

    @Column(name = "email")
    @Lob
    private String email = "";

    @Column(name = "phone")
    private String phone = "";

    @Column(name = "mobile")
    private String mobile = "";

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_account_has_permission",
            joinColumns = {
                @JoinColumn(referencedColumnName = "id", name = "user_account_id", nullable = false)},
            inverseJoinColumns = {
                @JoinColumn(referencedColumnName = "id", name = "permission_id", nullable = false)})
    private List<Permission> permissions = new ArrayList<>();

    /**
     * Creates a new instance of user.
     */
    public UserAccount() {
    }

    /**
     * Creates a new instance of {@link UserAccount}.
     *
     * @param uid Username of the user
     */
    public UserAccount(String uid) {
        this.username = uid;
    }

    /**
     * Gets the unique identifier of the user. The unique identifier is
     * automatically generated upon the creation of the user.
     *
     * @return Unique identifier of the user
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the user.
     *
     * @param id Unique identifier of the user
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the username of this {@link UserAccount}.
     *
     * @return Username of this {@link UserAccount}.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of this {@link UserAccount}.
     *
     * @param username Username of this {@link UserAccount}.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the display name of this {@link UserAccount}.
     *
     * @return Display name of this {@link UserAccount}
     */
    public String getDisplayName() {
        return this.displayName;
    }

    /**
     * Sets the display name this {@link UserAccount}.
     *
     * @param displayName Display name of the {@link UserAccount}.
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Gets the given name of the person owning the {@link UserAccount}.
     *
     * @return Given name of the person owning the {@link UserAccount}
     */
    public String getGivenName() {
        return givenName;
    }

    /**
     * Sets the given name of the person owning the {@link UserAccount}.
     *
     * @param givenName Given name of the person owning the {@link UserAccount}
     */
    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    /**
     * Gets the surname of the person owning the {@link UserAccount}.
     *
     * @return Surname of the person owning the {@link UserAccount}
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Sets the surname of the person owning the {@link UserAccount}.
     *
     * @param surname Surname of the person owning the {@link UserAccount}
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Gets the e-mail address of the {@link UserAccount}.
     *
     * @return E-mail address of the {@link UserAccount}
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the e-mail address of the {@link UserAccount}.
     *
     * @param email E-mail address of the {@link UserAccount}
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the phone number of the {@link UserAccount}.
     *
     * @return Phone number of the {@link UserAccount}
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the phone number of the {@link UserAccount}.
     *
     * @param phone Phone number of the {@link UserAccount}
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Gets the mobile phone number of the {@link UserAccount}.
     *
     * @return Mobile phone number of the {@link UserAccount}
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * Sets the mobile phone number of the {@link UserAccount}.
     *
     * @param mobile Mobile phone number of the {@link UserAccount}
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * Gets the job title of the {@link UserAccount}.
     *
     * @return Job title of the {@link UserAccount}
     */
    public String getJobTitle() {
        return jobTitle;
    }

    /**
     * Sets the job title of the {@link UserAccount}.
     *
     * @param jobTitle Job title of the {@link UserAccount}
     */
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    /**
     * Gets the name of the organization that employs the user account.
     *
     * @return Name of the organization that employs the user account
     */
    public String getOrganization() {
        return organization;
    }

    /**
     * Sets the name of the organization that employs the user account.
     *
     * @param organization Name of the organization that employs the user
     * account
     */
    public void setOrganization(String organization) {
        this.organization = organization;
    }

    /**
     * Gets the {@link Permission}s granted directly to the user account.
     *
     * @return {@link List} of {@link Permission}s granted directly to the user
     * account.
     */
    public List<Permission> getPermissions() {
        return permissions;
    }

    /**
     * Sets the {@link Permission}s granted directly to the user account.
     *
     * @param permissions {@link List} of {@link Permission}s granted directly
     * to the user account.
     */
    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    /**
     * Gets a {@link List} of the {@link UserRole}s where the user is a member.
     *
     * @return {@link List} of the {@link UserRole}s where the user is a member
     */
    public List<UserRole> getUserRoles() {
        return userRoles;
    }

    /**
     * Sets a {@link List} of the {@link UserRole}s where the user is a member.
     *
     * @param userRoles {@link List} of the {@link UserRole}s where the user is
     * a member
     */
    public void setUserRoles(List<UserRole> userRoles) {
        this.userRoles = userRoles;
    }

    /**
     * Gets the {@link TimeZone} of this {@link UserAccount}. Dates and times
     * should be adjusted to given {@link TimeZone} for this
     * {@link UserAccount}.
     *
     * @return {@link TimeZone} of the {@link UserAccount}
     */
    public TimeZone getTimeZone() {
        if (this.timeZone == null) {
            return TimeZone.getDefault();
        } else {
            return TimeZone.getTimeZone(this.timeZone);
        }
    }

    /**
     * Sets the {@link TimeZone} of this {@link UserAccount}.
     *
     * @param timeZone {@link TimeZone} of this {@link UserAccount}
     */
    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone.getID();
    }

    /**
     * Sets the {@link TimeZone} of this {@link UserAccount}.
     *
     * @param timeZoneId {@link String} identifier of the {@link TimeZone}
     */
    public void setTimeZoneAsString(String timeZoneId) {
        this.timeZone = timeZoneId;
    }

    /**
     * Gets the {@link TimeZone} as a {@link String} of this
     * {@link UserAccount}.
     *
     * @return {@link String} identifier of the {@link TimeZone}
     */
    public String getTimeZoneAsString() {
        return this.timeZone;
    }

    /**
     * Gets the preferred language of the user. The language is expressed as a
     * two character code corresponding to RFC2068.
     *
     * @return Preferred language of the user
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Sets the preferred language of the user.
     *
     * @param language Preferred language of the user; expressed as a two
     * character code corresponding to RFC2068
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * Determine the text orientation of the user based on the preferred
     * {@link Locale}.
     *
     * @return {@code true} if the text orientation should be right-to-left
     * (RTL), otherwise {@code false} if the text orientation should be
     * left-to-right (LTR)
     */
    public boolean isRightToLeft() {
        Locale l = getPreferredLocale();
        if (l == null) {
            return false;
        }

        return !ComponentOrientation.getOrientation(l).isLeftToRight();
    }

    /**
     * Gets the preferred language based on the preferred language string. If
     * the language string stored in the preferred language string cannot be
     * converted to a locale null will be returned.
     *
     * @return {@link Locale} corresponding to the preferred language, or
     * {@code null} if preferred language is invalid.
     */
    public Locale getPreferredLocale() {
        // Valid lengths of the preferred language string
        final int fiveCharValidLength = 5;
        final int twoCharValidLength = 2;

        // Position of the language
        final int startLanguage = 0;
        final int endLanguage = 2;

        // Position of the language speciality
        final int startLanguageSpeciality = 3;
        final int endLanguageSpeciality = 5;

        // Locale object to return
        Locale l = null;

        // A valid language string is either two or five characters: la_sp where
        // la is Language, sp is Speciality or la where la is Language
        if (this.language.length() == fiveCharValidLength) {
            l = new Locale(this.language.substring(startLanguage,
                    endLanguage), this.language.substring(
                            startLanguageSpeciality, endLanguageSpeciality));
        } else if (this.language.length() == twoCharValidLength) {
            l = new Locale(this.language);
        }

        return l;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (username != null ? username.hashCode() : 0);
        return hash;
    }

    /**
     * A {@link UserAccount} ({@code a}) is equal to another object ({@code b})
     * only if {@code b} is a {@link UserAccount} and
     * {@code a.getUsername().equals(b.getUsername())}.
     *
     * @param object Object to determine if this object is equal
     * @return {@code true} if this {@link UserAccount} is equal to the given
     * object, otherwise {@code false}
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof UserAccount)) {
            return false;
        }

        UserAccount other = (UserAccount) object;
        return (this.username != null || other.username == null) && (this.username == null || this.username.equals(other.username));
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(getClass().getName());
        sb.append("[id=").append(id).append("/username=").append(username).append("]");
        return sb.toString();
    }
}
