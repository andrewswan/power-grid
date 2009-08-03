/*
 * Created on 07/02/2008
 */
package com.andrewswan.powergrid.domain.impl;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.andrewswan.powergrid.domain.IncomeChart;


/**
 * The standard income chart
 */
public class StandardIncomeChart implements IncomeChart {

  // Constants
  private static final int[] INCOMES;
  static final int MIN_INCOME = 10;
  static final int MAX_INCOME = 150;

  static {
    INCOMES = new int[] {
      MIN_INCOME, 22, 33, 44, 54, 64, 73,      // 0-6
      82, 90, 98, 105, 112, 118, 124,          // 7-13
      129, 134, 138, 142, 145, 148, MAX_INCOME // 14-20
    };
  }

  public int[] getAllIncomes() {
    return INCOMES.clone();
  }

  public int getIncome(final int cities) {
    if (cities < 0) {
      throw new IllegalArgumentException("Can't have negative cities");
    }
    if (cities > INCOMES.length - 1) {
      return MAX_INCOME;
    }
    return INCOMES[cities];
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
  }
}
