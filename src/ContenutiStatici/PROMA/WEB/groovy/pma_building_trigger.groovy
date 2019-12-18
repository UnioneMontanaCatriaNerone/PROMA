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

public class pma_building_trigger extends com.geowebframework.dataservice.querybuilder.EventTrigger {
    
public boolean beforeInsert(HashMap<String,Object> valuesMap){

	log.debug("Mappa in entrata: "+valuesMap);

	//recupero i campi dalla pma_tab_cities
	def uk_istat=valuesMap.get("city");
	def descr_state=valuesMap.get("descr_state");
	log.debug("Stato: "+descr_state);
	def record=services.queryService.executeQuery("select * from pma_dati_gw.cmn_cities where uk_istat='"+uk_istat+"'",null)[0];
	log.debug("Record Istat"+record);
	Integer count_type=services.queryService.executeQuery("select count(*) as count_type from pma_dati_gw.pma_building where city='"+uk_istat+"'",null)[0].count_type;
	Integer new_progr=count_type+1;
	if(descr_state.equals("IT")){
		valuesMap.cod_building='IT'+uk_istat+new_progr.toString().padLeft(6, '0');
	}else{
		valuesMap.cod_building=descr_state+uk_istat+new_progr.toString().padLeft(9, '0');
	}
	
	valuesMap.insert_user=valuesMap.insert_activeuser;
	valuesMap.insert_date=new Date();

	//faccio una count per il primo inserimento
	def primo=services.queryService.executeQuery("select count(substring(cod_building,9,10)) as primo from pma_dati_gw.pma_building where (substring(cod_building,3,6)=#{map.city} or substring(cod_building,3,3)=#{map.city})",valuesMap)[0].primo;
	//def primo=services.queryService.executeQuery("select count(substring(cod_building,9,10)) as primo from pma_dati_gw.pma_building where (substring(cod_building,3,6)=#{map.city} or substring(cod_building,3,3)=#{map.city}) and type_ownership='Edificio'",valuesMap)[0].primo;
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
		//select max(substring(cod_building,9,10))+1 as prog from PMA_BUILDING;//where substring(cod_building,3,6)='028001';
		def prog=services.queryService.executeQuery("select max(substring(cod_building,9,10))::integer + 1 as prog from pma_dati_gw.PMA_BUILDING where (substring(cod_building,3,6)=#{map.city} or substring(cod_building,3,3)=#{map.city})",valuesMap)[0].prog;
		//def prog=services.queryService.executeQuery("select max(substring(cod_building,9,10))+1 as prog from PMA_BUILDING where (substring(cod_building,3,6)=#{map.city} or substring(cod_building,3,3)=#{map.city}) and type_ownership='Edificio '",valuesMap)[0].prog;
		
		
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
*/	
		
//verifico che non esista un edificio con lo stesso cod_building

		def codBuilding=valuesMap.cod_building;
		def queryControllo= "SELECT description from pma_dati_gw.cmn_cities where uk_istat=#{map.city}";
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
		
		def cod_building = valuesMap.get("cod_building");
		//def check_cod = services.queryService.executeQuery("SELECT count(id_system) as check_cod FROM gwd_system WHERE cod_building='"+cod_building+"'", null)[0].check_cod;	
		def check_cod = services.queryService.executeQuery("SELECT count(cod_building) as check_cod FROM pma_dati_gw.pma_building WHERE cod_building='"+cod_building+"'", null)[0].check_cod;	
		
		log.info("loggo la check_cod: "+check_cod);
		if(check_cod!=null && check_cod>0){
			throw new RuntimeException("Attenzione: Codice Edificio già in uso. <br>Scegliere una codifica differente!");
		}

		//inserisce in automatico il titolo di possesso e la tipologia partendo dalla classificazione
		String code_classification=valuesMap.get("code_classification");
		def recordprop=services.queryService.executeQuery("select type_property, code_type_building from pma_dati_gw.PMA_TAB_CLASSIFICATION WHERE code_classification='" + code_classification + "'",null)[0];
		valuesMap.put("cod_type_building", recordprop.code_type_building);
		valuesMap.put("type_property", recordprop.type_property);
		
		//inserisce in automatico il polo direzionale partendo dal presidio
		String fk_aid=valuesMap.get("fk_aid");
		def recordprop2=services.queryService.executeQuery("select fk_pole from pma_dati_gw.PMA_TAB_AID WHERE cod_aid='" + fk_aid + "'",null)[0];
		valuesMap.put("fk_pole", recordprop2.fk_pole);
		
		return true;
    };
  
