// #####groovy da lanciare per popolare i tabelloni per creare le report dello scheduling

import java.util.*;
import java.text.*;

//***************************************************************************************	
//  CRUSCOTTO SU STATO PATRIMONIALE
//***************************************************************************************
//cancello tutte le righe del tabellone per poi re-inserirle


queryService.executeDeleteQuery("delete from pma_dati_gw.pri_dtm_obj_depreciation",null);

//def start_date=new Date();

//inizializzo le date
Calendar calDate = Calendar.getInstance();
calDate.setTime(new Date());
int month   = 1; //calDate.get(Calendar.MONTH) + 1; // beware of month indexing from zero
int year    = calDate.get(Calendar.YEAR);
int quarter = 1; //(month / 3) + 1;
calDate.set(Calendar.MONTH,month-1);
calDate.set(Calendar.YEAR,year);
calDate.set(Calendar.DAY_OF_MONTH,1);

//*************************************************************
//inserire variabili per definire
//DATA_INIZIO_PERIODO_PROGRAMMAZIONE
def start_valutazione=calDate.getTime();
//DATA MASSIMA ANNI PROGRAMMAZIONE(per ora è 12)
def nTotYear=5;
//
//*************************************************************	
	
int year_start = year;
int year_end = year+nTotYear;
	
def param = [:];

Calendar data_inizio = Calendar.getInstance();
Calendar data_fine = Calendar.getInstance();
data_inizio.set(Calendar.MONTH,0);
data_inizio.set(Calendar.YEAR,year_start);
data_inizio.set(Calendar.DAY_OF_MONTH,1);

data_fine.set(Calendar.MONTH,11);
data_fine.set(Calendar.YEAR,year_end);
data_fine.set(Calendar.DAY_OF_MONTH,31);

param.put("data_inizio",data_inizio.getTime());
param.put("data_fine",data_fine.getTime());

//log.debug("data_inizio " + data_inizio.getTime());
//log.debug("data_fine " + data_fine.getTime());

//queryService.executeQuery(queryInsert,param);	
	
