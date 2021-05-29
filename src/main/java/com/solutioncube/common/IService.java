package com.solutioncube.common;

import java.util.Collection;
import java.util.concurrent.Future;

public interface IService {

	Collection<Future<Boolean>> run(TaskType taskType, boolean isAsync);
	String getServiceName();
}