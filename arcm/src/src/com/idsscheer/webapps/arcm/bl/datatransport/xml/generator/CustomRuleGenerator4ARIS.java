package com.idsscheer.webapps.arcm.bl.datatransport.xml.generator;

import com.idsscheer.webapps.arcm.bl.datatransport.xml.ExportModus;
import com.idsscheer.webapps.arcm.common.constants.metadata.ObjectType;
import com.idsscheer.webapps.arcm.dl.framework.metadata.IDataLayerAttrMetaDataList;
import com.idsscheer.webapps.arcm.dl.framework.metadata.IDataLayerObjMetaData;

public class CustomRuleGenerator4ARIS extends XMLImportMigrationRuleGenerator4ARIS {
	protected ExportModus getExportModus() {
		return super.getExportModus();
	}
	
	protected void printMerge(IDataLayerObjMetaData smd) {
		super.printMerge(smd);
	}
	
	protected String[] getObjectAndVersionColumnNames() {
		return super.getObjectAndVersionColumnNames();
	}
	
	protected String getCreatorUserFunctionName() {
		return super.getCreatorUserFunctionName();
	}
	
	protected String getChangeUserFunctionName() {
		return super.getChangeTypeFunctionName();
	}
	
	protected String getChangeTypeFunctionName() {
		return super.getChangeTypeFunctionName();
	}	
	
	protected void printCustomColumMaps(IDataLayerObjMetaData smd) {
		super.printCustomColumMaps(smd);
		if (smd.getDataLayerObjectType().getName().equalsIgnoreCase(ObjectType.RISK.getId())){
			printColumnMap("setStartDate", ObjectType.RISK.getId(), "startdate", "startdate");
			printColumnMap("setEndDate", ObjectType.RISK.getId(), "enddate", "enddate");
		}
	}
	
	protected void printObjectAndVersionColumMaps(IDataLayerObjMetaData smd) {
		super.printObjectAndVersionColumMaps(smd);
	}
	
	protected void printColumMap(IDataLayerAttrMetaDataList attr, IDataLayerObjMetaData smd) {
		super.printColumMap(attr, smd);
	}	
	
}
