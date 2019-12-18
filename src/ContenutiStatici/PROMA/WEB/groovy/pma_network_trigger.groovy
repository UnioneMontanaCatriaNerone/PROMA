// \\wincom\c$\projects\BM\WEB\groovy\pri_building_trigger.groovy
import java.util.List;

import com.geowebframework.transfer.model.dataservice.GwdFolder;
import com.geowebframework.dataservice.GwdFolderService;
import com.geowebframework.dataservice.service.GwClassListService;
import com.geowebframework.gwMnemonicCode.model.widget.MnemonicCodeSelectionWidget;
import com.geowebframework.gwMnemonicCode.model.widget.MnemonicCodeWidget;
import com.geowebframework.metadataservice.ClassService;
import com.geowebframework.metadataservice.GwmMnemonicCodeService;
import com.geowebframework.transfer.model.metadataservice.Class;
import com.geowebframework.transfer.model.metadataservice.GwmMnemonicCode;
import com.geowebframework.transfer.model.metadataservice.McPart;
import com.geowebframework.transfer.model.metadataservice.MnemonicCode;
import org.apache.commons.lang.StringUtils;

public class pma_network_trigger extends com.geowebframework.dataservice.querybuilder.EventTrigger {
    
public boolean beforeInsert(HashMap<String,Object> valuesMap){

log.debug("Mappa in entrata: "+valuesMap);

//recupero i campi dalla cmn_cities
def record=services.queryService.executeQuery("select * from cmn_cities where uk_istat=#{map.city}",valuesMap)[0];
log.debug("Risultato query: "+record);


//faccio una count per il primo inserimento
def primo=services.queryService.executeQuery("select count(substring(cod_building,9,10)) as primo from pma_building where (substring(cod_building,3,6)=#{map.city} or substring(cod_building,3,3)=#{map.city}) and type_ownership='Infrastruttura'",valuesMap)[0].primo;
log.debug("Primo inserimento: "+primo);

if(primo==0 || primo==null || primo==''){
	primo=1;
	//controllo la sigla dello stato e setto il codice
	if(record.descr_state.equals("IT")){
		//.toString().padLeft(5, '0');
		valuesMap.cod_building='IT'+record.uk_istat+primo.toString().padLeft(6, '0');
	}else{
		valuesMap.cod_building=record.descr_state+record.uk_istat+primo.toString().padLeft(9, '0');
	}
	log.debug("Codice al primo inserimento: "+valuesMap.cod_building);
}else{
	//select max(substring(cod_building,9,10))+1 as prog from PMA_BUILDING where substring(cod_building,3,6)='028001'
	def prog=services.queryService.executeQuery("select max(substring(cod_building,9,10))+1 as prog from PMA_BUILDING where (substring(cod_building,3,6)=#{map.city} or substring(cod_building,3,3)=#{map.city}) and type_ownership='Infrastruttura'",valuesMap)[0].prog;
	log.debug("Progressivo: "+prog);
	//controllo la sigla dello stato e setto il codice
	if(record.descr_state.equals("IT")){
		//.toString().padLeft(5, '0');
		valuesMap.cod_building='IT'+record.uk_istat+prog.toString().padLeft(6, '0');
	}else{
		valuesMap.cod_building=record.descr_state+record.uk_istat+prog.toString().padLeft(9, '0');
	}
	log.debug("Codice dopo il primo inserimento: "+valuesMap.cod_building);
}

valuesMap.insert_user=valuesMap.insert_activeuser;
valuesMap.insert_date=new Date();
log.debug("Utente inserimento: "+valuesMap.insert_user);
log.debug("Data inserimento: "+valuesMap.insert_date);


/*

//conto in pma_buildin di quanti uk_istat ho
// creo progr su uk se IT altrimenti in descr_state


// verifico se codice istat uk_istat associato al comune scelto è >5
// Se true, allora cerca il max per quel comune ed inserisci +1, concateno descr_state+uk_istat+Progressivo(per uk_istat)
// Se false, concatena in cod_building i campi descr_state+000000+Progressivo(per descr_state)

		def cCity=valuesMap.city; //uk_istat
	//	def stato= "SELECT descr_state from cmn_cities where uk_istat=#{map.city}";
		def map_query =[:];
		map_query.city=cCity;
		map_query
		
		if cCity.length>5 {
		// Se true, allora cerca il max per quel comune ed inserisci +1, concateno descr_state+uk_istat+Progressivo(per uk_istat)


		}
		
		else
	
		
//verifico che non esista un edificio con lo stesso cod_building
		def codBuilding=valuesMap.cod_building;
		def queryControllo= "SELECT description from cmn_cities where uk_istat=#{map.city}";
		def map_query =[:];
		map_query.cod_building=codBuilding;
		def codbuildingOk=services.queryService.executeQuery(queryControllo,map_query);
		if (codbuildingOk.size()>0) {
			throw new RuntimeException("Attenzione Edificio gia esistente!");
		}
		valuesMap.put("cod_building", codBuilding);
																		  
       def codice_gerarchico = valuesMap.cod_building+"Prog";
		log.error("trigger inserimento codice_gerarchico ");
		valuesMap.put("codice_gerarchico",codice_gerarchico );
		
	/*	def cod_building = valuesMap.get("cod_building");
		def check_cod = services.queryService.executeQuery("SELECT count(id_system) as check_cod FROM gwd_system WHERE cod_building='"+cod_building+"'", null)[0].check_cod;	
		log.info("loggo la check_cod: "+check_cod);
		if(check_cod!=null && check_cod>0){
			throw new RuntimeException("Attenzione: Codice Edificio già in uso. <br>Scegliere una codifica differente!");
		}
*/	
		return true;
    };
  
