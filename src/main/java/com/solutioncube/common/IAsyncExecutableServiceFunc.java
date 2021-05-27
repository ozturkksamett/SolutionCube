package com.solutioncube.common;

import java.util.Collection;
import java.util.concurrent.Future;

@FunctionalInterface
public interface IAsyncExecutableServiceFunc {


	Collection<Future<Boolean>> runAsync(ServiceRunType serviceRunType);
}
