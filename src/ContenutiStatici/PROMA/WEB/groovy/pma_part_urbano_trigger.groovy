import java.util.List;

import com.geowebframework.transfer.model.dataservice.GwdFolder;
import com.geowebframework.dataservice.GwdFolderService;
import com.geowebframework.dataservice.service.GwClassListService;
import com.geowebframework.gwMnemonicCode.model.widget.MnemonicCodeSelectionWidget;
import com.geowebframework.gwMnemonicCode.model.widget.MnemonicCodeWidget;
import com.geowebframework.metadataservice.ClassService;
import com.geowebframework.metadataservice.GwmMnemonicCodeService;
import org.apache.commons.lang.StringUtils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import com.geowebframework.dataservice.ConfigurationProperties;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import com.geowebframework.transfer.model.metadataservice.*;


public class NameTriggerGroovyTrigger extends com.geowebframework.dataservice.querybuilder.EventTrigger {
    
	public boolean beforeInsert(HashMap<String,Object> valuesMap){

		log.debug("NameTrigger beforeInsert START");
		//controllo se esiste gia un codice uguale
		//richiamo funzione di controllo del codice univoco
		//def genericServiceFunction= services.gse.getInstanceByScriptName("genericServiceFunction.groovy");
		//genericServiceFunction.init(services); 
		//Funzione che controlla se esiste un codice univoco in una specifica tabelle
		//table_name (obbligatorio): nome della tabella in cui effettuare il controllo
		//column_name (obbligatorio): nome del campo univoco da controllare
		//column_value (obbligatorio): valore del campo univoco da controllare
		//tenant_column (facoltativo, passare null se non serve): nome del campo ambito
		//tenant_value (facoltativo, passare null se non serve): valore del campo ambito se tenant è valorizzato
		//genericServiceFunction.tableExistingCode("pma_patrimonial_entities","cod_patr_entities",valuesMap.cod_patr_entities,null,null);
		
		//inserisce l'uk_catastale concatenando codice belfiore, sezione, foglio, particella e subalterno
		//EMA def sez;
		def fog;
		def part;
		def sub;
		def ukb;
		
		if(valuesMap.id_comuni==null || valuesMap.id_comuni=='') {
			//inserisce il comune partendo dall'immobile
			String cod_building=valuesMap.get("cod_building");
			def recordprop=services.queryService.executeQuery("select cc.uk_belfiore from pma_dati_gw.CMN_CITIES AS cc LEFT JOIN pma_dati_gw.PMA_BUILDING AS pb ON cc.uk_istat=pb.city WHERE pb.cod_building='"+cod_building+"'",null)[0];
			valuesMap.id_comuni=recordprop.uk_belfiore;
		}
		
		ukb=valuesMap.id_comuni;
		
		/*if(valuesMap.sezione==null) {
			def warning_title ='<b>ATTENZIONE</b><br>';
			def warning_info ='E\' necessario inserire la sezione';		
			def warning_message = warning_title+warning_info;
			throw new RuntimeException(warning_message);
		}*/
		if(valuesMap.foglio==null) {
			def warning_title ='<b>ATTENZIONE</b><br>';
			def warning_info ='E\' necessario inserire il foglio';		
			def warning_message = warning_title+warning_info;
			throw new RuntimeException(warning_message);
		}
		if(valuesMap.numero==null) {
			def warning_title ='<b>ATTENZIONE</b><br>';
			def warning_info ='E\' necessario inserire la particella';		
			def warning_message = warning_title+warning_info;
			throw new RuntimeException(warning_message);
		}
		/*if(valuesMap.subalterno==null) {
			def warning_title ='<b>ATTENZIONE</b><br>';
			def warning_info ='E\' necessario inserire il subalterno';		
			def warning_message = warning_title+warning_info;
			throw new RuntimeException(warning_message);
		}*/
		
		//log.debug("Codice Belfiore Trigger: "+valuesMap.id_comuni);
		/*log.debug("Foglio Trigger: "+valuesMap.foglio);
		log.debug("Particella Trigger: "+valuesMap.particella);
		log.debug("Subalterno Trigger: "+valuesMap.subalterno);
		log.debug("Sezione Trigger: "+valuesMap.sezione);*/
		
		/*EMA if(valuesMap.sezione!=null && valuesMap.sezione!=''){
			sez=valuesMap.sezione.toString().padLeft(2, '0');
		}else{
			sez=''.toString().padLeft(2, '0');
		}
		*/
		//if(valuesMap.foglio!=null && valuesMap.foglio!=''){
			fog=valuesMap.foglio.toString().padLeft(5, '0');
		/*}else{
			fog=''.toString().padLeft(5, '0');
		}*/
		//if(valuesMap.particella!=null && valuesMap.particella!=''){
			part=valuesMap.numero.toString().padLeft(5, '0');
		/*}else{
			part=''.toString().padLeft(5, '0');
		}*/
		if(valuesMap.subalterno!=null && valuesMap.subalterno!=''){
			sub=valuesMap.subalterno.toString().padLeft(4, '0');
		}else{
			sub='0000';
		}
		
		//EMA log.debug("Sezione Trigger2: "+sez);
		log.debug("Foglio Trigger2: "+fog);
		log.debug("Particella Trigger2: "+part);
		log.debug("Subalterno Trigger2: "+sub);
		//ukb=valuesMap.id_comuni;
		
		valuesMap.uk_catastale=ukb+/*sez+*/fog+part+sub;
		log.debug("UK Catastale: "+valuesMap.uk_catastale);
		
		
		
		log.debug("NameTrigger beforeInsert END");	
		return true;
	};
	
