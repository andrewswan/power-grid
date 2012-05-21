/*
 * Created on 30/03/2008
 */
package com.andrewswan.powergrid.domain.impl;

import junit.framework.TestCase;

import com.andrewswan.powergrid.domain.ResourcePool;

/**
 * Unit test of the {@link ResourcePool} implementation
 */
public class ResourcePoolImplTest extends TestCase {

  public void testEmptyPoolsAreEqual() {
    assertEquals(new ResourcePoolImpl(), new ResourcePoolImpl());
  }
}
