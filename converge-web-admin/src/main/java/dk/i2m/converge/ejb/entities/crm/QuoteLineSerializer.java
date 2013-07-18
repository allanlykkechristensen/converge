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

import java.util.List;

/**
 * Serializer for a {@link QuoteLine}. A serialiser is responsible for 
 * processing the content of a {@link QuoteSection} and indicating which fields
 * to display in the summary and detail view.
 *
 * @author Allan Lykke Christensen
 */
public interface QuoteLineSerializer {

    public boolean isDiscountPossible();

    public boolean isRateVisible();

    public List<QuoteLineField> getDetailFields(Quote quote);

    public List<QuoteLineField> getDetailFrozenFields(Quote quote);

    public List<QuoteLineField> getDetailNonFrozenFields(Quote quote);

    /**
     * Get the fields that should be displayed in summary view for a given 
     * quote.
     * 
     * @param quote
     *          {@link Quote} for which to display the {@link QuoteLineField}s
     * @return {@link List} of {@link QuoteLineField}s to display
     */
    public List<QuoteLineField> getFields(Quote quote);

    /**
     * Updates the details of a {@link QuoteLine} based on its properties.
     * 
     * @param quoteLine
     *          {@link QuoteLine} to update
     */
    public void update(QuoteLine quoteLine);
}
