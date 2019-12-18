public class PriDocfaFabUiGroovyTrigger extends com.geowebframework.dataservice.querybuilder.EventTrigger {

    public boolean beforeInsert(HashMap<String,Object> valuesMap){
        // getting cod_object and cod_project
		// getting foglio_cu and particella_cu and subalterno_cu and sezione_cu
		def id_pri_part_urbano=valuesMap.id_pri_part_urbano;
        //def cod_document_archive = valuesMap.cod_document_archive;
		def res;
		if(id_pri_part_urbano!=null){
			res = services.queryService.executeQuery("select a.foglio,a.numero,a.subalterno,a.sezione from pri_part_urbano a where a.id_pri_part_urbano='"+id_pri_part_urbano+"'",null);
		}
		def foglio_cu = null;
        def particella_cu = null;
        def subalterno_cu = null;
        def sezione_cu = null;
		if(res!=null){
            foglio_cu = res[0].foglio;
            particella_cu = res[0].numero;
			subalterno_cu = res[0].subalterno;
			sezione_cu = res[0].sezione;
        }
        valuesMap.put("foglio_cu",foglio_cu);
        valuesMap.put("particella_cu",particella_cu);
		valuesMap.put("subalterno_cu",subalterno_cu);
		valuesMap.put("sezione_cu",sezione_cu);
        log.info("GEOWEB_INFO:");
        log.info(valuesMap);
       
	   def fk_particella_cu='';
		

		fk_particella_cu=valuesMap.id_comuni+valuesMap.foglio_cu.toString().padLeft(5, '0')+valuesMap.particella_cu.toString().padLeft(5, '0')+valuesMap.subalterno.toString().padLeft(4, '0');
		valuesMap.put("fk_particella_cu",fk_particella_cu);
		
        return true;
    };	

 	public boolean afterInsert(HashMap<String,Object> valuesMap){
		
		return true;
	};
	
	public boolean beforeUpdate(HashMap<String,Object> valuesMap,HashMap<String,Object> oldValuesMap){
		if (valuesMap.id_pri_part_urbano!=null && valuesMap.id_pri_part_urbano!=oldValuesMap.id_pri_part_urbano)
		{
		def id_pri_part_urbano=valuesMap.id_pri_part_urbano;
        //def cod_document_archive = valuesMap.cod_document_archive;

        def res = services.queryService.executeQuery("select a.foglio,a.numero,a.subalterno,a.sezione from pri_part_urbano a where a.id_pri_part_urbano='"+id_pri_part_urbano+"'",null);
		
		def foglio_cu = null;
        def particella_cu = null;
        def subalterno_cu = null;
        def sezione_cu = null;
		if(res!=null){
            foglio_cu = res[0].foglio;
            particella_cu = res[0].numero;
			subalterno_cu = res[0].subalterno;
			sezione_cu = res[0].sezione;
        }
        valuesMap.put("foglio_cu",foglio_cu);
        valuesMap.put("particella_cu",particella_cu);
		valuesMap.put("subalterno_cu",subalterno_cu);
		valuesMap.put("sezione_cu",sezione_cu);
        log.info("GEOWEB_INFO:");
        log.info(valuesMap);
       
	   def fk_particella_cu='';
		

		fk_particella_cu=valuesMap.id_comuni+valuesMap.foglio_cu.toString().padLeft(5, '0')+valuesMap.particella_cu.toString().padLeft(5, '0')+valuesMap.subalterno.toString().padLeft(4, '0');
		valuesMap.put("fk_particella_cu",fk_particella_cu);
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
