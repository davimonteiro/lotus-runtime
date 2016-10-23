/**
 * The MIT License
 * Copyright © 2016 Davi Monteiro Barbosa
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package br.com.davimonteiro.lotus_runtime.monitor;

import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.Map;

import lombok.extern.java.Log;

@Log
public class FileWatcher {

	private Path traceFile;
	private LineNumberReader lineReader;
	
	private WatchService watcher;
	private Map<WatchKey, Path> keys;
	private MonitorComponentService monitorService;

	public FileWatcher(MonitorComponentService monitorService) throws IOException {
		this.monitorService = monitorService;
		init();
	}

	private void init() throws IOException {
		this.watcher = FileSystems.getDefault().newWatchService();
		this.keys = new HashMap<WatchKey, Path>();
		this.traceFile = monitorService.getTracePath();
		this.register(traceFile.getParent());
		
		lineReader = new LineNumberReader(Files.newBufferedReader(traceFile));
		lineReader.mark(0);
	}

	public void processEvents() throws InterruptedException {
		// Wait for an event
		WatchKey wk = watcher.take();
		
		// Process
		wk.pollEvents().forEach(event -> process(event));

		// reset the key
		boolean valid = wk.reset();
		if (!valid) {
			log.info("Key has been unregisterede");
		}
	}
	
	private void process(WatchEvent<?> event) {
		Path changed = (Path) event.context();
		if (changed.endsWith(traceFile.getFileName())) {
			readTrace();
		}
	}
	
	private void register(Path dir) throws IOException {
		WatchKey key = dir.register(watcher, ENTRY_MODIFY);
		log.info("register: " + dir.toString());
		keys.put(key, dir);
	}
	
	public void readTrace() {
		try {
			lineReader.reset();
			
			String line = null;
			while ((line = lineReader.readLine()) != null) {
				if (!line.isEmpty()) {
					log.info(lineReader.getLineNumber() + " : " + line);
					String[] trace = line.split(",");
					monitorService.processTrace(trace);
				}
			}
			
			lineReader.mark(0);
			log.info("Reading the file. Done!");
		} catch (IOException e) {
			e.printStackTrace();
			log.severe(e.getMessage());
		}
	}

}
