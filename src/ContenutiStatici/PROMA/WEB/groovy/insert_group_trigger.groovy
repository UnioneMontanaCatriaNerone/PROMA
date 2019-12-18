import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import com.geowebframework.dataservice.ConfigurationProperties;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import com.geowebframework.transfer.model.metadataservice.Group;
import com.geowebframework.transfer.model.metadataservice.User;

public class BaseGroovyTrigger extends com.geowebframework.dataservice.querybuilder.EventTrigger {
    
    public boolean beforeInsert(HashMap<String,Object> valuesMap){
		
		
		
	//DEFINISCO VARIABILI CAMPI CLASSE
		def funzione = valuesMap.get("funzione");
		//def progetto = valuesMap.get("progetto");
		//def lotto = valuesMap.get("lotto");
        //def commessa = valuesMap.get("commessa");
		def descrizione = valuesMap.get("descr_group");
		log.info("sono qui");
		def cod_group=valuesMap.get("cod_group");
				
		/*
	    //DEFINISCO IL CODICE GRUPPO
		//Sono stati inseriti controlli per scrivere correttamente il codice del gruppo!
		def cod_group = null;
		if(commessa==null && lotto!=null){
		cod_group = progetto+"_"+funzione+"_"+lotto			
		}else if(commessa==null && lotto==null){
		cod_group = progetto+"_"+funzione
		}else if(commessa!=null && lotto==null){
		throw new RuntimeException("Una commessa deve essere associata ad un lotto. Inserire un lotto o eliminare la commessa.")	
		}else{
		cod_group = progetto+"_"+funzione+"_"+lotto+"_"+commessa;
		}
		*/
		log.info("cod_group: "+cod_group);
	//CONTROLLO CHE NON CI SIA UN'ALTRO CODICE GRUPPO
		
		HashMap<String,Object> queryGroup = new HashMap<String,Object>();
		log.info("queryGroup: "+queryGroup);
		queryGroup.cod_group=cod_group;
		log.info("queryGroup: "+queryGroup);
		//Servizio che controlla gruppi nei metadati, se trova un gruppo con stesso codice manda un errore!		
		Group SelectGroup = new Group(cod_group);
		def GruppoSelezionato = services.gwm_groupService.selectByUserGroup(SelectGroup);
		log.info("Gruppo selezionato dalla select sui metadati: "+GruppoSelezionato);
		log.info("SelectGROUP: "+SelectGroup);
		
		if(GruppoSelezionato!=null){								
		throw new RuntimeException("ATTENZIONE: Codice Gruppo già in uso!!");
		} 
		
		
		
		
	//SCRIVO NELLA CLASSE
		

		// valorizzo il numero di contratto (GA + codice tipo contratto + anno + progressivo)
		//valuesMap.put("cod_group",cod_group);	
		log.info(valuesMap);
		
/*	
	//VALORIZZO STORICO GRUPPO (geow_group_history)
	
			def descr_op = "Creazione Gruppo";
			def data_operazione = new Date();
			
			def valuesMap2 = [:] ;
			
			valuesMap2.put("descr_op",descr_op);
			valuesMap2.put("data_operazione",data_operazione);
			valuesMap2.put("cod_group",cod_group);
			
			//INSERIMENTO DATI SU STORICO GRUPPO
			
			services.queryService.executeInsertQuery("INSERT INTO geow_group_history (cod_group,data,descrizione) VALUES (#{map.cod_group},#{map.data_operazione},#{map.descr_op})",valuesMap2);
*/		
		return true;
    };
    

