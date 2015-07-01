package br.com.davimonteiro.lotus_runtime.monitor;

import java.nio.file.Path;

import br.com.davimonteiro.lotus_runtime.ComponentService;

public interface TraceWatcherComponentService extends ComponentService {
	
	public void updateModel(String[] trace);
	
	public Path getTraceFile();
	
}
