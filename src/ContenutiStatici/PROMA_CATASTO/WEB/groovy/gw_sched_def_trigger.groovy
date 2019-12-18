import java.util.Locale;
import net.redhogs.cronparser.CronExpressionDescriptor;
import org.springframework.scheduling.support.CronTrigger;

public class gwSchedDefTrigger extends com.geowebframework.dataservice.querybuilder.EventTrigger {
   
	public boolean beforeInsert(HashMap<String,Object> valuesMap){
		log.info("gwSchedDefTrigger - beforeInsert()");
		log.info("valuesMap: "+valuesMap);


		
		String cron = valuesMap.cron;
			
					
		if(cron!=null){
			
			
			//testing cron expression
			CronTrigger cronTrigger = new CronTrigger(cron);
						
			String human_readable_cron = CronExpressionDescriptor.getDescription(cron);
			//String human_readable_cron = CronExpressionDescriptor.getDescription(cron, Locale.ITALIAN);
			valuesMap.human_readable_cron = human_readable_cron;
			log.info("human_readable_cron: "+human_readable_cron);
		}
		
		return true;
	};
	
	public boolean afterInsert(HashMap<String,Object> valuesMap){
		log.info("gwSchedDefTrigger - afterInsert()");
		log.info("valuesMap: "+valuesMap);
				
		String sched_def_name = valuesMap.sched_def_name;
		services.gwScheduler.activateSchedulation(sched_def_name);
		
		return true;
	};
	
	public boolean beforeUpdate(HashMap<String,Object> valuesMap,HashMap<String,Object> oldValuesMap){
		log.info("gwSchedDefTrigger - beforeUpdate()");
		log.info("valuesMap: "+valuesMap);
		log.info("oldValuesMap: "+oldValuesMap);
		
		String cron = valuesMap.cron;
		
		if(cron!=null){
			//testing cron expression
			CronTrigger cronTrigger = new CronTrigger(cron);
						
			String human_readable_cron = CronExpressionDescriptor.getDescription(cron);
			//String human_readable_cron = CronExpressionDescriptor.getDescription(cron, Locale.ITALIAN);
			valuesMap.human_readable_cron = human_readable_cron;
			log.info("human_readable_cron: "+human_readable_cron);
		}
		
		return true;
	};
	
	public boolean afterUpdate(HashMap<String,Object> valuesMap,HashMap<String,Object> oldValuesMap){
		log.info("gwSchedDefTrigger - afterUpdate()");
		
		log.info("valuesMap: "+valuesMap);
		log.info("oldValuesMap: "+oldValuesMap);
		
		String sched_def_name = valuesMap.sched_def_name;
		String sched_def_name_old = oldValuesMap.sched_def_name;
		Integer enabled = valuesMap.enabled;
		String cron = valuesMap.cron;
		Date start_time = valuesMap.start_time;
		String period = valuesMap.period;
		String delay = valuesMap.delay;
		
		if(sched_def_name==null) sched_def_name = sched_def_name_old;
		
		boolean needToRecompute = sched_def_name!=null || enabled!=null || cron!=null || start_time!=null || period!=null || delay!=null;
		
		log.info("needToRecompute: "+needToRecompute);
		
		if(needToRecompute==true){
			if(enabled!=null && enabled==false) services.gwScheduler.deactivateSchedulation(sched_def_name);
			else services.gwScheduler.activateSchedulation(sched_def_name);
		}
		
		return true;
	};
	
	public boolean beforeDelete(HashMap<String,Object> valuesMap){
		return true;
	};
	
	public boolean afterDelete(HashMap<String,Object> valuesMap){
		log.info("gwSchedDefTrigger - afterDelete()");
		log.info("valuesMap: "+valuesMap);
		
		Integer sched_def_id = valuesMap.sched_def_id;
		def rec = services.queryService.executeQuery("select * from gw_sched_def where sched_def_id="+sched_def_id, null);
		log.info("rec: "+rec);
		if(rec!=null){
			String sched_def_name = rec.sched_def_name;
			services.gwScheduler.deactivateSchedulation(sched_def_name);
		}
		return true;
	};
}
