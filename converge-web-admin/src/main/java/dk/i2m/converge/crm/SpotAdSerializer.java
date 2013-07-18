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

import dk.i2m.converge.ejb.entities.crm.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Serializer for Spot ads. This serializer contains a broadcast pattern that
 * can be filled out in detail mode and a summary broadcast pattern (aggregated)
 * shown in summary mode.
 *
 * @author Allan Lykke Christensen
 */
public class SpotAdSerializer implements QuoteLineSerializer {

    private List<QuoteLineField> fields = null;

    private List<QuoteLineField> detailFields = null;

    private SimpleDateFormat dayFormat = new SimpleDateFormat("E d/M");

    private SimpleDateFormat periodFormat = new SimpleDateFormat("d/M");

    public enum Qualifier {

        BROADCAST_PATTERN,
        BROADCAST_PATTERN_SUMMARY

    }

    public enum Field {

        DAY_PART,
        LENGHT,
        START,
        END,
        SUNDAYS,
        MONDAYS,
        TUESDAYS,
        WEDNESDAYS,
        THURSDAYS,
        FRIDAYS,
        SATURDAYS,
        TITLE,
        RATE_CARD

    }

    @Override
    public boolean isDiscountPossible() {
        return true;
    }
    
    @Override
    public boolean isRateVisible() {
        return true;
    }

    /** {@inheritDoc } */
    @Override
    public List<QuoteLineField> getFields(Quote quote) {
        if (fields == null) {
            this.fields = new ArrayList<QuoteLineField>();
            this.fields.add(new QuoteLineField(Field.RATE_CARD.name(), "Select rate card", "Day part", "", "text", "input-mini", 1, false, false, false));
            this.fields.add(new QuoteLineField(Field.LENGHT.name(), "Length", "Length", "", "text", "input-mini", 2, false, false, false));
            this.fields.add(new QuoteLineField(Field.SUNDAYS.name(), "S", "S", "", "int", "input-mini", Qualifier.BROADCAST_PATTERN_SUMMARY.toString(), 3, true, false, false));
            this.fields.add(new QuoteLineField(Field.MONDAYS.name(), "M", "M", "", "int", "input-mini", Qualifier.BROADCAST_PATTERN_SUMMARY.toString(), 4, true, false, false));
            this.fields.add(new QuoteLineField(Field.TUESDAYS.name(), "T", "T", "", "int", "input-mini", Qualifier.BROADCAST_PATTERN_SUMMARY.toString(), 5, true, false, false));
            this.fields.add(new QuoteLineField(Field.WEDNESDAYS.name(), "W", "W", "", "int", "input-mini", Qualifier.BROADCAST_PATTERN_SUMMARY.toString(), 6, true, false, false));
            this.fields.add(new QuoteLineField(Field.THURSDAYS.name(), "T", "T", "", "int", "input-mini", Qualifier.BROADCAST_PATTERN_SUMMARY.toString(), 7, true, false, false));
            this.fields.add(new QuoteLineField(Field.FRIDAYS.name(), "F", "F", "", "int", "input-mini", Qualifier.BROADCAST_PATTERN_SUMMARY.toString(), 8, true, false, false));
            this.fields.add(new QuoteLineField(Field.SATURDAYS.name(), "S", "S", "", "int", "input-mini", Qualifier.BROADCAST_PATTERN_SUMMARY.toString(), 9, true, false, false));
            this.fields.add(new QuoteLineField(Field.START.name(), "Start", "Start", "", "date", "input-mini", 11, false, false, false));
            this.fields.add(new QuoteLineField(Field.END.name(), "End", "End", "", "date", "input-mini", 12, false, false, false));
            this.fields.add(new QuoteLineField(Field.TITLE.name(), "Title", "Title", "", "text", "input-mini", 13, false, false, false));
        }
        return fields;

    }

    /** {@inheritDoc } */
    @Override
    public List<QuoteLineField> getDetailFields(Quote quote) {
        if (this.detailFields == null) {
            this.detailFields = new ArrayList<QuoteLineField>();
            this.detailFields.add(new QuoteLineField(Field.RATE_CARD.name(), "Select rate card", "Rate Card", "", "rateCard", "input-medium", 1, false, true, false));
            this.detailFields.add(new QuoteLineField(Field.LENGHT.name(), "", "Size", "", "text", "input-medium", 2, false, true, true));
            Calendar quoteStartDate = Calendar.getInstance();
            Calendar startDate = Calendar.getInstance();
            startDate.setTime(quote.getStartDate());
            quoteStartDate.setTime(quote.getStartDate());
            Calendar endDate = Calendar.getInstance();
            endDate.setTime(quote.getEndDate());

            // Subtract a day until we are at monday
            int add = 0;
            while (startDate.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                add++;
                startDate.add(Calendar.DAY_OF_MONTH, -1);
            }

            int displayOrder = 3;
            while (startDate.before(endDate)) {
                QuoteLineField qlf = new QuoteLineField(dayFormat.format(startDate.getTime()),
                        "" + startDate.getTimeInMillis(),
                        dayFormat.format(startDate.getTime()),
                        "", "int", "input-mini", Qualifier.BROADCAST_PATTERN.toString(), displayOrder++, true, false, false);
                if (startDate.before(quoteStartDate)) {
                    qlf.setReadOnly(true);
                }
                this.detailFields.add(qlf);
                startDate.add(Calendar.DAY_OF_MONTH, 1);
            }

            while (startDate.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
                this.detailFields.add(new QuoteLineField(dayFormat.format(startDate.getTime()), "" + startDate.getTimeInMillis(), dayFormat.format(startDate.getTime()), "", "int", "input-mini", Qualifier.BROADCAST_PATTERN.toString(), displayOrder++, true, false, true));
                startDate.add(Calendar.DAY_OF_MONTH, 1);
            }

            this.detailFields.add(new QuoteLineField(Field.TITLE.name(), "Title", "Title", "", "text", "input-medium", displayOrder++, false, false, false));
        }
        return detailFields;
    }

