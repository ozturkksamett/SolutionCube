package com.solutioncube.helper;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.solutioncube.common.IService;
import com.solutioncube.common.TaskType;

@Component
public class ServiceRunner {

	private static final Logger logger = LoggerFactory.getLogger(ServiceRunner.class);
	
	@Autowired
	private AsyncHelper asyncHelper;

	public void runServices(List<IService> services, TaskType taskType, boolean isAsync) {
    	
		ApiErrorLogger.clear();
    	Instant start = Instant.now();		
		Collection<Future<Boolean>> futures = new ArrayList<Future<Boolean>>();		
		services.forEach(service -> {

			futures.addAll(service.run(taskType, isAsync));
		});
		asyncHelper.waitTillEndOfSynchronizedFunc(futures);					
		Instant finish = Instant.now();
		logger.info(String.format("%s services finished running. Duration: %d minutes."
				, services.stream().map(IService::getServiceName).collect(Collectors.joining(", "))
				, Duration.between(start, finish).toMinutes()));  
		ApiErrorLogger.print();		
	}	
	
	public void runService(IService service, TaskType taskType, boolean isAsync) {
    	
		ApiErrorLogger.clear();
    	Instant start = Instant.now();
		asyncHelper.waitTillEndOfSynchronizedFunc(service.run(taskType, isAsync));	    	
		Instant finish = Instant.now();
		logger.info(String.format("%s service finished running. Duration: %d minutes."
				, service.getServiceName()
				, Duration.between(start, finish).toMinutes()));
		ApiErrorLogger.print(); 
	}	
}