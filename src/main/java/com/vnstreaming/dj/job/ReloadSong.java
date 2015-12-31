package com.vnstreaming.dj.job;

import com.vnstreaming.dj.manager.SongsManager;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * Created by linhnc on 12/31/15.
 */
public class ReloadSong extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext jobContext)
            throws JobExecutionException {
        SongsManager.getInstance().reload();
    }
}
