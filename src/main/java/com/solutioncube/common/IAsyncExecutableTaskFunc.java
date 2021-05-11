package com.solutioncube.common;

import com.solutioncube.pojo.TaskParameter;

@FunctionalInterface
public interface IAsyncExecutableTaskFunc {

	void execAsync(TaskParameter taskParameter);
}