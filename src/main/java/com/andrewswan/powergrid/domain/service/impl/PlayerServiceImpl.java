/**
 * 
 */
package com.andrewswan.powergrid.domain.service.impl;

import java.util.HashMap;
import java.util.Map;

import com.andrewswan.powergrid.domain.PlayerStrategy;
import com.andrewswan.powergrid.domain.impl.player.strategy.DoNothingStrategy;
import com.andrewswan.powergrid.domain.service.PlayerService;


/**
 * POJO implementation of the {@link PlayerService}
 *  
 * @author Admin
 */
public class PlayerServiceImpl implements PlayerService {

    @Override
    public Map<Class<? extends PlayerStrategy>, String> getComputerPlayerStrategies() {
        final Map<Class<? extends PlayerStrategy>, String> map =
            new HashMap<Class<? extends PlayerStrategy>, String>();
        map.put(DoNothingStrategy.class, "ai_do_nothing");
        return map;
    }
}
