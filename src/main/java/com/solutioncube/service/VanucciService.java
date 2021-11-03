package com.solutioncube.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Future;

import org.bson.Document;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.solutioncube.collection.AlarmHistoryReport;
import com.solutioncube.collection.EnergyMeasurementsHistoryReport;
import com.solutioncube.collection.EnergyMeters;
import com.solutioncube.collection.SensorMeasurementHistoryReport;
import com.solutioncube.collection.Sensors;
import com.solutioncube.common.ExecutionType;
import com.solutioncube.common.IProcess;
import com.solutioncube.common.IService;
import com.solutioncube.common.ITask;
import com.solutioncube.helper.AsyncHelper;
import com.solutioncube.helper.CacheManager;
import com.solutioncube.helper.Executor;
import com.solutioncube.helper.MongoTemplateGenerator;
import com.solutioncube.helper.ParameterGenerator;
import com.solutioncube.pojo.Parameter;

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
	
	private static final List<ITask> COLLECTIONS_WHICH_WITH_BOTH_SINCE_AND_TILL_PARAM = Arrays.asList(new ITask[] {
			new AlarmHistoryReport()
			//,new SensorMeasurementHistoryReport()
	});
	
	private static final List<ITask> COLLECTIONS_WHICH_ONLY_WITH_SINCE_PARAM = Arrays.asList(new ITask[] {
			new EnergyMeasurementsHistoryReport()			
	});
	
	
	@Autowired
	private Executor executor;	
	
	@Autowired
	private MongoTemplateGenerator mongoTemplateGenerator;

	@Autowired
	private ParameterGenerator parameterGenerator;
	
	@Autowired
	private AsyncHelper asyncHelper;
	
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
	    	ParameterGenerator.isBulkData = true;
			futures = executor.execTasks(COLLECTIONS_WHICH_ONLY_WITH_SINCE_PARAM, CONFIG_INDEX, isAsync);
	    	ParameterGenerator.isBulkData = false;
			break;
		case BULK_DATA_WITH_BOTH_SINCE_AND_TILL_PARAM: 
			ParameterGenerator.isBulkData = true;
	    	while (ParameterGenerator.getInitialDate().isBefore(LocalDate.now())) {	    		
	    		asyncHelper.waitTillEndOfSynchronizedFunc(executor.execTasks(COLLECTIONS_WHICH_WITH_BOTH_SINCE_AND_TILL_PARAM, CONFIG_INDEX, isAsync)); 		
	    		ParameterGenerator.setInitialDate(ParameterGenerator.getInitialDate().plusDays(ParameterGenerator.getIntervalDay()));    		
	    	}
	    	ParameterGenerator.isBulkData = false;
			break;
		case PROCESS_DAILY_COLLECTIONS:
			futures = executor.execProcesses(COLLECTIONS_TO_BE_PROCESSED, CONFIG_INDEX, isAsync);
			break;
		case PROCESS_CONVERSION:
			for (IProcess process : COLLECTIONS_TO_BE_PROCESSED) {
				
				Parameter parameter = parameterGenerator.generateTaskParameter(CONFIG_INDEX);
				List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
				MongoCollection<Document> mongoCollection = mongoTemplateGenerator.generateMongoTemplate(CONFIG_INDEX).getCollection(process.getCollectionName());
				FindIterable<Document> iterable = mongoCollection.find();
				iterable.noCursorTimeout(false);
				for (Iterator<Document> iterator = iterable.iterator(); iterator.hasNext();) {
					Document document = (Document) iterator.next();
					if(document.containsKey("_id"))
						document.remove("_id");
					jsonObjects.add(new JSONObject(document.toJson()));
					if(jsonObjects.size() == 10000) {

						CacheManager.add(process.getCollectionName()+CONFIG_INDEX, jsonObjects);
						process.process(parameter);
						jsonObjects = new ArrayList<JSONObject>();
					}
				}
				CacheManager.add(process.getCollectionName()+CONFIG_INDEX, jsonObjects);
				process.process(parameter);				
			}
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