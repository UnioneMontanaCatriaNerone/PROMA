
import org.apache.commons.lang.StringUtils;
import java.nio.charset.StandardCharsets;
import com.geowebframework.dataservice.CaseInsensitiveHashMap;
import java.text.SimpleDateFormat;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import oracle.sql.BLOB;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import com.geowebframework.dataservice.ConfigurationProperties;
import com.geowebframework.transfer.objects.webclient.GwBeanDocument;
import com.geowebframework.transfer.model.metadataservice.Class;
import com.geowebframework.transfer.model.metadataservice.GwmMnemonicCode;
import com.geowebframework.transfer.model.metadataservice.McPart;
import com.geowebframework.transfer.model.metadataservice.MnemonicCode;



public class BaseGroovyTrigger extends com.geowebframework.dataservice.querybuilder.EventTrigger {
    
	public boolean beforeInsert(HashMap<String,Object> valuesMap){
		/*
		String author = valuesMap.get("author");
		log.info("loggo l'autore: "+author+usergroup);
		Integer from_revision = valuesMap.get("from_revision");
		Integer single_insert = valuesMap.get("single_insert");
		String cod_building = valuesMap.get("cod_building");
		String cod_class_type = null;
		log.info("cod_building: "+cod_building);
		log.info("single_insert: "+single_insert);
		def defCodContent = null;

	//CONTROLLO CHE L'UTENTE ABBIA I PERMESSI PER L'INSERIMENTO; SONO GLI STESSI DELLA VISUALIZZAZIONE:
		 def queryPermission = services.queryService.executeQuery("SELECT DISTINCT username FROM V_AIM_BIM_FILTER_TEAM_USER WHERE username='"+author+"' and groups='"+usergroup+"'",null)[0];
		 if(queryPermission!=null && queryPermission.size()>0){
	//L'UTENTE DISPONE DEI PERMESSI DI VISUALIZZAZIONE E PUò PROCEDERE CON LA CREAZIONE DEL
			if(single_insert==1){		
				//RECUPERO I CAMPI DELLA CLASSIFICAZIONE
				String content_class = valuesMap.get("content_class"); 	
				def query_class = services.queryService.executeQuery("SELECT * FROM aim_content_classification WHERE cod_classification='"+content_class+"'", null)[0];
				valuesMap.put("cod_class_arc",query_class.cod_class_arc);
				valuesMap.put("cod_class_cat",query_class.cod_class_cat);	
				valuesMap.put("cod_class_subcat",query_class.cod_class_subcat);	
				cod_class_type = query_class.cod_class_type;
				valuesMap.put("cod_class_type",cod_class_type);
			}else{				
				String cod_class_arc = valuesMap.get("cod_class_arc");
				String cod_class_cat = valuesMap.get("cod_class_cat");
				String cod_class_subcat = valuesMap.get("cod_class_subcat");
				cod_class_type = valuesMap.get("cod_class_type");
				valuesMap.put("content_class",cod_class_subcat+"-"+cod_class_type);
			}
			//popolo i valori di default
			valuesMap.put("creation_date",new Date());
			if(from_revision==1){
				Integer rev_content = valuesMap.get("rev_content");
				Integer num_content = valuesMap.get("num_content");
				defCodContent = cod_building +'-'+ StringUtils.leftPad(num_content.toString(), 6, "0") + "-"+StringUtils.leftPad(rev_content.toString(), 2, "0");
				valuesMap.put("cod_content", defCodContent);
			}else{
				//CALCOLO IL PROGRESSIVO DEL CODICE DEL CONTENUTO (CODICE EDIFICIO + PROGRESSIVO + REVISIONE)
				def num_cont = services.queryService.executeQuery("SELECT count(id_content) as num_cont FROM aim_content WHERE cod_building='"+cod_building+"'", null)[0].num_cont;
				log.info("num_cont: "+num_cont);
				
				if(num_cont==0){
					num_cont=1;
					log.info("num_cont lungo: "+StringUtils.leftPad(num_cont.toString(), 6, "0"));
					defCodContent = cod_building +'-'+ StringUtils.leftPad(num_cont.toString(), 6, "0") + "-00";
					valuesMap.put("cod_content", defCodContent);
					valuesMap.put("num_content",num_cont);
					valuesMap.put("rev_content",0);
					log.info("cod_content codifica: "+(cod_building +'-'+ StringUtils.leftPad(num_cont.toString(), 6, "0") + "-00"));
				}else {
					def max_cont = services.queryService.executeQuery("SELECT max(num_content) max_cont FROM aim_content WHERE cod_building='"+cod_building+"'", null)[0].max_cont;
					max_cont=max_cont++;
					log.info("max_cont: "+max_cont++);
					defCodContent = cod_building +'-'+ StringUtils.leftPad(max_cont.toString(), 6, "0") + "-00";
					valuesMap.put("cod_content", defCodContent);
					valuesMap.put("num_content",max_cont);
					valuesMap.put("rev_content",0);
					log.info("cod_content codifica: "+(cod_building+'-'+StringUtils.leftPad(max_cont.toString(), 6, "0") + "-00"));
				}
			}			
		
			Integer num_revision = valuesMap.get("num_revision");
			Integer num_content = valuesMap.get("num_content");
			
			
			//recupero il vlore appena inserito
			String cod_class_subcat = valuesMap.get("cod_class_subcat");
//script temporaneo SOSTITUIRE CON WIDGET CONTENT HANDLER (dice Anna)
			if(cod_class_type.equals('V2D') || cod_class_type.equals('FUN') || cod_class_type.equals('V3D')){
				valuesMap.put("layout_name", defCodContent);
			}
			/*else if(cod_class_type.equals('SCH') && cod_class_subcat.equals('F.1.1')){
				valuesMap.put("cod_content_template", "SPAZI");
				valuesMap.put("sch_code", defCodContent);
				valuesMap.put("sch_cod_column", "cod_content");
				valuesMap.put("sch_class", 72827);
				valuesMap.put("is_uploaded", 1);
			}else if(cod_class_type.equals('SCH') && cod_class_subcat.equals('T.20.1')){
				valuesMap.put("cod_content_template", "CONS");
				valuesMap.put("sch_code", defCodContent);
				valuesMap.put("sch_cod_column", "cod_content");
				valuesMap.put("sch_class", 72824);
				valuesMap.put("is_uploaded", 1);				
			}
			*/
//fine script temporaneo
			
			//SEZIONE PER IL RECUPERO DELLO STATO DEL CONTENUTO
			//CONTROLLO SE CI SONO DELLE TRACK, IN CASO NEGATIVO NON FACCIO CREARE IL CONTENUTO
			/*
			def validQuery = false;
			def stugots = null;
			//COMMENTO LA SELEZIONE TOTALE DELLE TRACK E LA SOSTITUISCO CON QUELLA DA PERMESSI DELL'UTENTE
			//def num_track = services.queryService.executeQuery("SELECT count(id_track) as num_track FROM AIM_TRACK", null)[0].num_track;
			def num_track = services.queryService.executeQuery("SELECT count(1) as num_track FROM V_AIM_BIM_FILTER_TEAM_USER WHERE username='"+author+"' and groups='"+usergroup+"'",null)[0].num_track;
			log.info("loggo il numero delle track: "+num_track);
			//if(num_track!=null && num_track>0){
			if(num_track!=null && num_track==0){
				throw new RuntimeException("Attenzione: Non ci sono Track compatibili coi permessi dell'utente. <br>Impossibile creare contenuto");
				//throw new RuntimeException("Attenzione: Non ci sono Track. Impossibile creare contenuto");
			}else{
			//CONTROLLO QUALE STATO DEVE ESSERE SALVATO PER IL CONTENUTO
				//recupero le variabili del contenuto che mi servono per il controllo della track
				def arc = valuesMap.get("cod_class_arc");
				def cat = valuesMap.get("cod_class_cat");
				def type = valuesMap.get("cod_class_type");
				//faccio l'elenco delle track presenti ordinandole per priorità
				//COMMENTO LA SELEZIONE TOTALE DELLE TRACK E LA SOSTITUISCO CON QUELLA DA PERMESSI DELL'UTENTE
				//def queryTrack = services.queryService.executeQuery("select * from AIM_TRACK order by priority desc",null);
				def queryTrack = services.queryService.executeQuery("SELECT * FROM V_AIM_BIM_FILTER_TEAM_USER WHERE username='"+author+"' and groups='"+usergroup+"' order by priority desc",null);
				log.info("loggo la lista di track ordinate per priorità: "+queryTrack);
				
				if(queryTrack!=null && queryTrack.size()>0){
					def k = 0;
					while(!validQuery && k<queryTrack.size()){
						log.info("loggo la track kappesima: "+queryTrack[k]);
						//commento la query qui sotto perchè mi sa che non mi serve!! (l'anna sta ridacchiando con l'espressione di quella che sembra dire : "..io l'avevo detto..")
						//def queryFilter = services.queryService.executeQuery("select * from AIM_BIM_FILTER where cod_bim_filter='"+queryTrack[k].cod_bim_filter+"'",null)[0];
						//log.info("loggo il filtro da track massima priorita: "+queryFilter);
						def contType = services.queryService.executeQuery("select * from AIM_BIM_FILTER where cod_bim_filter='"+queryTrack[k].cod_bim_filter+"' and (cod_content_type='"+type+"' or cod_content_type is null)",null)[0];
						log.info("loggo  tipo da track massima priorita: "+contType);
						def contArc = services.queryService.executeQuery("select * from AIM_BIM_FILTER where cod_bim_filter='"+queryTrack[k].cod_bim_filter+"' and (cod_content_arc='"+arc+"' or cod_content_arc is null)",null)[0];
						log.info("loggo  archivio da track massima priorita: "+contArc);
						def contCat = services.queryService.executeQuery("select * from AIM_BIM_FILTER where cod_bim_filter='"+queryTrack[k].cod_bim_filter+"' and (cod_content_cat='"+cat+"' or cod_content_cat is null)",null)[0];
						log.info("loggo categoria da track massima priorita: "+contCat);
						log.info("queryTrack[k].cod_track: "+queryTrack[k].cod_track);
						if((contType!=null && contType.size()>0)&&(contArc!=null && contArc.size()>0)&&(contCat!=null && contCat.size()>0)){
							def chkTrack = services.queryService.executeQuery("select cod_track,min(ord_track_status) as min_status from AIM_TRACK_STATUS where cod_track='"+queryTrack[k].cod_track+"' group by cod_track",null)[0]; 
							if(chkTrack!=null){
								def Track = services.queryService.executeQuery("select uk_track_status from AIM_TRACK_STATUS where cod_track='"+chkTrack.cod_track+"' and ord_track_status="+chkTrack.min_status,null)[0];
								log.info("stato prescelto per essere il primo!: "+Track.uk_track_status);
								valuesMap.put("status_approval", Track.uk_track_status);
								stugots = Track.uk_track_status;
								validQuery = true;
							}else{
								throw new RuntimeException("Attenzione: Non ci sono Stati o Azioni nella Track. <br>Impossibile creare contenuto");	
							}
						}
						k++;
					}
					if(stugots==null){
						throw new RuntimeException("Attenzione: Non sono presenti track valide per la creazione del contenuto.<br>Impossibile creare nuovo contenuto");
					}
				}
			}
		}else{
			throw new RuntimeException("Attenzione: L'utente non dispone dei permessi di creazione di un nuovo contenuto");
		}
	
		log.info("loggo valuesMap fine inserimento: "+valuesMap);
		*/
		return true;	
	};
    
