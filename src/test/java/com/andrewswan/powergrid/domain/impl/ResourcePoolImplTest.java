/*
 * Created on 30/03/2008
 */
package com.andrewswan.powergrid.domain.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.andrewswan.powergrid.domain.ResourcePool;

/**
 * Unit test of the {@link ResourcePool} implementation
 */
public class ResourcePoolImplTest {

    @Test
  public void testEmptyPoolsAreEqual() {
    assertEquals(new ResourcePoolImpl(), new ResourcePoolImpl());
  }
}
