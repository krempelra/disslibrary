package de.rkrempel.diss.core.dbtools;

import java.util.HashMap;
import java.util.Map;

/**
 * This is a tool developed to Analyze the Arachne Database from the MYSQL Table Perspective
 * It Wraps alot of Knowledge of the Database into an Object
 */
public class ArachneTableInfoTool {
	Map<String,String> nameToDescriptioName;
	Map<String,String> nameToUpperName;
	
	Map<String,String> nameToPrimaryKey;
	
	
	public ArachneTableInfoTool() {
		nameToDescriptioName= new HashMap<String,String>();
		nameToUpperName= new HashMap<String,String>();
		
		nameToPrimaryKey= new HashMap<String,String>();
		
		nameToDescriptioName.put("person","CONCAT_WS( `person`.`Kurzbeschreibung`,' ', `person`.`VornameSonst`,' ',`person`.`Cognomen`,' ',`person`.`FamVatersnameSonst`)");
		nameToDescriptioName.put("literatur"," `literatur`.`DAIRichtlinien`");
		nameToDescriptioName.put("ort","CONCAT_WS( `ort`.`Aufbewahrungsort`, ' ', `ort`.`Ort_antik`, ' ',`ort`.`Stadt`)");

		
	}
	
	
	public String getKeyNamebyName(String name){
		
		if(nameToPrimaryKey.containsKey(name))
			return nameToPrimaryKey.get(name);
		else 
			return "PS_"+this.getTableNamebyName(name)+"ID";
						
					
	}
	public String getTableNamebyName(String name){
		
		if(nameToUpperName.containsKey(name))
			return nameToUpperName.get(name);	
		else 
			return name.substring(0,1).toUpperCase() + name.substring(1);
		
	}
	
	public String getDescriptionNamebyName(String name){
		
		if(nameToDescriptioName.containsKey(name))
			return nameToDescriptioName.get(name);	
		else 
			return "`"+name+"`.`Kurzbeschreibung"+this.getTableNamebyName(name)+"`";
		
	}

	
}
