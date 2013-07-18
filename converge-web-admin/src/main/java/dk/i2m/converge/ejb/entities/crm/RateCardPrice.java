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
import javax.persistence.*;

/**
 * Price definition in a {@link RateCard}.
 *
 * @author Allan Lykke Christensen
 */
@Entity
@Table(name = "rate_card_price")
public class RateCardPrice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name = "";

    @ManyToOne
    @JoinColumn(name = "rate_card_id")
    private RateCard rateCard;

    @ManyToOne
    @JoinColumn(name = "band_id")
    private Band band;

    @ManyToOne
    @JoinColumn(name = "ad_size_id")
    private AdSize adSize;

    @Column(name = "price")
    private BigDecimal price;

    public RateCardPrice() {
        this.name = "";
        this.price = BigDecimal.ZERO;
    }
    
    public RateCardPrice(RateCard rateCard) {
        this();
        this.rateCard = rateCard;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AdSize getAdSize() {
        return adSize;
    }

    public void setAdSize(AdSize adSize) {
        this.adSize = adSize;
    }

    public Band getBand() {
        return band;
    }

    public void setBand(Band band) {
        this.band = band;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RateCardPrice)) {
            return false;
        }
        RateCardPrice other = (RateCardPrice) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dk.i2m.converge.ejb.entities.crm.RateCardPrice[ id=" + id + " ]";
    }
}
