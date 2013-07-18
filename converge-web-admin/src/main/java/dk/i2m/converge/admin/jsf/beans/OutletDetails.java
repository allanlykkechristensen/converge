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
package dk.i2m.converge.admin.jsf.beans;

import dk.i2m.converge.ejb.entities.Outlet;
import dk.i2m.converge.ejb.entities.crm.*;
import dk.i2m.converge.ejb.facades.OutletFacadeLocal;
import dk.i2m.converge.ejb.facades.QuoteFacadeLocal;
import dk.i2m.converge.ejb.services.ConfigurationKey;
import dk.i2m.converge.ejb.services.ConfigurationServiceLocal;
import dk.i2m.converge.ejb.services.DataNotFoundException;
import com.getconverge.faces.JsfUtils;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.commons.io.FileUtils;

/**
 * Backing bean for {@code OutletDetail.xhtml}.
 *
 * @author Allan Lykke Christensen
 */
@ManagedBean
@ViewScoped
public class OutletDetails implements Serializable {

    /** Application logger. */
    private static final Logger LOG = Logger.getLogger(OutletDetails.class.getName());

    /** Unique identifier of the {@link Outlet} to update. */
    private Long outletId;

    /** {@link Outlet} being updated or created. */
    private Outlet outlet;

    /** {@link RateCard} being updated or created. */
    private RateCard rateCard;

    private Band band;

    private AdSize adSize;

    /** {@link File} being uploaded as the logo for the {@link Outlet}. */
    private File logo;

    /** Interface for the OutletFacade EJB. */
    @EJB
    private OutletFacadeLocal outletFacade;

    /** Interface for the QuoteFacade EJB. */
    @EJB
    private QuoteFacadeLocal quoteFacade;

    /** Interface for the Configuration Service EJB. */
    @EJB
    private ConfigurationServiceLocal cfgService;

    /** 
     * Creates a new instance of {@link OutletDetails}.
     */
    public OutletDetails() {
    }

    /**
     * Gets the unique identifier of the {@link Outlet} to update. If this value
     * is {@code null}, it means that a new {@link Outlet} is being created
     * rather than updated.
     * 
     * @return Unique identifier of the {@link Outlet} being updated
     */
    public Long getOutletId() {
        return outletId;
    }

