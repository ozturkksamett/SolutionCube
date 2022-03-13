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
import com.solutioncube.collection.AlarmRules;
import com.solutioncube.collection.EnergyConsumptionReport;
import com.solutioncube.collection.EnergyMeasurementsHistoryReport;
import com.solutioncube.collection.EnergyMeters;
import com.solutioncube.collection.FloorPlans;
import com.solutioncube.collection.LatestEnergyMeasurementsReport;
import com.solutioncube.collection.LatestPositionsReport;
import com.solutioncube.collection.LatestSensorMeasurementReport;
import com.solutioncube.collection.LatestTemperatureMeasurementsReport;
import com.solutioncube.collection.OutageHistoryReport;
import com.solutioncube.collection.PositionHistoryReport;
import com.solutioncube.collection.SensorCountHistoryReport;
import com.solutioncube.collection.SensorCountLatestReport;
import com.solutioncube.collection.SensorCountSummaryReport;
import com.solutioncube.collection.SensorCounters;
import com.solutioncube.collection.SensorMeasurementHistoryReport;
import com.solutioncube.collection.SensorMeasurementSummaryReport;
import com.solutioncube.collection.Sensors;
import com.solutioncube.collection.TemperatureMeasurementsHistoryReport;
import com.solutioncube.collection.TemperatureMeasurementsSummaryReport;
import com.solutioncube.collection.TemperatureSensors;
import com.solutioncube.collection.Trackers;
import com.solutioncube.collection.ZonePresenceHistoryReport;
import com.solutioncube.collection.ZonePresenceSummaryReport;
import com.solutioncube.collection.Zones;
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
@Qualifier("erisyemService")
public class ErisyemService implements IService {
	
	private static final String SERVICE_NAME = "erisyem";
	
	private static final int CONFIG_INDEX = 0;

	private static final List<ITask> STATIC_COLLECTIONS = Arrays.asList(new ITask[] {

			//new Sensors()
			//,new AlarmRules()
			//,new SensorCounters()
			//,new Trackers()
			//,new FloorPlans()
			//,new Zones()
			//,
			new EnergyMeters()
			//,new TemperatureSensors()
	});

	private static final List<ITask> DAILY_COLLECTIONS = Arrays.asList(new ITask[] {

			new PositionHistoryReport()
			,new LatestPositionsReport()
			,new OutageHistoryReport()
			,new LatestEnergyMeasurementsReport()
			,new EnergyMeasurementsHistoryReport()
			,new EnergyConsumptionReport()
			,new LatestTemperatureMeasurementsReport()
			,new TemperatureMeasurementsHistoryReport()
			,new TemperatureMeasurementsSummaryReport()
			,new ZonePresenceHistoryReport()
			,new ZonePresenceSummaryReport()
			,new AlarmHistoryReport()
			,new LatestSensorMeasurementReport()
			,new SensorMeasurementHistoryReport()
			,new SensorMeasurementSummaryReport()
			,new SensorCountHistoryReport()
			,new SensorCountLatestReport()
			,new SensorCountSummaryReport()
	});
	
	private static final List<ITask> COLLECTIONS_WHICH_ONLY_WITH_SINCE_PARAM = Arrays.asList(new ITask[] {

			//new PositionHistoryReport()
			new EnergyMeasurementsHistoryReport()			
			//new TemperatureMeasurementsHistoryReport()			
			//new SensorCountHistoryReport()
	});

	private static final List<ITask> COLLECTIONS_WHICH_WITH_BOTH_SINCE_AND_TILL_PARAM = Arrays.asList(new ITask[] {

			//new OutageHistoryReport()
			//,new EnergyConsumptionReport()
			//,new TemperatureMeasurementsSummaryReport()
			//,new ZonePresenceHistoryReport()
			//,new ZonePresenceSummaryReport()
			//,new AlarmHistoryReport()
			//,new SensorMeasurementHistoryReport()
			new SensorMeasurementSummaryReport()
			,new SensorCountSummaryReport()
	});
	
	private static final List<IProcess> COLLECTIONS_TO_BE_PROCESSED = Arrays.asList(new IProcess[] {

			//new AlarmHistoryReport()			
			//,
			new SensorMeasurementHistoryReport()
	});
	
	@Autowired
	private Executor executor;

	@Autowired
	private AsyncHelper asyncHelper;

	@Autowired
	private MongoTemplateGenerator mongoTemplateGenerator;

	@Autowired
	private ParameterGenerator parameterGenerator;
	
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
					if(jsonObjects.size() == 10000) { // java.lang.OutOfMemoryError: Java heap space exception handled by process data 10k by 10k

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