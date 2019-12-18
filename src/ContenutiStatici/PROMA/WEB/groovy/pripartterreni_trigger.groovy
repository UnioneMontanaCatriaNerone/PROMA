public class PriPartTerreniGroovyTrigger extends com.geowebframework.dataservice.querybuilder.EventTrigger {
	

   
	public boolean beforeInsert(HashMap<String,Object> valuesMap){
		def uk_particella='';
		def uk_cat_particella='';
		
		uk_particella=valuesMap.id_comuni+valuesMap.foglio.toString().padLeft(5, '0')+valuesMap.numero.toString().padLeft(5, '0');
		valuesMap.put("uk_particella",uk_particella);
		uk_cat_particella=valuesMap.id_comuni+valuesMap.foglio.toString().padLeft(5, '0')+valuesMap.numero.toString().padLeft(5, '0')+'00000';
		valuesMap.put("uk_cat_particella",uk_cat_particella);
		return true;
	};
	
	public boolean afterInsert(HashMap<String,Object> valuesMap){
		
		return true;
	};
	
	public boolean beforeUpdate(HashMap<String,Object> valuesMap,HashMap<String,Object> oldValuesMap){
		
		def uk_particella='';
		def uk_cat_particella='';

		if(valuesMap.foglio!=null || valuesMap.numero!=null || valuesMap.id_comuni!=null ){
			if(valuesMap.foglio==null)         valuesMap.foglio = oldValuesMap.foglio;
			if(valuesMap.numero==null) 	       valuesMap.numero = oldValuesMap.numero;
			if(valuesMap.id_comuni==null)   valuesMap.id_comuni = oldValuesMap.id_comuni;
			
			uk_particella=valuesMap.id_comuni+valuesMap.foglio.toString().padLeft(5, '0')+valuesMap.numero.toString().padLeft(5, '0');
			valuesMap.put("uk_particella",uk_particella);
			uk_cat_particella=valuesMap.id_comuni+valuesMap.foglio.toString().padLeft(5, '0')+valuesMap.numero.toString().padLeft(5, '0')+'00000';
			valuesMap.put("uk_cat_particella",uk_cat_particella);
		}
		
		return true;
	};
	
	public boolean afterUpdate(HashMap<String,Object> valuesMap,HashMap<String,Object> oldValuesMap){
			
		return true;
	};
	
	public boolean beforeDelete(HashMap<String,Object> valuesMap){
		return true;
	};
	
	public boolean afterDelete(HashMap<String,Object> valuesMap){
		return true;
	};
}
