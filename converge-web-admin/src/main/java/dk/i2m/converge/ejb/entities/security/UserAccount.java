/*
 * Copyright (C) 2010 - 2012 Interactive Media Management
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

import dk.i2m.converge.ejb.entities.Outlet;
import java.awt.ComponentOrientation;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
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
@Table(name = "user_account", uniqueConstraints =
@UniqueConstraint(columnNames = {"username"}))
@NamedQueries({
    @NamedQuery(name = UserAccount.FIND_BY_USERNAME, query = "SELECT u FROM UserAccount u WHERE u.username = :" + UserAccount.PARAM_USERNAME)
})
public class UserAccount implements Serializable {

    public static final String FIND_BY_USERNAME = "UserAccount.findByUserName";

    public static final String PARAM_USERNAME = "username";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username", length = 255)
    private String username = "";

    @Column(name = "time_zone")
    private String timeZone;

    @ManyToOne()
    @JoinColumn(name = "default_outlet")
    private Outlet defaultOutlet;

    @Column(name = "dn")
    @Lob
    private String distinguishedName = "";

    @Column(name = "lang")
    private String preferredLanguage = "";

    @Column(name = "full_name")
    private String fullName = "";

    @Column(name = "given_name")
    private String givenName = "";

    @Column(name = "surname")
    private String surname = "";

    @Column(name = "job_title")
    private String jobTitle = "";

    @Column(name = "organisation")
    @Lob
    private String organisation = "";

    @Column(name = "email")
    @Lob
    private String email = "";

    @Column(name = "phone")
    private String phone = "";

    @Column(name = "mobile")
    private String mobile = "";

    @ManyToMany(mappedBy = "userAccounts")
    private List<UserRole> userRoles;

    /**
     * Creates a new instance of {@link UserAccount}.
     */
    public UserAccount() {
    }

    /**
     * Creates a new instance of {@link UserAccount}.
     * 
     * @param uid
     *      Username of the user
     */
    public UserAccount(String uid) {
        this.username = uid;
    }

    /**
     * Gets the unique identifier of the user. The unique identifier is automatically generated
     * upon the creation of the user.
     *
     * @return Unique identifier of the user
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the user.
     *
     * @param id
     *          Unique identifier of the user
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the distinguished name of the user in the directory service.
     *
     * @return Distinguished name of the user in the directory service
     */
    public String getDistinguishedName() {
        return distinguishedName;
    }

    /**
     * Sets the distinguished name of the user in the directory service.
     *
     * @param distinguishedName
     *          Distinguished name of the user in the directory service
     */
    public void setDistinguishedName(String distinguishedName) {
        this.distinguishedName = distinguishedName;
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
     * Gets the full or common name of this {@link UserAccount}. This property is
     * supplied by the LDAP directory.
     * 
     * @return Full or common name of this {@link UserAccount}
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Sets the full or common name of this {@link UserAccount}.
     *
     * @param fullName
     *          Full or common name of this {@link UserAccount}
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
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
     * @param givenName
     *          Given name of the person owning the {@link UserAccount}
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
     * @param surname
     *          Surname of the person owning the {@link UserAccount}
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Outlet getDefaultOutlet() {
        return defaultOutlet;
    }

    public void setDefaultOutlet(Outlet defaultOutlet) {
        this.defaultOutlet = defaultOutlet;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getOrganisation() {
        return organisation;
    }

    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    /**
     * Gets the {@link TimeZone} of this {@link UserAccount}. Dates and times should be
     * adjusted to given {@link TimeZone} for this {@link UserAccount}.
     *
     * @return {@link TimeZone} of the {@link UserAccount}
     */
    public TimeZone getTimeZone() {
        if (this.timeZone == null) {
            return TimeZone.getDefault();
        } else {
            try {
                return TimeZone.getTimeZone(this.timeZone);
            } catch (Exception ex) {
                return TimeZone.getDefault();
            }
        }
    }

    /**
     * Sets the {@link TimeZone} of this {@link UserAccount}.
     *
     * @param timeZone
     *          {@link TimeZone} of this {@link UserAccount}
     */
    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone.getID();
    }

    /**
     * Sets the {@link TimeZone} of this {@link UserAccount}.
     *
     * @param timeZoneId
     *          {@link String} identifier of the {@link TimeZone}
     */
    public void setTimeZoneAsString(String timeZoneId) {
        this.timeZone = timeZoneId;
    }

    /**
     * Gets the {@link TimeZone} as a {@link String} of this {@link UserAccount}.
     *
     * @return {@link String} identifier of the {@link TimeZone}
     */
    public String getTimeZoneAsString() {
        return this.timeZone;
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
     * @param userRoles
     *          {@link List} of the {@link UserRole}s where the user is a member
     */
    public void setUserRoles(List<UserRole> userRoles) {
        this.userRoles = userRoles;
    }

    /**
     * Determines if the {@link UserAccount} has a given privilege.
     *
     * @param privilege
     *          Privilege to determine if the {@link UserAccount} has
     * @return <code>true</code> if the {@link UserAccount} has the given
     *         privilege, otherwise <code>false</code>
     */
    public boolean isPrivileged(SystemPrivilege privilege) {
        for (UserRole userRole : this.userRoles) {
            for (Privilege p : userRole.getPrivileges()) {
                if (p.getId().equals(privilege)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Gets the preferred language of the user. The language is expressed as a two
     * character code corresponding to RFC2068.
     *
     * @return Preferred language of the user
     */
    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    /**
     * Sets the preferred language of the user.
     *
     * @param preferredLanguage
     *          Preferred language of the user; expressed as a two character code
     *          corresponding to RFC2068
     */
    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    /**
     * Determine if the display should be rendered right-to-left for the
     * user.
     * 
     * @return {@code true} if the display should be rendered right-to-left, 
     *         otherwise {@code false}
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
     *         <code>null</code> if preferred language is invalid.
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
        if ((this.preferredLanguage.length() == fiveCharValidLength)) {
            l = new Locale(this.preferredLanguage.substring(startLanguage,
                    endLanguage), this.preferredLanguage.substring(
                    startLanguageSpeciality, endLanguageSpeciality));
        } else if (this.preferredLanguage.length() == twoCharValidLength) {
            l = new Locale(this.preferredLanguage);
        }

        return l;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (username != null ? username.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof UserAccount)) {
            return false;
        }

        if (object == null) {
            return false;
        }

        UserAccount other = (UserAccount) object;

        if ((this.username == null && other.username != null) || (this.username
                != null && !this.username.equals(other.username))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(getClass().getName());
        sb.append("[id=").append(id).append("/username=").append(username).append("]");
        return sb.toString();
    }
}
