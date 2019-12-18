// \\wincom\c$\projects\BM\WEB\groovy\pri_building_ext_trigger.groovy
import java.util.List;

import com.geowebframework.transfer.model.dataservice.GwdFolder;
import com.geowebframework.dataservice.GwdFolderService;
import com.geowebframework.dataservice.service.GwClassListService;
import com.geowebframework.gwMnemonicCode.model.widget.MnemonicCodeSelectionWidget;
import com.geowebframework.gwMnemonicCode.model.widget.MnemonicCodeWidget;
import com.geowebframework.metadataservice.ClassService;
import com.geowebframework.metadataservice.GwmMnemonicCodeService;
import com.geowebframework.transfer.model.metadataservice.Class;
import com.geowebframework.transfer.model.metadataservice.GwmMnemonicCode;
import com.geowebframework.transfer.model.metadataservice.McPart;
import com.geowebframework.transfer.model.metadataservice.MnemonicCode;
import org.apache.commons.lang.StringUtils;

public class pma_building_value_trigger extends com.geowebframework.dataservice.querybuilder.EventTrigger {
    
public boolean beforeInsert(HashMap<String,Object> valuesMap){
	
	valuesMap.date_insert=new Date();
	log.debug("DATA INSERIMENTO: "+valuesMap.date_insert);
		
	return true;
		
	}; 	
	
	
    public boolean beforeUpdate(HashMap<String,Object> valuesMap,HashMap<String,Object> OldvaluesMap){
        	
		
			
		return true;
		
    }; 
	
	public boolean afterUpdate(HashMap<String,Object> valuesMap, HashMap<String,Object> valuesOldMap){
			return true;
			
	}
	


} 