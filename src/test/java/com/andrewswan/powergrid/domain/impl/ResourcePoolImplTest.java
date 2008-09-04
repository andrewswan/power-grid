/*
 * Created on 30/03/2008
 */
package com.andrewswan.powergrid.domain.impl;

import com.andrewswan.powergrid.domain.ResourcePool;

import junit.framework.TestCase;

/**
 * Unit test of the {@link ResourcePool} implementation
 */
public class ResourcePoolImplTest extends TestCase {

  public void testEmptyPoolsAreEqual() {
    assertEquals(new ResourcePoolImpl(), new ResourcePoolImpl());
  }
}
