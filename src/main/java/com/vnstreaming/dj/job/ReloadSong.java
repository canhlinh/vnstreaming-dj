package com.vnstreaming.dj.job;

import com.vnstreaming.dj.manager.FFmpegManager;
import com.vnstreaming.dj.manager.SongsManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * Created by linhnc on 12/31/15.
 */
public class ReloadSong extends QuartzJobBean {
    static final Logger logger = LogManager.getLogger(ReloadSong.class.getName());
    @Override
    protected void executeInternal(JobExecutionContext jobContext)
            throws JobExecutionException {
        logger.info("Reload songs in folder");
        FFmpegManager.getInstance().stop();
        SongsManager.getInstance().load();
        FFmpegManager.getInstance().start();
    }
}
