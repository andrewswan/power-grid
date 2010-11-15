/**
 * 
 */
package com.andrewswan.powergrid.domain.service;

import java.util.Map;

import com.andrewswan.powergrid.domain.Player;
import com.andrewswan.powergrid.domain.PlayerStrategy;

/**
 * Services relating to the {@link Player} domain type.
 * 
 * @author Admin
 */
public interface PlayerService {

    /**
     * Returns details of the available AI strategies
     * 
     * @return a non-<code>null</code> map of class names to resource keys
     */
    Map<Class<? extends PlayerStrategy>, String> getComputerPlayerStrategies();
}
