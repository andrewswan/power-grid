/**
 * 
 */
package com.andrewswan.powergrid.domain.service.impl;

import java.util.Arrays;

import com.andrewswan.powergrid.domain.service.BoardService;

/**
 * POJO implementation of the {@link BoardService}
 * 
 * @author Admin
 */
public class BoardServiceImpl implements BoardService {

    @Override
    public Iterable<String> getBoardNameCodes() {
        return Arrays.asList("usa");
    }
}
