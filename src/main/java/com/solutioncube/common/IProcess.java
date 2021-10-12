package com.solutioncube.common;

import org.springframework.data.mongodb.core.MongoTemplate;

public interface IProcess {

	void process(MongoTemplate mongoTemplate);
}
