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
package dk.i2m.converge.ejb.entities;

import dk.i2m.converge.ejb.entities.crm.AdSize;
import dk.i2m.converge.ejb.entities.crm.Band;
import dk.i2m.converge.ejb.entities.crm.QuotePackage;
import dk.i2m.converge.ejb.entities.crm.RateCard;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import org.apache.commons.lang3.StringUtils;

/**
 * Entity representing an {@link Outlet} of content. Responsible for containing
 * information about a single {@link Outlet} of news.
 * 
 * @author Allan Lykke Christensen
 */
@Entity
@Table(name = "outlet")
@NamedQueries({})
public class Outlet implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Unique identifier of the {@link Outlet}.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Name of the {@link Outlet}.
     */
    @Column(name = "name")
    private String name;

    /**
     * Abbreviation of the {@link Outlet}.
     */
    @Column(name = "abbreviation")
    private String abbreviation;

    /**
     * {@link Language} of the items in the {@link Outlet}.
     */
    @ManyToOne
    @JoinColumn(name = "language_id")
    private Language language;

    /**
     * Physical {@link Address} of the {@link Outlet}.
     */
    @Embedded
    private Address address = new Address();

    @Column(name = "last_quote_number")
    private Long lastQuoteNumber = 0L;

    @Column(name = "logo_url")
    @Lob
    private String logoUrl;

    @ManyToOne
    @JoinColumn(name = "default_currency")
    private Currency defaultCurrency;

    @OneToMany(mappedBy = "outlet")
    private List<QuotePackage> quotePackages = new ArrayList<QuotePackage>();

    @Enumerated(EnumType.STRING)
    @Column(name = "outlet_type")
    private OutletType type;

    @Column(name = "vat_rate")
    private BigDecimal vatRate = BigDecimal.ZERO;

    /** Determines if the {@link Outlet} is in-use. */
    @Column(name = "active")
    private boolean active = true;

    @OneToMany(mappedBy = "outlet", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RateCard> rateCards = new ArrayList<RateCard>();

    @OneToMany(mappedBy = "outlet", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Band> bands = new ArrayList<Band>();

    @OneToMany(mappedBy = "outlet", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AdSize> adSizes = new ArrayList<AdSize>();

    /**
     * Creates a new instance of {@link Outlet}.
     */
    public Outlet() {
        this("", "", null, new Address());
    }

    /**
     * Creates a new instance of {@link Outlet}.
     *
     * @param name 
     *          Name of the {@link Outlet}
     * @param abbreviation 
     *          Abbreviation of the {@link Outlet}
     * @param language 
     *          Language of the {@link Outlet}
     * @param address 
     *          Physical {@link Address} of the {@link Outlet}
     */
    public Outlet(String name, String abbreviation, Language language, Address address) {
        this.name = name;
        this.abbreviation = abbreviation;
        this.language = language;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Long getLastQuoteNumber() {
        return lastQuoteNumber;
    }

    public void setLastQuoteNumber(Long lastQuoteNumber) {
        this.lastQuoteNumber = lastQuoteNumber;
    }

    public Currency getDefaultCurrency() {
        return defaultCurrency;
    }

    public void setDefaultCurrency(Currency defaultCurrency) {
        this.defaultCurrency = defaultCurrency;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    /**
     * Determine if the logo for the {@link Outlet} has been set.
     * 
     * @return {@code true} if the logo has been set, otherwise {@code false}
     */
    public boolean isLogoSet() {
        if (StringUtils.isEmpty(this.logoUrl)) {
            return false;
        } else {
            return true;
        }
    }

    public List<QuotePackage> getQuotePackages() {
        return quotePackages;
    }

    public void setQuotePackages(List<QuotePackage> quotePackages) {
        this.quotePackages = quotePackages;
    }

    /**
     * Gets the media type of the {@link Outlet}.
     * 
     * @return Media type of the {@link Outlet}
     */
    public OutletType getType() {
        return type;
    }

    /**
     * Sets the media type of the {@link Outlet}.
     * 
     * @param type
     *          Media type of the {@link Outlet}
     */
    public void setType(OutletType type) {
        this.type = type;
    }

    /**
     * Determines if the {@link Outlet} is currently in-use.
     * 
     * @return {@code true} if the {@link Outlet} is currently in-use, otherwise
     *         {@code false}
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the status of the {@link Outlet}.
     * 
     * @param active
     *          {@code true} if the {@link Outlet} is currently in-use,
     *          otherwise {@code false}
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    public int getNumberOfAdSizes() {
        return adSizes.size();
    }

    public List<AdSize> getAdSizes() {
        return adSizes;
    }

    public void setAdSizes(List<AdSize> adSizes) {
        this.adSizes = adSizes;
    }

    public List<Band> getBands() {
        return bands;
    }

    public int getNumberOfBands() {
        return bands.size();
    }

    public void setBands(List<Band> bands) {
        this.bands = bands;
    }

    public List<RateCard> getRateCards() {
        return rateCards;
    }

    public int getNumberOfRateCards() {
        return rateCards.size();
    }

    public void setRateCards(List<RateCard> rateCards) {
        this.rateCards = rateCards;
    }

    public BigDecimal getVatRate() {
        return vatRate;
    }

    public void setVatRate(BigDecimal vatRate) {
        this.vatRate = vatRate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Outlet)) {
            return false;
        }
        Outlet other = (Outlet) object;
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
