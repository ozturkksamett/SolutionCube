package com.solutioncube.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.solutioncube.helper.AsyncHelper;

@Component
public class ServiceRunner {

	@Autowired
	private AsyncHelper asyncHelper;

	public Collection<Future<Boolean>> runServicesAsync(List<IService> services, ServiceRunType serviceRunType) {
		
		Collection<Future<Boolean>> futures = new ArrayList<Future<Boolean>>();
		
		services.forEach(service -> {

			IAsyncExecutableServiceFunc serviceFunc = (svcRunType) -> { return service.runAsync(svcRunType); };
			futures.addAll(asyncHelper.runAsync(serviceFunc, serviceRunType));
		});
		
		return futures;
	}	
	
	public void runServices(List<IService> services, ServiceRunType serviceRunType) {

		services.forEach(service -> {
			
			service.run(serviceRunType); 
		});		
	}	
}