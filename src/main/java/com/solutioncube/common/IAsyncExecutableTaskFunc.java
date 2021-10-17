package com.solutioncube.common;

import com.solutioncube.pojo.Parameter;

@FunctionalInterface
public interface IAsyncExecutableTaskFunc {

	void execAsync(Parameter parameter);
}