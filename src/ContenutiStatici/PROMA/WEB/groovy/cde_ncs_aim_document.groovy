//user:    ABA 
//date:    21/10/2016
//ver:     4.2.5
//project: CDE 
//type:    class trigger
//class:   cde_bim_model
//note:    popolamneto codice

import org.apache.commons.lang.StringUtils;


public class BaseGroovyTrigger extends com.geowebframework.dataservice.querybuilder.EventTrigger {
    
	public boolean beforeInsert(HashMap<String,Object> valuesMap){
		log.info("GEOWEB_INFO: beforeInsert");

		
		def arc = services.queryService.executeQuery("SELECT * FROM view_cde_doc_arc_type WHERE cod_arc_type=#{map.cod_arc_type}", valuesMap)[0];
		log.info ("loggo la query per recuperare i parametri: " + arc);
		
		def cod_arc_cat    = arc.fk_doc_cat;
		log.info ("cod_arc_cat = arc.fk_doc_cat = "+cod_arc_cat);
		def cod_arc_subcat = arc.fk_doc_subcat;
		log.info ("cod_arc_subcat = arc.fk_doc_subcat = "+cod_arc_subcat);
		def cod_arc_type   = arc.cod_arc_type;
		log.info ("cod_arc_type = arc.cod_arc_type = "+cod_arc_type);

		
		valuesMap.put("cod_arc_cat",cod_arc_cat);
		valuesMap.put("cod_arc_subcat",cod_arc_subcat);
		valuesMap.put("cod_arc_type",cod_arc_type);
		log.info ("ho scritto le variabili categoria e sottocategoria");
  
		return true;
	};
    
	public boolean afterInsert(HashMap<String,Object> valuesMap){	
	
		
	
		return true;		
    };
    
    public boolean beforeUpdate(HashMap<String,Object> valuesMap,HashMap<String,Object> oldvaluesMap) {
			
		
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