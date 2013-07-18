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
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author Allan Lykke Christensen
 */
@Entity
@Table(name = "expenditure")
public class Expenditure implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "buyer")
    private Account buyer;

    @ManyToOne
    @JoinColumn(name = "brand")
    private AccountBrand buyerBrand;

    @ManyToOne
    @JoinColumn(name = "industry")
    private Industry industry;

    @ManyToOne
    @JoinColumn(name = "seller")
    private Account seller;

    @Column(name = "amount")
    private Double amount;

    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name = "start_date")
    private Date startDate;

    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name = "end_date")
    private Date endDate;

    public Expenditure() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Account getBuyer() {
        return buyer;
    }

    public void setBuyer(Account buyer) {
        this.buyer = buyer;
    }

    public AccountBrand getBuyerBrand() {
        return buyerBrand;
    }

    public void setBuyerBrand(AccountBrand buyerBrand) {
        this.buyerBrand = buyerBrand;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Industry getIndustry() {
        return industry;
    }

    public void setIndustry(Industry industry) {
        this.industry = industry;
    }

    public Account getSeller() {
        return seller;
    }

    public void setSeller(Account seller) {
        this.seller = seller;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Expenditure)) {
            return false;
        }
        Expenditure other = (Expenditure) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dk.i2m.converge.ejb.entities.crm.Expenditure[ id=" + id + " ]";
    }
}
