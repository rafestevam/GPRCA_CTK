package src.com.idsscheer.webapps.arcm.ui.components.issuemanagement.actioncommands;

import java.util.Iterator;
import java.util.List;

import com.idsscheer.webapps.arcm.bl.models.objectmodel.IAppObj;
import com.idsscheer.webapps.arcm.bl.models.objectmodel.IAppObjFacade;
import com.idsscheer.webapps.arcm.bl.models.objectmodel.attribute.IEnumAttribute;
import com.idsscheer.webapps.arcm.bl.models.objectmodel.attribute.IListAttribute;
import com.idsscheer.webapps.arcm.common.constants.metadata.ObjectType;
import com.idsscheer.webapps.arcm.common.constants.metadata.attribute.IHierarchyAttributeType;
import com.idsscheer.webapps.arcm.common.constants.metadata.attribute.IIssueAttributeType;
import com.idsscheer.webapps.arcm.common.constants.metadata.attribute.IIssueAttributeTypeCustom;
import com.idsscheer.webapps.arcm.common.constants.metadata.attribute.IRiskAttributeType;
import com.idsscheer.webapps.arcm.common.constants.metadata.attribute.IRiskAttributeTypeCustom;
import com.idsscheer.webapps.arcm.common.constants.metadata.attribute.ITestcaseAttributeType;
import com.idsscheer.webapps.arcm.common.constants.metadata.attribute.ITestdefinitionAttributeType;
import com.idsscheer.webapps.arcm.common.notification.NotificationTypeEnum;
import com.idsscheer.webapps.arcm.common.util.ARCMCollections;
import com.idsscheer.webapps.arcm.common.util.ovid.IOVID;
import com.idsscheer.webapps.arcm.config.metadata.enumerations.IEnumerationItem;
public class CustomIssueGetRiskActionCommand2 extends IssueCacheActionCommand{

