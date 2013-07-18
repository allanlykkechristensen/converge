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
package dk.i2m.converge.crm;

import dk.i2m.converge.ejb.entities.crm.Quote;
import dk.i2m.converge.ejb.entities.crm.QuoteLine;
import dk.i2m.converge.ejb.entities.crm.QuoteLineField;
import dk.i2m.converge.ejb.entities.crm.QuoteLineSerializer;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Allan Lykke Christensen
 */
public class ProductionSerializer implements QuoteLineSerializer {

    private List<QuoteLineField> fields = null;

    @Override
    public boolean isDiscountPossible() {
        return true;
    }

    @Override
    public boolean isRateVisible() {
        return true;
    }
    
    enum Field {
        PRODUCT,
        RATE
    }

    @Override
    public List<QuoteLineField> getFields(Quote quote) {
        if (fields == null) {
            this.fields = new ArrayList<QuoteLineField>();
            this.fields.add(new QuoteLineField(Field.PRODUCT.name(), "Select", "Product", "", "rateCard", "input-medium", 1, false, false, false));
        }
        return fields;

    }

    @Override
    public List<QuoteLineField> getDetailFields(Quote quote) {
        if (fields == null) {
            this.fields = new ArrayList<QuoteLineField>();
            this.fields.add(new QuoteLineField(Field.PRODUCT.name(), "Select", "Product", "", "rateCard", "input-medium", 1, false, false, false));
        }
        return fields;
    }

    @Override
    public List<QuoteLineField> getDetailFrozenFields(Quote quote) {
        List<QuoteLineField> calculatedFields = new ArrayList<QuoteLineField>();
        for (QuoteLineField f : getDetailFields(quote)) {
            if (f.isFreeze()) {
                calculatedFields.add(f);
            }
        }
        return calculatedFields;
    }

    @Override
    public List<QuoteLineField> getDetailNonFrozenFields(Quote quote) {
        List<QuoteLineField> calculatedFields = new ArrayList<QuoteLineField>();
        for (QuoteLineField f : getDetailFields(quote)) {
            if (!f.isFreeze()) {
                calculatedFields.add(f);
            }
        }
        return calculatedFields;
    }

    @Override
    public void update(QuoteLine quoteLine) {
        
    }
}