	public boolean afterInsert(HashMap<String,Object> valuesMap){
		
		log.info("loggo la mappa dei valori in ingresso x il nuovo contenuto: "+valuesMap);
		String cod_patr_entities = valuesMap.get("cod_patr_entities");
		def value_expertise = valuesMap.get("value_expertise") != null ? valuesMap.get("value_expertise") : '';
		def found = false;
		def interrupt = false;
		def valueList = services.queryService.executeQuery("SELECT * FROM pma_dati_gw.pma_building_valuation WHERE cod_building='"+cod_patr_entities+"' order by id_building_value desc", null);
		log.debug("valueList: "+valueList);
		log.debug("valueList != null && !valueList.isEmpty(): " + valueList != null && !valueList.isEmpty());
		if(valueList != null && !valueList.isEmpty() && valueList[0] != null && !valueList[0].isEmpty()){
			Date value_date = valueList[0].value_date;
			def value_building = valueList[0].value_building;
			log.debug("value_building: "+value_building);
			def pma_tab_istat = services.queryService.executeQuery("SELECT * FROM pma_dati_gw.pma_tab_istat ORDER BY anno", null);
			def value_building_istat = value_building;
			if(valueList.size < 1)
				valuesMap.put("code_state_valuation", "NV");
			else{
				valuesMap.put("code_state_valuation", "VV");
				//log.debug("new Date().format('dd/MM/yyyy'): "+ value_date.format( 'dd/MM/yyyy' ).toString().split("/").last());
				log.debug("value_date: "+ value_date);
				Calendar calendar = new GregorianCalendar();
				calendar.setTime(value_date);
				log.debug("calendar.setTime(date): "+ calendar.get(Calendar.YEAR));
				def valuation_date = calendar.get(Calendar.YEAR);
				
				int year = calendar.get(Calendar.YEAR);
				int i = 0;
				int currentYear = Calendar.getInstance().get(Calendar.YEAR);
				def lastYear = pma_tab_istat[i].get("anno");
				log.debug("pma_tab_istat: "+pma_tab_istat);
				log.debug("valuation_date: "+valuation_date);
				log.debug("value_building_istat before: "+value_building_istat);
				/*
				if(pma_tab_istat[i].anno >= valuation_date){
					log.debug("pma_tab_istat[i].anno: "+pma_tab_istat[i].anno);
					value_building_istat += value_building_istat*pma_tab_istat[i].indice_istat /100;
				}*/
				for(i = 0; i < pma_tab_istat.size; i++){
					log.debug("pma_tab_istat[i].anno: "+pma_tab_istat[i].anno);
					if(!interrupt){
						if(found && pma_tab_istat[i].anno < currentYear + 1){
							if(lastYear < pma_tab_istat[i].anno - 1){//controllo sequenza annuale su tabella istat
								interrupt = true;
								clientMessage = "Impossibile eseguire la valorizzazione dell\' Entit&agrave Patrimoniale &#9472; Parametro ISTAT mancante";
								services.queryService.executeQuery("UPDATE pma_dati_gw.pma_patrimonial_entities2 SET code_state_valuation = 'NI', value_expertise = null WHERE cod_patr_entities = '"+cod_patr_entities+"'", null);
								log.debug("lastYear: "+lastYear.toString()+" è maggiore di pma_tab_istat[i].anno - 1: "+pma_tab_istat[i].anno - 1);
								break;
							}
							value_building_istat += value_building_istat*pma_tab_istat[i].indice_istat /100;
							log.debug("value_building_istat: "+value_building_istat);
						}
						else if(pma_tab_istat[i].anno < valuation_date){
							//DONOTHING
						}
						else if(pma_tab_istat[i].anno.equals(valuation_date) && !found){
							found = true;
							value_building_istat += value_building_istat*pma_tab_istat[i].indice_istat /100;
							log.debug("value_building_istat: "+value_building_istat);
						}
					}
					
					lastYear = pma_tab_istat[i].anno;
					log.debug("lastYear: "+lastYear);
					
				}
				if(!found || interrupt){
					clientMessage = "Impossibile eseguire la valorizzazione dell\' Entit&agrave Patrimoniale &#9472; Parametro ISTAT mancante";
					services.queryService.executeQuery("UPDATE pma_dati_gw.pma_patrimonial_entities2 SET code_state_valuation = 'NI', value_expertise = null WHERE cod_patr_entities = '"+cod_patr_entities+"'", null);
				}else{
					log.debug("value_building_istat after: "+value_building_istat);
					def enhancementsList = services.queryService.executeQuery("SELECT * FROM pma_dati_gw.pma_building_value WHERE cod_building='"+cod_patr_entities+"' order by id_building_value desc", null);
					def value_partial_temp = 0;
					def valid = false;
					log.debug("enhancementsList: "+enhancementsList);
					log.debug("gwinsert1");
					if(enhancementsList != null && !enhancementsList.isEmpty() && enhancementsList[0] != null && !enhancementsList[0].isEmpty()){
						log.debug("gwinsert2");
						interrupt = false;
						found = false;
						for(int index = 0; index < enhancementsList.size; index++){
							if(enhancementsList[index].to_valid.toString().equalsIgnoreCase("1")){
								Calendar calendar_enh = new GregorianCalendar();
								calendar_enh.setTime(enhancementsList[index].value_date);
								log.debug("calendar_enh.setTime(date): "+ calendar_enh.get(Calendar.YEAR));
								def valuation_date_enh = calendar_enh.get(Calendar.YEAR);
								valid = true;
								lastYear = pma_tab_istat[0].anno;
								value_partial_temp = enhancementsList[index].value_partial;
								log.debug("value_partial_temp: "+value_partial_temp);
								for(i = 0; i < pma_tab_istat.size; i++){
									log.debug("pma_tab_istat[i].anno: "+pma_tab_istat[i].anno);
									if(!interrupt){
										if(found && pma_tab_istat[i].anno < currentYear + 1){
											if(lastYear < pma_tab_istat[i].anno - 1){//controllo sequenza annuale su tabella istat
												interrupt = true;
												clientMessage = "Impossibile eseguire la valorizzazione dell\' Entit&agrave Patrimoniale &#9472; Parametro ISTAT mancante";
												services.queryService.executeQuery("UPDATE pma_dati_gw.pma_patrimonial_entities2 SET code_state_valuation = 'NI', value_expertise = null WHERE cod_patr_entities = '"+cod_patr_entities+"'", null);
												log.debug("lastYear: "+lastYear.toString()+" è maggiore di pma_tab_istat[i].anno - 1: "+pma_tab_istat[i].anno - 1);
												break;
											}
											value_partial_temp += value_partial_temp*pma_tab_istat[i].indice_istat /100;
											log.debug("value_partial_temp: "+value_partial_temp);
										}
										else if(pma_tab_istat[i].anno < valuation_date_enh){
											//DONOTHING
										}
										else if(pma_tab_istat[i].anno.equals(valuation_date_enh) && !found){
											found = true;
											value_partial_temp += value_partial_temp*pma_tab_istat[i].indice_istat /100;
											log.debug("value_partial_temp: "+value_partial_temp);
										}
									}
									
									lastYear = pma_tab_istat[i].anno;
									log.debug("lastYear: "+lastYear);
									
								}
							}
							
						}
					}
					if(valid && (!found || interrupt)){
						clientMessage = "Impossibile eseguire la valorizzazione dell\' Entit&agrave Patrimoniale &#9472; Parametro ISTAT mancante";
						services.queryService.executeQuery("UPDATE pma_dati_gw.pma_patrimonial_entities2 SET code_state_valuation = 'NI', value_expertise = null WHERE cod_patr_entities = '"+cod_patr_entities+"'", null);
					} else{
						log.debug("value_partial_temp: "+value_partial_temp);
						value_building_istat += value_partial_temp;
					}
				}
			}
			log.debug("code_state_valuation: "+valuesMap.code_state_valuation);
			
			value_expertise = value_building_istat;
			valuesMap.put("value_expertise", value_expertise);
			log.debug("new valuesMap"+valuesMap);
			valuesMap.put("pk_patr_entities", valuesMap.get("itemId"));
			//newValuesMap
			services.classService.updateClassRecord("pma_patrimonial_entities_2", valuesMap);
		}
		
		
		/*
		def status_approval = valuesMap.get("status_approval");
		def cod_content = valuesMap.get("cod_content");
		def author = valuesMap.get("author");
		def codClassType = valuesMap.get("cod_class_type");
		def contentDescr = valuesMap.get("descr_content");
		def buildingCode = valuesMap.get("cod_building");
		def cod_class_subcat = valuesMap.get("cod_class_subcat");
		def tenantCode = valuesMap.get("tenant_code");
		
		//recupero la track e il filtro dallo stato del contenuto
		def queryFil1 = services.queryService.executeQuery("select cod_track from AIM_TRACK_STATUS where uk_track_status='"+status_approval+"'",null)[0];
		def queryFil2 = services.queryService.executeQuery("select cod_bim_filter from AIM_TRACK where cod_track='"+queryFil1.cod_track+"'",null)[0];
		log.info("loggo il cod_bim_filter: "+queryFil2.cod_bim_filter);	


		// definisco l'oggetto per l'inserimento: vado ad inserire il contenuto nella lista di quelli visibili (da permesso);
		def ins_cont = [:];
		ins_cont.cod_bim_filter = queryFil2.cod_bim_filter;
		ins_cont.cod_content = cod_content;						
		// inserisco il record
		//predispongo l'inserimento delle nuove azioni
		def contIns = "INSERT INTO AIM_BIM_FILTER_R_CONTENT (cod_bim_filter, cod_content)";
		contIns+=" VALUES ";
		contIns+="(#{map.cod_bim_filter},#{map.cod_content})";
		def insert = services.queryService.executeQuery(contIns,ins_cont);
		
		//vado a poplare la tabella delle schede digitali di tipo spazi e
		//variabili comuni
		def ins_schede = [:];
		ins_schede.cod_building = buildingCode;
		ins_schede.cod_content = cod_content;			
		ins_schede.utente = author;
		def dateUp = new Date().format( 'dd/MM/yyyy' );
		ins_schede.sch_title = "<div style='font-size: 25px; margin-top:10px'>Immobile "+buildingCode+"</div>";
		ins_schede.SCH_DATA_HTML = "<div style='font-size: 14px;text-align: right;'>Data ultima modifica: <span style='font-size: 16px;'><b>"+dateUp+"</b></span></div>";
		//inserisco gli spazi
		if(codClassType.equals('SCH') && cod_class_subcat.equals('F.1.1')){
			ins_schede.sch_protocol =	"<div style='font-size: 14px; margin-top:15px;text-align: right;'>Protocollo: <span style='font-size: 16px;'><b>SCH_SPAZI_01</b></span></div>";
			ins_schede.sch_model = "<span style='font-size: 15px;'>Schede Spazi</span>";
			ins_schede.sch_last_date = dateUp;
			def spazIns = "INSERT INTO AIM_SCH_SPAZI (cod_building, cod_content, utente, sch_title, sch_protocol,sch_model ,SCH_DATA_HTML)";
			spazIns+=" VALUES ";
			spazIns+="(#{map.cod_building},#{map.cod_content},#{map.utente},#{map.sch_title},#{map.sch_protocol},#{map.sch_model},#{map.SCH_DATA_HTML})";
			def ins = services.queryService.executeQuery(spazIns,ins_schede);
		}else if(codClassType.equals('SCH') && cod_class_subcat.equals('T.20.1')){
			def queryBuild = services.queryService.executeQuery("SELECT * FROM GWD_BUILDING WHERE cod_building='"+buildingCode+"'",null)[0];
			log.info("loggo lA queryBuild: "+queryBuild);
			ins_schede.fk_bank = queryBuild.fk_bank;
			ins_schede.polo = queryBuild.fk_pole;
			ins_schede.presidio = queryBuild.fk_aid;
			ins_schede.comune = queryBuild.cod_city;
			ins_schede.address = queryBuild.address;
			ins_schede.sch_protocol =	"<div style='font-size: 14px; margin-top:15px;text-align: right;'>Protocollo: <span style='font-size: 16px;'><b>SCH_SPAZI_01</b></span></div>";
			ins_schede.sch_model = "<span style='font-size: 15px;'>Schede Spazi</span>";			
			def consIns = "INSERT INTO AIM_SCH_CONSISTENZA_ZONE (cod_building, fk_bank, polo, presidio, comune, address, cod_content, utente, sch_title, sch_protocol,sch_model ,SCH_DATA_HTML)";
			consIns+=" VALUES ";
			consIns+="(#{map.cod_building},#{map.fk_bank},#{map.polo},#{map.presidio},#{map.comune},#{map.address},#{map.cod_content},#{map.utente},#{map.sch_title},#{map.sch_protocol},#{map.sch_model},#{map.SCH_DATA_HTML})";
			def ins = services.queryService.executeQuery(consIns,ins_schede);	
		}
		
		def contentTypeHM = [:];
		contentTypeHM.cod_content = cod_content;
		contentTypeHM.tenant_code = tenantCode;
		if(codClassType.equals('DOC')){
			contentTypeHM.file_path = buildingCode + "/" + "DOC";
			def insertDoc = "INSERT INTO AIM_CONTENT_DOC (cod_content, file_path, tenant_code) VALUES (#{map.cod_content},#{map.file_path},#{map.tenant_code})";
			def ins = services.queryService.executeQuery(insertDoc,contentTypeHM);	
		}else if(codClassType.equals('SCH')){
			def insertSch = "INSERT INTO AIM_CONTENT_SCH (cod_content, tenant_code) VALUES (#{map.cod_content},#{map.tenant_code})";
			def ins = services.queryService.executeQuery(insertSch,contentTypeHM);	
		} else if (codClassType.equals('B2D')){
			contentTypeHM.bim_data_project = cod_content;
			def insertB2d = "INSERT INTO AIM_CONTENT_B2D (cod_content, bim_data_project, tenant_code) VALUES (#{map.cod_content}, #{map.bim_data_project}, #{map.tenant_code})";
			def ins = services.queryService.executeQuery(insertB2d,contentTypeHM);	
		
			def fedModel = [:];
			fedModel.cod_fed_model = cod_content+'_FEDMOD';
			fedModel.name_fed_model = 'Modello federato per '+cod_content;
			fedModel.fk_project = cod_content;
			fedModel.is_active = 1;
			fedModel.tenant_code = tenantCode;
			services.classService.insertClassRecord('gwd_fed_model',fedModel);
			
		}
		
		
		
	
		//CREAZIONE MODELLO GRAFICO ASSOCIATO AL CONTENUTO:
		//SE COD_CLASS_TYPE == V3D => CREO SU GWD_BIM_MODEL
		//SE COD_CLASS_TYPE == V2D || FUN => CREO SU GWD_DRAWING e ANCHE SU GWD_LAYOUT
		//
		//MA LA GESTIONE DEI PTS???
		//
		/*
		def codClassType = valuesMap.cod_class_type;
		def contentCode  = valuesMap.cod_content;
		def contentDescr = valuesMap.descr_content;
		def buildingCode = valuesMap.cod_building;
		
		if(codClassType.equals('V3D')){
			
			def bim = [:];
			bim.put("name",contentCode);
			bim.put("description",contentDescr);
			bim.put("layout_code",buildingCode);
			bim.put("type","IFC");

			def bimModelId = services.classService.insertClassRecord('gwd_bim_model',bim);
			
			def queryResult = services.queryService.executeQuery("SELECT pk_model_set FROM gwd_model_set WHERE model_set_code =#{map.modelSetCode}",[modelSetCode:buildingCode])[0];
			if(queryResult!=null && queryResult.size()!=0){
				
				def map = [:];
				map.put("fk_model_set", queryResult.pk_model_set);
				map.put("fk_bim_model", bimModelId.toInteger());
				
				services.classService.insertClassRecord('gwd_r_mod_set_bim', map);
			}
			
			
		}  else if (codClassType.equals('V2D') || codClassType.equals('FUN')){
			
			def layout = [:];
			layout.put("layout_code",contentCode);
			layout.put("layout_label",contentDescr);
			layout.put("layout_type",'2D');
			
			def layoutId = services.classService.insertClassRecord('gwd_layout',layout);
			
			//creo il drawingSet associato al layout
			def drawingSet = [:];
			drawingSet.put("drawing_set_code", contentCode);
			drawingSet.put("drawing_set_label", contentDescr);
			drawingSet.put("fk_layout", layoutId.toInteger());
			drawingSet.put("map", "floorPlan");
			drawingSet.put("is_default", 1);
			def drawingSetId = services.classService.insertClassRecord('gwd_drawing_set',drawingSet);
		
			//creo il drawing su cui caricare il modello grafico tramite uploadManager
			def drawing = [:];
			drawing.put("dwg_name",contentCode);
			drawing.put("fk_layout",layoutId.toInteger());
			drawing.put("drawing_type", 1);
			def drawingId = services.classService.insertClassRecord('gwd_drawing',drawing);	
			
			//creo il record di relazione tra drawing e drawing set
			def relDrawDrawSet = [:];
			relDrawDrawSet.put("fk_drawing", drawingId.toInteger());
			relDrawDrawSet.put("fk_drawing_set", drawingSetId.toInteger());
			services.classService.insertClassRecord('gwd_r_draw_set_draw',relDrawDrawSet);	
			
			
		}
		*/
	return true;		
    };
    