/*
def queryObj="SELECT gwd_item.id_item as id_obj,'gwd_item' as object_class,gwd_item.cod_item as code,gwd_item.name_item as name,gwd_item.class_item as object_type,gwd_item.validity_start_date,gwd_item.validity_end_date,gwd_item.manufacture_date,(DATE_PART('year', current_date) - DATE_PART('year', gwd_item.manufacture_date)) * 12 + (DATE_PART('month', current_date) - DATE_PART('month', gwd_item.manufacture_date)) as obj_age_month,gwd_item.average_lifetime,gwd_item.optimum_lifetime,gwd_item.item_price as obj_price,gwd_item.item_value as obj_value"; 
	queryObj+=" FROM gwd_item"; 
	queryObj+=" WHERE (validity_start_date>=#{map.data_inizio} AND validity_start_date<=#{map.data_fine})"; 
	queryObj+=" OR (validity_end_date>=#{map.data_inizio} AND validity_end_date<=#{map.data_fine})"; 
	queryObj+=" UNION";  
	queryObj+=" SELECT gwd_assets.id_asset as id_obj,'gwd_assets' as object_class,gwd_assets.cod_asset as code,gwd_assets.name_asset as name,gwd_assets.class_path as object_type,gwd_assets.validity_start_date,gwd_assets.validity_end_date,gwd_assets.manufacture_date,(DATE_PART('year', current_date) - DATE_PART('year', gwd_assets.manufacture_date)) * 12 + (DATE_PART('month', current_date) - DATE_PART('month', gwd_assets.manufacture_date)) as obj_age_month,gwd_assets.average_lifetime,gwd_assets.optimum_lifetime,gwd_assets.asset_price as obj_price,gwd_assets.asset_value as obj_value";
	queryObj+=" FROM gwd_assets";
	queryObj+=" WHERE (validity_start_date>=#{map.data_inizio} AND validity_start_date<=#{map.data_fine})"; 
	queryObj+=" OR (validity_end_date>=#{map.data_inizio} AND validity_end_date<=#{map.data_fine})"; 
*/	
/*
def queryObj="SELECT gwd_item.id_item as id_obj,'gwd_item' as object_class,gwd_item.cod_item as code,gwd_item.name_item as name,gwd_item.class_item as object_type,gwd_item.validity_start_date,gwd_item.validity_end_date,gwd_item.manufacture_date,(DATE_PART('year', current_date) - DATE_PART('year', gwd_item.manufacture_date)) * 12 + (DATE_PART('month', current_date) - DATE_PART('month', gwd_item.manufacture_date)) as obj_age_month,gwd_item.average_lifetime,gwd_item.optimum_lifetime,gwd_item.item_price as obj_price,gwd_item.item_value as obj_value,gwd_item.coef_riv_sval,obj_replacement_cost"; 
	queryObj+=" FROM gwd_item"; 
	//queryObj+=" WHERE (validity_start_date>=#{map.data_inizio} AND validity_start_date<=#{map.data_fine})"; 
	//queryObj+=" OR (validity_end_date>=#{map.data_inizio} AND validity_end_date<=#{map.data_fine})"; 
	queryObj+=" WHERE (validity_start_date>=#{map.data_inizio} OR validity_end_date<=#{map.data_fine})"; 
	queryObj+=" OR (validity_start_date<=#{map.data_inizio} AND validity_end_date>=#{map.data_fine})"; 
	queryObj+=" AND gwd_item.is_asset=1"; 
	queryObj+=" UNION";  
	queryObj+=" SELECT gwd_assets.id_asset as id_obj,'gwd_assets' as object_class,gwd_assets.cod_asset as code,gwd_assets.name_asset as name,gwd_assets.class_path as object_type,gwd_assets.validity_start_date,gwd_assets.validity_end_date,gwd_assets.manufacture_date,(DATE_PART('year', current_date) - DATE_PART('year', gwd_assets.manufacture_date)) * 12 + (DATE_PART('month', current_date) - DATE_PART('month', gwd_assets.manufacture_date)) as obj_age_month,gwd_assets.average_lifetime,gwd_assets.optimum_lifetime,gwd_assets.asset_price as obj_price,gwd_assets.asset_value as obj_value,gwd_assets.coef_riv_sval,obj_replacement_cost";
	queryObj+=" FROM gwd_assets";
	//queryObj+=" WHERE (validity_start_date>=#{map.data_inizio} AND validity_start_date<=#{map.data_fine})"; 
	//queryObj+=" OR (validity_end_date>=#{map.data_inizio} AND validity_end_date<=#{map.data_fine})"; 
	queryObj+=" WHERE (validity_start_date>=#{map.data_inizio} OR validity_end_date<=#{map.data_fine})"; 
	queryObj+=" OR (validity_start_date<=#{map.data_inizio} AND validity_end_date>=#{map.data_fine})"; 
	queryObj+=" AND gwd_assets.is_asset=1"; 	
	queryObj+=" UNION"; 
	queryObj+=" SELECT pri_building.id_building as id_obj,'pri_building' as object_class,pri_building.cod_building as code,pri_building.name_building as name,pri_building.cod_type_building as object_type,pri_building.validity_start_date,pri_building.validity_end_date,pri_building.manufacture_date,(DATE_PART('year', current_date) - DATE_PART('year', pri_building.manufacture_date)) * 12 + (DATE_PART('month', current_date) - DATE_PART('month', pri_building.manufacture_date)) as obj_age_month,pri_building.average_lifetime,pri_building.optimum_lifetime,pri_building.building_price as obj_price,pri_building.building_value as obj_value,pri_building.coef_riv_sval,obj_replacement_cost";
	queryObj+=" FROM pri_building";
	//queryObj+=" WHERE (validity_start_date>=#{map.data_inizio} AND validity_start_date<=#{map.data_fine})"; 
	//queryObj+=" OR (validity_end_date>=#{map.data_inizio} AND validity_end_date<=#{map.data_fine})"; 
	queryObj+=" WHERE (validity_start_date>=#{map.data_inizio} OR validity_end_date<=#{map.data_fine})"; 
	queryObj+=" OR (validity_start_date<=#{map.data_inizio} AND validity_end_date>=#{map.data_fine})"; 
	queryObj+=" AND pri_building.is_asset=1";
	queryObj+=" UNION"; 
	queryObj+=" SELECT gwd_system.id_system as id_obj,'gwd_system' as object_class,gwd_system.cod_system as code,gwd_system.name_system as name,gwd_system.type_system as object_type,gwd_system.validity_start_date,gwd_system.validity_end_date,gwd_system.manufacture_date,(DATE_PART('year', current_date) - DATE_PART('year', gwd_system.manufacture_date)) * 12 + (DATE_PART('month', current_date) - DATE_PART('month', gwd_system.manufacture_date)) as obj_age_month,gwd_system.average_lifetime,gwd_system.optimum_lifetime,gwd_system.system_price as obj_price,gwd_system.system_value as obj_value,gwd_system.coef_riv_sval,obj_replacement_cost";
	queryObj+=" FROM gwd_system";
	//queryObj+=" WHERE (validity_start_date>=#{map.data_inizio} AND validity_start_date<=#{map.data_fine})"; 
	//queryObj+=" OR (validity_end_date>=#{map.data_inizio} AND validity_end_date<=#{map.data_fine})"; 
	queryObj+=" WHERE (validity_start_date>=#{map.data_inizio} OR validity_end_date<=#{map.data_fine})"; 
	queryObj+=" OR (validity_start_date<=#{map.data_inizio} AND validity_end_date>=#{map.data_fine})"; 
	queryObj+=" AND gwd_system.is_asset=1"; 
	
	//log.error("queryObj: " + queryObj);
*/
/*def queryObj="SELECT pri_building.id_building as id_obj,'pri_building' as object_class,pri_building.cod_company,pri_building.type_ownership,pri_building.cod_site as site_code,pri_building.cod_building as code,pri_building.name_building as name,pri_building.cod_type_building as object_type,pri_building.anno_costr,((extract(year from current_timestamp) - pri_building.anno_costr) * 12) as obj_age_month,pri_building.purchase_value as obj_price,pri_building.value_expertise as obj_value,pri_building.date_expertise,pri_building.purchase_date,pri_building.degrad_coeff_ann";
	queryObj+=",gwd_site.district,gwd_site.description as descr_site";
	queryObj+=" FROM pri_building";
	queryObj+=" left join gwd_site on gwd_site.site_code=pri_building.cod_site";
	//queryObj+=" WHERE (validity_start_date>=#{map.data_inizio} AND validity_start_date<=#{map.data_fine})"; 
	//queryObj+=" OR (validity_end_date>=#{map.data_inizio} AND validity_end_date<=#{map.data_fine})"; 
	queryObj+=" WHERE pri_building.status_building='1' or pri_building.status_building is null"; 
	//queryObj+=" OR (validity_start_date<=#{map.data_inizio} AND validity_end_date>=#{map.data_fine})"; 
	//queryObj+=" AND pri_building.is_asset=1";*/
