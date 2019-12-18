//user:    MPE
//date:    01/03/2019
//ver:     4.3.0
//project: Equipment Management
//type:    action groovy
//class:   geow_users
//note:    groovy per il reset password dell'utente
//			1. reimposta la password di default
//			2. reimposta il cambio password di defaultù
// 			3. se è attivo la notifica, invia una e-mail all'utente

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



// RECUPERO INFO UTENTE
String user = item.username;
String password = item.password;
Integer pk = item.pk_geow_users;
Integer change_password = item.change_password;
Integer notify = item.notify;
String descr_user = item.descr_user;
String email = item.email;
log.info("username reset password: " + user);

// REIMPOSTO LA PASSWORD CON IL VALORE DI DEFAULT
String pass_new = password;

// RICHIAMO IL SERVIZIO DI CRIPTAZIONE
ShaPasswordEncoder shaPasswordEncoder = new ShaPasswordEncoder(512);
def encodedPassword = shaPasswordEncoder.encodePassword(pass_new, null);
log.info("valore della password crittografata: " + encodedPassword);

// AGGIORNO VALORE DI PASSWORD IN CHIARO E CRITTOGRAFATA
def upd_user = [:];
upd_user.pk_geow_users = pk;
upd_user.password = pass_new;
upd_user.password_crittografata = encodedPassword;
log.info("mappa di aggiornamento dell'utente: " + upd_user);
classService.updateClassRecord("geow_users", upd_user);

// AGGIORNO PASSWORD NEI METADATI
gwm_userService.updateUserPassword(user,encodedPassword);

// IMPOSTO IL CAMBIO PASSWORD AL PRIMO ACCESSO
gwm_userService.updateUserCredentialsNonExpired(user,0);


// SE E' ATTIVA LA NOTIFICA PER L'UTENTE, ALLORA INVIO E-MAIL DELL'AVVENUTA CANCELLAZIONE
if (notify==1){
	// recupero i parametri da utilizzare nell'e-mail dal configuration.properties
	String automatic_email = ConfigurationProperties.getInstance().getProperty("info.automatic_email");
	String automatic_regards = ConfigurationProperties.getInstance().getProperty("info.automatic_regards");
	String automatic_signature = ConfigurationProperties.getInstance().getProperty("info.automatic_signature");
	String name_web = ConfigurationProperties.getInstance().getProperty("info.name_web");
	String link_web = ConfigurationProperties.getInstance().getProperty("info.link_web");	
	String subject = name_web + " - RESET PASSWORD DI ACCESSO";
	String message = "<html><body>Buongiorno <b>" + descr_user +"</b>,";
	message= message +"<br>con la presente si notifica che è stato effettuato il reset della password per il sistema <b>" + name_web + "</b>."
	message= message +"<br><br>Puoi effettuare l'accesso collegandoti all'indirizzo <b><a href='" + link_web + "'>" + link_web + "</a></b> utilizzando i seguenti parametri di login:";	
	message= message +"<br>username <b>" + user + "</b>";
	message= message +"<br>password <b>" + pass_new + "</b>";				
	message= message +"<br><br>La password è temporanea e dovrà essere modificata al primo accesso.";	
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
	gwMailService.sendMail(from, to, cc, bcc, replyTo, subject, text, priority, gwBeanDocumentList);
};	



