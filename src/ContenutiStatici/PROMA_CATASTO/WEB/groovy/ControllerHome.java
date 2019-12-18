package com.geowebframework.catasto.controller;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.geowebframework.catasto.utils.ReaderFiles;
import com.geowebframework.transfer.objects.JsonServerResponse;

@Controller
@RequestMapping("/home.htm") 
public class ControllerHome {
	private static Logger log4j = Logger.getLogger(ControllerHome.class);
	@Autowired
	private ReaderFiles readerFile;
	@RequestMapping(method = RequestMethod.GET)
	
	
	public String initForm(Model model) {

		// File dir = new
		// File("C:\\workspacenewfont\\ettcatasto\\src\\main\\resources\\fileProva\\catastocensuario");
		// File dir1 = new
		// File("C:\\workspacenewfont\\ettcatasto\\src\\main\\resources\\fileProva\\catastomappa");
		//File dir = new File("C:\\workspacenewfont\\ettcatasto\\src\\main\\resources\\fileProva\\catastomappa"); //[[[MAPPA]]]
		//File dir = new File("C:\\workspacenewfont\\ettcatasto\\src\\main\\resources\\fileProva\\catastocensuario"); //[[[CENSUARIO]]]
		//File dir = new File("C:\\workspacenewfont\\ettcatasto\\src\\main\\resources\\fileProva\\docfa"); //[[[DOCFA]]]
		//File dir = new File("C:\\workspacenewfont\\ettcatasto\\src\\main\\resources\\fileProva\\planimetrie"); //[[[PLAN]]]
		try {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			
			File dir = null;
			
			//dir = new File("C:\\workspacenewfont\\ettcatasto\\src\\main\\resources\\Marzo2018\\Mappe"); //[[[MAPPA]]]
			//readerFile.caricaDatiMappa(dir, pw); //[[[MAPPA]]]//OK TUTTI I DATABASE
			
			//dir = new File("C:\\workspacenewfont\\ettcatasto\\src\\main\\resources\\Marzo2018\\catastocensuario"); //[[[CENSUARIO]]]
			//readerFile.caricaDatiCensuario(dir, pw); //[[[CENSUARIO]]]//OK TUTTI I DATABASE
			
			//dir = new File("C:\\workspacenewfont\\ettcatasto\\src\\main\\resources\\test_new\\tarsu"); //[[[TARSU]]]
			//readerFile.caricaDatiTarsu(dir, pw);  //[[[PLAN]]]	//OK TUTTI I DATABASE

			dir = new File("C:\\workspacenewfont\\ettcatasto\\src\\main\\resources\\fileProva\\docfa"); //[[[DOCFA]]]
			readerFile.caricaDatiDocFa(dir, pw);  //[[[DOCFA]]]
			
			
			
			
			//dir = new File("C:\\workspacenewfont\\ettcatasto\\src\\main\\resources\\fileProva\\docfacensuariottobre"); //[[[DOCFA]]]
			//readerFile.caricaDatiDocFaDatiCensuario(dir, pw);  //[[[DOCFA]]]
			
			//dir = new File("C:\\workspacenewfont\\ettcatasto\\src\\main\\resources\\fileProva\\docfaplanimetricigenn"); //[[[DOCFA]]]
			//readerFile.caricaDatiDocFaDatiPlanimetrici(dir, pw);  //[[[DOCFA]]]
			
			//dir = new File("C:\\workspacenewfont\\ettcatasto\\src\\main\\resources\\fileProva\\planimetrie"); //[[[PLAN]]]
			//readerFile.caricaDatiPlanimetrie(dir, pw);  //[[[PLAN]]]
			
		/*dir = new File("C:\\workspacenewfont\\ettcatasto\\src\\main\\resources\\fileProva\\ici"); //[[[DOCFA]]]
	    ReaderFiles.caricaDatiDocFaICI(dir, pw);*/
			
			
			
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			log4j.debug(ex.getMessage(), ex);
		}
		return "home";
	}
	
	
	
}