def queryObj="SELECT pb.id_building as id_obj,'pma_building' as object_class,pb.cod_company,pb.type_ownership,pb.cod_site as site_code,pb.cod_building as code,pb.name_building as name,pb.cod_type_building as object_type,pb.anno_costr,((extract(year from current_timestamp) - pb.anno_costr) * 12) as obj_age_month,pb.purchase_value as obj_price,pb.value_expertise as obj_value,pb.date_expertise,pb.purchase_date,pb.degrad_coeff_ann";
	queryObj+=",gs.district,gs.description as descr_site";
	queryObj+=" FROM pma_dati_gw.pma_building as pb";
	queryObj+=" left join pma_dati_gw.gwd_site as gs on gs.site_code=pb.cod_site";
	//queryObj+=" WHERE (validity_start_date>=#{map.data_inizio} AND validity_start_date<=#{map.data_fine})"; 
	//queryObj+=" OR (validity_end_date>=#{map.data_inizio} AND validity_end_date<=#{map.data_fine})"; 
	queryObj+=" WHERE pb.status_building='PC' or pb.status_building is null"; 
	//queryObj+=" OR (validity_start_date<=#{map.data_inizio} AND validity_end_date>=#{map.data_fine})"; 
	//queryObj+=" AND pri_building.is_asset=1";
def oggetti=queryService.executeQuery(queryObj,param);	


