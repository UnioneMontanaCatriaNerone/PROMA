import com.geowebframework.catasto.service.ETTCatastoService;


HashMap<String, Object> services = new HashMap<String, Object>();
services.put("classService",classService);
services.put("queryService",queryService);
services.put("ettCatastoService",ettCatastoService);

def itemId= parameterMap.itemId;
def activeUser= parameterMap.activeUser;
def map = [:];
map.message="itemId...."+itemId+"activeUser............"+activeUser;
map.success = true;

def mapFornitura= [stato:"IN CORSO"];
mapFornitura.put("esito",3);
mapFornitura.put("pk_catasto_fornitura",itemId);
mapFornitura.put("log_message","");
mapFornitura.put("messaggio","");
def querydelete="DELETE FROM catasto_dati.gw_catasto_dettaglio_fornitura WHERE FK_FORNITURA=#{map.pk_catasto_fornitura}";
def ris=services.queryService.executeQuery(querydelete,mapFornitura);
def pkGiunzione=services.classService.updateClassRecord("Fornitura",mapFornitura);

map=services.ettCatastoService.caricaCatasto(itemId);
return map;