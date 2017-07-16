package com.github.yangwk.more.demo.activiti.fincreport;

import java.util.List;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.activiti.engine.task.Task;

/**
预先在activiti ui新建如下组和用户
组->用户
management -> kermit
accountancy -> fozzie
 */
public class FinancialReportProcess {

	/**
	 * 程序执行后打印：
Following task is available for accountancy group: Write monthly financial report
Task for fozzie: Write monthly financial report
Number of tasks for fozzie: 0
Following task is available for management group: Verify monthly financial report
Process instance end time: Mon Jul 17 01:56:31 CST 2017


	 */
	public static void main(String[] args) {
		// Create Activiti process engine
		ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
				.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/activiti6ui?characterEncoding=UTF-8")
				.setJdbcUsername("root")
				.setJdbcPassword("123456")
				.setJdbcDriver("com.mysql.jdbc.Driver")
				.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
		ProcessEngine processEngine = cfg.buildProcessEngine();

		// Get Activiti services
		RepositoryService repositoryService = processEngine.getRepositoryService();
		RuntimeService runtimeService = processEngine.getRuntimeService();

		// Deploy the process definition
		repositoryService.createDeployment().addClasspathResource("FinancialReport.bpmn20.xml").deploy();

		// Start a process instance
	    String procId = runtimeService.startProcessInstanceByKey("financialReport").getId();
		
	    // Get the first task
	    TaskService taskService = processEngine.getTaskService();
	    List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("accountancy").list();
	    for (Task task : tasks) {
	      System.out.println("Following task is available for accountancy group: " + task.getName());

	      // claim it
	      taskService.claim(task.getId(), "fozzie");
	    }
	    
	    
	    // Verify Fozzie can now retrieve the task
	    tasks = taskService.createTaskQuery().taskAssignee("fozzie").list();
	    for (Task task : tasks) {
	      System.out.println("Task for fozzie: " + task.getName());

	      // Complete the task
	      taskService.complete(task.getId());
	    }
	    
	    System.out.println("Number of tasks for fozzie: "
	            + taskService.createTaskQuery().taskAssignee("fozzie").count());

	    // Retrieve and claim the second task
	    tasks = taskService.createTaskQuery().taskCandidateGroup("management").list();
	    for (Task task : tasks) {
	      System.out.println("Following task is available for management group: " + task.getName());
	      taskService.claim(task.getId(), "kermit");
	    }

	    // Completing the second task ends the process
	    for (Task task : tasks) {
	      taskService.complete(task.getId());
	    }

	    // verify that the process is actually finished
	    HistoryService historyService = processEngine.getHistoryService();
	    HistoricProcessInstance historicProcessInstance =
	      historyService.createHistoricProcessInstanceQuery().processInstanceId(procId).singleResult();
	    System.out.println("Process instance end time: " + historicProcessInstance.getEndTime());
		
	    
	}

}
