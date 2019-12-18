//user:    MPE
//date:    01/03/2019
//ver:     4.3.0
//project: Equipment Management
//type:    action groovy
//class:   geow_users
//note:    groovy per l'abilitazione dell'utente
//		   aggiorna i metadati abilitando l'utente



import com.geowebframework.dataservice.ConfigurationProperties; 
import com.geowebframework.transfer.model.metadataservice.User;


// RECUPERO I DATI UTILI
def username = parameterMap.username;
log.info("username da abilitare: "+username);
	
// ESEGUO IL SERVIZIO DI ABILITAZIONE DELL'UTENTE
gwm_userService.updateUserEnabled(username,1)

// AGGIORNO IL VALORE DELLA CLASS
def pk = parameterMap.pk_geow_users;
def upd_us = [:];
upd_us.pk_geow_users=pk;
upd_us.is_active=1;
classService.updateClassRecord("geow_users", upd_us);
