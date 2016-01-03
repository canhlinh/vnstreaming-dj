package com.vnstreaming.ffmpeg.job;

import com.vnstreaming.ffmpeg.FFmpeg;

/**
 * TODO Read progress from output
 * @author bramp
 *
 */
public abstract class FFmpegJob implements Runnable {

	public static enum State {
		WAITING,
		RUNNING,
		FINISHED,
		FAILED,
	}

	final FFmpeg ffmpeg;
	State state = State.WAITING;

	public FFmpegJob(FFmpeg ffmpeg) {
		this.ffmpeg = ffmpeg;
	}

	public State getState() {
		return state;
	}
}