def pk_table=0;
//log.error("obj_NR: " + oggetti.size());
for (int j=0; j<oggetti.size(); j++){
	def datamart = [:];

	//recupero le info dell'oggetto 
/*	def objDescr=getAllInfoObj(oggetti[j].code,oggetti[j].object_class);
	log.debug("obj_code: " + oggetti[j].code+" - obj_class: "+oggetti[j].object_class);
	//log.error("obj_code: " + oggetti[j].code+" - obj_class: "+oggetti[j].object_class);
	datamart.putAll(objDescr);
*/	
	//inserisco nel datamart i dati degli oggetti dalla query
	datamart.putAll(oggetti[j]);
	def valore_iniziale=0;
	
	valore_iniziale=oggetti[j].obj_price;		
	data_valore=oggetti[j].purchase_date;
	if (oggetti[j].obj_value!=null) {
		valore_iniziale=oggetti[j].obj_value;	
		data_valore=oggetti[j].date_expertise;
	}
	datamart.obj_value=valore_iniziale;
	//def durata_media_mesi=oggetti[j].average_lifetime;
	//coefficiente di degradazione annaule
	def degradation_coeff_ann=oggetti[j].degrad_coeff_ann;
	
/*	Calendar calStartDate = Calendar.getInstance();
	calStartDate.setTime(oggetti[j].validity_start_date);
	def start_date=oggetti[j].validity_start_date;
	def year_start_date = calStartDate.get(Calendar.YEAR);
	def month_start_date = calStartDate.get(Calendar.MONTH);

	Calendar calEndDate = Calendar.getInstance();
	calEndDate.setTime(oggetti[j].validity_end_date);
	def end_date=oggetti[j].validity_end_date;
	def year_end_date = calEndDate.get(Calendar.YEAR);
	def month_end_date = calEndDate.get(Calendar.MONTH);
*/	
	def a=0;
	def m=0;
	def	cont_a=0;
	def	cont_m=0;
	
	Calendar data_produzione_ogg = Calendar.getInstance();
	DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	def anno_costr;
	if (oggetti[j].anno_costr!=null) anno_costr=oggetti[j].anno_costr; else anno_costr=2000;
	Date date = format.parse(anno_costr+"-01-01");
	log.debug("parse date " + date);
	data_produzione_ogg.setTime(date);
	
	if (data_valore==null) {
	//data_valore=data_produzione_ogg;
	Calendar data_valore = Calendar.getInstance();
	DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
	Date date2 = format2.parse("2000-01-01");
	log.debug("parse date " + date2);
	data_valore.setTime(date2);
	}
	
	/*if (oggetti[j].obj_value!=null) {
		data_produzione_ogg.setTime(data_valore);
	}*/
	
	datamart.manufacture_date=data_produzione_ogg.getTime();
	def yearDiff = data_inizio.get(Calendar.YEAR) - data_produzione_ogg.get(Calendar.YEAR); 
	def monthDiff = yearDiff * 12 + data_inizio.get(Calendar.MONTH) - data_produzione_ogg.get(Calendar.MONTH);			
	def eta_bene_anni=yearDiff;
	def eta_bene_mesi=monthDiff;	
	
	for(a=year; a<=year+nTotYear; a++){
		cont_a=cont_a+1;
		//faccio un ciclo per anno
		for(m=month; m<=12; m++){	
		cont_m=cont_m+1;
		if (m==6 || m==12) {
			//controllo che anno e mese che controllo nel ciclo ricade nel periodo di validità dell'oggetto			
			def validity_end=1;
			def validity_start=1;				
/*
			if (a>year_start_date) {
				validity_start=1;	
			} else {
				if ((a==year_start_date) && (m>=month_start_date)) {
				validity_start=1;	
				}
			}
			
			if (a<year_end_date) {
				validity_end=1;	
			} else {
				if ((a==year_end_date) && (m<=month_end_date)) {
				validity_end=1;	
				}
			}
*/					
			if(validity_start==1 && validity_end==1){
				pk_table=pk_table+1;
				datamart.pk_table=pk_table;
				datamart.year=a; 
				if (m<10) {
					month_code='0'+m;
				} else {
					month_code=m;
				}
				datamart.dtim_month=m; 
				datamart.dtim_month_code=month_code; 
				
				if(m<=3){
					datamart.dtim_trim=1;
					datamart.dtim_trimname="Primo Trimestre";
				}
				if(m<=6&&m>3){
					datamart.dtim_trim=2;
					datamart.dtim_trimname="Secondo Trimestre";
				}
				if(m<=9&&m>6){
					datamart.dtim_trim=3;
					datamart.dtim_trimname="Terzo Trimestre";
				}			
				if(m<=12&&m>9){
					datamart.dtim_trim=4;
					datamart.dtim_trimname="Quarto Trimestre";
				}							
				
				Calendar date_row = Calendar.getInstance();  
				date_row.set(Calendar.YEAR, a);
				date_row.set(Calendar.MONTH, m-1);
				date_row.set(Calendar.DAY_OF_MONTH, 1);                 
				date_row.set(Calendar.HOUR_OF_DAY, 0);
				date_row.set(Calendar.MINUTE, 0);
				date_row.set(Calendar.SECOND, 0);
				
				datamart.date_row=date_row.getTime();
				
				switch (m) {
					case 1:  datamart.dtim_monthname = "01_gen";
							 break;
					case 2:  datamart.dtim_monthname = "02_feb";
							 break;
					case 3:  datamart.dtim_monthname = "03_mar";
							 break;
					case 4:  datamart.dtim_monthname = "04_apr";
							 break;
					case 5:  datamart.dtim_monthname = "05_mag";
							 break;
					case 6:  datamart.dtim_monthname = "06_giu";
							 break;
					case 7:  datamart.dtim_monthname = "07_lug";
							 break;
					case 8:  datamart.dtim_monthname = "08_ago";
							 break;
					case 9:  datamart.dtim_monthname = "09_set";
							 break;
					case 10: datamart.dtim_monthname = "10_ott";
							 break;
					case 11: datamart.dtim_monthname = "11_nov";
							 break;
					case 12: datamart.dtim_monthname = "12_dic";
							 break;
					default: datamart.dtim_monthname = "Invalid month";
							 break;
				}				
									
				def eta_bene_mesi_att=eta_bene_mesi+cont_m;		
				def eta_bene_anni_att=Math.ceil(eta_bene_mesi_att/12)-1;
				if (eta_bene_mesi_att>0) {		
					if (degradation_coeff_ann==null) {degradation_coeff_ann=0;}
					log.debug("eta_bene_anni_att: " +eta_bene_anni_att);
					log.debug("degradation_coeff_ann: " +degradation_coeff_ann);
					log.debug("datamart.dtim_month: " +datamart.dtim_month);
					//coefficiente di degradazione a anno e mese considerato nel ciclo
					def degradation_coeff=(eta_bene_anni_att*degradation_coeff_ann)+((degradation_coeff_ann/12)*datamart.dtim_month);
					//coefficiente di degradazione a anno e mese precedente considerato nel ciclo
					def degradation_coeff_prec=degradation_coeff-(degradation_coeff_ann/12);
													
					datamart.degradation_coeff_ann=degradation_coeff_ann;
					datamart.degradation_coeff=degradation_coeff;
					datamart.degradation_coeff_prec=degradation_coeff_prec;
					//def mdeprecation=valore_iniziale+(valore_iniziale*(1+degradation_coeff));
					def mdeprecation=0;
					if (valore_iniziale==null) {valore_iniziale=0;}
					log.error("valore_iniziale: " +valore_iniziale);
					log.error("degradation_coeff: " +degradation_coeff);
					mdeprecation=valore_iniziale*degradation_coeff;
					mdeprecation=Math.round(mdeprecation);
					//def actual_value=valore_iniziale*(1+degradation_coeff);
					def actual_value=valore_iniziale+mdeprecation;
					
					
					def actual_value_prec=valore_iniziale*(1+degradation_coeff_prec);					
					
					datamart.eta_bene_mesi=eta_bene_mesi_att;
					datamart.eta_bene_anni=eta_bene_anni_att;
					datamart.mdeprecation=mdeprecation;
					datamart.mactual_value=actual_value;					

					datamart.actual_value=actual_value;
					datamart.degradation_coeff_prec=degradation_coeff_prec;
					datamart.actual_value_prec=actual_value_prec;
					
					//recupero le info degli interventi migliorativi
					def betterment_value=getBetterment(datamart.code,datamart.object_class,data_valore);
					datamart.betterment_value=betterment_value;
					def obj_tot_value=actual_value+betterment_value;
					if (obj_tot_value<0) {obj_tot_value=0;}
					//if (obj_tot_value<0) {obj_tot_value=0;}
					
					datamart.obj_tot_value=obj_tot_value;
					
					
					def queryInsert="INSERT INTO pma_dati_gw.pri_dtm_obj_depreciation";
					queryInsert+=" (pk,id_object,object_class,object_code,object_name,object_type,";
					queryInsert+="obj_price,obj_value,dobj_sitecode,manufacture_date,obj_age_month,";
					queryInsert+="mcos_tot,mpri_price,mcos_total,mdeprecation,mactual_value,dtim_year,dtim_yearcode,dtim_yearname,dtim_month,";
					queryInsert+="dtim_monthcode,dtim_monthname,dtim_trim,dtim_trimcode,dtim_trimname,degradation_coeff,degradation_coeff_prec,mactual_value_prec,";
					queryInsert+="betterment_value,obj_tot_value,degradation_coeff_ann,obj_age_year,obj_replacement_cost,date_row,cod_company,type_ownership,district,descr_site)";
					queryInsert+=" VALUES ";
					queryInsert+="(#{map.pk_table},#{map.id_obj},#{map.object_class},#{map.code},#{map.name},#{map.object_type},"; 				
					queryInsert+="#{map.obj_price},#{map.obj_value},#{map.site_code},#{map.manufacture_date},#{map.eta_bene_mesi},";
					queryInsert+="#{map.mcos_tot},#{map.mpri_price},#{map.mcos_total},#{map.mdeprecation},#{map.mactual_value},#{map.year},#{map.year},#{map.year},#{map.dtim_month},";
					queryInsert+="#{map.dtim_month_code},#{map.dtim_monthname},#{map.dtim_trim},#{map.dtim_trim},#{map.dtim_trimname},#{map.degradation_coeff},#{map.degradation_coeff_prec},#{map.actual_value_prec},";
					queryInsert+="#{map.betterment_value},#{map.obj_tot_value},#{map.degradation_coeff_ann},#{map.eta_bene_anni},#{map.obj_replacement_cost},#{map.date_row},#{map.cod_company},#{map.type_ownership},#{map.district},#{map.descr_site})";
					
					log.debug("queryInsert"+ queryInsert);
					
					queryService.executeInsertQuery(queryInsert,datamart);
				}
			}
		}
		}
	} 
	}


