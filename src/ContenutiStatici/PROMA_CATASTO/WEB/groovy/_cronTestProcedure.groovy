//user:    MPE 
//date:    15/12/2018
//ver:     4.3.0
//project: TUTTI I PROGETTI
//type:    cron trigger
//class:   gw_sched_def
//note:    groovy per il test di corretto funzionamento dello schedulatore


log.error("########################################################################################################");
log.error("########## CIAO SONO LO SCHEDULATORE ####################");
log.error("########################################################################################################");

//VERIFICO SE CI SONO CARICAMENTI DA SCHEDULARE, OVVERO O NESSUNO E' PARTITO O QUELLO PARTITO HA FINITO

	def schedNew=false;
	def resultSched=queryService.executeQuery("SELECT gw_catasto_sched_fornitura.*,gw_catasto_fornitura.esito FROM gw_catasto_sched_fornitura LEFT JOIN gw_catasto_fornitura ON fk_fornitura=pk_catasto_fornitura  WHERE sched=1 ",null);
	if(resultSched!=null && resultSched[0]!=null){
		log.error("########## NE HO TROVATO UNO IN ESECUZIONE ####################");
		//NE HO TROVATO UNO IN ESECUZIONE
		//CONTROLLO SE HA FINITO
		if(resultSched[0].esito!=3){
			log.error("##########  HA FINITO ####################");
			schedNew=true;
			def mapSched = [:];
			mapSched.data_termine_caricamento = new Date();
			mapSched.pk_catasto_sched_fornitura=resultSched[0].pk_catasto_sched_fornitura;
			mapSched.sched = 3;
			mapSched.caricamento_terminato = 1;
			classService.updateClassRecord('gw_catasto_sched_fornitura',mapSched);
			log.error("########## AGGIORNO CHE HA FINITO ####################");
		}else{
			//NON HA FINITO
			log.error("########## NON HA FINITO ####################");
			schedNew=false;
		}
	}else{
		schedNew=true;
	}
if(schedNew){
//DEVO FARNE PARTIRE UN ALTRO
log.error("########## DEVO FARNE PARTIRE UN ALTRO ####################");
def resultDaSched=queryService.executeQuery("SELECT * FROM gw_catasto_sched_fornitura WHERE sched=0 AND caricamento_terminato=3 ORDER BY  data_richiesta_caricamento ",null);
	if(resultDaSched!=null && resultDaSched[0]!=null){
		def mapSched = [:];
			mapSched.data_caricamento = new Date();
			mapSched.pk_catasto_sched_fornitura=resultDaSched[0].pk_catasto_sched_fornitura;
			mapSched.sched = 1;
			mapSched.caricamento_terminato = 0;
			classService.updateClassRecord('gw_catasto_sched_fornitura',mapSched);
			def mapCaric=ettCatastoService.caricaCatasto(new Integer(resultDaSched[0].fk_fornitura));
			log.error("########## PARTITO ####################");
	}
}