    /** {@inheritDoc } */
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

    /** {@inheritDoc } */
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

    /** {@inheritDoc } */
    @Override
    public void update(QuoteLine quoteLine) {
        //quoteLine.setBookRate(BigDecimal.ZERO);
        //quoteLine.setDiscount(BigDecimal.ZERO);

        // Set default quantity
        BigDecimal quantity = BigDecimal.ZERO;

        // Set default start and end date
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        start.setTime(quoteLine.getSection().getQuote().getStartDate());
        end.setTime(quoteLine.getSection().getQuote().getStartDate());

        BigInteger mondays = BigInteger.ZERO;
        BigInteger tuesdays = BigInteger.ZERO;
        BigInteger wednesdays = BigInteger.ZERO;
        BigInteger thursdays = BigInteger.ZERO;
        BigInteger fridays = BigInteger.ZERO;
        BigInteger saturdays = BigInteger.ZERO;
        BigInteger sundays = BigInteger.ZERO;

        for (QuoteLineProperty property : quoteLine.getProperties()) {
            if (property.getQualifier() != null && property.getQualifier().equals(Qualifier.BROADCAST_PATTERN.toString())) {

                try {
                    // Quantity of spots in the pattern
                    BigInteger add = new BigInteger(property.getValue());
                    boolean noAds = add == BigInteger.ZERO;
                    quantity = quantity.add(new BigDecimal(add));

                    //Date date = dayFormat.parse(property.getName());
                    Calendar propertyDate = Calendar.getInstance();
                    propertyDate.setTimeInMillis(Long.valueOf(property.getName()));
                    if (propertyDate.before(start) && !noAds) {
                        start.setTime(propertyDate.getTime());
                    }

                    if (propertyDate.after(end) && !noAds) {
                        end.setTime(propertyDate.getTime());
                    }

                    if (propertyDate.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                        mondays = mondays.add(add);
                    } else if (propertyDate.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {
                        tuesdays = tuesdays.add(add);
                    } else if (propertyDate.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {
                        wednesdays = wednesdays.add(add);
                    } else if (propertyDate.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
                        thursdays = thursdays.add(add);
                    } else if (propertyDate.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
                        fridays = fridays.add(add);
                    } else if (propertyDate.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                        saturdays = saturdays.add(add);
                    } else if (propertyDate.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                        sundays = sundays.add(add);
                    }

                } catch (NumberFormatException ex) {
                    // Swallow exception as it may occur often and not have any impact
                }
            }
        }

        for (QuoteLineProperty property : quoteLine.getProperties()) {
            if (property.getNamedIdentifier().equals(Field.TITLE.name())) {
                property.setName(property.getValue());
            } else if (property.getNamedIdentifier().equals(Field.START.name())) {
                property.setValue(periodFormat.format(start.getTime()));
                property.setName(periodFormat.format(start.getTime()));
            } else if (property.getNamedIdentifier().equals(Field.END.name())) {
                property.setValue(periodFormat.format(end.getTime()));
                property.setName(periodFormat.format(end.getTime()));
            } else if (property.getNamedIdentifier().equals(Field.MONDAYS.name())) {
                property.setValue(mondays.toString());
                property.setName(mondays.toString());
            } else if (property.getNamedIdentifier().equals(Field.TUESDAYS.name())) {
                property.setValue(tuesdays.toString());
                property.setName(tuesdays.toString());
            } else if (property.getNamedIdentifier().equals(Field.WEDNESDAYS.name())) {
                property.setValue(wednesdays.toString());
                property.setName(wednesdays.toString());
            } else if (property.getNamedIdentifier().equals(Field.THURSDAYS.name())) {
                property.setValue(thursdays.toString());
                property.setName(thursdays.toString());
            } else if (property.getNamedIdentifier().equals(Field.FRIDAYS.name())) {
                property.setValue(fridays.toString());
                property.setName(fridays.toString());
            } else if (property.getNamedIdentifier().equals(Field.SATURDAYS.name())) {
                property.setValue(saturdays.toString());
                property.setName(saturdays.toString());
            } else if (property.getNamedIdentifier().equals(Field.SUNDAYS.name())) {
                property.setValue(sundays.toString());
                property.setName(sundays.toString());
            }
        }


        quoteLine.setQuantity(quantity);
    }
}
