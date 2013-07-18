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

import dk.i2m.converge.ejb.entities.Address;
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
@Table(name = "account")
@NamedQueries(
@NamedQuery(name = "Account.findByName", query = "SELECT a FROM Account a WHERE a.name LIKE :name"))
public class Account implements Serializable {

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

    @Column(name = "website")
    private String website;

    @Column(name = "office_phone")
    private String officePhone;

    @Column(name = "fax")
    private String fax;

    @OneToMany(mappedBy = "account")
    private List<AccountBrand> brands = new ArrayList<AccountBrand>();

    @ManyToOne
    @JoinColumn(name = "account_manager")
    private UserAccount accountManager;

    @ManyToMany
    @JoinTable(name = "account_contacts", joinColumns =
    @JoinColumn(name = "account_id"),
    inverseJoinColumns =
    @JoinColumn(name = "contact_id"))
    private List<Contact> contacts = new ArrayList<Contact>();

    @ManyToMany
    @JoinTable(name = "account_phone", joinColumns =
    @JoinColumn(name = "account_id"),
    inverseJoinColumns =
    @JoinColumn(name = "phone_id"))
    @OrderBy("preference ASC")
    private List<PhoneNumber> phones = new ArrayList<PhoneNumber>();

    @ManyToMany
    @JoinTable(name = "account_email", joinColumns =
    @JoinColumn(name = "account_id"),
    inverseJoinColumns =
    @JoinColumn(name = "email_id"))
    @OrderBy("preference ASC")
    private List<EmailAddress> emailAddresses = new ArrayList<EmailAddress>();

    @ManyToOne
    @JoinColumn(name = "payment_terms_id")
    private PaymentTerms paymentTerms;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "created")
    private Date created;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "updated")
    private Date updated;

    @Embedded
    private Address billingAddress;

    @ManyToMany
    @JoinTable(name = "account_account_type", joinColumns =
    @JoinColumn(name = "account_id"),
    inverseJoinColumns =
    @JoinColumn(name = "account_type_id"))
    private List<AccountType> types = new ArrayList<AccountType>();

    @ManyToMany(mappedBy = "accounts")
    private List<Activity> activities = new ArrayList<Activity>();

    public Account() {
    }

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

    public UserAccount getAccountManager() {
        return accountManager;
    }

    public void setAccountManager(UserAccount accountManager) {
        this.accountManager = accountManager;
    }

    public List<AccountBrand> getBrands() {
        return brands;
    }

    public void setBrands(List<AccountBrand> brands) {
        this.brands = brands;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getOfficePhone() {
        return officePhone;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public PaymentTerms getPaymentTerms() {
        return paymentTerms;
    }

    public void setPaymentTerms(PaymentTerms paymentTerms) {
        this.paymentTerms = paymentTerms;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }

    public List<AccountType> getTypes() {
        return types;
    }

    public void setTypes(List<AccountType> types) {
        this.types = types;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public List<PhoneNumber> getPhones() {
        return phones;
    }

    public void setPhones(List<PhoneNumber> phones) {
        this.phones = phones;
    }

    public List<EmailAddress> getEmailAddresses() {
        return emailAddresses;
    }

    public void setEmailAddresses(List<EmailAddress> emailAddresses) {
        this.emailAddresses = emailAddresses;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Account)) {
            return false;
        }
        Account other = (Account) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dk.i2m.converge.ejb.entities.crm.Account[ id=" + id + " ]";
    }
}
