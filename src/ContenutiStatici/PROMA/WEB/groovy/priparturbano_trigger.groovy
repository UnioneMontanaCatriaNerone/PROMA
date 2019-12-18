public class PriPartUrbanoGroovyTrigger extends com.geowebframework.dataservice.querybuilder.EventTrigger {
	

   
	public boolean beforeInsert(HashMap<String,Object> valuesMap){
		def uk_subalterno='';
		def uk_particella='';
		def uk_cat_particella='';
		def uk_cat_subalterno='';

		uk_subalterno=valuesMap.id_comuni+valuesMap.foglio.toString().padLeft(5, '0')+valuesMap.numero.toString().padLeft(5, '0')+valuesMap.subalterno.toString().padLeft(4, '0');
		valuesMap.put("uk_subalterno",uk_subalterno);
		uk_particella=valuesMap.id_comuni+valuesMap.foglio.toString().padLeft(5, '0')+valuesMap.numero.toString().padLeft(5, '0');
		valuesMap.put("uk_particella",uk_particella);
		uk_cat_particella=valuesMap.id_comuni+valuesMap.foglio.toString().padLeft(5, '0')+valuesMap.numero.toString().padLeft(5, '0')+'00000';
		valuesMap.put("uk_cat_particella",uk_cat_particella);
		uk_cat_subalterno=valuesMap.id_comuni+valuesMap.foglio.toString().padLeft(5, '0')+valuesMap.numero.toString().padLeft(5, '0')+valuesMap.subalterno.toString().padLeft(8, '0')+'0';
		valuesMap.put("uk_cat_subalterno",uk_cat_subalterno);
	};
	
	public boolean afterInsert(HashMap<String,Object> valuesMap){
		
		return true;
	};
	
	public boolean beforeUpdate(HashMap<String,Object> valuesMap,HashMap<String,Object> oldValuesMap){

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