	public boolean afterInsert(HashMap<String,Object> valuesMap){
	 /*   
		def record = services.classService.selectClassRecord('pri_building',''+valuesMap.id_building);
			
		def queryUp= "UPDATE pma_dati_gw.pma_building SET CITY= (select uk_istat from cmn_cities";
		queryUp+= " where uk_istat=#{map.city}) where id_building=#{map.id_building}";
		
		log.error("query: "+queryUp+"-"+record);
        def resListActObj = services.queryService.executeUpdateQuery(queryUp,record);
	*/
		return true;
		
	}; 	
	
	
    public boolean beforeUpdate(HashMap<String,Object> valuesMap,HashMap<String,Object> OldvaluesMap){
        	
		log.debug("vecchio comune: "+OldvaluesMap.city);
		log.debug("nuovo comune: "+valuesMap.city);

		def modify_user;
		
		if(OldvaluesMap.city!=valuesMap.city && valuesMap.city!=null){
			throw new RuntimeException('<b>Attenzione</b><br><br> Non e\' possibile cambiare il comune dell\'edificio!');
		}
		if(valuesMap.insert_activeuser!=OldvaluesMap.insert_user && valuesMap.insert_activeuser!=null){
			modify_user=valuesMap.insert_activeuser;
		}else{
			modify_user=OldvaluesMap.insert_activeuser;
		}
		valuesMap.modify_user=modify_user;
		valuesMap.modify_date=new Date();
		log.debug("Utente Modifica: "+valuesMap.modify_user);
		log.debug("Data Modifica: "+valuesMap.modify_date);		
		
		//se l'edificio è un edificio principale non si può cambiare la categoria se ci sono edifici agganciati sotto
		def mapCompleto = [:] ;
		mapCompleto.putAll(OldvaluesMap);
		mapCompleto.putAll(valuesMap);	
		def checkSite=services.queryService.executeCountQuery("Select count(1) from pma_dati_gw.pma_building where cod_site=#{map.cod_building}",OldvaluesMap);
		log.debug("Check Sito: "+checkSite);
		if(checkSite>0 && mapCompleto.hierarchy!='PC'){
			throw new RuntimeException('<b>Attenzione</b><br><br> Non e\' possibile cambiare la categoria. Risultano degli edifici associati a questo complesso!');
		}
		
		//inserisce in automatico il titolo di possesso e la tipologia partendo dalla classificazione
		String code_classification="";
		if(valuesMap.code_classification!=null) {
			code_classification=valuesMap.get("code_classification");
		}
		else {
			code_classification=OldvaluesMap.get("code_classification");
		}
		def recordprop=services.queryService.executeQuery("select type_property, code_type_building from pma_dati_gw.PMA_TAB_CLASSIFICATION WHERE code_classification='"+code_classification+"'",null)[0];
		valuesMap.cod_type_building=recordprop.code_type_building;
		valuesMap.type_property=recordprop.type_property;
		
		//inserisce in automatico il polo direzionale partendo dal presidio
		String fk_aid="";
		log.debug("Polo direzionale: "+valuesMap.fk_pole);	
		if(valuesMap.fk_aid!=null) {
			fk_aid=valuesMap.get("fk_aid");
		}
		else {
			fk_aid=OldvaluesMap.get("fk_aid");
		}
		def recordprop2=services.queryService.executeQuery("select fk_pole from pma_dati_gw.PMA_TAB_AID WHERE cod_aid='" + fk_aid + "'",null)[0];
		valuesMap.fk_pole=recordprop2.fk_pole;
		
		//popolo la classe del registro eventi, questa viene popolata ogni volta un attributo OBBLIGATORIO SUBISCE UNA VARIAZIONE
		
		// recupero l'id dell'edificio
		def id = OldvaluesMap.get("id_building");
		log.debug ("id accertamento edificio: " + id);
		/*
		// recupero l'utente che sta effettuando le modifiche
		def new_user = valuesMap.get("insert_activeuser");
		def old_user = OldvaluesMap.get("insert_activeuser");
		log.debug ("nuovo utente: " + new_user);
		log.debug ("nuovo utente: " + old_user);
		*/
		// recupero la categoria dell'immobile
		def new_hierarchy = valuesMap.get("hierarchy");								
		def old_hierarchy = OldvaluesMap.get("hierarchy");		
		log.debug ("nuova gerarchia: " + new_hierarchy);		
		log.debug ("vecchia gerarchia: " + old_hierarchy);
		// recupero il tipo di immobile
		def new_type = valuesMap.get("descr_building_type");								
		def old_type = OldvaluesMap.get("descr_building_type");			
		log.debug ("nuova tipologia: " + new_type);		
		log.debug ("vecchai tipologia: " + old_type);
		// recupero i valori della descrizione
		def new_descr_building = valuesMap.get("descr_building");								
		def old_descr_building = OldvaluesMap.get("descr_building");			
		log.debug ("nuova descrizione: " + new_descr_building);		
		log.debug ("vecchia descrizione: " + new_descr_building);		
		// recupero i valori della classificazione
		def new_classification = valuesMap.get("code_classification");								
		def old_classification = OldvaluesMap.get("code_classification");			
		log.debug ("nuova classificazione: " + new_classification);		
		log.debug ("vecchia classificazione: " + old_classification);		
		// recupero i valori del comune
		def new_city = valuesMap.get("city");								
		def old_city = OldvaluesMap.get("city");			
		log.debug ("nuovo comune: " + new_city);		
		log.debug ("vecchio comune: " + old_city);			
		// recupero i valori dell'indirizzo	
		def new_address = valuesMap.get("address");								
		def old_address = OldvaluesMap.get("address");			
		log.debug ("nuovo indirizzo: " + new_address);		
		log.debug ("vecchio indirizzo: " + old_address);
		// recupero i valori del presidio
		def new_fk_aid = valuesMap.get("fk_aid");								
		def old_fk_aid = OldvaluesMap.get("fk_aid");			
		log.debug ("nuovo presidio: " + new_fk_aid);		
		log.debug ("vecchio presidio: " + old_fk_aid);
		// recupero i valori della banca
		def new_fk_bank = valuesMap.get("fk_bank");								
		def old_fk_bank = OldvaluesMap.get("fk_bank");			
		log.debug ("nuova banca: " + new_fk_aid);		
		log.debug ("vecchia banca: " + old_fk_aid);
		
		//mappa di aggiornamento
		def ins_ass_proc = [:];
		
		// se il valore della categoria è cambiato, allora scrivo nella tabella delle azioni utente
		if (new_hierarchy!=null && !new_hierarchy.equals(old_hierarchy)){
			
			// definisco oggetto per inserimento nuova azione utente
			ins_ass_proc.id_building = id;
			ins_ass_proc.type_process='Modifica Categoria';
			ins_ass_proc.user_process = modify_user;
			ins_ass_proc.date_process = new Date();
			ins_ass_proc.note_process = modify_user + ' ha modificato la categoria dell\'immobile da \'' + old_hierarchy + '\' a \'' + new_hierarchy+'\'.';
			// inserisco nella classe dei processi di classe
			services.classService.insertClassRecord('pma_activity_log_building',ins_ass_proc);
		};
			
		// se è cambiato il valore del tipo di immobile	
		if (new_type!=null && !new_type.equals(old_type)){
			
			// definisco oggetto per inserimento nuova azione utente
			ins_ass_proc.id_building = id;
			ins_ass_proc.type_process='Modifica Tipologia';
			ins_ass_proc.user_process = modify_user;
			ins_ass_proc.date_process = new Date();
			ins_ass_proc.note_process = modify_user + ' ha modificato la tipologia dell\'immobile da  \'' + old_type + '\' a \'' + new_type+'\'.';
			// inserisco nella classe dei processi di classe
			services.classService.insertClassRecord('pma_activity_log_building',ins_ass_proc);							
		};
					
		// se è cambiato il valore della descrizione	
		if (new_descr_building !=null && !new_descr_building.equals(old_descr_building)){
			
			// definisco oggetto per inserimento nuova azione utente
			ins_ass_proc.id_building = id;
			ins_ass_proc.type_process='Modifica Descrizione Immobile';
			ins_ass_proc.user_process = modify_user;
			ins_ass_proc.date_process = new Date();
			ins_ass_proc.note_process = modify_user + ' ha modificato la descrizione da \'' + old_descr_building + '\' a \'' + new_descr_building+'\'.';
			// inserisco nella classe dei processi di classe
			services.classService.insertClassRecord('pma_activity_log_building',ins_ass_proc);							
		};			

		// se è cambiato il valore della classificazione	
		if (new_classification!=null && !new_classification.equals(old_classification)){
			
			//recupero le descrizioni della classificazione
			def old_descr=services.queryService.executeQuery("SELECT description_classification FROM pma_dati_gw.pma_tab_classification where code_classification=#{map.code_classification}",OldvaluesMap);
			/*
			if(old_descr.description_classification==null || old_descr.description_classification==''){
				old_descr.description_classification='nullo';
			}
			*/
			def new_descr=services.queryService.executeQuery("SELECT description_classification FROM pma_dati_gw.pma_tab_classification where code_classification=#{map.code_classification}",valuesMap);
			
			ins_ass_proc.id_building = id;
			ins_ass_proc.type_process='Modifica Classificazione';
			ins_ass_proc.user_process = modify_user;
			ins_ass_proc.date_process = new Date();
			ins_ass_proc.note_process = modify_user + ' ha modificato la classificazione da  \'' + old_classification + '\' a \'' + new_descr[0].description_classification+'\'.';
			// inserisco nella classe dei processi di classe
			services.classService.insertClassRecord('pma_activity_log_building',ins_ass_proc);							
		};
			
		// se è cambiato il valore del comune	
		if (new_city!=null && !new_city.equals(old_city)){
			
			//recupero la descrizione del comune
			del odl_city=services.queryService.executeQuery("select description  from pma_dati_gw.cmn_cities where uk_istat=#{map.cities}", OldvaluesMap);
			del new_city=services.queryService.executeQuery("select description  from pma_dati_gw.cmn_cities where uk_istat=#{map.cities}", valuesMap);
			
			// definisco oggetto per inserimento nuova azione utente
			ins_ass_proc.id_building = id;
			ins_ass_proc.type_process='Modifica Comune';
			ins_ass_proc.user_process = modify_user;
			ins_ass_proc.date_process = new Date();
			ins_ass_proc.note_process = modify_user + ' ha modificato il comune da  \'' + odl_city[0].description + '\' a \'' + new_city[0].description+'\'.';
			// inserisco nella classe dei processi di classe
			services.classService.insertClassRecord('pma_activity_log_building',ins_ass_proc);							
		};

		// se è cambiato il valore dell'indirizzo	
		if (new_address!= null && !new_address.equals(old_address)){
			
			// definisco oggetto per inserimento nuova azione utente
			ins_ass_proc.id_building = id;
			ins_ass_proc.type_process='Modifica Indirizzo Immobile';
			ins_ass_proc.user_process = modify_user;
			ins_ass_proc.date_process = new Date();
			ins_ass_proc.note_process = modify_user + ' ha modificato l\'indirizzo dell\'immobile da \'' + old_address + '\' a \'' + new_address+'\'.';
			// inserisco nella classe dei processi di classe
			services.classService.insertClassRecord('pma_activity_log_building',ins_ass_proc);							
		};
		
		// se è cambiato il valore del presidio	
		if (new_fk_aid!= null && !new_fk_aid.equals(old_fk_aid)){
			
			//recupero la descrizione del presidio
			def old_aid=services.queryService.executeQuery("select descr_aid from pma_dati_gw.pma_tab_aid where cod_aid =#{map.fk_aid}", OldvaluesMap);
			def new_aid=services.queryService.executeQuery("select descr_aid from pma_dati_gw.pma_tab_aid where cod_aid =#{map.fk_aid}", valuesMap);
			
			// definisco oggetto per inserimento nuova azione utente
			ins_ass_proc.id_building = id;
			ins_ass_proc.type_process='Modifica Presidio';
			ins_ass_proc.user_process = modify_user;
			ins_ass_proc.date_process = new Date();
			ins_ass_proc.note_process = modify_user + ' ha modificato il presidio da \'' + old_fk_aid + '\' a \'' + new_fk_aid+'\'.';
			// inserisco nella classe dei processi di classe
			services.classService.insertClassRecord('pma_activity_log_building',ins_ass_proc);							
		};
		
		// se è cambiato il valore della banca	
		if (new_fk_bank!= null && !new_fk_bank.equals(old_fk_bank)){
			
			//recupero la descrizione della banca
			def old_bank=services.queryService.executeQuery("select  descr_bank from pma_dati_gw.pma_tab_bank where code_bank=#{map.fk_bank}", OldvaluesMap);
			def new_bank=services.queryService.executeQuery("select  descr_bank from pma_dati_gw.pma_tab_bank where code_bank=#{map.fk_bank}", valuesMap);
			
			// definisco oggetto per inserimento nuova azione utente
			ins_ass_proc.id_building = id;
			ins_ass_proc.type_process='Modifica Titolare del Bene';
			ins_ass_proc.user_process = modify_user;
			ins_ass_proc.date_process = new Date();
			ins_ass_proc.note_process = modify_user + ' ha modificato il titolare del bene da \'' + old_fk_bank + '\' a \'' + new_bank[0].descr_bank+'\'.';
			// inserisco nella classe dei processi di classe
			services.classService.insertClassRecord('pma_activity_log_building',ins_ass_proc);							
		};
					
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