/*
 * Created on 18/12/2007
 */
package com.andrewswan.powergrid.domain.impl;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.andrewswan.powergrid.domain.Plant;
import com.andrewswan.powergrid.domain.PlantMarket;
import com.andrewswan.powergrid.domain.impl.deck.StandardDeck;
import com.andrewswan.powergrid.domain.impl.plant.CoalPlant;
import com.andrewswan.powergrid.domain.impl.plant.GarbagePlant;
import com.andrewswan.powergrid.domain.impl.plant.HybridPlant;
import com.andrewswan.powergrid.domain.impl.plant.OilPlant;

/**
 * The {@link PlantMarket} used in the standard game
 */
public class StandardPlantMarket extends AbstractPlantMarket {
  
  private static final Set<Plant> STARTING_PLANTS;
  
  static {
    STARTING_PLANTS = new HashSet<Plant>();
    STARTING_PLANTS.add(new OilPlant(3, 2, 1));
    STARTING_PLANTS.add(new CoalPlant(4, 2, 1));
    STARTING_PLANTS.add(new HybridPlant(5, 2, 1));
    STARTING_PLANTS.add(new GarbagePlant(6, 1, 1));
    STARTING_PLANTS.add(new OilPlant(7, 3, 2));
    STARTING_PLANTS.add(new CoalPlant(8, 3, 2));
    STARTING_PLANTS.add(new OilPlant(9, 1, 1));
    STARTING_PLANTS.add(new CoalPlant(10, 2, 2));
  }
  /**
   * Constructor for the standard starting market
   * 
   * @param players the number of players in the game; affects how many plants
   *   are taken out of the deck at the start of the game
   */
  public StandardPlantMarket(int players) {
    super(STARTING_PLANTS, new StandardDeck(players));
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
  }
}
