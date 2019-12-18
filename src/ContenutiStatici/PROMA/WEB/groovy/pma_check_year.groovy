def map = [:];
def mylistAnni = [];


def resultYears = queryService.executeQuery("SELECT distinct year FROM PMA_DATI_GW.pma_patrimonial_valuation",null);
if(resultYears!=null){
	for (int i=0; i<resultYears.size(); i++) {
		Integer currentYear = resultYears[i].year;
		mylistAnni[i]=currentYear;
	}
}
//mylistAnni[0]=1998;
//mylistAnni[1]=2019;
//mylistAnni[2]=2017;
map.success = true;
map.message="ok";
map.anni=mylistAnni;
return map;