    /**
     * Sets the unique identifier of the {@link Outlet} to update.
     * 
     * @param outletId 
     *          Unique identifier of the {@link Outlet} to update or
     *          {@code null} if a new {@link Outlet} should be created
     */
    public void setOutletId(Long outletId) {
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.log(Level.FINEST, "Setting the ID of the Outlet: {0}", outletId);
        }
        this.outletId = outletId;
        if (this.outletId == null) {
            this.outlet = new Outlet();
        } else {
            loadOutlet(outletId);
        }
    }

    public void loadOutlet(Long outletId) {
        try {
            this.outlet = outletFacade.find(this.outletId);
        } catch (DataNotFoundException ex) {
            if (LOG.isLoggable(Level.WARNING)) {
                LOG.log(Level.WARNING, "Outlet could not be opened for editing", ex);
            }
        }
    }

    public RateCardPrice rateCardPrice(Band band, AdSize size) {
        if (band == null || size == null) {
            return null;
        }
        for (RateCardPrice price : rateCard.getPrices()) {
            if (price.getBand().equals(band) && price.getAdSize().equals(size)) {
                LOG.log(Level.FINEST, "Match ({0} - {1})", new Object[]{band, size});
                return price;
            }
        }

        // No rate card was found
        RateCardPrice p = new RateCardPrice();
        p.setRateCard(rateCard);
        p.setAdSize(size);
        p.setBand(band);
        p.setPrice(BigDecimal.ZERO);
        rateCard.getPrices().add(p);
        return p;
    }

    /**
     * Gets the {@link Outlet} that is being updated or created.
     * 
     * @return {@link Outlet} that is being updated or created
     */
    public Outlet getOutlet() {
        if (this.outlet == null) {
            this.outlet = new Outlet();
        }
        return outlet;
    }

    public RateCard getRateCard() {
        if (this.rateCard == null) {
            this.rateCard = new RateCard();
            this.rateCard.setOutlet(outlet);
        }
        return rateCard;
    }

    public void setRateCard(RateCard rateCard) {
        this.rateCard = rateCard;
    }

    public AdSize getAdSize() {
        if (this.adSize == null) {
            this.adSize = new AdSize();
            this.adSize.setOutlet(this.outlet);
        }
        return adSize;
    }

    public void setAdSize(AdSize adSize) {
        this.adSize = adSize;
    }

    public Band getBand() {
        if (this.band == null) {
            this.band = new Band();
            this.band.setOutlet(this.outlet);
        }

        return band;
    }

    public void onAddBandSlot() {
        onSave();
        BandSlot bs = new BandSlot();
        bs.setBand(band);
        band.getSlots().add(bs);
        onSave();
    }

    public void onRemoveBandSlot(BandSlot bandSlot) {
        onSave();
        band.getSlots().remove(bandSlot);
        onSave();
    }

    public void onUpdateBand() {
        onSave();
        this.band = null;
    }

    public void setBand(Band band) {
        this.band = band;
    }

    /**
     * Get the file representing the uploaded outlet logo.
     * 
     * @return {@link File} containing the uploaded outlet logo
     */
    public File getLogo() {
        return logo;
    }

    /**
     * Sets the file containing the uploaded outlet logo.
     * 
     * @param logo 
     *          {@link File} containing the uploaded outlet logo
     */
    public void setLogo(File logo) {
        this.logo = logo;
    }

    /**
     * Determine if a new {@link Outlet} is being created.
     * 
     * @return {@code true} if a new {@link Outlet} is being created, otherwise
     *         {@code false}
     */
    public boolean isNew() {
        if (this.outletId == null) {
            return true;
        } else {
            return false;
        }
    }

    // -- EVENT HANDLERS ------------------------------------------------------
    /**
     * Event handler for saving the {@link Outlet} details.
     */
    public String onSave() {
        if (isNew()) {
            outletFacade.create(outlet);
            if (LOG.isLoggable(Level.FINEST)) {
                LOG.log(Level.FINEST, "Outlet {0} created with ID {1}",
                        new Object[]{outlet.getName(), outlet.getId()});
            }
        }

        LOG.log(Level.FINE, "Saving OutletDetails for {0}", outlet.getId());

        // Save uploaded logo
        if (getLogo() != null) {
            String tmpDir = cfgService.getString(ConfigurationKey.WORKING_DIRECTORY);
            String lib = cfgService.getString(ConfigurationKey.LIB_OUTLET_LOGOS);
            try {
                // Copy uploaded file to expected location
                File moveToDir = new File(tmpDir, lib);
                File moveTo = new File(moveToDir, "" + getOutlet().getId());
                if (moveTo.exists()) {
                    moveTo.delete();
                }
                FileUtils.moveFile(getLogo(), moveTo);
                outlet.setLogoUrl(moveTo.getAbsolutePath());
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, "Could not upload logo. {0}",
                        new Object[]{ex.getMessage()});
            }
        }

        outletFacade.update(outlet);

        if (isNew()) {
            JsfUtils.createFacesMessage(FacesMessage.SEVERITY_INFO, "msgs",
                    "OutletDetails_OUTLET_X_CREATED",
                    new Object[]{outlet.getName()});
        } else {
            JsfUtils.createFacesMessage(FacesMessage.SEVERITY_INFO, "msgs",
                    "OutletDetails_OUTLET_X_SAVED",
                    new Object[]{outlet.getName()});
        }
        return "Outlets";
    }

    /**
     * Event handler for removing the logo from the {@link Outlet}.
     */
    public void onRemoveLogo() {
        LOG.log(Level.FINE, "Removing logo from {0}", new Object[]{outlet});
        File currentLogo = new File(outlet.getLogoUrl());
        if (currentLogo.exists()) {
            LOG.log(Level.FINE, "Deleting logo ({0}) from file system",
                    new Object[]{outlet.getLogoUrl()});
            currentLogo.delete();
        } else {
            LOG.log(Level.WARNING, "Outlet logo ({0}) does not exist",
                    new Object[]{outlet.getLogoUrl()});
        }
        outlet.setLogoUrl("");

        outletFacade.update(outlet);
    }

    /**
     * Event handler for preparing the creation of a new {@link RateCard}.
     */
    public void onNewRateCard() {
        this.rateCard = null;
    }

    /**
     * Event handler for saving a {@link RateCard}.
     */
    public void onSaveRateCard() {
        onSave();
        this.rateCard = null;
    }

    public void onAddSimpleRateCardPrice() {
        onSave();
        RateCardPrice p = new RateCardPrice(this.rateCard);
        this.rateCard.getPrices().add(p);
        onSave();
    }

    public void onRemoveSimpleRateCardPrice(RateCardPrice price) {
        onSave();
        this.rateCard.getPrices().remove(price);
        onSave();
    }

    /**
     * Event handler for creating a new {@link RateCard}.
     */
    public void onAddRateCard() {
        getOutlet().getRateCards().add(rateCard);
        onSave();
        this.rateCard = null;
    }

    public void onDeleteRateCard(RateCard rateCard) {
        getOutlet().getRateCards().remove(rateCard);
        onSave();
    }

    public void onOpenBand(Band band) {
        this.band = band;
    }

    public void onAddBand() {
        getOutlet().getBands().add(this.band);
        onSave();
        this.band = null;
    }

    public void onDeleteBand(Band band) {
        getOutlet().getBands().remove(band);
        onSave();
    }

    public void onOpenAdSize(AdSize adSize) {
        this.adSize = adSize;
    }

    public void onAddAdSize() {
        getOutlet().getAdSizes().add(this.adSize);
        onSave();
        this.adSize = null;
    }

    public void onUpdateAdSize() {
        onSave();
        this.adSize = null;
    }

    public void onDeleteAdSize(AdSize adSize) {
        getOutlet().getAdSizes().remove(adSize);
        onSave();
    }
}