    public boolean beforeUpdate(HashMap<String,Object> valuesMap,HashMap<String,Object> oldValuesMap){
		
		//def layout_name = valuesMap.get("layout_name");
		
        return true;
	};
    
    public boolean afterUpdate(HashMap<String,Object> valuesMap,HashMap<String,Object> oldValuesMap){
		
		log.info("afterUpdate valuesMap: "+valuesMap);
		log.info("afterUpdate oldValuesMap: "+oldValuesMap);
		def value_expertise = valuesMap.get("value_expertise") != null ? valuesMap.get("value_expertise") : oldValuesMap.get("value_expertise");
		String cod_patr_entities = valuesMap.get("cod_patr_entities") != null ? valuesMap.get("cod_patr_entities") : oldValuesMap.get("cod_patr_entities");
		def found = false;
		def interrupt = false;
		def valueList = services.queryService.executeQuery("SELECT * FROM pma_dati_gw.pma_building_valuation WHERE cod_building='"+cod_patr_entities+"' order by id_building_value desc", null);
		log.debug("valueList: "+valueList);
		//valueList: [[max:2233155]]//SELECT * FROM pma_dati_gw.pma_building_valuation WHERE cod_building='"+cod_patr_entities+"' order by value_date desc, id_building_value desc
		if(valueList != null && !valueList.isEmpty() && valueList[0] != null && !valueList[0].isEmpty()){
			Date value_date = valueList[0].value_date;
			def value_building = valueList[0].value_building;
			log.debug("value_building: "+value_building);
			def pma_tab_istat = services.queryService.executeQuery("SELECT * FROM pma_dati_gw.pma_tab_istat ORDER BY anno", null);
			def value_building_istat = value_building;
			
			valuesMap.put("code_state_valuation", "VV");
			//log.debug("new Date().format('dd/MM/yyyy'): "+ value_date.format( 'dd/MM/yyyy' ).toString().split("/").last());
			log.debug("value_date: "+ value_date);
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(value_date);
			log.debug("calendar.setTime(date): "+ calendar.get(Calendar.YEAR));
			def valuation_date = calendar.get(Calendar.YEAR);
			
			int year = calendar.get(Calendar.YEAR);
			int i = 0;
			int currentYear = Calendar.getInstance().get(Calendar.YEAR);
			def lastYear = pma_tab_istat[i].get("anno");
			log.debug("pma_tab_istat: "+pma_tab_istat);
			log.debug("valuation_date: "+valuation_date);
			log.debug("value_building_istat before: "+value_building_istat);
			
			for(i = 0; i < pma_tab_istat.size; i++){
				log.debug("pma_tab_istat[i].anno: "+pma_tab_istat[i].anno);
				if(!interrupt){
					if(found && !interrupt && pma_tab_istat[i].anno < currentYear + 1){
						if(lastYear < pma_tab_istat[i].anno - 1){//controllo sequenza annuale su tabella istat
							interrupt = true;
							clientMessage = "Impossibile eseguire la valorizzazione dell\' Entit&agrave Patrimoniale &#9472; Parametro ISTAT mancante";
							services.queryService.executeQuery("UPDATE pma_dati_gw.pma_patrimonial_entities2 SET code_state_valuation = 'NI', value_expertise = null WHERE cod_patr_entities = '"+cod_patr_entities+"'", null);
							log.debug("lastYear: "+lastYear.toString()+" è maggiore di pma_tab_istat[i].anno - 1: "+pma_tab_istat[i].anno - 1);
							break;
						}
						value_building_istat += value_building_istat*pma_tab_istat[i].indice_istat /100;
						log.debug("value_building_istat: "+value_building_istat);
					}
					else if(pma_tab_istat[i].anno < valuation_date){
						//DONOTHING
					}
					else if(pma_tab_istat[i].anno.equals(valuation_date) && !found){
						found = true;
						value_building_istat += value_building_istat*pma_tab_istat[i].indice_istat /100;
						log.debug("value_building_istat: "+value_building_istat);
					}
				}
				lastYear = pma_tab_istat[i].anno;
				log.debug("lastYear: "+lastYear);
				
			}
			log.debug("value_building_istat after: "+value_building_istat);
			if(!found || interrupt){
				clientMessage = "Impossibile eseguire la valorizzazione dell\' Entit&agrave Patrimoniale &#9472; Parametro ISTAT mancante";
				services.queryService.executeQuery("UPDATE pma_dati_gw.pma_patrimonial_entities2 SET code_state_valuation = 'NI', value_expertise = null WHERE cod_patr_entities = '"+cod_patr_entities+"'", null);
			}else{
				log.debug("value_building_istat after: "+value_building_istat);
				def enhancementsList = services.queryService.executeQuery("SELECT * FROM pma_dati_gw.pma_building_value WHERE cod_building='"+cod_patr_entities+"' order by id_building_value desc", null);
				
				log.debug("enhancementsList: "+enhancementsList);
				log.debug("gwupdate1");
				def valid = false;
				if(enhancementsList != null && !enhancementsList.isEmpty() && enhancementsList[0] != null && !enhancementsList[0].isEmpty()){log.debug("gwupdate2");
					def value_partial_temp = 0;
					interrupt = false;
					found = false;
					log.debug("enhancementsList.size: "+enhancementsList.size);
					for(int index = 0; index < enhancementsList.size; index++){log.debug("gwupdate3");
						if(enhancementsList[index].to_valid.toString().equalsIgnoreCase("1")){log.debug("gwupdate4");
							Calendar calendar_enh = new GregorianCalendar();
							calendar_enh.setTime(enhancementsList[index].value_date);
							log.debug("calendar_enh.setTime(date): "+ calendar_enh.get(Calendar.YEAR));
							def valuation_date_enh = calendar_enh.get(Calendar.YEAR);
							valid = true;
							lastYear = pma_tab_istat[0].anno;
							log.debug("enhancementsList[index].value_partial: "+enhancementsList[index].value_partial)
							value_partial_temp = enhancementsList[index].value_partial;
							log.debug("value_partial_temp: "+value_partial_temp);
							for(i = 0; i < pma_tab_istat.size; i++){log.debug("interrupt123: "+interrupt);
								log.debug("pma_tab_istat[i].anno: "+pma_tab_istat[i].anno);
								if(!interrupt){
									if(found && pma_tab_istat[i].anno < currentYear + 1){
										if(lastYear < pma_tab_istat[i].anno - 1){//controllo sequenza annuale su tabella istat
											interrupt = true;
											clientMessage = "Impossibile eseguire la valorizzazione dell\' Entit&agrave Patrimoniale &#9472; Parametro ISTAT mancante";
											services.queryService.executeQuery("UPDATE pma_dati_gw.pma_patrimonial_entities2 SET code_state_valuation = 'NI', value_expertise = null WHERE cod_patr_entities = '"+cod_patr_entities+"'", null);
											log.debug("lastYear: "+lastYear.toString()+" è maggiore di pma_tab_istat[i].anno - 1: "+pma_tab_istat[i].anno - 1);
											break;
										}
										value_partial_temp += value_partial_temp*pma_tab_istat[i].indice_istat /100;
										log.debug("value_partial_temp: "+value_partial_temp);
									}
									else if(pma_tab_istat[i].anno < valuation_date_enh){
										//DONOTHING
									}
									else if(pma_tab_istat[i].anno.equals(valuation_date_enh) && !found){
										found = true;
										value_partial_temp += value_partial_temp*pma_tab_istat[i].indice_istat /100;
										log.debug("value_partial_temp: "+value_partial_temp);
									}
								}
								lastYear = pma_tab_istat[i].anno;
								log.debug("lastYear: "+lastYear);
								
							}
							log.debug("value_partial_temp: "+value_partial_temp);
							if(found && !interrupt)
								value_building_istat += value_partial_temp;
						}
						
					}
				}
				if(valid && (!found || interrupt)){
					clientMessage = "Impossibile eseguire la valorizzazione dell\' Entit&agrave Patrimoniale &#9472; Parametro ISTAT mancante";
					services.queryService.executeQuery("UPDATE pma_dati_gw.pma_patrimonial_entities2 SET code_state_valuation = 'NI', value_expertise = null WHERE cod_patr_entities = '"+cod_patr_entities+"'", null);
				} else{
					log.debug("code_state_valuation: "+valuesMap.code_state_valuation);
					def code_state_valuation = valuesMap.code_state_valuation
					log.debug("value_building_istat:"+value_building_istat);
					value_expertise = value_building_istat;
					valuesMap.put("value_expertise", value_expertise);
					log.debug("new valuesMap"+valuesMap);
					if(code_state_valuation.equalsIgnoreCase("NV"))
						value_expertise = "";
					
					valuesMap.put("pk_patr_entities", valuesMap.get("itemId"));
					//newValuesMap
					services.queryService.executeQuery("UPDATE pma_dati_gw.pma_patrimonial_entities2 SET code_state_valuation = '"+code_state_valuation+"', value_expertise = '"+value_expertise+"' WHERE cod_patr_entities = '"+cod_patr_entities+"'", null);
				}
			}
		}
		else{
			valuesMap.put("code_state_valuation", "NV");
			services.queryService.executeQuery("UPDATE pma_dati_gw.pma_patrimonial_entities2 SET code_state_valuation = 'NV', value_expertise = null WHERE cod_patr_entities = '"+cod_patr_entities+"'", null);
		}
	
        return true;
    };
 
    public boolean beforeDelete(HashMap<String,Object> valuesMap){
		return true;
    };
    
    public boolean afterDelete(HashMap<String,Object> valuesMap){
        return true;
    };

}
