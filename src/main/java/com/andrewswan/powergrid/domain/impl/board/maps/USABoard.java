/*
 * Created on 21/02/2008
 */
package com.andrewswan.powergrid.domain.impl.board.maps;

import com.andrewswan.powergrid.domain.Area;
import com.andrewswan.powergrid.domain.City;

/**
 * The USA board in the standard game
 */
public class USABoard extends AbstractBoard {

  /**
   * Constructor
   *
   * @param players
   */
  public USABoard(final int players) {
    super("U.S.A.", players);

    // Purple (NW) area
    final Area purple = addArea("Purple");
    final City seattle = addCity("Seattle", purple);
    final City portland = addCity("Portland", purple);
    final City boise = addCity("Boise", purple);
    final City billings = addCity("Billings", purple);
    final City cheyenne = addCity("Cheyenne", purple);
    final City omaha = addCity("Omaha", purple);
    final City denver = addCity("Denver", purple);
    addLink(seattle, portland, 3);
    addLink(seattle, billings, 9);
    addLink(seattle, boise, 12);
    addLink(portland, boise, 13);
    addLink(boise, billings, 12);
    addLink(boise, cheyenne, 24);
    addLink(billings, cheyenne, 9);
    addLink(cheyenne, omaha, 14);
    addLink(cheyenne, denver, 0);

    // Dark Green (SW) area
    final Area darkGreen = addArea("Dark Green");
    final City sanFrancisco = addCity("San Francisco", darkGreen);
    final City losAngeles = addCity("Los Angeles", darkGreen);
    final City sanDiego = addCity("San Diego", darkGreen);
    final City lasVegas = addCity("Las Vegas", darkGreen);
    final City phoenix = addCity("Phoenix", darkGreen);
    final City santaFe = addCity("Santa Fe", darkGreen);
    final City saltLakeCity = addCity("Salt Lake City", darkGreen);
    addLink(portland, sanFrancisco, 24);
    addLink(boise, sanFrancisco, 23);
    addLink(boise, saltLakeCity, 8);
    addLink(denver, saltLakeCity, 21);
    addLink(sanFrancisco, saltLakeCity, 27);
    addLink(sanFrancisco, lasVegas, 14);
    addLink(sanFrancisco, losAngeles, 9);
    addLink(losAngeles, lasVegas, 9);
    addLink(losAngeles, sanDiego, 3);
    addLink(sanDiego, lasVegas, 9);
    addLink(sanDiego, phoenix, 14);
    addLink(lasVegas, phoenix, 15);
    addLink(lasVegas, saltLakeCity, 18);
    addLink(lasVegas, santaFe, 27);
    addLink(phoenix, santaFe, 18);
    addLink(saltLakeCity, santaFe, 28);
    addLink(denver, santaFe, 13);

    // Yellow (N) area
    final Area yellow = addArea("Yellow");
    final City fargo = addCity("Fargo", yellow);
    final City duluth = addCity("Duluth", yellow);
    final City minneapolis = addCity("Minneapolis", yellow);
    final City chicago = addCity("Chicago", yellow);
    final City stLouis = addCity("St. Louis", yellow);
    final City cincinnati = addCity("Cincinnati", yellow);
    final City knoxville = addCity("Knoxville", yellow);
    addLink(billings, fargo, 17);
    addLink(billings, minneapolis, 18);
    addLink(cheyenne, minneapolis, 18);
    addLink(omaha, minneapolis, 8);
    addLink(omaha, chicago, 13);
    addLink(fargo, duluth, 6);
    addLink(fargo, minneapolis, 6);
    addLink(duluth, minneapolis, 5);
    addLink(duluth, chicago, 12);
    addLink(minneapolis, chicago, 8);
    addLink(chicago, stLouis, 10);
    addLink(chicago, cincinnati, 7);
    addLink(stLouis, cincinnati, 12);
    addLink(cincinnati, knoxville, 6);

    // Red (S) area
    final Area red = addArea("Red");
    final City kansasCity = addCity("Kansas City", red);
    final City oklahomaCity = addCity("Oklahoma City", red);
    final City dallas = addCity("Dallas", red);
    final City houston = addCity("Houston", red);
    final City memphis = addCity("Memphis", red);
    final City newOrleans = addCity("New Orleans", red);
    final City birmingham = addCity("Birmingham", red);
    addLink(santaFe, houston, 21);
    addLink(santaFe, dallas, 16);
    addLink(santaFe, oklahomaCity, 15);
    addLink(santaFe, kansasCity, 16);
    addLink(denver, kansasCity, 16);
    addLink(omaha, kansasCity, 5);
    addLink(chicago, kansasCity, 8);
    addLink(stLouis, kansasCity, 6);
    addLink(stLouis, memphis, 7);
    addLink(kansasCity, oklahomaCity, 8);
    addLink(oklahomaCity, dallas, 3);
    addLink(dallas, houston, 5);
    addLink(kansasCity, memphis, 12);
    addLink(oklahomaCity, memphis, 14);
    addLink(dallas, memphis, 12);
    addLink(dallas, newOrleans, 12);
    addLink(houston, newOrleans, 8);
    addLink(memphis, newOrleans, 7);
    addLink(memphis, birmingham, 6);
    addLink(newOrleans, birmingham, 11);

    // Brown (NE) area
    final Area brown = addArea("Brown");
    final City detroit = addCity("Detroit", brown);
    final City buffalo = addCity("Buffalo", brown);
    final City pittsburgh = addCity("Pittsburgh", brown);
    final City boston = addCity("Boston", brown);
    final City newYork = addCity("New York", brown);
    final City philadelphia = addCity("Philadelphia", brown);
    final City washingtonDC = addCity("Washington, D.C.", brown);
    addLink(duluth, detroit, 15);
    addLink(chicago, detroit, 7);
    addLink(cincinnati, detroit, 4);
    addLink(cincinnati, pittsburgh, 7);
    addLink(detroit, buffalo, 7);
    addLink(detroit, pittsburgh, 6);
    addLink(buffalo, newYork, 8);
    addLink(buffalo, pittsburgh, 7);
    addLink(pittsburgh, washingtonDC, 6);
    addLink(boston, newYork, 3);
    addLink(newYork, philadelphia, 0);
    addLink(philadelphia, washingtonDC, 3);

    // Light Green (SE) area
    final Area lightGreen = addArea("Light Green");
    final City atlanta = addCity("Atlanta", lightGreen);
    final City norfolk = addCity("Norfolk", lightGreen);
    final City raleigh = addCity("Raleigh", lightGreen);
    final City savannah = addCity("Savannah", lightGreen);
    final City jacksonville = addCity("Jacksonville", lightGreen);
    final City tampa = addCity("Tampa", lightGreen);
    final City miami = addCity("Miami", lightGreen);
    addLink(newOrleans, jacksonville, 16);
    addLink(birmingham, jacksonville, 9);
    addLink(birmingham, atlanta, 3);
    addLink(stLouis, atlanta, 12);
    addLink(knoxville, atlanta, 5);
    addLink(cincinnati, raleigh, 15);
    addLink(pittsburgh, raleigh, 7);
    addLink(washingtonDC, norfolk, 5);
    addLink(norfolk, raleigh, 3);
    addLink(atlanta, raleigh, 7);
    addLink(atlanta, savannah, 7);
    addLink(raleigh, savannah, 7);
    addLink(savannah, jacksonville, 0);
    addLink(jacksonville, tampa, 4);
    addLink(tampa, miami, 4);
  }
}
