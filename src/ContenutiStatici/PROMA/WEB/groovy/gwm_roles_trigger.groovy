import java.util.List;


public class RolesGroovyTrigger extends com.geowebframework.dataservice.querybuilder.EventTrigger {
    
    public boolean beforeInsert(HashMap<String,Object> valuesMap){
      //Definisco Variabile;
	  def cod_role = valuesMap.cod_roles;
      def fcod_role = services.queryService.executeQuery("Select cod_roles from geow_roles where cod_roles = #{map.cod_role}",[cod_role:cod_role]);
      //Controllo che il codice sia univoco;
      if (fcod_role != null && fcod_role.size()>0 && fcod_role.cod_role != null){
        throw new RuntimeException("<b>Attenzione:</b> Codice già utilizzato.<br>Utilizzare codice differente.")
      }
		return true;
    };
    
    public boolean afterInsert(HashMap<String,Object> valuesMap){
   
    	return true;
    };
    
    public boolean beforeUpdate(HashMap<String,Object> valuesMap,HashMap<String,Object> OldvaluesMap){
     
	 return true;
    };
    
    public boolean afterUpdate(HashMap<String,Object> valuesMap,HashMap<String,Object> oldvaluesMap){
	
	 return true;
    };

	public boolean beforeDelete(HashMap<String,Object> valuesMap){
	//Recupero ID:
  def id = valuesMap.pk_geow_roles;
  //Recupero Codice dall'ID:
  def fcod_role = services.queryService.executeQuery("Select cod_roles,description_roles from geow_roles where pk_geow_roles = #{map.id}",[id:id]);
  def cod_role = fcod_role[0].cod_roles;
  //Controllo che questa funzione non sia associata ad alcun gruppo:
  def fkcod_role = services.queryService.executeQuery("Select count(funzione) as conta_role from geow_groups where funzione = #{map.cod_role}",[cod_role:cod_role]);
  //Se funzione è associata allora mando errore:
  if (fkcod_role[0].conta_role > 0){
        def conta_role = fkcod_role[0].conta_role;
        def descr_role = fcod_role[0].description_roles;
        throw new RuntimeException("<b>Attenzione:</b> la funzione <b>"+descr_role+"</b> non può essere eliminata, perchè associata a <b>"+conta_role+"</b> gruppi.")
      }

	 return true;
	};
} 