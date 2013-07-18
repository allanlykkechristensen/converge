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

import dk.i2m.converge.ejb.entities.Currency;
import dk.i2m.converge.ejb.entities.Outlet;
import dk.i2m.converge.ejb.entities.security.UserAccount;
import dk.i2m.converge.ejb.entities.workflow.Workflowable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

/**
 * Quote for a product or service.
 *
 * @author Allan Lykke Christensen
 */
@Entity
@Table(name = "quote")
@NamedQueries({
    @NamedQuery(name = Quote.FIND_ACTIVE, query = "SELECT q FROM Quote q WHERE q.currentState NOT MEMBER OF q.type.quoteWorkflow.endStates AND q.currentState <> q.type.quoteWorkflow.trash"),
    @NamedQuery(name = Quote.FIND_CLOSED, query = "SELECT q FROM Quote q WHERE q.currentState MEMBER OF q.type.quoteWorkflow.endStates"),
    @NamedQuery(name = Quote.FIND_TRASH, query = "SELECT q FROM Quote q WHERE q.currentState = q.type.quoteWorkflow.trash"),
    @NamedQuery(name = Quote.DELETE_TRASH_BY_X, query = "DELETE FROM Quote q WHERE q.salesRepresentative.id = :" + Quote.PARAM_SALES_REP + " AND q.currentState = q.type.quoteWorkflow.trash")
})
public class Quote extends Workflowable {

    /** Query for finding all active quotes, i.e. quotes that are not complete nor trashed. */
    public static final String FIND_ACTIVE = "quote.findActive";

    /** Query for finding all active quotes, i.e. quotes that are not complete nor trashed. */
    public static final String FIND_CLOSED = "quote.findClosed";

    /** Query for finding all trashed quotes, i.e. quotes that are in a state marked as trash. */
    public static final String FIND_TRASH = "quote.findTrash";

    /** Query for deleting trashed quotes of a particular sales representative. Use the {@link Quote#PARAM_SALES_REP} parameter to specify the ID of the sales representative. */
    public static final String DELETE_TRASH_BY_X = "quote.deleteTrashByX";

    /** Parameter used to specific the unique identifier of a sales representative. */
    public static final String PARAM_SALES_REP = "salesRep";

    @Column(name = "quote_number")
    private String quoteNumber;

