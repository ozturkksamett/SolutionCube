package com.solutioncube.common;

import com.solutioncube.pojo.Parameter;

public interface ITask extends ICollection {

	void execute(Parameter parameter);
}