//***************************************************************************************	
//  DEFINIZIONI FUNZIONI GENERALI PER POPOLAMENTO DATAMART
//***************************************************************************************
	/*def getAllInfoObj(codeObj,classObj)
	{
		def queryCode;
		def retCode;
		
		if(classObj=="gwd_item"){
			queryCode="SELECT s.site_code,s.name_site,s.type_site,tu.cod_system,tu.name_system,tu.type_system,bld.cod_building,bld.name_building,bld.cod_type_building as type_building,gwd_item.cod_item,gwd_item.name_item,gwd_item.class_item as type_item,gwd_item.validity_start_date,gwd_item.validity_end_date,gwd_item.average_lifetime,gwd_item.optimum_lifetime,gwd_item.item_price as obj_price,gwd_item.item_value as obj_value FROM gwd_item left join gwd_system tu on tu.cod_system=gwd_item.cod_system left join pri_building bld on bld.cod_building=tu.cod_building left join gwd_site s on s.site_code=bld.cod_site WHERE gwd_item.cod_item='"+codeObj+"'";
			retCode=queryService.executeQuery(queryCode,null);
			
		}
		if(classObj=="gwd_assets"){
			queryCode="SELECT s.site_code,s.name_site,s.type_site,tu.cod_system,tu.name_system,tu.type_system,bld.cod_building,bld.name_building,bld.cod_type_building as type_building,gwd_assets.cod_asset,gwd_assets.name_asset,gwd_assets.asset_type as type_asset,gwd_assets.validity_start_date,gwd_assets.validity_end_date,gwd_assets.average_lifetime,gwd_assets.optimum_lifetime,gwd_assets.asset_price as obj_price,gwd_assets.asset_value as obj_value FROM gwd_assets left join gwd_system tu on tu.cod_system=gwd_assets.cod_system left join pri_building bld on bld.cod_building=tu.cod_building left join gwd_site s on s.site_code=bld.cod_site WHERE gwd_assets.cod_asset='"+codeObj+"'";
			retCode=queryService.executeQuery(queryCode,null);
			
		}
		if(retCode!=null && retCode[0]!=null){
				return retCode[0];
		}else{
			return null;
		}
		log.debug("codice sito"+ codeObj + " classe oggetto "+ classObj);	
	}*/
	
	def getAllInfoObj(codeObj,classObj)
	{
		def queryCode;
		def retCode;
		
		if(classObj=="gwd_item"){
			queryCode="SELECT s.site_code,s.name_site,s.type_site,tu.cod_system,tu.name_system,tu.type_system,bld.cod_building,bld.name_building,bld.cod_type_building as type_building,gi.cod_item,gi.name_item,gi.class_item as type_item,gi.validity_start_date,gi.validity_end_date,gi.average_lifetime,gi.optimum_lifetime,gi.item_price as obj_price,gi.item_value as obj_value FROM pma_dati_gw.gwd_item as gileft join pma_dati_gw.gwd_system tu on tu.cod_system=gi.cod_system left join pma_dati_gw.pma_building bld on bld.cod_building=tu.cod_building left join pma_dati_gw.gwd_site s on s.site_code=bld.cod_site WHERE gi.cod_item='"+codeObj+"'";
			retCode=queryService.executeQuery(queryCode,null);
			
		}
		if(classObj=="gwd_assets"){
			queryCode="SELECT s.site_code,s.name_site,s.type_site,tu.cod_system,tu.name_system,tu.type_system,bld.cod_building,bld.name_building,bld.cod_type_building as type_building,ga.cod_asset,ga.name_asset,ga.asset_type as type_asset,ga.validity_start_date,ga.validity_end_date,ga.average_lifetime,ga.optimum_lifetime,ga.asset_price as obj_price,ga.asset_value as obj_value FROM pma_dati_gw.gwd_assets ga left join pma_dati_gw.gwd_system tu on tu.cod_system=ga.cod_system left join pma_dati_gw.pma_building bld on bld.cod_building=tu.cod_building left join pma_dati_gw.gwd_site s on s.site_code=bld.cod_site WHERE ga.cod_asset='"+codeObj+"'";
			retCode=queryService.executeQuery(queryCode,null);
			
		}
		if(classObj=="pma_building"){
			queryCode="SELECT s.site_code,s.name_site,s.type_site,'' as cod_system,'' as name_system,bld.cod_building,bld.name_building,bld.cod_type_building as type_building,bld.validity_start_date,bld.validity_end_date,bld.average_lifetime,bld.optimum_lifetime,bld.building_price as obj_price,bld.building_value as obj_value  FROM pma_dati_gw.pma_building bld left join pma_dati_gw.gwd_site s on s.site_code=bld.cod_site WHERE cod_building='"+codeObj+"'";
			retCode=queryService.executeQuery(queryCode,null);
			
		}
		if(classObj=="gwd_site"){
			queryCode="SELECT s.site_code,s.name_site,s.type_site,'' as cod_system,'' as name_system,'' as cod_building,'' as name_building,s.validity_start_date,s.validity_end_date,s.average_lifetime,s.optimum_lifetime,0 as obj_price, 0 as obj_value FROM pma_dati_gw.gwd_site s WHERE s.site_code='"+codeObj+"'";
			retCode=queryService.executeQuery(queryCode,null);
			
		}
		if(classObj=="gwd_system"){
			queryCode="SELECT  s.site_code,s.name_site,s.type_site,tu.cod_system,tu.name_system,tu.type_system,bld.cod_building,bld.name_building,bld.cod_type_building as type_building,tu.validity_start_date,tu.validity_end_date,tu.average_lifetime,tu.optimum_lifetime,tu.system_price as obj_price,tu.system_value as obj_value FROM pma_dati_gw.gwd_system tu left join pma_dati_gw.pma_building bld on bld.cod_building=tu.cod_building left join pma_dati_gw.gwd_site s on s.site_code=bld.cod_site WHERE tu.cod_system='"+codeObj+"'";
			retCode=queryService.executeQuery(queryCode,null);
			
		}
		if(retCode!=null && retCode[0]!=null){
				return retCode[0];
		}else{
			return null;
		}
		log.debug("codice sito"+ codeObj + " classe oggetto "+ classObj);
		
	}	
	
def getBetterment(codeObj,classObj,data_valore)
	{
		def queryCode;
		def retCode;
		
		queryCode="SELECT pbv.cod_building,SUM(pbv.value_building) as valore_migliorativo";
		queryCode+=" from pma_dati_gw.pma_building_value as pbv";
		queryCode+=" left join pma_dati_gw.pma_building pb on pb.cod_building=pbv.cod_building";
		queryCode+=" where pb.date_expertise<=pbv.value_date AND pb.cod_building='"+codeObj+"'";
		queryCode+=" group by pbv.cod_building";

		retCode=queryService.executeQuery(queryCode,null);
				
		if(retCode!=null && retCode[0]!=null){		
			ret=retCode[0].valore_migliorativo;
		}else{
			ret=0;
		}
		/*if(retCode!=null && retCode[2]!=null){
			return retCode[2];
		}else{
			return null;
		}*/
		return ret;
		//log.debug("codice sito"+ codeObj + " classe oggetto "+ classObj);
	}	