	public boolean afterInsert(HashMap<String,Object> valuesMap){
	 /*   
		def record = services.classService.selectClassRecord('pri_building',''+valuesMap.id_building);
			
		def queryUp= "UPDATE pri_building SET CITY= (select description from cmn_cities";
		queryUp+= " where uk_istat=#{map.city}) where id_building=#{map.id_building}";
		
		log.error("query: "+queryUp+"-"+record);
        def resListActObj = services.queryService.executeUpdateQuery(queryUp,record);
	*/
		return true;
		
	}; 	
	
	
    public boolean beforeUpdate(HashMap<String,Object> valuesMap,HashMap<String,Object> OldvaluesMap){
        	
		log.debug("vecchio comune: "+OldvaluesMap.city);
		log.debug("nuovo comune: "+valuesMap.city);
			
		if(OldvaluesMap.city!=valuesMap.city && valuesMap.city!=null){
			throw new RuntimeException('<b>Attenzione</b><br><br> Non e\' possibile cambiare il comune dell\'infrastruttura!');
		}
		if(valuesMap.insert_activeuser!=OldvaluesMap.insert_user && valuesMap.insert_activeuser!=null){
			valuesMap.modify_user=valuesMap.insert_activeuser;
		}else{
			valuesMap.modify_user=OldvaluesMap.insert_activeuser;
		}
		valuesMap.modify_date=new Date();
		log.debug("Utente Modifica: "+valuesMap.modify_user);
		log.debug("Data Modifica: "+valuesMap.modify_date);		
					
				return true;
		
		
    }; 
	
	public boolean afterUpdate(HashMap<String,Object> valuesMap, HashMap<String,Object> valuesOldMap){
	/*
	def map3= [fk_sostegno:valuesOldMap.fk_sostegno];		
	def query3 = "select uk_lanterna from impianti_semaforici.lanterne_semaforiche where fk_sostegno=#{map.fk_sostegno}";
	def result3 = services.queryService.executeQuery(query3,map3);
	def uklanterna= result3[0].uk_lanterna;
	log.debug("log uk_lanterna "+uklanterna)
	def queryUp= "UPDATE impianti_semaforici.lanterne_semaforiche SET ='LR"+uklanterna+"'";
	services.queryService.executeUpdateQuery(queryUp,map3);	
	
	if ((valuesMap.fk_tipologia_lanterna!=null) || (valuesMap.codice_flusso!=null) ||(valuesMap.descr_flusso!=null) || (valuesMap.tipo_accesso!=null)){
	setClientMessage("Attenzione, sono stati variati attributi che rendono necessario ristampare il Report");
	}*/
		return true;
			
	}
	


} 