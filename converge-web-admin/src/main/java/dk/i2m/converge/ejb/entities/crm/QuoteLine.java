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

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

/**
 * Line on a {@link Quote} containing a product or service.
 *
 * @author Allan Lykke Christensen
 */
@Entity
@Table(name = "quote_line")
public class QuoteLine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "quote_line_section_id")
    private QuoteLineSection section;

    @OneToMany(mappedBy = "quoteLine", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<QuoteLineProperty> properties = new ArrayList<QuoteLineProperty>();

    @Column(name = "quantity")
    private BigDecimal quantity;

    @Column(name = "bookRate")
    private BigDecimal bookRate;

    @Column(name = "discount")
    private BigDecimal discount;

    public QuoteLine() {
        this(null);
    }
    
    public QuoteLine(QuoteLineSection section) {
        this.section = section;
        this.quantity = BigDecimal.ZERO;
        this.bookRate = BigDecimal.ZERO;
        this.discount = BigDecimal.ZERO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getBookRate() {
        return bookRate;
    }

    public void setBookRate(BigDecimal bookRate) {
        this.bookRate = bookRate;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    /**
     * Calculates the rate by subtracting {@link  #getDiscount()} from 
     * {@link #getBookRate()}.
     * 
     * @return Actual rate used for calculating {@link #getTotal()}
     */
    public BigDecimal getRate() {
        BigDecimal discountedRate = BigDecimal.ZERO;
        discountedRate = discountedRate.add(bookRate);
        discountedRate = discountedRate.subtract(discount);
        return discountedRate;
    }

    public QuoteLineSection getSection() {
        return section;
    }

    public void setSection(QuoteLineSection section) {
        this.section = section;
    }

    public BigDecimal getSubtotal() {
        BigDecimal subtotal = BigDecimal.ZERO;
        subtotal = subtotal.add(getQuantity()).multiply(getBookRate());
        return subtotal;
    }

    public BigDecimal getTotal() {
        BigDecimal total = BigDecimal.ZERO;
        total = total.add(getQuantity()).multiply(getRate());
        return total;
    }

    public List<QuoteLineProperty> getProperties() {
        return properties;
    }

    public void setProperties(List<QuoteLineProperty> properties) {
        this.properties = properties;
    }

    /**
     * Get a specific (custom) field stored in the {@link QuoteLine}.
     * 
     * @param field
     *          Field to obtain
     * @return Value of the custom field, or an empty string if the field 
     *         doesn't exist
     */
    public QuoteLineProperty getField(QuoteLineField field) {
        if (field == null) {
            return null;
        }
        for (QuoteLineProperty p : getProperties()) {
            if (p.getName() != null && field != null && p.getNamedIdentifier().equalsIgnoreCase(field.getId())) {
                return p;
            }
        }
        QuoteLineProperty p = new QuoteLineProperty(field.getId(), field.getName(), field.getLabel(), field.getQualifier(), "", this);
        getProperties().add(p);

        return p;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof QuoteLine)) {
            return false;
        }
        QuoteLine other = (QuoteLine) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getClass().getName() + "[id=" + id + "]";
    }
}
