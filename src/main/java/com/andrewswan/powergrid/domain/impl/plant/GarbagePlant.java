/*
 * Created on 18/12/2007
 */
package com.andrewswan.powergrid.domain.impl.plant;

import com.andrewswan.powergrid.domain.Plant;
import com.andrewswan.powergrid.domain.ResourceMarket.Resource;

/**
 * A garbage-fired {@link Plant}
 */
public class GarbagePlant extends AbstractPlant {

    /**
     * Constructor
     * 
     * @param minimumPrice
     * @param fuelUsage
     * @param capacity
     */
    public GarbagePlant(final int minimumPrice, final int fuelUsage,
            final int capacity) {
        super(minimumPrice, fuelUsage, PlantType.GARBAGE, capacity);
    }

    @Override
    protected boolean isUsable(final Resource resource) {
        return Resource.GARBAGE.equals(resource);
    }
}
