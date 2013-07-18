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
import javax.persistence.*;

/**
 * Definition of a section that must appear in a Quote.
 *
 * @author Allan Lykke Christensen
 */
@Entity
@Table(name = "quote_section_type")
public class QuoteSectionType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "display_order")
    private int displayOrder;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "quote_type_id")
    private QuoteType quoteType;

    @ManyToOne
    @JoinColumn(name = "quote_line_type_id")
    private QuoteLineType type;

    @Column(name = "exclude_from_vat")
    private boolean excludeFromVat = false;

    @ManyToOne
    @JoinColumn(name = "rate_card_id")
    private RateCard rateCard;

    public QuoteSectionType() {
        this("", null, null, 0);
    }

    public QuoteSectionType(String name, QuoteType quoteType, QuoteLineType type, int displayOrder) {
        this.displayOrder = displayOrder;
        this.name = name;
        this.quoteType = quoteType;
        this.type = type;
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

    public QuoteType getQuoteType() {
        return quoteType;
    }

    public void setQuoteType(QuoteType quoteType) {
        this.quoteType = quoteType;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof QuoteSectionType)) {
            return false;
        }
        QuoteSectionType other = (QuoteSectionType) object;
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
