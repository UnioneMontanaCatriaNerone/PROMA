//user:    MPE
//date:    08/04/2019
//ver:     4.4.0
//project: PEM - Compliance Verification
//type:    event trigger (TRIGGER DI CLASSE)
//class:   pcr_allocate_costs
//note:    groovy per controllo che data inizio periodo sia antecedente alla data di fine periodo, e per controllo che non esista già un consumo per l'immobile e il periodo inserito

public class pma_electricity_consumption_trigger extends com.geowebframework.dataservice.querybuilder.EventTrigger {
    
	public boolean beforeInsert(HashMap<String,Object> valuesMap){

		// CONTROLLO DATE DI INIZIO E FINE PERIODO
		
		// recupero le date di inizio e fine periodo inserite dall'utente
		String start_validity_date = valuesMap.get("start_date");
		String end_validity_date = valuesMap.get("end_date");
		
		// se la data di inizio periodo non è antecedente alla data di fine periodo allora blocco l'inserimento e visualizzo un messaggio di errore
		def df = "yyyy-MM-dd HH:mm:ss.S";
		def dateTime1 = new Date().parse(df, start_validity_date);
		def dateTime2 = new Date().parse(df, end_validity_date);
		if (dateTime1>dateTime2){
			// configuro il messaggio di alert	
			def warning_title ='<b>ATTENZIONE</b><br>';
			def warning_info ='la data di inizio periodo deve essere antecedente alla data di fine periodo';		
			def warning_message = warning_title+warning_info;
			throw new RuntimeException(warning_message);						
		};
		
		//controllo che non esista già un consumo per l'immobile e il periodo inserito
		String cod_building=valuesMap.get("cod_building");
		Integer count_type = services.queryService.executeQuery("SELECT count(1) AS count_type FROM pma_dati_gw.pma_electricity_consumption where cod_building='" + cod_building + "' and start_date='"+start_validity_date.split(' ')[0]+"' and end_date='"+end_validity_date.split(' ')[0]+"'",null)[0].count_type;
		
		if (count_type>0){
			// configuro il messaggio di alert	
			def warning_title ='<b>ATTENZIONE</b><br>';
			def warning_info ='esiste già un consumo per il periodo e l\'immobile inseriti';		
			def warning_message = warning_title+warning_info;
			throw new RuntimeException(warning_message);	
		}
		
		return true;
	};
 
	public boolean afterInsert(HashMap<String,Object> valuesMap){	
		return true;		
    };
    
    public boolean beforeUpdate(HashMap<String,Object> valuesMap,HashMap<String,Object> oldvaluesMap) {	

		// CONTROLLO DATE DI INIZIO E FINE PERIODO
		
		// recupero le date di inizio e fine periodo inserite dall'utente, se modificate
		def df = "yyyy-MM-dd";
		
		if(valuesMap.start_date!=null && valuesMap.end_date!=null) {
			String start_validity_date = valuesMap.get("start_date");
			String end_validity_date = valuesMap.get("end_date");
			
			def dateTime1 = new Date().parse(df, start_validity_date);
			def dateTime2 = new Date().parse(df, end_validity_date);
			if (dateTime1>dateTime2){
				// configuro il messaggio di alert	
				def warning_title ='<b>ATTENZIONE</b><br>';
				def warning_info ='la data di inizio periodo deve essere antecedente alla data di fine periodo';		
				def warning_message = warning_title+warning_info;
				throw new RuntimeException(warning_message);						
			};
			
			//controllo che non esista già un consumo per l'immobile e il periodo inserito
			String cod_building="";
			if(valuesMap.cod_building!=null) {
				cod_building=valuesMap.get("cod_building");
			}
			else {
				cod_building=oldvaluesMap.get("cod_building");
			}
			Integer count_type = services.queryService.executeQuery("SELECT count(1) AS count_type FROM pma_dati_gw.pma_electricity_consumption where cod_building='" + cod_building + "' and start_date='"+start_validity_date.split(' ')[0]+"' and end_date='"+end_validity_date.split(' ')[0]+"'",null)[0].count_type;
			
			if (count_type>0){
				// configuro il messaggio di alert	
				def warning_title ='<b>ATTENZIONE</b><br>';
				def warning_info ='esiste già un consumo per il periodo e l\'immobile inseriti';		
				def warning_message = warning_title+warning_info;
				throw new RuntimeException(warning_message);	
			}
			
		}
		else if(valuesMap.start_date!=null && valuesMap.end_date==null) {
			String start_validity_date = valuesMap.get("start_date");
			String end_validity_date = oldvaluesMap.get("end_date");
			
			def dateTime1 = new Date().parse(df, start_validity_date);
			def dateTime2 = new Date().parse(df, end_validity_date);
			if (dateTime1>dateTime2){
				// configuro il messaggio di alert	
				def warning_title ='<b>ATTENZIONE</b><br>';
				def warning_info ='la data di inizio periodo deve essere antecedente alla data di fine periodo';		
				def warning_message = warning_title+warning_info;
				throw new RuntimeException(warning_message);						
			};
			
			//controllo che non esista già un consumo per l'immobile e il periodo inserito
			String cod_building="";
			if(valuesMap.cod_building!=null) {
				cod_building=valuesMap.get("cod_building");
			}
			else {
				cod_building=oldvaluesMap.get("cod_building");
			}
			Integer count_type = services.queryService.executeQuery("SELECT count(1) AS count_type FROM pma_dati_gw.pma_electricity_consumption where cod_building='" + cod_building + "' and start_date='"+start_validity_date.split(' ')[0]+"' and end_date='"+end_validity_date+"'",null)[0].count_type;
			
			if (count_type>0){
				// configuro il messaggio di alert	
				def warning_title ='<b>ATTENZIONE</b><br>';
				def warning_info ='esiste già un consumo per il periodo e l\'immobile inseriti';		
				def warning_message = warning_title+warning_info;
				throw new RuntimeException(warning_message);	
			}
			
		}
		else if(valuesMap.start_date==null && valuesMap.end_date!=null) {
			String start_validity_date = oldvaluesMap.get("start_date");
			String end_validity_date = valuesMap.get("end_date");
			
			def dateTime1 = new Date().parse(df, start_validity_date);
			def dateTime2 = new Date().parse(df, end_validity_date);
			if (dateTime1>dateTime2){
				// configuro il messaggio di alert	
				def warning_title ='<b>ATTENZIONE</b><br>';
				def warning_info ='la data di inizio periodo deve essere antecedente alla data di fine periodo';		
				def warning_message = warning_title+warning_info;
				throw new RuntimeException(warning_message);						
			};
			
			//controllo che non esista già un consumo per l'immobile e il periodo inserito
			String cod_building="";
			if(valuesMap.cod_building!=null) {
				cod_building=valuesMap.get("cod_building");
			}
			else {
				cod_building=oldvaluesMap.get("cod_building");
			}
			Integer count_type = services.queryService.executeQuery("SELECT count(1) AS count_type FROM pma_dati_gw.pma_electricity_consumption where cod_building='" + cod_building + "' and start_date='"+start_validity_date+"' and end_date='"+end_validity_date.split(' ')[0]+"'",null)[0].count_type;
			
			if (count_type>0){
				// configuro il messaggio di alert	
				def warning_title ='<b>ATTENZIONE</b><br>';
				def warning_info ='esiste già un consumo per il periodo e l\'immobile inseriti';		
				def warning_message = warning_title+warning_info;
				throw new RuntimeException(warning_message);	
			}
			
		}
		
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