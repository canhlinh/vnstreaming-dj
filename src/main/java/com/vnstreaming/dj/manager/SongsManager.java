package com.vnstreaming.dj.manager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by linhnc on 12/31/15.
 */
public class SongsManager {
    private static SongsManager ourInstance = new SongsManager();

    public static SongsManager getInstance() {
        return ourInstance;
    }

    static final Logger logger = LogManager.getLogger(SongsManager.class.getName());

    private SongsManager() {
    }

    public void reload() {
        logger.info("reload all songs in folder");
    }

    public String nextSongPath() {
        return "song";
    }
}
