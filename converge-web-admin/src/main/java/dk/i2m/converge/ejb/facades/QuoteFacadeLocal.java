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

import dk.i2m.converge.ejb.entities.crm.Quote;
import dk.i2m.converge.ejb.services.DataNotFoundException;
import java.util.List;
import javax.ejb.Local;

/**
 * Local interface for the {@link QuoteFacade}.
 *
 * @author Allan Lykke Christensen
 */
@Local
public interface QuoteFacadeLocal {

    /**
     * Creates and stores a new {@link Quote} in the database.
     * 
     * @param quote
     *          {@link Quote} to create
     */
    void create(Quote quote);

    /**
     * Updates an existing {@link Quote} in the database. If the 
     * {@link Quote#bookedBy} has changed, the {@link PaymentTerms} of the 
     * {@link Quote} is updated.
     * 
     * @param quote
     *          {@link Quote} to update
     */
    void update(Quote quote);

    /**
     * Gets a {@link List} of {@link Quote} that are not completed and not in
     * the trash state.
     * 
     * @return {@link List} of active {@link Quote}s
     */
    List<Quote> findActiveQuotes();

    /**
     * Gets a {@link List} of {@link Quote} that are closed.
     * 
     * @return {@link List} of closed {@link Quote}s
     */
    List<Quote> findClosedQuotes();

    /**
     * Gets a {@link List} of {@link Quote}s that has been trashed. That is,
     * {@link Quote}s that are in a {@link WorkflowState} representing trash.
     * 
     * @return {@link List} of {@link Quote}s that has been trashed
     */
    List<Quote> findTrashedQuotes();
    
    /**
     * Permanently deletes the trashed quotes where the given 
     * {@link UserAccount} was the sales representative.
     * 
     * @param userId
     *          Unique identifier of the {@link UserAccount}
     * @return Number of quotes deleted
     */
    int emptyTrash(Long userId);

    void remove(Quote quote);

    Quote find(Object id) throws DataNotFoundException;

    List<Quote> findAll();

    List<Quote> findRange(int[] range);

    int count();

    /**
     * Creates a {@link RateCard}.
     * 
     * @param rateCard 
     *          {@link RateCard} to create
     */
    void createRateCard(dk.i2m.converge.ejb.entities.crm.RateCard rateCard);
}
