package com.solutioncube.common;

import java.util.Collection;
import java.util.concurrent.Future;

public interface IService {

	void run(ServiceRunType serviceRunType);
	Collection<Future<Boolean>> runAsync(ServiceRunType serviceRunType);
}