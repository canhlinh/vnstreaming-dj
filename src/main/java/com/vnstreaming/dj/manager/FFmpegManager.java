package com.vnstreaming.dj.manager;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.*;

import com.vnstreaming.dj.App;
import com.vnstreaming.ffmpeg.FFmpeg;
import com.vnstreaming.ffmpeg.FFmpegExecutor;
import com.vnstreaming.ffmpeg.builder.FFmpegBuilder;
import com.vnstreaming.ffmpeg.job.FFmpegJob;
import com.vnstreaming.ffmpeg.job.SinglePassFFmpegJob;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StringUtils;

/**
 * Created by linhnc on 12/31/15.
 */
public class FFmpegManager  {
    private static FFmpegManager ourInstance;
    public static FFmpegManager getInstance() {
        if(ourInstance == null) {
            ourInstance = (FFmpegManager) App.getBean("ffmpegManager");
        }
        return ourInstance;
    }
    private static Logger logger = LogManager.getLogger(FFmpegManager.class.getName());
    ExecutorService executor = Executors.newSingleThreadExecutor();
    FFmpeg ffmpeg;
    Future<?> future;
    private boolean startStreaming = true;
    String imagePath;
    String rtmpPath;

    private FFmpegManager() {
        try {
            ffmpeg = new FFmpeg();
        } catch (Exception ex) {
            logger.error("Has error when start ffmpeg : " + ex.getMessage());
        }
    }

    public void setImagePath(String value) {
        this.imagePath = value;
        logger.debug("Image path : " + imagePath);
    }

    public void setRtmpPath(String value) {
        this.rtmpPath = value;
    }

    public void start() {
        while (startStreaming) {
            String song = SongsManager.getInstance().nextSongName();
            if (!StringUtils.isEmpty(song)) {
                logger.debug("Streaming start song " + song);
                String songPath = SongsManager.getInstance().getMusicPath() + File.separator + song;
                FFmpegBuilder builder = new FFmpegBuilder()
 //                       .setImage(imagePath)
                        .setInput(songPath)
                        .overrideOutputFiles(false)
                        .addOutput(rtmpPath)
                            .setFormat("flv")
                            .setAudioCodec("aac")
                            .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL)
//                            .setVideoCodec("libx264")
//                            .setVideoFrameRate(5)
//                            .setPixelFormat("yuv420p")
//                            .setVideoPreset("ultrafast")
                            .done();

                final List<String> args = builder.build();
                logger.debug("FFmpeg parameter : " + args.toString());
                FFmpegJob job = new SinglePassFFmpegJob(ffmpeg, args);
                try {
                    runAndWait(job);
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            } else {
                startStreaming = false;
                logger.warn("Stop stream");
            }

        }
        stop();
    }

    public void stop() {
        startStreaming = false;
        executor.shutdown();
        if(future != null)
            future.cancel(true);
        logger.info("Streaming stoped");
    }

    protected void runAndWait(FFmpegJob job) throws ExecutionException, InterruptedException {
        future = executor.submit(job);

        while (!future.isDone()) {
            try {
                future.get(100, TimeUnit.MILLISECONDS);
                break;
            } catch (TimeoutException e) {}
        }
    }
}
