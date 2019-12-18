// c$\projects\PROMA\WEB\groovy\pma_dati_gw.pma_building_ext_trigger.groovy
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

public class pma_building_ext extends com.geowebframework.dataservice.querybuilder.EventTrigger {
    
public boolean beforeInsert(HashMap<String,Object> valuesMap){

	log.debug("Mappa in entrata: "+valuesMap);

	//recupero i campi dalla pma_dati_gw.cmn_cities
	def uk_istat=valuesMap.get("city");
	def descr_state=valuesMap.get("descr_state");
	def record=services.queryService.executeQuery("select * from pma_dati_gw.cmn_cities where uk_istat='"+uk_istat+"'",null)[0];
	
	Integer count_type=services.queryService.executeQuery("select count(*) as count_type from pma_dati_gw.pma_dati_gw.pma_building where city='"+uk_istat+"'",null)[0].count_type;
	Integer new_progr=count_type+1;
	if(descr_state.equals("IT")){
		valuesMap.cod_building='IT'+uk_istat+new_progr.toString().padLeft(6, '0');
	}else{
		valuesMap.cod_building=descr_state+uk_istat+new_progr.toString().padLeft(9, '0');
	}
	
	valuesMap.insert_user2=valuesMap.insert_activeuser2;
	valuesMap.insert_date2=new Date();

	//faccio una count per il primo inserimento
	/*def primo=services.queryService.executeQuery("select count(substring(cod_building,9,10)) as primo from pma_dati_gw.pma_building where (substring(cod_building,3,6)=#{map.city} or substring(cod_building,3,3)=#{map.city}) and type_ownership='Edificio'",valuesMap)[0].primo;
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
		//select max(substring(cod_building,9,10))+1 as prog from pma_dati_gw.pma_building where substring(cod_building,3,6)='028001'
		def prog=services.queryService.executeQuery("select max(substring(cod_building,9,10))+1 as prog from pma_dati_gw.pma_building where (substring(cod_building,3,6)=#{map.city} or substring(cod_building,3,3)=#{map.city}) and type_ownership='Edificio'",valuesMap)[0].prog;
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
	log.debug("Data inserimento: "+valuesMap.insert_date);*/
	
	


/*

//conto in pma_dati_gw.pma_building di quanti uk_istat ho
// creo progr su uk se IT altrimenti in descr_state


// verifico se codice istat uk_istat associato al comune scelto è >5
// Se true, allora cerca il max per quel comune ed inserisci +1, concateno descr_state+uk_istat+Progressivo(per uk_istat)
// Se false, concatena in cod_building i campi descr_state+000000+Progressivo(per descr_state)

		def cCity=valuesMap.city; //uk_istat
	//	def stato= "SELECT descr_state from pma_dati_gw.cmn_cities where uk_istat=#{map.city}";
		def map_query =[:];
		map_query.city=cCity;
		map_query
		
		if cCity.length>5 {
		// Se true, allora cerca il max per quel comune ed inserisci +1, concateno descr_state+uk_istat+Progressivo(per uk_istat)


		}
		
		else
	
		
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
		
	/*	def cod_building = valuesMap.get("cod_building");
		def check_cod = services.queryService.executeQuery("SELECT count(id_system) as check_cod FROM gwd_system WHERE cod_building='"+cod_building+"'", null)[0].check_cod;	
		log.info("loggo la check_cod: "+check_cod);
		if(check_cod!=null && check_cod>0){
			throw new RuntimeException("Attenzione: Codice Edificio già in uso. <br>Scegliere una codifica differente!");
		}
*/	
		//inserisce in automatico il titolo di possesso e la tipologia partendo dalla classificazione
		/*String code_classification=valuesMap.get("code_classification");
		def recordprop=services.queryService.executeQuery("select type_property, code_type_building from PMA_TAB_CLASSIFICATION WHERE code_classification='" + code_classification + "'",null)[0];
		valuesMap.put("cod_type_building", recordprop.code_type_building);
		valuesMap.put("type_property", recordprop.type_property);
		*/
		//inserisce in automatico il polo direzionale partendo dal presidio
		String fk_aid=valuesMap.get("fk_aid");
		def recordprop2=services.queryService.executeQuery("select fk_pole from pma_dati_gw.pma_tab_aid WHERE cod_aid='" + fk_aid + "'",null)[0];
		valuesMap.put("fk_pole", recordprop2.fk_pole);
		
		return true;
    };
	
