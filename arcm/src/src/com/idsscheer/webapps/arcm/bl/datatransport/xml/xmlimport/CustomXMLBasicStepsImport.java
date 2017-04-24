package com.idsscheer.webapps.arcm.bl.datatransport.xml.xmlimport;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import com.idsscheer.webapps.arcm.bl.datatransport.xml.AAMMLLogger;
import com.idsscheer.webapps.arcm.bl.datatransport.xml.resources.AAMMLResourceBundle;
import com.idsscheer.webapps.arcm.dl.framework.IDataLayerObject;
import com.idsscheer.webapps.arcm.dl.migframe.IMerge;
import com.idsscheer.webapps.arcm.dl.migframe.IMigrationRecord;
import com.idsscheer.webapps.arcm.dl.migframe.MigrationException;
import com.idsscheer.webapps.arcm.dl.migframe.logic.IColumnMap;
import com.idsscheer.webapps.arcm.dl.migframe.logic.IObjMap;

public class CustomXMLBasicStepsImport extends XMLImportMigrationBasisSteps {
	private final String CLASSNAME = super.getClass().getName();
	private final AAMMLResourceBundle rsBundle = AAMMLResourceBundle.getBundle((Locale) null);
	//private final String CLIENT_ADMIN_PREFIX = "system_";
	//private final String CLIENT_ADMINGROUP_PREFIX = "Administrators_";
	//private Map objectListsMap = new HashMap();
	//private IFilterFactory filterFactory = PersistenceAPI.getFilterFactory();
	//private IPersistenceManager persistenceManager = PersistenceAPI.getPersistenceManager();

	public boolean preCondition(IMigrationRecord sourceRec, IMigrationRecord targetRec, IObjMap objectMap)
			throws MigrationException {
		return super.preCondition(sourceRec, targetRec, objectMap);
	}

	protected boolean isObjectWithoutARISChangedate(String objectName) {
		return super.isObjectWithoutARISChangedate(objectName);
	}

	public void adddummyclient(IMigrationRecord sourceRec, IMigrationRecord targetRec, IColumnMap columnMap)
			throws MigrationException {
		super.adddummyclient(sourceRec, targetRec, columnMap);
	}

	public void changelistsizeone(IMigrationRecord sourceRec, IMigrationRecord targetRec, IColumnMap columnMap)
			throws MigrationException {
		super.changelistsizeone(sourceRec, targetRec, columnMap);
	}

	public void ignore(IMigrationRecord sourceRec, IMigrationRecord targetRec, IColumnMap columnMap)
			throws MigrationException {
		super.ignore(sourceRec, targetRec, columnMap);
	}

	public void setlistsizeone(IMigrationRecord sourceRec, IMigrationRecord targetRec, IColumnMap columnMap)
			throws MigrationException {
		super.setlistsizeone(sourceRec, targetRec, columnMap);
	}

	public void mergelist(IMigrationRecord sourceRec, IMigrationRecord targetRec, IColumnMap columnMap)
			throws MigrationException {
		super.mergelist(sourceRec, targetRec, columnMap);
	}

	public void copylist(IMigrationRecord sourceRec, IMigrationRecord targetRec, IColumnMap columnMap)
			throws MigrationException {
		super.copylist(sourceRec, targetRec, columnMap);
	}

	public void preservevalue(IMigrationRecord sourceRec, IMigrationRecord targetRec, IColumnMap columnMap)
			throws MigrationException {
		super.preservevalue(sourceRec, targetRec, columnMap);
	}

	public void setpassword(IMigrationRecord sourceRec, IMigrationRecord targetRec, IColumnMap columnMap)
			throws MigrationException {
		super.setpassword(sourceRec, targetRec, columnMap);
	}

	public void setchangeuserid(IMigrationRecord sourceRec, IMigrationRecord targetRec, IColumnMap columnMap)
			throws MigrationException {
		super.setchangeuserid(sourceRec, targetRec, columnMap);
	}

	public void setchangetype(IMigrationRecord sourceRec, IMigrationRecord targetRec, IColumnMap columnMap)
			throws MigrationException {
		super.setchangetype(sourceRec, targetRec, columnMap);
	}

	public void setcreatoruserid(IMigrationRecord sourceRec, IMigrationRecord targetRec, IColumnMap columnMap)
			throws MigrationException {
		super.setcreatoruserid(sourceRec, targetRec, columnMap);
	}

	public void setvisibledefault(IMigrationRecord sourceRec, IMigrationRecord targetRec, IColumnMap columnMap)
			throws MigrationException {
		super.setvisibledefault(sourceRec, targetRec, columnMap);
	}

