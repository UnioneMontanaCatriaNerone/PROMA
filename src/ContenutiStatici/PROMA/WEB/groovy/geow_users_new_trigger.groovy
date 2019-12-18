//user:    MPE
//date:    01/03/2019
//ver:     4.3.0
//project: Equipment Management
//type:    event trigger (TRIGGER DI CLASSE)
//class:   geow_users_new
//note:    groovy per la creazione di un nuovo utente
//			1. controlla preventivamente se nei metadati è presente un utente con lo stesso username
//			2. controlla preventivamente se nei metadati è presente un utente con la stessa e-mail
//			3. nel caso di controlli negativi visualizza all'utente un messaggio di alert
//			4. nel caso di controlli positivi viene creato il nome completo dell'utente,criptata la password e inserito lo user nei metadati
//			N.B. i settaggi per l'invio delle notifiche e-mail costituisce personalizzazione


// importo le classi 
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

public class geow_users_new_trigger extends com.geowebframework.dataservice.querybuilder.EventTrigger {
    
    public boolean beforeInsert(HashMap<String,Object> valuesMap){

		// recupero lo username, l'indirizzo e-mail, la password inserito dall'utente
		String username=valuesMap.get("username");
		String email=valuesMap.get("email");
		String password=valuesMap.get("password");
		log.info("valori inseriti dall'utente per username: " + username + " - email: " + email + " - password: " + password);
		
		// controllo che l'e-mail sia sintatticamente corretta
		def regex = "^(.+)@(.+)\$";
        def pattern = Pattern.compile(regex);
		def matcher = pattern.matcher((CharSequence) email);
		def okk=matcher.matches();
		
		// se il controllo sintattico della e-mail è superato, procedo con le altre verifiche
		if(okk){		
			// controllo se nei metadati è presente un utente con lo stesso username		
			User SelectUser = new User(username);
			def UserVerify = services.gwm_userService.selectByUserName(SelectUser);
			log.info("esito ricerca username nei metadati: " + UserVerify);
			// se lo username esiste nei metadati allora visualizzo un messaggio di alert
			if(UserVerify!=null){								
				throw new RuntimeException("<b>L\'UTENTE NON PUO\' ESSERE CREATO </b><br>In archivio e\' gia\' presente un utente con lo stesso username.<br>Assegnare all'utente un altro username.");
			};			
			
			// controllo se nei metadati è presente un utente con la stessa e-mail
			def SelectEmail = email;
			def EmailVerify = services.gwm_userService.selectByEmail(SelectEmail);
			log.info("verifica ricerca e-mail: " + EmailVerify);
			// se l'e-mail esiste nei metadati allora visualizzo un messaggio di alert
			if(EmailVerify!=null){								
				throw new RuntimeException("<b>L\'UTENTE NON PUO\' ESSERE CREATO </b><br>In archivio e\' gia\' presente un utente con la stessa e-mail.<br>Assegnare all'utente un'altra e-mail.");
			};			

			// se entrambe le verifiche sono positive, allora creo il nuovo utente e valorizzo il nome completo
			if (UserVerify==null && EmailVerify==null){
				// recupero l'eventuale valore assegnato alla password
				def passwordUser=valuesMap.get("password");
				// se la password è vuota, allora ne genero una random
				if(passwordUser==null){
					passwordUser = RandomStringUtils.randomAlphabetic(1)+RandomStringUtils.randomAlphanumeric(10);					
				};
				// richiamo il servizio di criptazione della password
				ShaPasswordEncoder shaPasswordEncoder = new ShaPasswordEncoder(512);
				def encodedPassword = shaPasswordEncoder.encodePassword(passwordUser, null);
				// valorizzo password (in chiaro) e password crittografata
				valuesMap.put("password_encrypted",encodedPassword);
				valuesMap.put("password",passwordUser);	

				// valorizzo il nome completo
				String name_us = valuesMap.get("name_user");
				String surname_us = valuesMap.get("surname_user");			
				String descr_us = surname_us + " " + name_us;
				valuesMap.put("descr_user",descr_us);	
				
			};		
		} 
		// se il controllo sintattico della e-mail non è superato visualizzo un messaggio di alert
		else{
		    throw new RuntimeException("<b>ATTENZIONE</b><br>L'indirizzo e-mail inserito non e' valido");
		};
		
		return true;
    };
    

    public boolean afterInsert(HashMap<String,Object> valuesMap){

			// N.B. effettuo l'inserimento dell'utente nei metadati (tutti i controlli sono stati eseguiti before insert)
								
			// definisco i valori da passare ai metadati
			def username = valuesMap.get("username");
			def password = valuesMap.get("password");
			def descr_user = valuesMap.get("descr_user");
			def encodedPassword =valuesMap.get("password_encrypted");
			def email = valuesMap.get("email");
			log.info("valori da inserire nei metadati: username " + username + " - email: " + email + " - password criptata: " + encodedPassword);			
			
			// inserisco l'utente in gwm_users
			User gwUser = new User(username,encodedPassword);
			services.gwm_userService.insert(gwUser);
			
			// aggiorno l'utente con il valore della e-mail
			services.gwm_userService.updateUserEmail(username,email);

			// aggiorno l'utente con la forzatura del cambio password al primo accesso
			services.gwm_userService.updateUserCredentialsNonExpired(username,0);		


			// RECUPERO IL FLAG DI NOTIFICA
			Integer flag_notify=valuesMap.get("notify");
			log.info("flag di notifica attiva: " + flag_notify);
		
			// SE E' ATTIVA LA NOTIFICA PER L'UTENTE, ALLORA INVIO E-MAIL DELL'AVVENUTA CANCELLAZIONE
			if (flag_notify==1){
				// recupero i parametri da utilizzare nell'e-mail dal configuration.properties
				String automatic_email = ConfigurationProperties.getInstance().getProperty("info.automatic_email");
				String automatic_regards = ConfigurationProperties.getInstance().getProperty("info.automatic_regards");
				String automatic_signature = ConfigurationProperties.getInstance().getProperty("info.automatic_signature");
				String title = ConfigurationProperties.getInstance().getProperty("info.title");
				String link_web = ConfigurationProperties.getInstance().getProperty("url.in.conf");	
				String subject = title + " - CREAZIONE UTENZA DI ACCESSO";
				String message = "<html><body>Buongiorno <b>" + descr_user +"</b>,";
				message= message +"<br>con la presente si notifica la creazione di una utenza a te associata per il sistema <b>" + title + "</b>."
				message= message +"<br><br>Puoi effettuare l'accesso collegandoti all'indirizzo <b><a href='" + link_web + "'>" + link_web + "</a></b> utilizzando i seguenti parametri di login:";
				message= message +"<br>username <b>" + username + "</b>";
				message= message +"<br>password <b>" + password + "</b>";				
				message= message +"<br><br>Al primo accesso ti verrà richiesto di modificare la password.";	
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
			
			
			
			
			
			return true;			
			
		};

    
    public boolean beforeUpdate(HashMap<String,Object> valuesMap,HashMap<String,Object> oldvaluesMap){
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