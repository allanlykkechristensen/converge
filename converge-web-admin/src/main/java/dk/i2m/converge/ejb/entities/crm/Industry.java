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
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

/**
 * {@link Industry} that an {@link Account} belongs.
 *
 * @author Allan Lykke Christensen
 */
@Entity
@Table(name = "industry")
public class Industry implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "industry_broader",
    joinColumns = {
        @JoinColumn(referencedColumnName = "id", name = "broader_id", nullable = false)},
    inverseJoinColumns = {
        @JoinColumn(referencedColumnName = "id", name = "narrower_id", nullable = false)})
    private List<Industry> broader = new ArrayList<Industry>();

    @ManyToMany(mappedBy = "broader")
    private List<Industry> narrower = new ArrayList<Industry>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Industry> getBroader() {
        return broader;
    }

    public void setBroader(List<Industry> broader) {
        this.broader = broader;
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

    public List<Industry> getNarrower() {
        return narrower;
    }

    public void setNarrower(List<Industry> narrower) {
        this.narrower = narrower;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Industry)) {
            return false;
        }
        Industry other = (Industry) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dk.i2m.converge.ejb.entities.crm.Industry[ id=" + id + " ]";
    }
}
