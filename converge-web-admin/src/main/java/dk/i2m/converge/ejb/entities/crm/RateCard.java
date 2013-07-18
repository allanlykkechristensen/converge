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

import dk.i2m.converge.ejb.entities.Outlet;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.*;
import org.apache.commons.beanutils.BeanComparator;

/**
 * Definition of prices and descriptions for ad placement options of an
 * {@link Outlet}.
 *
 * @author Allan Lykke Christensen
 */
@Entity
@Table(name = "rate_card")
public class RateCard implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "outlet_id")
    private Outlet outlet;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    @Lob
    private String description;

    @Column(name = "quote_line_type")
    private String quoteLineType;

    @OneToMany(mappedBy = "rateCard", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<RateCardPrice> prices = new ArrayList<RateCardPrice>();

    @Column(name = "simple")
    private boolean simple = false;

    public RateCard() {
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

    public Outlet getOutlet() {
        return outlet;
    }

    public void setOutlet(Outlet outlet) {
        this.outlet = outlet;
    }

    public List<RateCardPrice> getPrices() {
        return prices;
    }

    public void setPrices(List<RateCardPrice> prices) {
        this.prices = prices;
    }

    public String getQuoteLineType() {
        return quoteLineType;
    }

    public void setQuoteLineType(String quoteLineType) {
        this.quoteLineType = quoteLineType;
    }

    /**
     * Determine if the {@link RateCard} is a simple rate card not using
     * {@link Band}s and {@link AdSize}s. A simple {@link RateCard} is useful
     * to describe production costs
     * @return 
     */
    public boolean isSimple() {
        return this.simple;
    }

    public void setSimple(boolean simple) {
        this.simple = simple;
    }

    public List<Band> getAvailableBands() {
        if (isSimple()) {
            return Collections.EMPTY_LIST;
        }
        
        List<Band> bands = new ArrayList<Band>();
        for (RateCardPrice price : getPrices()) {
            if (!bands.contains(price.getBand())) {
                bands.add(price.getBand());
            }
        }
        
        Collections.sort(bands, new BeanComparator("start"));

        return bands;
    }

    public List<AdSize> getAvailableAdSizes() {
        if (isSimple()) {
            return Collections.EMPTY_LIST;
        }
        
        List<AdSize> adSizes = new ArrayList<AdSize>();
        for (RateCardPrice price : getPrices()) {
            if (!adSizes.contains(price.getAdSize())) {
                adSizes.add(price.getAdSize());
            }
        }
        Collections.sort(adSizes, new BeanComparator("name"));
        return adSizes;
    }
    
    public int getNumberOfAdSizes() {
        return getAvailableAdSizes().size();
    }

    public RateCardPrice getPrice(Band band, AdSize size) {
        for (RateCardPrice price : getPrices()) {
            if (band.equals(price.getBand()) && size.equals(price.getAdSize())) {
                return price;
            }
        }
        return null;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof RateCard)) {
            return false;
        }
        RateCard other = (RateCard) object;
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
