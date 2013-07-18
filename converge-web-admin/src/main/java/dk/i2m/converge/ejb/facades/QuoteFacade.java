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
package dk.i2m.converge.ejb.facades;

import dk.i2m.converge.ejb.entities.Outlet;
import dk.i2m.converge.ejb.entities.crm.*;
import dk.i2m.converge.ejb.entities.security.UserAccount;
import dk.i2m.converge.ejb.entities.workflow.WorkflowDefinition;
import dk.i2m.converge.ejb.entities.workflow.WorkflowStateTransition;
import dk.i2m.converge.ejb.services.*;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Implementation of the {@link QuoteFacade} stateless session bean.
 *
 * @author Allan Lykke Christensen
 */
@Stateless
public class QuoteFacade extends AbstractEntityFacade<Quote> implements QuoteFacadeLocal {

    @PersistenceContext(unitName = "convergePlatformPU")
    private EntityManager em;

    @EJB
    private DaoServiceLocal daoService;

    @EJB
    private OutletFacadeLocal outletFacade;

    @EJB
    private WorkflowFacadeLocal workflowFacade;

    @EJB
    private UserFacadeLocal userFacade;

    @Resource
    private SessionContext sessionCtx;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public QuoteFacade() {
        super(Quote.class);
    }

    /** {@inheritDoc } */
    @Override
    public void create(Quote quote) {
        // Get abbreviation for the qupte number
        Outlet o = quote.getOutlet();
        String abbrev = o.getAbbreviation();

        // Get the workflow for the quote
        QuoteType type = quote.getType();
        WorkflowDefinition wd = type.getQuoteWorkflow();
        
        // Set-up sections
        List<QuoteSectionType> sectionsFromTemplate = type.getSections();
        
        for (QuoteSectionType s : sectionsFromTemplate) {
            QuoteLineSection qls = new QuoteLineSection(s.getName(), quote, s.getType(), s.isExcludeFromVat(), s.getDisplayOrder(), s.getRateCard());
            quote.getSections().add(qls);
        }

        // Update last quote number
        Long lastId = o.getLastQuoteNumber();
        lastId++;
        o.setLastQuoteNumber(lastId);
        outletFacade.update(o);

        // Set the quote number based on the abbreviation and last ID
        quote.setQuoteNumber(abbrev + "/" + lastId);

        // Set the sales representative
        UserAccount salesRep = null;
        try {
            String username = sessionCtx.getCallerPrincipal().getName();
            salesRep = userFacade.findUserAccountByUsername(username);
            quote.setSalesRepresentative(salesRep);
        } catch (UserNotFoundException ex) {
            quote.setSalesRepresentative(null);
            Logger.getLogger(QuoteFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DirectoryException ex) {
            quote.setSalesRepresentative(null);
            Logger.getLogger(QuoteFacade.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Set the currency based on the outlet
        quote.setCurrency(o.getDefaultCurrency());

        // Calculate the duration of the quote
        quote.setDuration(type.getDefaultDuration());

        // Calculate the start date of the quote
        Calendar today = Calendar.getInstance();
        today.add(Calendar.DAY_OF_MONTH, type.getDefaultStartDate());
        quote.setStartDate(today.getTime());

        // Set date of quote
        quote.setQuoteDate(Calendar.getInstance().getTime());

        // Store the quote in the database
        super.create(quote);

        // Conduct initial workflow transition
        WorkflowStateTransition transition = new WorkflowStateTransition();
        transition.setUser(salesRep);
        transition.setSubject(quote);
        transition.setTimestamp(Calendar.getInstance().getTime());
        transition.setState(wd.getStart());
        daoService.create(transition);

        quote.getHistory().add(transition);
        quote.setCurrentState(wd.getStart());
        daoService.update(quote);
    }

    /** {@inheritDoc } */
    @Override
    public void update(Quote entity) {
        try {
            Quote old = find(entity.getId());

            // Chcek if the BookedBy has just been set
            if (old.getBookedBy() == null && entity.getBookedBy() != null) {
                entity.setPaymentTerms(entity.getBookedBy().getPaymentTerms());
            }

            // Check if "Booked By" has changed - Update Payment Terms
            if (old.getBookedBy() != null
                    && !old.getBookedBy().equals(entity.getBookedBy())
                    && entity.getBookedBy() != null) {
                entity.setPaymentTerms(entity.getBookedBy().getPaymentTerms());
            }

            super.update(entity);
        } catch (DataNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    /** {@inheritDoc } */
    @Override
    public List<Quote> findActiveQuotes() {
        return daoService.findWithNamedQuery(Quote.FIND_ACTIVE);
    }

    /** {@inheritDoc } */
    @Override
    public List<Quote> findClosedQuotes() {
        return daoService.findWithNamedQuery(Quote.FIND_CLOSED);
    }

    /** {@inheritDoc } */
    @Override
    public List<Quote> findTrashedQuotes() {
        return daoService.findWithNamedQuery(Quote.FIND_TRASH);
    }

    /** {@inheritDoc } */
    @Override
    public int emptyTrash(Long userId) {
        QueryBuilder qb = QueryBuilder.with(Quote.PARAM_SALES_REP, userId);
        return daoService.executeQuery(Quote.DELETE_TRASH_BY_X, qb);
    }

    /** {@inheritDoc} */
    @Override
    public void createRateCard(RateCard rateCard) {
        daoService.create(rateCard);
    }
}
