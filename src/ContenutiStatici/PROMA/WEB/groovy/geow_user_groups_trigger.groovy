//user:    MPE
//date:    01/03/2019
//ver:     4.3.0
//project: Equipment Management
//type:    action groovy
//class:   geow_user_groups
//note:    groovy per aggiornamento metadati con associazione/disassociazione utente-gruppo
//		   	1. in insert effettua l'aggiornamento dei metadati andando ad associare l'utente al gruppo/gruppi selezionati
//			2. in delete effettua l'aggiornamento dei metadati andando a dissociate l'utente dal gruppo/gruppi selezionati
//			N.B. il trigger viene richiamato (per la parte di delete) dalla cancellazione dell'utente


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
		return true;
    };
    

    public boolean afterInsert(HashMap<String,Object> valuesMap){
	    
			// RECUPERO VALORI DI UTENTE E GRUPPO
			def username = valuesMap.get("username");
			def usergroup = valuesMap.get("usergroup");
			
			// AGGIORNO I METADATI (ASSOCIO UTENTE A GRUPPO/I)
		   	User gwUser = new User(username);
			Group gwGroup = new Group(usergroup);
			services.gwm_groupService.addUserToGroup(gwUser,gwGroup);
			return true;
    };
    
    public boolean beforeUpdate(HashMap<String,Object> valuesMap,HashMap<String,Object> oldvaluesMap){
        return true;
    };
    
    public boolean afterUpdate(HashMap<String,Object> valuesMap,HashMap<String,Object> oldvaluesMap){	
        return true;

    };
    
    public boolean beforeDelete(HashMap<String,Object> valuesMap){

        // RECUPERO LO USERNAME DELLA RISORSA IN DISASSOCIAZIONE
        def username=services.queryService.executeQuery("select username from pma_dati_gw.geow_user_groups where pk_geow_user_groups=" + valuesMap.get("pk_geow_user_groups"),null)[0].username;
        log.error("username in cancellazione: " + username);
        // RECUPERO IL CODICE GRUPPO DELLA RISORSA IN DISASSOCIAZIONE
        def usergroup=services.queryService.executeQuery("select usergroup from pma_dati_gw.geow_user_groups where pk_geow_user_groups=" + valuesMap.get("pk_geow_user_groups"),null)[0].usergroup;
        log.error("usergroup in cancellazione: " + usergroup);		
		// AGGIUNGO I VALORI RECUPERATI ALLA MAPPA (PER PASSARLI AD AFTER DELETE)
		valuesMap.var_username=username;
		valuesMap.var_usergroup=usergroup;
		log.info("valore della mappa in uscita: " + valuesMap);
	
		return true;

 };
 
    public boolean afterDelete(HashMap<String,Object> valuesMap){
		
		// RECUPERO VALORI UTENTE E GRUPPO IN DISASSOCIAZIONE	
		log.info("valore della mappa in entrata: " + valuesMap);
		def username = valuesMap.var_username;
		def usergroup = valuesMap.var_usergroup;
		
		// AGGIORNO I METADATI (DISSOCIO UTENTE DA GRUPPO/I)
		User gwUser = new User(username);
		Group gwGroup = new Group(usergroup);
		services.gwm_groupService.removeUserFromGroup(gwUser,gwGroup);
		
		return true;
    };

} 