    @ManyToOne
    @JoinColumn(name = "outlet_id")
    private Outlet outlet;

    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name = "quote_date")
    private Date quoteDate;

    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name = "valid_until")
    private Date validUntil;

    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "duration")
    private Integer duration;

    @ManyToOne
    @JoinColumn(name = "sales_rep")
    private UserAccount salesRepresentative;

    @ManyToOne
    @JoinColumn(name = "quote_type")
    private QuoteType type;

    @ManyToOne
    @JoinColumn(name = "quote_package")
    private QuotePackage quotePackage;

    @ManyToOne
    @JoinColumn(name = "quote_for")
    private Account quoteFor;

    @ManyToOne
    @JoinColumn(name = "quote_for_contact")
    private Contact quoteForContact;

    @ManyToOne
    @JoinColumn(name = "booked_by")
    private Account bookedBy;

    @ManyToOne
    @JoinColumn(name = "booked_by_contact")
    private Contact bookedByContact;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private AccountBrand brand;

    @ManyToOne
    @JoinColumn(name = "currency")
    private Currency currency;

    @ManyToOne
    @JoinColumn(name = "payment_terms_id")
    private PaymentTerms paymentTerms;

    @OneToMany(mappedBy = "quote", orphanRemoval = true, cascade = CascadeType.ALL)
    @OrderBy(value = "posted ASC")
    private List<QuoteComment> comments = new ArrayList<QuoteComment>();

    @OneToMany(mappedBy = "quote", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @OrderBy("displayOrder ASC")
    private List<QuoteLineSection> sections = new ArrayList<QuoteLineSection>();

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Outlet getOutlet() {
        return outlet;
    }

    public void setOutlet(Outlet outlet) {
        this.outlet = outlet;
    }

    public Date getQuoteDate() {
        return quoteDate;
    }

    public void setQuoteDate(Date quoteDate) {
        this.quoteDate = quoteDate;
    }

    public String getQuoteNumber() {
        return quoteNumber;
    }

    public void setQuoteNumber(String quoteNumber) {
        this.quoteNumber = quoteNumber;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        if (startDate == null || duration == null) {
            return null;
        }
        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        start.add(Calendar.WEEK_OF_YEAR, duration);
        return start.getTime();
    }

    public Date getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(Date validUntil) {
        this.validUntil = validUntil;
    }

    public UserAccount getSalesRepresentative() {
        return salesRepresentative;
    }

    public void setSalesRepresentative(UserAccount salesRepresentative) {
        this.salesRepresentative = salesRepresentative;
    }

    public QuoteType getType() {
        return type;
    }

    public void setType(QuoteType type) {
        this.type = type;
    }

    public Account getBookedBy() {
        return bookedBy;
    }

    public void setBookedBy(Account bookedBy) {
        this.bookedBy = bookedBy;
    }

    public Account getQuoteFor() {
        return quoteFor;
    }

    public void setQuoteFor(Account quoteFor) {
        this.quoteFor = quoteFor;
    }

    public AccountBrand getBrand() {
        return brand;
    }

    public void setBrand(AccountBrand brand) {
        this.brand = brand;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Contact getBookedByContact() {
        return bookedByContact;
    }

    public void setBookedByContact(Contact bookedByContact) {
        this.bookedByContact = bookedByContact;
    }

    public Contact getQuoteForContact() {
        return quoteForContact;
    }

    public void setQuoteForContact(Contact quoteForContact) {
        this.quoteForContact = quoteForContact;
    }

    public PaymentTerms getPaymentTerms() {
        return paymentTerms;
    }

    public void setPaymentTerms(PaymentTerms paymentTerms) {
        System.out.println("Setting payment terms: " + paymentTerms.getName());
        this.paymentTerms = paymentTerms;
    }

    public QuotePackage getQuotePackage() {
        return quotePackage;
    }

    public void setQuotePackage(QuotePackage quotePackage) {
        this.quotePackage = quotePackage;
    }

    public List<QuoteComment> getComments() {
        return comments;
    }

    public void setComments(List<QuoteComment> comments) {
        this.comments = comments;
    }

    public List<QuoteLineSection> getSections() {
        return sections;
    }

    public List<QuoteLineSection> getSectionsWithVat() {
        List<QuoteLineSection> withVat = new ArrayList<QuoteLineSection>();
        for (QuoteLineSection s : getSections()) {
            if (!s.isExcludeFromVat()) {
                withVat.add(s);
            }
        }
        return withVat;
    }

    public List<QuoteLineSection> getSectionsWithoutVat() {
        List<QuoteLineSection> withoutVat = new ArrayList<QuoteLineSection>();
        for (QuoteLineSection s : getSections()) {
            if (s.isExcludeFromVat()) {
                withoutVat.add(s);
            }
        }
        return withoutVat;
    }

    public void setSections(List<QuoteLineSection> sections) {
        this.sections = sections;
    }

    public BigDecimal getCalculatedSubtotalBeforeDiscounts() {
        BigDecimal beforeDiscounts = BigDecimal.ZERO;
        for (QuoteLineSection s : getSectionsWithVat()) {
            beforeDiscounts = beforeDiscounts.add(s.getSubtotal());
        }

        return beforeDiscounts;
    }

    public BigDecimal getCalculatedSubtotalAfterDiscounts() {
        BigDecimal afterDiscounts = BigDecimal.ZERO;
        for (QuoteLineSection s : getSectionsWithVat()) {
            afterDiscounts = afterDiscounts.add(s.getTotal());
        }
        return afterDiscounts;
    }

    public BigDecimal getCalculatedDiscount() {
        BigDecimal calcDiscounts = BigDecimal.ZERO;
        for (QuoteLineSection s : getSectionsWithVat()) {
            calcDiscounts = calcDiscounts.add((s.getDiscount()));
        }
        return calcDiscounts;
    }

    public BigDecimal getCalculatedVAT() {
        BigDecimal vat = BigDecimal.ZERO;
        vat = vat.add(getCalculatedSubtotalAfterDiscounts());
        vat = vat.multiply(getOutlet().getVatRate());
        return vat;
    }

    public BigDecimal getCalculatedSubtotalAfterVAT() {
        return getCalculatedSubtotalAfterDiscounts().add(getCalculatedVAT());
    }

    public BigDecimal getCalculatedSubtotalNonVat() {
        BigDecimal nonVatSubtotal = BigDecimal.ZERO;
        for (QuoteLineSection s : getSectionsWithoutVat()) {
            nonVatSubtotal = nonVatSubtotal.add(s.getTotal());
        }
        return nonVatSubtotal;
    }

    /**
     * Gets the total amount of the {@link Quote} including VAT and discounts.
     * <p/>
     * @return Total amount of the {@link Quote} including VAT and discounts
     */
    public BigDecimal getTotal() {
        return getCalculatedSubtotalAfterVAT().add(getCalculatedSubtotalNonVat());
    }
}