	public boolean afterInsert(HashMap<String,Object> valuesMap){
	
		return true;
		
	}; 	

    public boolean beforeUpdate(HashMap<String,Object> valuesMap,HashMap<String,Object> OldvaluesMap){
        	
		log.debug("vecchio comune: "+OldvaluesMap.city);
		log.debug("nuovo comune: "+valuesMap.city);

		def modify_user2;
		
		if(OldvaluesMap.city!=valuesMap.city && valuesMap.city!=null){
			throw new RuntimeException('<b>Attenzione</b><br><br> Non e\' possibile cambiare il comune dell\'edificio!');
		}
		if(valuesMap.insert_activeuser2!=OldvaluesMap.insert_user2 && valuesMap.insert_activeuser2!=null){
			modify_user2=valuesMap.insert_activeuser2;
		}else{
			modify_user2=OldvaluesMap.insert_activeuser2;
		}
		valuesMap.modify_user2=modify_user2;
		valuesMap.modify_date2=new Date();
		log.debug("Utente Modifica: "+valuesMap.modify_user2);
		log.debug("Data Modifica: "+valuesMap.modify_date2);		
		
		//inserisce in automatico il titolo di possesso e la tipologia partendo dalla classificazione
		String code_classification="";
		if(valuesMap.code_classification!=null) {
			code_classification=valuesMap.get("code_classification");
		}
		else {
			code_classification=OldvaluesMap.get("code_classification");
		}
		def recordprop=services.queryService.executeQuery("select type_property, code_type_building from pma_dati_gw.PMA_TAB_CLASSIFICATION WHERE code_classification='"+code_classification+"'",null)[0];
		//EMA valuesMap.cod_type_building=recordprop.code_type_building;
		//EMA valuesMap.type_property=recordprop.type_property;
		
		//inserisce in automatico il polo direzionale partendo dal presidio
		String fk_aid="";
		if(valuesMap.fk_aid!=null) {
			fk_aid=valuesMap.get("fk_aid");
		}
		else {
			fk_aid=OldvaluesMap.get("fk_aid");
		}
		def recordprop2=services.queryService.executeQuery("select fk_pole from pma_dati_gw.pma_tab_aid WHERE cod_aid='" + fk_aid + "'",null)[0];
		valuesMap.fk_pole=recordprop2.fk_pole;
		
		//popolo la classe del registro eventi, questa viene popolata ogni volta un attributo OBBLIGATORIO SUBISCE UNA VARIAZIONE
		
		// recupero l'id dell'edificio
		def id = OldvaluesMap.get("id_building");
		log.debug ("id accertamento edificio: " + id);
		
		// recupero la tipologia_filiale
		def new_tipologia_filiale = valuesMap.get("tipologia_filiale");								
		def old_tipologia_filiale = OldvaluesMap.get("tipologia_filiale");		
		log.debug ("nuova Tipologia Filiale: " + new_tipologia_filiale);		
		log.debug ("vecchia Tipologia Filiale: " + old_tipologia_filiale);
	
		// recupero l'area_prelievo
		def new_area_prelievo = valuesMap.get("area_prelievo");								
		def old_area_prelievo = OldvaluesMap.get("area_prelievo");			
		log.debug ("nuova area prelievo: " + new_area_prelievo);		
		log.debug ("vecchai area prelievo: " + old_area_prelievo);
		
		// recupero i valori della fruibilita_atm_mta
		def new_fruibilita_atm_mta = valuesMap.get("fruibilita_atm_mta");								
		def old_fruibilita_atm_mta = OldvaluesMap.get("fruibilita_atm_mta");			
		log.debug ("nuova fruibilità ATM: " + new_fruibilita_atm_mta);		
		log.debug ("vecchia fruibilità ATM: " + new_fruibilita_atm_mta);	

		// recupero i valori dell' area_filiale
		def new_area_filiale = valuesMap.get("area_filiale");								
		def old_area_filiale = OldvaluesMap.get("area_filiale");			
		log.debug ("nuova area Filiale: " + new_area_filiale);		
		log.debug ("vecchia area Filiale: " + old_area_filiale);	
	
		// recupero i valori dell' area_consulenza
		def new_area_consulenza = valuesMap.get("area_consulenza");								
		def old_area_consulenza = OldvaluesMap.get("area_consulenza");			
		log.debug ("nuova area consulenza: " + new_area_filiale);		
		log.debug ("vecchia area consulenza: " + old_area_filiale);	
	
		// recupero i valori del servizio_cassa
		def new_servizio_cassa = valuesMap.get("servizio_cassa");								
		def old_servizio_cassa = OldvaluesMap.get("servizio_cassa");			
		log.debug ("nuovo comune: " + new_servizio_cassa);		
		log.debug ("vecchio comune: " + old_servizio_cassa);		
		
		// recupero i valori del locale_cassetta	
		def new_locale_cassetta = valuesMap.get("locale_cassetta");								
		def old_locale_cassetta = OldvaluesMap.get("locale_cassetta");			
		log.debug ("nuovo indirizzo: " + new_locale_cassetta);		
		log.debug ("vecchio indirizzo: " + old_locale_cassetta);
		// recupero i valori dei servizi_igienici
		def new_servizi_igienici = valuesMap.get("servizi_igienici");								
		def old_servizi_igienici = OldvaluesMap.get("servizi_igienici");			
		log.debug ("nuovi servizi: " + new_servizi_igienici);		
		log.debug ("vecchi servizi: " + old_servizi_igienici);
		// recupero i valori di ascensori
		def new_ascensori = valuesMap.get("ascensori");								
		def old_ascensori = OldvaluesMap.get("ascensori");			
		log.debug ("nuovo ascensore: " + new_ascensori);		
		log.debug ("vecchio ascensore: " + old_ascensori);
		// recupero i valori di spazi_corridoi
		def new_spazi_corridoi = valuesMap.get("spazi_corridoi");								
		def old_spazi_corridoi = OldvaluesMap.get("spazi_corridoi");			
		log.debug ("nuovo presidio: " + new_spazi_corridoi);		
		log.debug ("vecchio presidio: " + old_spazi_corridoi);
		
		// recupero i valori di percorsi_tattili
		def new_percorsi_tattili = valuesMap.get("percorsi_tattili");								
		def old_percorsi_tattili = OldvaluesMap.get("percorsi_tattili");			
		log.debug ("nuova banca: " + new_percorsi_tattili);		
		log.debug ("vecchia banca: " + old_percorsi_tattili);
		// recupero i valori di collegamento_piani
		def new_collegamento_piani = valuesMap.get("collegamento_piani");								
		def old_collegamento_piani = OldvaluesMap.get("collegamento_piani");			
		log.debug ("nuovo presidio: " + new_collegamento_piani);		
		log.debug ("vecchio presidio: " + old_collegamento_piani);
		// recupero i valori delle note
		def new_note = valuesMap.get("note");								
		def old_note = OldvaluesMap.get("note");			
		log.debug ("nuova banca: " + new_note);		
		log.debug ("vecchia banca: " + new_note);
		
		//mappa di aggiornamento
		def ins_ass_proc = [:];
		
		// se il valore della tipologia_filiale è cambiato, allora scrivo nella tabella delle azioni utente
		if (new_tipologia_filiale!=null && !new_tipologia_filiale.equals(old_tipologia_filiale)){
			
			// definisco oggetto per inserimento nuova azione utente
			ins_ass_proc.id_building = id;
			ins_ass_proc.type_process='Modifica Tipologia Filiale';
			ins_ass_proc.user_process = modify_user2;
			ins_ass_proc.date_process = new Date();
			ins_ass_proc.note_process = modify_user2 + ' ha modificato la Tipologia Filiale dell\'immobile da \'' + old_tipologia_filiale + '\' a \'' + new_tipologia_filiale+'\'.';
			// inserisco nella classe dei processi di classe
			services.classService.insertClassRecord('pma_activity_log_building_ext',ins_ass_proc);
		};
			
		// se è cambiato il valore dell'Area Prelievo
		if (new_area_prelievo!=null && !new_area_prelievo.equals(old_area_prelievo)){
			
			// definisco oggetto per inserimento nuova azione utente
			ins_ass_proc.id_building = id;
			ins_ass_proc.type_process='Modifica Area Prelievo';
			ins_ass_proc.user_process = modify_user2;
			ins_ass_proc.date_process = new Date();
			ins_ass_proc.note_process = modify_user2 + ' ha modificato l\'area del Prelievo da  \'' + old_area_prelievo + '\' a \'' + new_area_prelievo+'\'.';
			// inserisco nella classe dei processi di classe
			services.classService.insertClassRecord('pma_activity_log_building_ext',ins_ass_proc);							
		};
					
		// se è cambiato il valore della fruibilità atm	
		if (new_fruibilita_atm_mta !=null && !new_fruibilita_atm_mta.equals(old_fruibilita_atm_mta)){
			
			// definisco oggetto per inserimento nuova azione utente
			ins_ass_proc.id_building = id;
			ins_ass_proc.type_process='Modifica Fruibilità ATM-MTA';
			ins_ass_proc.user_process = modify_user2;
			ins_ass_proc.date_process = new Date();
			ins_ass_proc.note_process = modify_user2 + ' ha modificato la fruibilità ATM-MTA da \'' + old_fruibilita_atm_mta + '\' a \'' + new_fruibilita_atm_mta+'\'.';
			// inserisco nella classe dei processi di classe
			services.classService.insertClassRecord('pma_activity_log_building_ext',ins_ass_proc);							
		};			

		// se è cambiato il valore dell'area_filiale
		if (new_area_filiale!=null && !new_area_filiale.equals(old_area_filiale)){
			
			//recupero le descrizioni della classificazione
			def old_descr=services.queryService.executeQuery("SELECT description_classification FROM pma_dati_gw.pma_tab_classification where code_classification=#{map.code_classification}",OldvaluesMap);
			if(old_descr.description_classification==null || old_descr.description_classification==''){
				old_descr.description_classification='nullo';
			}
			
			//def new_descr=services.queryService.executeQuery("SELECT description_classification FROM pma_dati_gw.pma_tab_classification where code_classification=#{map.code_classification}",valuesMap);
			
			ins_ass_proc.id_building = id;
			ins_ass_proc.type_process='Modifica Area Filiale';
			ins_ass_proc.user_process = modify_user2;
			ins_ass_proc.date_process = new Date();
			ins_ass_proc.note_process = modify_user2 + ' ha modificato l\'area della filiale da  \'' + old_area_filiale + '\' a \'' +new_area_filiale+'\'.';
			// inserisco nella classe dei processi di classe
			services.classService.insertClassRecord('pma_activity_log_building_ext',ins_ass_proc);							
		};
		
// se è cambiato il valore dell'Area Consulenza
		if (new_area_consulenza!=null && !new_area_consulenza.equals(old_area_consulenza)){
			
			// definisco oggetto per inserimento nuova azione utente
			ins_ass_proc.id_building = id;
			ins_ass_proc.type_process='Modifica Area Consulenza';
			ins_ass_proc.user_process = modify_user2;
			ins_ass_proc.date_process = new Date();
			ins_ass_proc.note_process = modify_user2 + ' ha modificato l\'area della Consulenza da  \'' + old_area_consulenza + '\' a \'' + new_area_consulenza+'\'.';
			// inserisco nella classe dei processi di classe
			services.classService.insertClassRecord('pma_activity_log_building_ext',ins_ass_proc);							
		};

		// se è cambiato il valore del Servizio Cassa	
		if (new_servizio_cassa!=null && !new_servizio_cassa.equals(old_servizio_cassa)){		
			// definisco oggetto per inserimento nuova azione utente
			ins_ass_proc.id_building = id;
			ins_ass_proc.type_process='Modifica Servizio Cassa';
			ins_ass_proc.user_process = modify_user2;
			ins_ass_proc.date_process = new Date();
			ins_ass_proc.note_process = modify_user2 + ' ha modificato il servizio cassa da  \'' + old_servizio_cassa+ '\' a \'' + new_servizio_cassa +'\'.';
			// inserisco nella classe dei processi di classe
			services.classService.insertClassRecord('pma_activity_log_building_ext',ins_ass_proc);							
		};

		// se è cambiato il valore del locale_cassetta	
		if (new_locale_cassetta!= null && !new_locale_cassetta.equals(old_locale_cassetta)){
			// definisco oggetto per inserimento nuova azione utente
			ins_ass_proc.id_building = id;
			ins_ass_proc.type_process='Modifica locale_cassetta ';
			ins_ass_proc.user_process = modify_user2;
			ins_ass_proc.date_process = new Date();
			ins_ass_proc.note_process = modify_user2 + ' ha modificato il Locale Cassetta da \'' + old_locale_cassetta + '\' a \'' + new_locale_cassetta+'\'.';
			// inserisco nella classe dei processi di classe
			services.classService.insertClassRecord('pma_activity_log_building_ext',ins_ass_proc);							
		};
		
		// se è cambiato il valore della servizi_igienici	
		if (new_servizi_igienici!= null && !new_servizi_igienici.equals(old_servizi_igienici)){
			// definisco oggetto per inserimento nuova azione utente
			ins_ass_proc.id_building = id;
			ins_ass_proc.type_process='Modifica Servizi Igienici';
			ins_ass_proc.user_process = modify_user2;
			ins_ass_proc.date_process = new Date();
			ins_ass_proc.note_process = modify_user2 + ' ha modificato il titolare del bene da \'' + old_servizi_igienici + '\' a \'' + new_servizi_igienici+'\'.';
			// inserisco nella classe dei processi di classe
			services.classService.insertClassRecord('pma_activity_log_building_ext',ins_ass_proc);							
		};
		
		// se è cambiato il valore della ascensori
		if (new_ascensori!= null && !new_ascensori.equals(old_ascensori)){
			// definisco oggetto per inserimento nuova azione utente
			ins_ass_proc.id_building = id;
			ins_ass_proc.type_process='Modifica Ascensore';
			ins_ass_proc.user_process = modify_user2;
			ins_ass_proc.date_process = new Date();
			ins_ass_proc.note_process = modify_user2 + ' ha modificato il titolare del bene da \'' + old_ascensori + '\' a \'' + new_ascensori+'\'.';
			// inserisco nella classe dei processi di classe
			services.classService.insertClassRecord('pma_activity_log_building_ext',ins_ass_proc);							
		};
			
		// se è cambiato il valore degli spazi_corridoi	
		if (new_spazi_corridoi!= null && !new_spazi_corridoi.equals(old_spazi_corridoi)){
			// definisco oggetto per inserimento nuova azione utente
			ins_ass_proc.id_building = id;
			ins_ass_proc.type_process='Modifica spazi manovra';
			ins_ass_proc.user_process = modify_user2;
			ins_ass_proc.date_process = new Date();
			ins_ass_proc.note_process = modify_user2 + ' ha modificato lo spazio da \'' + old_spazi_corridoi + '\' a \'' + new_spazi_corridoi+'\'.';
			// inserisco nella classe dei processi di classe
			services.classService.insertClassRecord('pma_activity_log_building_ext',ins_ass_proc);							
		};
		// se è cambiato il valore della collegamento_piani
		if (new_collegamento_piani!= null && !new_collegamento_piani.equals(old_collegamento_piani)){
			
			
			// definisco oggetto per inserimento nuova azione utente
			ins_ass_proc.id_building = id;
			ins_ass_proc.type_process='Modifica Collegamento Piani';
			ins_ass_proc.user_process = modify_user2;
			ins_ass_proc.date_process = new Date();
			ins_ass_proc.note_process = modify_user2 + ' ha modificato il Collegamento Piani da \'' + old_collegamento_piani + '\' a \'' + new_collegamento_piani+'\'.';
			// inserisco nella classe dei processi di classe
			services.classService.insertClassRecord('pma_activity_log_building_ext',ins_ass_proc);							
		};
				// se è cambiato il valore della percorsi_tattili
		if (new_percorsi_tattili!= null && !new_percorsi_tattili.equals(old_percorsi_tattili)){
			
			
			// definisco oggetto per inserimento nuova azione utente
			ins_ass_proc.id_building = id;
			ins_ass_proc.type_process='Modifica Percorsi Tattili';
			ins_ass_proc.user_process = modify_user2;
			ins_ass_proc.date_process = new Date();
			ins_ass_proc.note_process = modify_user2 + ' ha modificato Percorsi Tattili da \'' + old_percorsi_tattili + '\' a \'' + new_percorsi_tattili+'\'.';
			// inserisco nella classe dei processi di classe
			services.classService.insertClassRecord('pma_activity_log_building_ext',ins_ass_proc);							
		};
		// se è cambiato il valore della note
		if (new_note!= null && !new_note.equals(old_note)){
			
			
			// definisco oggetto per inserimento nuova azione utente
			ins_ass_proc.id_building = id;
			ins_ass_proc.type_process='Modifica Note';
			ins_ass_proc.user_process = modify_user2;
			ins_ass_proc.date_process = new Date();
			ins_ass_proc.note_process = modify_user2 + ' ha modificato le note da \'' + old_note + '\' a \'' + new_note+'\'.';
			// inserisco nella classe dei processi di classe
			services.classService.insertClassRecord('pma_activity_log_building_ext',ins_ass_proc);							
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