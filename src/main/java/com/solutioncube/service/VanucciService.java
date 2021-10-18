package com.solutioncube.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.solutioncube.collection.AlarmHistoryReport;
import com.solutioncube.collection.EnergyMeasurementsHistoryReport;
import com.solutioncube.collection.EnergyMeters;
import com.solutioncube.collection.SensorMeasurementHistoryReport;
import com.solutioncube.collection.Sensors;
import com.solutioncube.common.IService;
import com.solutioncube.common.ITask;
import com.solutioncube.common.ExecutionType;
import com.solutioncube.common.IProcess;
import com.solutioncube.helper.CacheManager;
import com.solutioncube.helper.Executor;
import com.solutioncube.helper.MongoTemplateGenerator;

@Service
@Qualifier("vanucciService")
public class VanucciService implements IService {

	private static final String SERVICE_NAME = "vanucci";
	
	private static final int CONFIG_INDEX = 1;
	
	private static final List<ITask> STATIC_COLLECTIONS = Arrays.asList(new ITask[] {

			new Sensors()
			,new EnergyMeters()
	});

	private static final List<ITask> DAILY_COLLECTIONS = Arrays.asList(new ITask[] {

			new EnergyMeasurementsHistoryReport()
			,new AlarmHistoryReport()
			,new SensorMeasurementHistoryReport()
	});

	private static final List<IProcess> COLLECTIONS_TO_BE_PROCESSED = Arrays.asList(new IProcess[] {

			new AlarmHistoryReport()			
			,new SensorMeasurementHistoryReport()
	});
	
	@Autowired
	private Executor executor;	

	@Autowired
	private MongoTemplateGenerator mongoTemplateGenerator;
	
	@Override
	public Collection<Future<Boolean>> run(ExecutionType executionType, boolean isAsync) {

		Collection<Future<Boolean>> futures = new ArrayList<Future<Boolean>>();
		
		switch (executionType) {
		case STATIC_COLLECTIONS :			
			futures = executor.execTasks(STATIC_COLLECTIONS, CONFIG_INDEX, isAsync);
			break;
		case DAILY_COLLECTIONS :			
			futures = executor.execTasks(DAILY_COLLECTIONS, CONFIG_INDEX, isAsync);
			break;
		case BULK_DATA_ONLY_WITH_SINCE_PARAM:
			break;
		case BULK_DATA_WITH_BOTH_SINCE_AND_TILL_PARAM:
			break;
		case PROCESS_DAILY_COLLECTIONS:
			futures = executor.execProcesses(COLLECTIONS_TO_BE_PROCESSED, CONFIG_INDEX, isAsync);
			break;
		case PROCESS_CONVERSION:
			CacheManager.clear();
			for (IProcess process : COLLECTIONS_TO_BE_PROCESSED) 
				CacheManager.add(process.getCollectionName()+CONFIG_INDEX, mongoTemplateGenerator.generateMongoTemplate(CONFIG_INDEX).findAll(JSONObject.class, process.getCollectionName()));
			futures = executor.execProcesses(COLLECTIONS_TO_BE_PROCESSED, CONFIG_INDEX, isAsync);
			break;
		default:
			break;
		}
		
		return futures;
	}

	@Override
	public String getServiceName() {

		return SERVICE_NAME;
	}
}