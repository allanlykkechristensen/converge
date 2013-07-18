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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.*;

/**
 *
 * @author Allan Lykke Christensen
 */
@Entity
@Table(name = "quote_line_section")
public class QuoteLineSection implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "quote_id")
    private Quote quote;

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<QuoteLine> quoteLines;

    @ManyToOne
    @JoinColumn(name = "quote_line_type_id")
    private QuoteLineType type;

    @Column(name = "displayOrder")
    private int displayOrder;

    @Column(name = "exclude_from_vat")
    private boolean excludeFromVat = false;
    
    @ManyToOne
    @JoinColumn(name="rate_card_id")
    private RateCard rateCard = null;

    public QuoteLineSection() {
        this("", null, null, false, 0);
    }

    public QuoteLineSection(String name, Quote quote, QuoteLineType type, boolean excludeFromVat, int displayOrder) {
        this(name, quote, type, excludeFromVat, displayOrder, null);
    }
    
    public QuoteLineSection(String name, Quote quote, QuoteLineType type, boolean excludeFromVat, int displayOrder, RateCard rateCard) {
        this.name = name;
        this.quote = quote;
        this.displayOrder = displayOrder;
        this.type = type;
        this.excludeFromVat = excludeFromVat;
        this.rateCard = rateCard;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Quote getQuote() {
        return quote;
    }

    public void setQuote(Quote quote) {
        this.quote = quote;
    }

    public List<QuoteLine> getQuoteLines() {
        return quoteLines;
    }

    public void setQuoteLines(List<QuoteLine> quoteLines) {
        this.quoteLines = quoteLines;
    }

    public void addQuoteLine() {
        QuoteLine quoteLine = new QuoteLine(this);
        try {
            QuoteLineSerializer serializer = getType().getSerialiserInstance();
            for (QuoteLineField field : serializer.getFields(quote)) {
                quoteLine.getField(field);
            }
            
            for (QuoteLineField field : serializer.getDetailFields(quote)) {
                quoteLine.getField(field);
            }
        } catch (PluginInstantiationException ex) {
            Logger.getLogger(QuoteLineSection.class.getName()).log(Level.SEVERE, null, ex);
        }


        getQuoteLines().add(quoteLine);
    }

    public QuoteLineType getType() {
        return type;
    }

    public void setType(QuoteLineType type) {
        this.type = type;
    }

    public boolean isExcludeFromVat() {
        return excludeFromVat;
    }

    public void setExcludeFromVat(boolean excludeFromVat) {
        this.excludeFromVat = excludeFromVat;
    }

    public RateCard getRateCard() {
        return rateCard;
    }

    public void setRateCard(RateCard rateCard) {
        this.rateCard = rateCard;
    }
    
    /**
     * Determine if this section has a rate card available.
     * 
     * @return true if a rate card is available for this section, otherwise 
     *         false
     */
    public boolean isRateCardAvailable() {
        if (getRateCard() == null) { 
            return false;
        } else {
            return true;
        }
    }

    public Object getSummaryField(QuoteLineField field) {
        if (!field.isSummary()) {
            return "";
        }
        Object value;
        if ("int".equalsIgnoreCase(field.getType())) {
            value = BigDecimal.ZERO;
        } else {
            value = new String();
        }

        for (QuoteLine line : getQuoteLines()) {
            if ("int".equalsIgnoreCase(field.getType())) {
                String fieldValue = line.getField(field).getValue();
                try {
                    value = ((BigDecimal) value).add(new BigDecimal(fieldValue));
                } catch (NumberFormatException ex) {
                }
            }
        }

        return value;
    }

    public BigDecimal getDiscount() {
        BigDecimal discount = BigDecimal.ZERO;
        for (QuoteLine line : getQuoteLines()) {
            BigDecimal lineDiscount = BigDecimal.ZERO;
            lineDiscount = lineDiscount.add(line.getDiscount()).multiply(line.getQuantity());
            discount = discount.add(lineDiscount);
        }
        return discount;
    }

    /**
     * Calculates the sub-total (before discount) for the 
     * {@link QuoteLineSection}.
     * 
     * @return Sub-total for the {@link QuoteLineSection}.
     */
    public BigDecimal getSubtotal() {
        BigDecimal subtotal = BigDecimal.ZERO;
        for (QuoteLine line : quoteLines) {
            subtotal = subtotal.add(line.getSubtotal());
        }
        return subtotal;
    }

    /**
     * Calculates the total for the {@link QuoteLineSection}.
     * 
     * @return Total for the {@link QuoteLineSection}.
     */
    public BigDecimal getTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (QuoteLine line : quoteLines) {
            total = total.add(line.getTotal());
        }
        return total;
    }

    /**
     * Calculates the total for the {@link QuoteLineSection}.
     * 
     * @return Total for the {@link QuoteLineSection}.
     */
    public BigDecimal getQuantity() {
        BigDecimal totalQuantity = BigDecimal.ZERO;
        for (QuoteLine line : quoteLines) {
            totalQuantity = totalQuantity.add(line.getQuantity());
        }
        return totalQuantity;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof QuoteLineSection)) {
            return false;
        }
        QuoteLineSection other = (QuoteLineSection) object;
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