    public boolean afterInsert(HashMap<String,Object> valuesMap){
	    
		//INSERISCO GRUPPO NEI METADATI
			
		//DEFINISCO VARIABILI
		
		def cod_group = valuesMap.get("cod_group");
		def funzione =valuesMap.get("funzione");
		//def progetto =valuesMap.get("progetto");
		//def lotto = valuesMap.get("lotto");
		//def commessa = valuesMap.get("commessa");
		def descrizione = valuesMap.get("descr_group");

		def valuesMap2 = [:] ;

		valuesMap2.put("cod_group",cod_group);
		valuesMap2.put("descrizione",descrizione);
		valuesMap2.put("funzione",funzione);
		
		//INSERTING METADATI gwm_groups
		Group gwGroup = new Group(cod_group, descrizione);
		services.gwm_groupService.insert(gwGroup);
		
/*
		//IF NECESSARY ADDING RELATED USER TOO
		//selecting eventually add user from DATA TABLE
		def mapGroup=services.queryService.executeQuery("select distinct(username) as ug from geow_user_groups where usergroup=#{map.cod_group}",valuesMap2);
		
		//aligning user in METADATA TABLE 
		if(mapGroup!=null && !mapGroup.isEmpty()){
			for(int i=0; i<mapGroup.size(); i++){
				String username = mapGroup[i].ug;
				User user = new User(username);
				Group group = new Group(cod_group);
				services.gwm_groupService.addUserToGroup(user,group);
			}
		}
*/


		//ASSEGNO PRIVILEGI AL GRUPPO

		//Prendo valori di interesse dalla tabella geow_roles
		def q_template="select template_scope,template_group from geow_roles where cod_roles = #{map.funzione}";
		log.error("q_template "+q_template+"-"+valuesMap2.funzione);
		def ris_geow_roles= services.queryService.executeQuery(q_template,valuesMap2);
		
		
		if (ris_geow_roles != null && ris_geow_roles[0] !=null && ris_geow_roles[0].template_scope!=null)
	{
		
		//Ambito di Partenza
		String	sourceScopeValueName =ris_geow_roles[0].template_scope;
		//Gruppo di Partenza
		String	sourceGroupName=ris_geow_roles[0].template_group;
		
		//Ambito di Arrivo
		String targetScopeValueName=null;
		//se presente il lotto e non la commessa prende il LOTTO
		if(commessa==null && lotto!=null){
			targetScopeValueName=lotto;			
			//Se non è presente commessa e lotto prende l'ambito di Default
		}else if(commessa==null && lotto==null){
			targetScopeValueName="Ambito di Default";
			//Se tutto presente prende commessa
		}else{
			targetScopeValueName=commessa
		}
		
		//Gruppo di Arrivo
		String targetGroupName= cod_group;
		

		Boolean	overwrite = true; //true sovrascive i permessi, false li accoda (merge)

		log.info("copyStaticACLFromScopeAndGroupAndInsertIntoScopeAndGroup: ");
		log.info("Ambito di Partenza: " +sourceScopeValueName);
		log.info("Ambito di Arrivo: "   +targetScopeValueName);
		log.info("Gruppo di Partenza: " +sourceGroupName);
		log.info("Gruppo di Arrivo: "   +targetGroupName);
			
		///ATTIVA QUANDO LE VARIABILI SONO PRONTE
		
		services.gwm_permissionService.copyStaticACLFromScopeAndGroupAndInsertIntoScopeAndGroup(sourceScopeValueName, targetScopeValueName,  sourceGroupName, targetGroupName, overwrite);
	}
		
		return true;
    };
    
    public boolean beforeUpdate(HashMap<String,Object> valuesMap,HashMap<String,Object> oldvaluesMap){

        return true;
    };
    
    public boolean afterUpdate(HashMap<String,Object> valuesMap,HashMap<String,Object> oldvaluesMap){

		//// la modifica è ammessa solo per l'associazione utenti + modifica DESCRIZIONE GRUPPO
		
		def cod_group = oldvaluesMap.get("cod_group");
		def descrizione = valuesMap.get("descr_group");
		def descrizione_old = oldvaluesMap.get("descr_group");
		def data_operazione = new Date();
		
		if(descrizione!=descrizione_old){
			if(valuesMap.get("descr_group")==null){
				descrizione =  descrizione_old;
			}
			
			
			
			
			def valuesMap2 = [:] ;
			
			valuesMap2.put("cod_group",cod_group);
			valuesMap2.put("descr_group",descrizione);
			
			def mapGroup=services.queryService.executeQuery("select distinct(username) as ug from geow_user_groups where usergroup=#{map.cod_group}",valuesMap2);


			//MODIFICO DESCRIZIONE GRUPPO SUI METADATI		
			Group gwmupdGroup = new Group(cod_group, descrizione);
			services.gwm_groupService.update(gwmupdGroup);
		}
	
        return true;

    };
    
    public boolean beforeDelete(HashMap<String,Object> valuesMap){

	
		def userkey = valuesMap.get("pk_geow_group");
		
		def valuesMap2 = [:] ;
		
		valuesMap2.put("userkey",userkey);
	
		def groupList=services.queryService.executeQuery("select cod_group from geow_groups where pk_geow_group=#{map.userkey}",valuesMap2)[0];
		log.debug("Gruppo Recuperato da Query: "+groupList.cod_group);
		
		Group selectGroup = new Group(groupList.cod_group);
		log.debug("Gruppo Selezionato dalla query: "+selectGroup)
		
		def selectGroupgwm = services.gwm_groupService.selectByUserGroup(selectGroup);
		log.debug("Gruppo Selezionato dai Servizi: "+selectGroupgwm)
		
		if(selectGroupgwm!=null){
		
			def cod_group = groupList.cod_group;
			log.debug("Codice Gruppo: "+cod_group);
		
			def valuesMap3 = [:] ;
			valuesMap3.put("cod_group",cod_group);
			
			//DELETE METADATI user_groups
			Group gwGroup = new Group(cod_group);
			services.gwm_groupService.delete(gwGroup);
			
			def usernameList=services.queryService.executeQuery("select username from geow_user_groups where usergroup=#{map.cod_group}",valuesMap3);
		
			if(usernameList!=null){
				for(int i=0; i<usernameList.size(); i++){
					
					def usernameMap = usernameList[i];
					User gwmUser = new User(usernameMap.username);
					log.info("Username da cancellare: "+usernameMap.username);
					
					//DELETE METADATI gwm_user_groups
					services.gwm_groupService.removeUserFromGroup(gwmUser, gwGroup);
					
					//DELETE TABELLA DI RELAZIONE geow_user_groups
					services.queryService.executeDeleteQuery("DELETE FROM geow_user_groups WHERE usergroup=#{map.cod_group}",valuesMap3);
					
					
					
				}
			}
		}
		return true;
	};
 
    public boolean afterDelete(HashMap<String,Object> valuesMap){
        return true;
    };
} 