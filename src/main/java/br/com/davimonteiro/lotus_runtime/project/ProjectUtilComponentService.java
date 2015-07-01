package br.com.davimonteiro.lotus_runtime.project;

import br.com.davimonteiro.lotus_runtime.ComponentService;
import br.com.davimonteiro.lotus_runtime.model.LotusComponent;
import br.com.davimonteiro.lotus_runtime.model.LotusProject;

public interface ProjectUtilComponentService extends ComponentService {
	
	public void updateProject(LotusProject project);
	
	public LotusProject getProject();
	
	public LotusComponent getComponent() ;

}
