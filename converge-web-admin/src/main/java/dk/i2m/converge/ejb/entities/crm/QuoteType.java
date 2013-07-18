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

import dk.i2m.converge.ejb.entities.workflow.WorkflowDefinition;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

/**
 * Type of {@link Quote} containing information about its workflow and quote 
 * lines.
 *
 * @author Allan Lykke Christensen
 */
@Entity
@Table(name = "quote_type")
public class QuoteType implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Unique identifier of the {@link QuoteType}. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /** Name of the {@link QuoteType}. */
    @Column(name = "name")
    private String name;

    /** Description of the purpose of the {@link QuoteType}. */
    @Column(name = "description")
    @Lob
    private String description;

    /** Workflow to use for {@link Quote}s of this type. */
    @ManyToOne
    @JoinColumn(name = "workflow_definition_id")
    private WorkflowDefinition quoteWorkflow;

    /** Default duration for quotes of this type. */
    @Column(name = "default_duration")
    private Integer defaultDuration = 1;

    /** Default number of days to add to the quote as the start date. */
    @Column(name = "default_start_date")
    private Integer defaultStartDate = 0;

    /** Sections that much appear in a {@link Quote} of this type. */
    @OneToMany(mappedBy = "quoteType")
    @OrderBy("displayOrder ASC")
    private List<QuoteSectionType> sections = new ArrayList<QuoteSectionType>();

    /**
     * Creates a new instance of {@link QuoteType}.
     */
    public QuoteType() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public WorkflowDefinition getQuoteWorkflow() {
        return quoteWorkflow;
    }

    public void setQuoteWorkflow(WorkflowDefinition quoteWorkflow) {
        this.quoteWorkflow = quoteWorkflow;
    }

    public Integer getDefaultDuration() {
        return defaultDuration;
    }

    public void setDefaultDuration(Integer defaultDuration) {
        this.defaultDuration = defaultDuration;
    }

    public Integer getDefaultStartDate() {
        return defaultStartDate;
    }

    public void setDefaultStartDate(Integer defaultStartDate) {
        this.defaultStartDate = defaultStartDate;
    }

    public List<QuoteSectionType> getSections() {
        return sections;
    }

    public void setSections(List<QuoteSectionType> sections) {
        this.sections = sections;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof QuoteType)) {
            return false;
        }
        QuoteType other = (QuoteType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dk.i2m.converge.ejb.entities.crm.QuoteType[ id=" + id + " ]";
    }
}
