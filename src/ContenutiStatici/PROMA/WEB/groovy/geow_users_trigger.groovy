//user:    MPE
//date:    01/03/2019
//ver:     4.3.0
//project: Equipment Management
//type:    event trigger (TRIGGER DI CLASSE)
//class:   geow_users
//note:    groovy per la modifica e cancellazione dell'utente, l'invio (se attivo) della notifica e-mail e l'aggiornamento dei metadati
//		   N.B. il trigger richiama il trigger della classe geow_user_groups


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
import com.geowebframework.transfer.objects.webclient.GwBeanDocument;
import com.geowebframework.transfer.model.metadataservice.User;
import com.geowebframework.transfer.model.metadataservice.Group;

public class geow_users_trigger extends com.geowebframework.dataservice.querybuilder.EventTrigger {
    
    public boolean beforeInsert(HashMap<String,Object> valuesMap){	
		return true;
    };
    

    public boolean afterInsert(HashMap<String,Object> valuesMap){		
		return true;
    };
    
    public boolean beforeUpdate(HashMap<String,Object> valuesMap,HashMap<String,Object> oldvaluesMap){
		// DEFINISCO MAPPA GLOBALE
		def AllMap = [:] ;
		AllMap.putAll(oldvaluesMap);
		AllMap.putAll(valuesMap);	
		log.info("mappa totale: " + AllMap);
		
		// SE L'UTENTE HA MODIFICATO NOME E/O COGNOME ALLORA RICALCOLO LA DESCRIZIONE 
		if (valuesMap.name_user!=null || valuesMap.surname_user!=null){
			def new_descr_user = AllMap.surname_user + ' ' + AllMap.name_user;
			valuesMap.put("descr_user",new_descr_user);
		};
		
		// SE L'UTENTE HA MODIFICATO L'E-MAIL DELL'UTENTE ALLORA AGGIORNO NEI METADATI
		if (valuesMap.email!=null){
			String new_email=valuesMap.email;
			// controllo se nei metadati è presente un utente con la stessa e-mail
			def SelectEmail = new_email;
			def EmailVerify = services.gwm_userService.selectByEmail(SelectEmail);
			log.info("verifica ricerca e-mail: " + EmailVerify);
			// se l'e-mail esiste nei metadati allora visualizzo un messaggio di alert
			if(EmailVerify!=null){								
				throw new RuntimeException("<b>ATTENZIONE </b><br>In archivio e\' gia\' presente un utente con la stessa e-mail.<br>Assegnare all'utente un'altra e-mail.");
			}
			// se l'e-mail non esiste, allora agiorno i metadati
			else if (EmailVerify==null){
			def  username=AllMap.username;
			services.gwm_userService.updateUserEmail(username,new_email);	
			};			
		};
		
		
        return true;
    };
    
    public boolean afterUpdate(HashMap<String,Object> valuesMap,HashMap<String,Object> oldvaluesMap){
        return true;
		
    };
    
    public boolean beforeDelete(HashMap<String,Object> valuesMap){

		// RECUPERO INFO DI INTERESSE USERNAME
		def username = valuesMap.get("username");
		def descr_user = valuesMap.get("descr_user");
		def notify = valuesMap.get("notify");
		def email = valuesMap.get("email");
		log.info("info recuperate per la cancellazione di " + username + " - nominativo " + descr_user + " notifica " + notify + " email " + email);
		
		// CONTO IL NUMERO DI GRUPPI A CUI L'UTENTE E' ASSOCIATO
		def count_group=services.queryService.executeQuery("SELECT count(1) AS count_group FROM pma_dati_gw.geow_user_groups WHERE username='" + username + "'",null)[0].count_group;
		log.info("numero di gruppi associati all'utente: " + count_group);
	
		/***************************************************************************************************************************************/
		
		//CANCELLO UTENTE DAI METADATI
		User gwUser = new User(username);
		services.gwm_userService.deleteByUserName(gwUser);			

		/***************************************************************************************************************************************/
		
		// SE E' ATTIVA LA NOTIFICA PER L'UTENTE, ALLORA INVIO E-MAIL DELL'AVVENUTA CANCELLAZIONE
		if (notify==1){
			// recupero i parametri da utilizzare nell'e-mail dal configuration.properties
			String automatic_email = ConfigurationProperties.getInstance().getProperty("info.automatic_email");
			String automatic_regards = ConfigurationProperties.getInstance().getProperty("info.automatic_regards");
			String automatic_signature = ConfigurationProperties.getInstance().getProperty("info.automatic_signature");
			String name_web = ConfigurationProperties.getInstance().getProperty("info.name_web");
			String link_web = ConfigurationProperties.getInstance().getProperty("info.link_web");	
			String subject = name_web + " - DISMISSIONE UTENZA DI ACCESSO";
			String message = "<html><body>Buongiorno <b>" + descr_user +"</b>,";
			message= message +"<br>con la presente si notifica la dismissione dell'utenza <b>" + username + "</b> a te associata per l'accesso al sistema <b>" + name_web + "</b>."
			message= message +"<br>";				
			message= message +"<br>" + automatic_regards;
			message= message +"<br>" + automatic_signature;
			message= message +"<br>";
			message= message +"<br>";
			message= message +"<br>";
			message= message +"<br>";
			message= message +"<br>";
			message= message +"<br>";
			message= message +"<br>";
			message= message +"<br>";
			message= message +"<i>" + automatic_email + "</i>";			
			message= message +"<br></body></html>";
			
			String from = null;			//optional
			Object to = email;			//required
			Object cc = null;			//optional
			Object bcc = null;			//optional
			String replyTo = null;		//optional
			String text = message;		//required
			Integer priority = null;	//optional, 1 = high, 3 = normal, 5 = low
			List<GwBeanDocument> gwBeanDocumentList = null;
			services.gwMailService.sendMail(from, to, cc, bcc, replyTo, subject, text, priority, gwBeanDocumentList);
		};


		/***************************************************************************************************************************************/

		// SE IL NUMERO DI GRUPPI ASSOCIATI ALL'UTENTE E' MAGGIORE DI ZERO ALLORA CICLO LA CANCELLAZIONE
		if (count_group>0){
			// RECUPERO LA LISTA DEI GRUPPI ASSOCIATI ALL'UTENTE
			def list_group=services.queryService.executeQuery("SELECT * FROM pma_dati_gw.geow_user_groups WHERE username='" + username + "'",null);
			log.info("elenco gruppi associati all'utente: " + list_group);				
			// RECUPERO LE CHIAVI DEI RECORD DA CANCELLARE
			for(int i=0; i<list_group.size(); i++){
				// definisco la chiave del record da cancellare
				Integer pk_delete = list_group[i].pk_geow_user_groups;
				// richiamo la cancellazione del record dalla classe geow_user_groups 
				// N.B. la cancellazione dalla classe geow_user_groups richiama l'esecuzione del relativo trigger che cancella dai metadati
				services.classService.deleteClassRecord("geow_user_groups",pk_delete);
			};
		};

        return true;
    };
    
    public boolean afterDelete(HashMap<String,Object> valuesMap){

			
        return true;
    };

} 