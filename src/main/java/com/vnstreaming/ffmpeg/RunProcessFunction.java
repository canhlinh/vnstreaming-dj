package com.vnstreaming.ffmpeg;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import org.apache.commons.io.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;

/**
 * Simple function that creates a Process with the arguments, and returns
 * a BufferedReader reading stdout 
 * 
 * @author bramp
 *
 */
public class RunProcessFunction implements ProcessFunction {

	public BufferedReader run(List<String> args) throws IOException {

		Preconditions.checkNotNull(args, "Arguments must not be null");
		Preconditions.checkArgument(!args.isEmpty(), "No arguments specified");

		File file = new File("vnstreaming-dj.log");
        ProcessBuilder builder = new ProcessBuilder(args);
        builder.redirectErrorStream(false);
		builder.redirectError(ProcessBuilder.Redirect.appendTo(file));
		builder.redirectError(ProcessBuilder.Redirect.INHERIT);
        Process p = builder.start();
        return new BufferedReader( new InputStreamReader(p.getInputStream(), Charsets.UTF_8) );
	}

}
