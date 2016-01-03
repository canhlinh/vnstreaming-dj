package com.vnstreaming.dj.manager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import com.vnstreaming.dj.App;
import org.springframework.util.StringUtils;

/**
 * Created by linhnc on 12/31/15.
 */
public class SongsManager {
    static final Logger logger = LogManager.getLogger(SongsManager.class.getName());
    private static SongsManager ourInstance;
    private final String[] AUDIO_FILE = {"mp3", "m4a"};
    private ArrayList<String> musicList = new ArrayList<String>();
    private File musicFolder;
    int currentSongIndex = -1;

    public static SongsManager getInstance() {
        if(ourInstance == null) {
            ourInstance = (SongsManager) App.getBean("songsManager");
        }
        return ourInstance;
    }

    private SongsManager() {
    }

    public void setMusicPath(String path) {
        try {
            logger.info("Set music path : " + path);
            this.musicFolder = new File(path);
            this.load();
        }catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public String getMusicPath() {
        return this.musicFolder.getPath();
    }

    public void load() {
        logger.info("Reload all songs in folder");
        musicList.clear();
        for (final File fileEntry : musicFolder.listFiles()) {
            if (!fileEntry.isDirectory()) {
                String fileName = convertFileName(fileEntry);
                if(isAudioFile(fileName))
                    musicList.add(fileName);
            }
        }
    }

    /**
     * convert local file name to linux stand file name
     * @param file
     * @return linux stand file name
     */
    private String convertFileName(File file) {
        String currentName = file.getName();
        String tmp = Normalizer.normalize(currentName, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").toLowerCase();
        tmp = tmp.replaceAll("\\s", "_");
        tmp = tmp.replaceAll("[^a-zA-Z0-9._]", "");
        String standOutput = "";
        int l = tmp.length();
        for(int i = 0; i < l ; i++) {
            if(tmp.charAt(i) == '_' && tmp.charAt(i+1) == '_') {
                continue;
            }
            standOutput += tmp.charAt(i);
        }
        if(!standOutput.equals(currentName)) {
            logger.debug("Convert file name " + currentName + " to : " + standOutput);
            File newFile = new File(getMusicPath() + File.separator + standOutput);
            if (!file.renameTo(newFile)) {
                logger.warn("File : " + currentName + " rename failure");
            }
            ;
        }
        return standOutput;
    }

    private boolean isAudioFile(String fileName) {
        if(!StringUtils.isEmpty(fileName)) {
            int i = fileName.lastIndexOf('.');
            if (i > 0) {
                String extension = fileName.substring(i + 1);
                return (Arrays.asList(AUDIO_FILE).indexOf(extension) >= 0);
            }
        }
        return false;
    }

    public String nextSongName() {
        if(musicList.size() == 0 )
            return null;
        ++currentSongIndex;
        if(currentSongIndex >= musicList.size())
            currentSongIndex = 0;
        return musicList.get(currentSongIndex);
    }
}