	private static final com.idsscheer.batchserver.logging.Logger debuglog = new com.idsscheer.batchserver.logging.Logger();	
	private static final boolean DEBUGGER_ON = true;
	protected void afterExecute(){
		
		IAppObj issueAppObj = this.formModel.getAppObj();
		
		//IListAttribute iroList = issueAppObj.getAttribute(IIssueAttributeType.LIST_ISSUERELEVANTOBJECTS);
		IListAttribute iroList = issueAppObj.getAttribute(IIssueAttributeType.LIST_ISSUERELEVANTOBJECTS);
		
		//List<IAppObj> iroElements = iroList.getElements(this.getUserContext());
		List<IAppObj> iroElements = iroList.getElements(this.getUserContext());		
		Iterator<IAppObj> iroIterator = iroElements.iterator();				
		IAppObjFacade testFacade = this.environment.getAppObjFacade(ObjectType.TESTCASE);			
		IEnumAttribute issueTypeList = issueAppObj.getAttribute(IIssueAttributeTypeCustom.ATTR_ACTIONTYPE);
		IEnumerationItem issueType = ARCMCollections.extractSingleEntry(issueTypeList.getRawValue(),true);
		
				
		/*if(issueType.getId() != null ){*/
		if(issueType.getId().equals("issue")){
			
			try{
				
				while(iroIterator.hasNext()){
					
					IAppObj iroAppObj = iroIterator.next();
					IOVID iroOVID = iroAppObj.getVersionData().getHeadOVID();
					IAppObj iroLstObj = testFacade.load(iroOVID, true);
					
										
					
					if(iroAppObj.getObjectType() != ObjectType.TESTCASE)
						continue;
					
					
					testFacade.allocateWriteLock(iroLstObj.getVersionData().getHeadOVID());								
					
					
					List<IAppObj> LstprocObj = iroLstObj.getAttribute(ITestcaseAttributeType.LIST_PROCESS).getElements(this.getUserContext());				
					
					for(IAppObj pcObj : LstprocObj ){
						
						String sprocess = pcObj.getAttribute(IHierarchyAttributeType.ATTR_NAME ).getRawValue();
						this.displayLog("SubProcesso : " + pcObj.getAttribute(IHierarchyAttributeType.ATTR_NAME ).getRawValue());
						issueAppObj.getAttribute(IIssueAttributeTypeCustom.ATTR_CST_PROCESS).setRawValue(sprocess);
						
						String smodelname = pcObj.getAttribute(IHierarchyAttributeType.ATTR_MODEL_NAME).getRawValue();
						this.displayLog("Processo:"+ smodelname );
				
					
						issueAppObj.getAttribute(IIssueAttributeTypeCustom.ATTR_CST_MODELNAME).setRawValue(smodelname);
						this.displayLog("Objeto relevan: " + String.valueOf(pcObj.getAttribute(IHierarchyAttributeType.ATTR_MODEL_NAME )));	
					}
					
										
					List<IAppObj> riskObj = iroLstObj.getAttribute(ITestcaseAttributeType.LIST_RISK).getElements(this.getUserContext());
										
					for(IAppObj rskObj : riskObj){
						
												
						String sresult = rskObj.getAttribute(IRiskAttributeTypeCustom.ATTR_RA_RESULT).getRawValue();
						String sresidual = rskObj.getAttribute(IRiskAttributeTypeCustom.ATTR_RA_RESIDUALFINAL).getRawValue();
						String srskname  = rskObj.getAttribute(IRiskAttributeTypeCustom.ATTR_NAME).getRawValue();
						
						this.displayLog("Classificação Risco Potencial Associado: " + sresult);
						issueAppObj.getAttribute(IIssueAttributeTypeCustom.ATTR_RA_RESULT).setRawValue(sresult);
						
						this.displayLog("Classificação Risco Residual Associado: " + sresidual);
						issueAppObj.getAttribute(IIssueAttributeTypeCustom.ATTR_RA_RESIDUALFINAL).setRawValue(sresidual);
						
						this.displayLog("Classificação Risco Residual Associado: " + srskname );
						issueAppObj.getAttribute(IIssueAttributeTypeCustom.ATTR_RSK_NAME).setRawValue(srskname);
						
						break;

                  }
					
				}
			}catch(Exception e){
				
						this.formModel.addControlInfoMessage(NotificationTypeEnum.INFO, e.getMessage(),new String[] {
								getStringRepresentation(this.formModel.getAppObj())
						}
				);
				
			}
			
			}
		else if (issueType.getId().equals("actionplan")) {
				try{
					this.displayLog("action type 2");
					while(iroIterator.hasNext()){
												
						
						IAppObj iroAppObj = iroIterator.next();
						IOVID iroOVID = iroAppObj.getVersionData().getHeadOVID();
						IAppObj iroLstObj = testFacade.load(iroOVID, true);
						
						
						if(iroAppObj.getObjectType() != ObjectType.ISSUE)
							continue;
						
						String sresult = iroAppObj.getAttribute(IIssueAttributeTypeCustom.ATTR_RA_RESULT).getRawValue();
						this.displayLog("Resultado:" + sresult);
						issueAppObj.getAttribute(IIssueAttributeTypeCustom.ATTR_RA_RESULT).setRawValue(sresult);
						
						String sresidual = iroAppObj.getAttribute(IIssueAttributeTypeCustom.ATTR_RA_RESIDUALFINAL).getRawValue();
						this.displayLog("Residual final:" + sresidual);
						issueAppObj.getAttribute(IIssueAttributeTypeCustom.ATTR_RA_RESIDUALFINAL).setRawValue(sresidual);
						
						String smodelname = iroAppObj.getAttribute(IIssueAttributeTypeCustom.ATTR_CST_MODELNAME).getRawValue();
						this.displayLog("Marco Processo:" + smodelname );
						issueAppObj.getAttribute(IIssueAttributeTypeCustom.ATTR_CST_MODELNAME).setRawValue(smodelname);
						
						String sprocesso = iroAppObj.getAttribute(IIssueAttributeTypeCustom.ATTR_CST_PROCESS).getRawValue();
						this.displayLog("Processo:" + smodelname );
						issueAppObj.getAttribute(IIssueAttributeTypeCustom.ATTR_CST_PROCESS).setRawValue(sprocesso);
						
						
						String srskname = iroAppObj.getAttribute(IIssueAttributeTypeCustom.ATTR_RSK_NAME).getRawValue();
						this.displayLog("risco name:" + srskname);
						issueAppObj.getAttribute(IIssueAttributeTypeCustom.ATTR_RSK_NAME).setRawValue(srskname);

						/*
						IEnumAttribute isTypeList = iroAppObj.getAttribute(IIssueAttributeTypeCustom.ATTR_ISSUESOURCE);
						IEnumerationItem isType = ARCMCollections.extractSingleEntry(isTypeList.getRawValue(), true);
						
						IEnumerationItem sorigemteste = iroAppObj.getAttribute(IIssueAttributeTypeCustom.ATTR_ISSUESOURCE).getRawValue();
						
						this.displayLog("Origem:" + isType.getId());*/
						
						
						break;
							
						}				
					
				}catch(Exception e){
					
							this.formModel.addControlInfoMessage(NotificationTypeEnum.INFO, e.getMessage(),new String[] {
									getStringRepresentation(this.formModel.getAppObj())
							}
					);
					
				}
				
			} 
			
		}

	private void displayLog(String message){
		if(DEBUGGER_ON){
			debuglog.info(this.getClass().getName(),message );
		}
	}
 }

