import com.geowebframework.catasto.service.ETTCatastoService;


HashMap<String, Object> services = new HashMap<String, Object>();
services.put("classService",classService);
services.put("queryService",queryService);
services.put("ettCatastoService",ettCatastoService);

def itemId= parameterMap.itemId;
def activeUser= parameterMap.activeUser;



def mapSched = [:];
mapSched.data_richiesta_caricamento = new Date();
mapSched.fk_fornitura = itemId;
mapSched.sched = 0;
mapSched.username = activeUser;
services.classService.insertClassRecord('gw_catasto_sched_fornitura',mapSched);
	
def map = [:];
map.message="itemId...."+itemId+"activeUser............"+activeUser;
map.message="Il caricamento è stato inserito correttamente. Verrà schedulato non appena possibile. Attendere grazie.";
map.success = true;

	
			
//map=services.ettCatastoService.caricaCatasto(new Integer(itemId));
return map;