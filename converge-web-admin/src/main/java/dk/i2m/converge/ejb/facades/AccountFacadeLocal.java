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

import java.util.List;
import javax.ejb.Local;

/**
 * Local interface for {@link AccountFacade}.
 *
 * @author Allan Lykke Christensen
 */
@Local
public interface AccountFacadeLocal {

    List<dk.i2m.converge.ejb.entities.crm.Account> findAccounts();

    /**
     * Gets a {@link List} of all available {@link PaymentTerms}.
     * 
     * @return {@link List} of all available {@link PaymentTerms}
     */
    List<dk.i2m.converge.ejb.entities.crm.PaymentTerms> findPaymentTerms();
}
