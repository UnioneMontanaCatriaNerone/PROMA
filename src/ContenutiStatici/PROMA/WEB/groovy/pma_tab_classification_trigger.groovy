//user:    MPE
//date:    08/04/2019
//ver:     4.4.0
//project: PEM - Compliance Verification
//type:    event trigger (TRIGGER DI CLASSE)
//class:   cov_permission_type
//note:    groovy per concatenazione della tipologia e del titolo di possesso per formare il codice classificazione
//		   calcolo della descrizione come concatenazione di codice e nome	

public class pma_tab_classification_trigger extends com.geowebframework.dataservice.querybuilder.EventTrigger {
    
	public boolean beforeInsert(HashMap<String,Object> valuesMap){

		def code_type_building=valuesMap.get("code_type_building");
		def type_property=valuesMap.get("type_property");
		
		//controllo che non esista già una classificazione con la stessa tipologia e lo stesso titolo di possesso
		Integer count_type = services.queryService.executeQuery("SELECT count(1) as count_type FROM pma_dati_gw.pma_tab_classification where type_property='" + type_property + "' and code_type_building='"+code_type_building+"'",null)[0].count_type;
		if(count_type>0) {
			def warning_title ='<b>ATTENZIONE</b><br>';
			def warning_info ='Esiste già una classificazione con la tipologia selezionata e il titolo di possesso selezionato';		
			def warning_message = warning_title + warning_info;
			throw new RuntimeException(warning_message);		
		}
		//def recordset = services.queryService.executeQuery("SELECT cod_type_property FROM pma_dati_gw.pma_type_property where cod_type_property='" + type_property + "'",null)[0];
		
		def code_classification=code_type_building+type_property;
		valuesMap.put("code_classification",code_classification);
		
		return true;
	};
 
	public boolean afterInsert(HashMap<String,Object> valuesMap){	
		return true;		
    };
    
    public boolean beforeUpdate(HashMap<String,Object> valuesMap,HashMap<String,Object> oldvaluesMap) {	
		
		def code_type_building="";
		def type_property="";
		if(valuesMap.code_type_building!=null) {
			code_type_building=valuesMap.get("code_type_building");
		}
		else {
			code_type_building=oldvaluesMap.get("code_type_building");
		}
		 
		if(valuesMap.type_property!=null) {
			type_property=valuesMap.get("type_property");
		}
		else {
			type_property=oldvaluesMap.get("type_property");
		}
		//controllo che non esista già una classificazione con la stessa tipologia e lo stesso titolo di possesso
		Integer count_type = services.queryService.executeQuery("SELECT count(1) as count_type FROM pma_dati_gw.pma_tab_classification where type_property='" + type_property + "' and code_type_building='"+code_type_building+"'",null)[0].count_type;
		if(count_type>0) {
			def warning_title ='<b>ATTENZIONE</b><br>';
			def warning_info ='Esiste già una classificazione con la tipologia selezionata e il titolo di possesso selezionato';		
			def warning_message = warning_title + warning_info;
			throw new RuntimeException(warning_message);		
		}
		//def recordset = services.queryService.executeQuery("SELECT cod_type_property FROM pma_dati_gw.pma_type_property where pk_sequ_type_property='" + type_property + "'",null)[0];
		
		def code_classification=code_type_building+type_property;
		valuesMap.code_classification=code_classification;
		
       return true;
	};

    
    public boolean afterUpdate(HashMap<String,Object> valuesMap,HashMap<String,Object> oldvaluesMap){	
	    return true;
	};
 
 
    public boolean beforeDelete(HashMap<String,Object> valuesMap){
		
        return true;
    };
    
    public boolean afterDelete(HashMap<String,Object> valuesMap){
        return true;
    };

}  