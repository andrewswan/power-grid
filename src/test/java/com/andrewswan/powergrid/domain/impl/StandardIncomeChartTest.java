/*
 * Created on 07/02/2008
 */
package com.andrewswan.powergrid.domain.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import com.andrewswan.powergrid.domain.IncomeChart;

/**
 * Tests the standard {@link IncomeChart}
 */
public class StandardIncomeChartTest {

  // Fixture
  private IncomeChart incomeChart;

  @Before
  public void setUp() throws Exception {
    incomeChart = new StandardIncomeChart();
  }

  @Test
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

  @Test
  public void testIncomeForZeroCities() {
    assertEquals(StandardIncomeChart.MIN_INCOME, incomeChart.getIncome(0));
  }

  @Test
  public void testIncomeForLargestExplicitNumberOfCities() {
    assertEquals(StandardIncomeChart.MAX_INCOME, incomeChart.getIncome(20));
  }

  @Test
  public void testIncomeForMoreThanLargestExplicitNumberOfCities() {
    assertEquals(StandardIncomeChart.MAX_INCOME, incomeChart.getIncome(21));
  }

  @Test
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

  @Test
  public void testGetAllIncomesReturnsDefensiveCopy() {
    // Invoke
    final int[] allIncomes = incomeChart.getAllIncomes();
    allIncomes[0] = -1;
    assertEquals(StandardIncomeChart.MIN_INCOME, incomeChart.getIncome(0));
  }
}