	public void setchangedate(IMigrationRecord sourceRec, IMigrationRecord targetRec, IColumnMap columnMap)
			throws MigrationException {
		super.setchangedate(sourceRec, targetRec, columnMap);
	}

	public void setisroot(IMigrationRecord sourceRec, IMigrationRecord targetRec, IColumnMap columnMap)
			throws MigrationException {
		super.setisroot(sourceRec, targetRec, columnMap);
	}

	public void setselectable(IMigrationRecord sourceRec, IMigrationRecord targetRec, IColumnMap columnMap)
			throws MigrationException {
		super.setselectable(sourceRec, targetRec, columnMap);
	}

	public void setdocumenttype(IMigrationRecord sourceRec, IMigrationRecord targetRec, IColumnMap columnMap)
			throws MigrationException {
		super.setdocumenttype(sourceRec, targetRec, columnMap);
	}

	public IDataLayerObject findDocument(IMigrationRecord record, IMerge mergeInfos) throws MigrationException {
		return super.findDocument(record, mergeInfos);
	}

	protected IDataLayerObject queryObject(String objName, String queryField, String value, String client) {
		return super.queryObject(objName, queryField, value, client);
	}

	public void preservedata(IMigrationRecord sourceRec, IMigrationRecord targetRec, IColumnMap columnMap)
			throws MigrationException {
		super.preservedata(sourceRec, targetRec, columnMap);
	}

	public void setdeficiencyid(IMigrationRecord sourceRec, IMigrationRecord targetRec, IColumnMap columnMap)
			throws MigrationException {
		super.setdeficiencyid(sourceRec, targetRec, columnMap);
	}

	public void adaptclientadmin(IMigrationRecord sourceRec, IMigrationRecord targetRec, IColumnMap columnMap)
			throws MigrationException {
		super.adaptclientadmin(sourceRec, targetRec, columnMap);
	}

	public void adaptclientadmingroup(IMigrationRecord sourceRec, IMigrationRecord targetRec, IColumnMap columnMap)
			throws MigrationException {
		super.adaptclientadmingroup(sourceRec, targetRec, columnMap);
	}

	public void id2objfromdb(IMigrationRecord sourceRec, IMigrationRecord targetRec, IColumnMap columnMap)
			throws MigrationException {
		super.id2objfromdb(sourceRec, targetRec, columnMap);
	}

	public IDataLayerObject reloadObjectFromDB(String objName, String aliasName, long objID, long versionNumber) {
		return super.reloadObjectFromDB(objName, aliasName, objID, versionNumber);
	}

	public void checkassessmentexists(IMigrationRecord sourceRec, IMigrationRecord targetRec, IColumnMap columnMap)
			throws MigrationException {
		super.checkassessmentexists(sourceRec, targetRec, columnMap);
	}

	public void setassessor(IMigrationRecord sourceRec, IMigrationRecord targetRec, IColumnMap columnMap)
			throws MigrationException {
		super.setassessor(sourceRec, targetRec, columnMap);
	}

	public void setmanagementrelevant(IMigrationRecord sourceRec, IMigrationRecord targetRec, IColumnMap columnMap)
			throws MigrationException {
		super.setmanagementrelevant(sourceRec, targetRec, columnMap);
	}

	public void setstartdate(IMigrationRecord sourceRec, IMigrationRecord targetRec, IColumnMap columnMap)
			throws MigrationException {
		try {
			Date e = new Date();
			targetRec.setDate(columnMap.getSource(), e);
		} catch (Exception arg4) {
			AAMMLLogger.error(this.CLASSNAME, this.rsBundle.getMessage("error.import.risk.setstartdate.ZZZ"));
			throw new MigrationException(MigrationException.EX_MIGRATION_STEPS);
		}
	}
	
	public void setenddate(IMigrationRecord sourceRec, IMigrationRecord targetRec, IColumnMap columnMap)
			throws MigrationException {
		try {
			Calendar sysDate = new GregorianCalendar(2050, Calendar.JANUARY, 31);
			//Date e = new Date(2050, 12, 31);
			Date e = sysDate.getTime();
			targetRec.setDate(columnMap.getSource(), e);
		} catch (Exception arg4) {
			AAMMLLogger.error(this.CLASSNAME, this.rsBundle.getMessage("error.import.risk.setstartdate.ZZZ"));
			throw new MigrationException(MigrationException.EX_MIGRATION_STEPS);
		}
	}	
	
}
