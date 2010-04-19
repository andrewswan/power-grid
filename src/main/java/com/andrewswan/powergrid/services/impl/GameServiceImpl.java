/*
 * Created on 29/03/2008
 */
package com.andrewswan.powergrid.services.impl;

import java.io.File;

import com.andrewswan.powergrid.Utils;
import com.andrewswan.powergrid.domain.Game;
import com.andrewswan.powergrid.services.GameService;

/**
 * Implementation of the {@link GameService}
 */
public class GameServiceImpl implements GameService {

    public Game load(final String directoryName, final String fileName) {
        // final File file = getFile(directoryName, fileName);
        // TODO Implement loading game from disk
        throw new UnsupportedOperationException("Game loading not implemented");
    }

    /**
     * Returns the file specified by the given directory and file names
     * 
     * @param directoryName must be a valid path
     * @param fileName must be a readable file in that directory
     */
    @SuppressWarnings("unused")
    private File getFile(final String directoryName, final String fileName) {
        Utils.checkNotNull(directoryName, fileName);
        final File directory = new File(directoryName);
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(
                    "Invalid directory '" + directoryName + "'");
        }
        final File file = new File(directory, fileName);
        if (!file.canRead()) {
            throw new IllegalArgumentException(
                    "Invalid file '" + fileName + "'");
        }
        return file;
    }

    public void save(
            final Game game, final String directory, final String fileName)
    {
        // TODO Implement saving game to disk
        throw new UnsupportedOperationException("Game saving not implemented");
    }

}
