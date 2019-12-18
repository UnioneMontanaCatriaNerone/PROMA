 /**********************************************************************************************************************/
 /*trigger per modifica testo siti (upper o low)     */
 /**********************************************************************************************************************/
  import org.apache.commons.lang.StringUtils;
  
  public class PriSiteTriggerText extends com.geowebframework.dataservice.querybuilder.EventTrigger {
   
	public boolean beforeInsert(HashMap<String,Object> valuesMap){
		if(valuesMap.city!=null)     valuesMap.city = valuesMap.city.trim().toUpperCase();
		if(valuesMap.country!=null)  valuesMap.country = StringUtils.capitalize(valuesMap.country.trim().toLowerCase());
		if(valuesMap.district!=null)  valuesMap.district = StringUtils.capitalize(valuesMap.district.trim().toLowerCase());
		if(valuesMap.name_site!=null)     valuesMap.name_site = valuesMap.name_site.trim().toUpperCase();
		if(valuesMap.address!=null)     valuesMap.address = valuesMap.address.trim().toUpperCase();
		if(valuesMap.locality!=null)     valuesMap.locality = valuesMap.locality.trim().toUpperCase();
		
		return true;
	};
	
	public boolean afterInsert(HashMap<String,Object> valuesMap){
		
		return true;
	};
	
	public boolean beforeUpdate(HashMap<String,Object> valuesMap,HashMap<String,Object> oldValuesMap){
		if(valuesMap.city!=null)     valuesMap.city = valuesMap.city.trim().toUpperCase();
		if(valuesMap.country!=null)  valuesMap.country = StringUtils.capitalize(valuesMap.country.trim().toLowerCase());
		if(valuesMap.district!=null)  valuesMap.district = StringUtils.capitalize(valuesMap.district.trim().toLowerCase());
		if(valuesMap.name_site!=null)     valuesMap.name_site = valuesMap.name_site.trim().toUpperCase();
		if(valuesMap.address!=null)     valuesMap.address = valuesMap.address.trim().toUpperCase();
		if(valuesMap.locality!=null)     valuesMap.locality = valuesMap.locality.trim().toUpperCase();
			
		
		return true;
	};
	
	public boolean afterUpdate(HashMap<String,Object> valuesMap,HashMap<String,Object> oldValuesMap){
		
		//recupero campo cambiato (cod_company)
		String cod_company = valuesMap.get("cod_company");
		String type_property_site = valuesMap.get("type_property_site");
		String cod_site = oldValuesMap.get("site_code");
		//eseguo query per recuperare lista record collegati dalla gwd_site
		def info_company = services.queryService.executeQuery("select id_building from pri_building where cod_site='"+cod_site+"'",null);
		log.error("xxx: +"+info_company);
		
		for(int k=0; k<info_company.size(); k++){
			// definisco la mappa per inserimento
			def map1 = [:];
			map1.id_building = info_company[k].id_building;
			
			if (cod_company!=null){
				// assegno i valori alla mappa
				map1.cod_company = cod_company;
			}
			
			if (type_property_site!=null){
				map1.type_property = type_property_site;
			}
			
			services.classService.updateClassRecord('pri_building', map1);
		}
			
		return true;
	};
	
	public boolean beforeDelete(HashMap<String,Object> valuesMap){
		return true;
	};
	
	public boolean afterDelete(HashMap<String,Object> valuesMap){
		return true;
	};
}