	public boolean afterInsert(HashMap<String,Object> valuesMap){
	
		log.debug("NameTrigger afterInsert START");
		
		log.debug("NameTrigger afterInsert END");
		return true;
	};
	
	public boolean beforeUpdate(HashMap<String,Object> valuesMap,HashMap<String,Object> oldvaluesMap){
	
		log.debug("NameTrigger beforeUpdate START");

		def mapCompleto = [:] ;
		
		mapCompleto.putAll(oldvaluesMap);
		mapCompleto.putAll(valuesMap);
		
		//controllo se esiste gia un codice uguale
		//richiamo funzione di controllo del codice univoco
		//def genericServiceFunction= services.gse.getInstanceByScriptName("genericServiceFunction.groovy");
		//genericServiceFunction.init(services); 
		//Funzione che controlla se esiste un codice univoco in una specifica tabelle
		//table_name (obbligatorio): nome della tabella in cui effettuare il controllo
		//column_name (obbligatorio): nome del campo univoco da controllare
		//column_value (obbligatorio): valore del campo univoco da controllare
		//tenant_column (facoltativo, passare null se non serve): nome del campo ambito
		//tenant_value (facoltativo, passare null se non serve): valore del campo ambito se tenant è valorizzato
		//genericServiceFunction.tableExistingCode("pma_patrimonial_entities","cod_patr_entities",valuesMap.cod_patr_entities,null,null);
		
		//inserisce l'uk_catastale concatenando codice belfiore, sezione, foglio, particella e subalterno
		def fog;
		def part;
		def sub;
		def sez;
		def ukb;
		
		if(valuesMap.id_comuni==null || valuesMap.id_comuni=='') {
			//inserisce il comune partendo dall'immobile
			String cod_building="";
			if(valuesMap.cod_building!=null) {
				cod_building=valuesMap.get("cod_building");
			}
			else {
				cod_building=oldvaluesMap.get("cod_building");
			}
			def recordprop=services.queryService.executeQuery("select cc.uk_belfiore from pma_dati_gw.CMN_CITIES AS cc LEFT JOIN pma_dati_gw.PMA_BUILDING AS pb ON cc.uk_istat=pb.city WHERE pb.cod_building='"+cod_building+"'",null)[0];
			valuesMap.id_comuni=recordprop.uk_belfiore;
		}
		ukb=valuesMap.id_comuni;
		
		/*if(valuesMap.sezione!=null && valuesMap.sezione=='') {
			def warning_title ='<b>ATTENZIONE</b><br>';
			def warning_info ='E\' necessario inserire la sezione';		
			def warning_message = warning_title+warning_info;
			throw new RuntimeException(warning_message);
		}*/
		if(valuesMap.foglio!=null && valuesMap.foglio=='') {
			def warning_title ='<b>ATTENZIONE</b><br>';
			def warning_info ='E\' necessario inserire il foglio';		
			def warning_message = warning_title+warning_info;
			throw new RuntimeException(warning_message);
		}
		if(valuesMap.numero!=null && valuesMap.numero=='') {
			def warning_title ='<b>ATTENZIONE</b><br>';
			def warning_info ='E\' necessario inserire la particella';		
			def warning_message = warning_title+warning_info;
			throw new RuntimeException(warning_message);
		}
		/*if(valuesMap.subalterno!=null && valuesMap.subalterno=='') {
			def warning_title ='<b>ATTENZIONE</b><br>';
			def warning_info ='E\' necessario inserire il subalterno';		
			def warning_message = warning_title+warning_info;
			throw new RuntimeException(warning_message);
		}*/
		
		
		//controllo che non esista già un record con lo stesso codice patrimoniale e gli stessi codice belfiore, foglio, particella e subalterno
		/*if(valuesMap.sezione!=null && valuesMap.sezione!=''){
			sez=valuesMap.sezione;
		}else{
			
			sez=oldvaluesMap.sezione;
			
		}
		if(valuesMap.foglio!=null && valuesMap.foglio!=''){
			fog=valuesMap.foglio.toString().padLeft(5, '0');
		}else{
			
			fog=oldvaluesMap.foglio.toString().padLeft(5, '0');
			
		}
		if(valuesMap.particella!=null && valuesMap.particella!=''){
			part=valuesMap.particella.toString().padLeft(5, '0');
		}else{
			
			part=oldvaluesMap.particella.toString().padLeft(5, '0');
			
		}
		if(valuesMap.subalterno!=null && valuesMap.subalterno!=''){
			sub=valuesMap.subalterno.toString().padLeft(4, '0');
		}else{
			
			sub=oldvaluesMap.subalterno.toString().padLeft(4, '0');
			
		}
		if(valuesMap.uk_belfiore!=null && valuesMap.uk_belfiore!=''){
			ukb=valuesMap.uk_belfiore;
		}else{
			
			ukb=oldvaluesMap.uk_belfiore;
			
		}
		Integer count_type = services.queryService.executeQuery("SELECT count(1) AS count_type FROM PMA_DATI_GW.PMA_PATRIMONIAL_ENTITIES where UK_BELFIORE='"+uk_belfiore+"' and COD_PATR_ENTITIES='" + cod_patr_entities+"' and FOGLIO='"+foglio+"' and PARTICELLA='"+particella+"' and SUBALTERNO='"+subalterno+"'",null)[0].count_type;
		if(count_type>0) {
			def warning_title ='<b>ATTENZIONE</b><br>';
			def warning_info ='Esiste già un record con lo stesso codice patrimoniale e gli stessi foglio, particella e subalterno';		
			def warning_message = warning_title+warning_info;
			throw new RuntimeException(warning_message);
		}*/
		
		//log.debug("Codice Belfiore Trigger: "+mapCompleto.uk_belfiore);
		/*log.debug("Foglio Trigger: "+mapCompleto.foglio);
		log.debug("Particella Trigger: "+mapCompleto.particella);
		log.debug("Subalterno Trigger: "+mapCompleto.subalterno);
		log.debug("Sezione Trigger: "+mapCompleto.sezione);*/
		
		/*EMA if(valuesMap.sezione!=null && valuesMap.sezione!=''){
			sez=valuesMap.sezione.toString().padLeft(2, '0');
		}else{
			if(oldvaluesMap.sezione!=null && oldvaluesMap.sezione!=''){
				sez=oldvaluesMap.sezione.toString().padLeft(2, '0');
			}
			else {
				sez="00";
			}
		}
		*/
		if(valuesMap.foglio!=null && valuesMap.foglio!=''){
			fog=valuesMap.foglio.toString().padLeft(5, '0');
		}else{
			
			fog=oldvaluesMap.foglio.toString().padLeft(5, '0');
			
		}
		if(valuesMap.numero!=null && valuesMap.numero!=''){
			part=valuesMap.numero.toString().padLeft(5, '0');
		}else{
			
			part=oldvaluesMap.numero.toString().padLeft(5, '0');
			
		}
		if(valuesMap.subalterno!=null && valuesMap.subalterno!=''){
			sub=valuesMap.subalterno.toString().padLeft(4, '0');
		}else{
			if(oldvaluesMap.subalterno!=null && oldvaluesMap.subalterno!=''){
				sub=oldvaluesMap.subalterno.toString().padLeft(4, '0');
			}
			else {
				
				sub='0000';
			}
		}
		
		log.debug("Foglio Trigger2: "+fog);
		log.debug("Particella Trigger2: "+part);
		log.debug("Subalterno Trigger2: "+sub);
		//EMA log.debug("Sezione Trigger2: "+sez);
		
		valuesMap.uk_catastale=ukb+/*sez+*/fog+part+sub;
		log.debug("UK Catastale: "+valuesMap.uk_catastale);
		
		log.debug("NameTrigger beforeUpdate END");
		return true;
	};
	
	public boolean afterUpdate(HashMap<String,Object> valuesMap,HashMap<String,Object> oldvaluesMap){
		log.debug("NameTrigger afterUpdate START");


		log.debug("NameTrigger afterUpdate END");	
		return true;
	};
	
	public boolean beforeDelete(HashMap<String,Object> valuesMap){
		log.debug("NameTrigger beforeDelete START");


		log.debug("NameTrigger beforeDelete END");	
		return true;
	};
	
	public boolean afterDelete(HashMap<String,Object> valuesMap){
		log.debug("NameTrigger afterDelete START");


		log.debug("NameTrigger afterDelete END");		
		return true;
	};
}