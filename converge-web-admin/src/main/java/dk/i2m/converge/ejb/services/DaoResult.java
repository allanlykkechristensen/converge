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
package dk.i2m.converge.ejb.services;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Allan Lykke Christensen
 */
public class DaoResult<T> {

    private List<T> results = new ArrayList<T>();

    private long page = 1;

    private long numberOfResults = 0;

    private int resultsPerPage = 0;

    private long searchTime = 0;

    public DaoResult() {
    }

    /**
     * Get the number of pages in the {@link DaoResult}.
     *
     * @return Number of pages in the {@link DaoResult}
     */
    public long getNumberOfPages() {
        if (numberOfResults <= resultsPerPage) {
            return 1;
        } else {
            long pageCount = (numberOfResults + resultsPerPage - 1) / resultsPerPage;
            return pageCount;
        }
    }

    public long getPage() {
        return page;
    }

    public void setPage(long page) {
        this.page = page;
    }

    /**
     * Gets the page currently contained in the {@link DaoResult}.
     *
     * @return Page currently contained in the {@link DaoResult}
     */
    public long getCurrentPage() {
        return page / resultsPerPage;
    }

    public List<T> getResults() {
        return this.results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }

    public long getNumberOfResults() {
        return numberOfResults;
    }

    public void setNumberOfResults(long numberOfResults) {
        this.numberOfResults = numberOfResults;
    }

    public int getResultsPerPage() {
        return resultsPerPage;
    }

    public void setResultsPerPage(int resultsPerPage) {
        this.resultsPerPage = resultsPerPage;
    }

    public long getSearchTime() {
        return searchTime;
    }

    public void setSearchTime(long searchTime) {
        this.searchTime = searchTime;
    }

    public double getSearchTimeInSeconds() {
        return ((double) searchTime) / 1000.0;
    }
}
