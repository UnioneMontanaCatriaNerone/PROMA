import java.util.List;


public class BaseGroovyTrigger extends com.geowebframework.dataservice.querybuilder.EventTrigger {

    public boolean beforeInsert(HashMap<String,Object> valuesMap){
		def cod_terrain_type_user=valuesMap.get("cod_terrain_type_user");
		def tenant_code=valuesMap.get("tenant_code");
		
		valuesMap.put("cod_terrain_type", tenant_code+'_'+cod_terrain_type_user);
		
        return true;
    };

    public boolean  afterInsert(HashMap<String,Object> valuesMap){
        //valuesMap.id = valore_della_chiave_primaria_del_record_appena_inserito                    
		return true;
    };

    public boolean beforeUpdate(HashMap<String,Object> valuesMap,HashMap<String,Object> oldValuesMap){
		
		def mapCompleto = [:] ;
		
		mapCompleto.putAll(oldValuesMap);
		mapCompleto.putAll(valuesMap);

		def cod_terrain_type_user=mapCompleto.get("cod_terrain_type_user");
		def tenant_code=mapCompleto.get("tenant_code");
		
		valuesMap.put("cod_terrain_type", tenant_code+'_'+cod_terrain_type_user);
		
        return true;
    };

    public boolean  afterUpdate(HashMap<String,Object> valuesMap,HashMap<String,Object> oldValuesMap){
        return true;
    };
    public boolean beforeDelete(HashMap<String,Object> valuesMap){
        return true;
    };

    public boolean afterDelete(HashMap<String,Object> valuesMap){
        return true;
    };
}