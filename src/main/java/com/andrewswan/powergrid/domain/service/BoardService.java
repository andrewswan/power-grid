/**
 * 
 */
package com.andrewswan.powergrid.domain.service;

import com.andrewswan.powergrid.domain.Board;

/**
 * Services relating to the {@link Board} domain type
 * 
 * @author Admin
 */
public interface BoardService {

    /**
     * Returns the available game boards (i.e. geographic maps)
     * 
     * @return a non-<code>null</code> list of resource keys, not in any
     *   specific order because they will require sorting after localisation
     *   anyway
     */
    Iterable<String> getBoardNameCodes();
}
