/*
 * Created on 07/02/2008
 */
package com.andrewswan.powergrid.domain.impl;

import com.andrewswan.powergrid.domain.IncomeChart;
import com.andrewswan.powergrid.domain.impl.StandardIncomeChart;

import junit.framework.TestCase;

/**
 * Tests the standard {@link IncomeChart}
 */
public class StandardIncomeChartTest extends TestCase {

  // Fixture
  private IncomeChart incomeChart;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    incomeChart = new StandardIncomeChart();
  }

  public void testGetIncomeForNegativeCities() {
    // Invoke
    try {
      incomeChart.getIncome(-1);
      fail("Shouldn't accept a negative number of cities");
    }
    catch (final IllegalArgumentException expected) {
      // Success
    }
  }

  public void testIncomeForZeroCities() {
    assertEquals(StandardIncomeChart.MIN_INCOME, incomeChart.getIncome(0));
  }

  public void testIncomeForLargestExplicitNumberOfCities() {
    assertEquals(StandardIncomeChart.MAX_INCOME, incomeChart.getIncome(20));
  }

  public void testIncomeForMoreThanLargestExplicitNumberOfCities() {
    assertEquals(StandardIncomeChart.MAX_INCOME, incomeChart.getIncome(21));
  }

  public void testGetAllIncomes() {
    // Invoke
    final int[] allIncomes = incomeChart.getAllIncomes();

    // Check
    assertNotNull(allIncomes);
    assertEquals(21, allIncomes.length);
    assertEquals(StandardIncomeChart.MIN_INCOME, allIncomes[0]);
    assertEquals(
        StandardIncomeChart.MAX_INCOME, allIncomes[allIncomes.length - 1]);
  }

  public void testGetAllIncomesReturnsDefensiveCopy() {
    // Invoke
    final int[] allIncomes = incomeChart.getAllIncomes();
    allIncomes[0] = -1;
    assertEquals(StandardIncomeChart.MIN_INCOME, incomeChart.getIncome(0));
  }
}
