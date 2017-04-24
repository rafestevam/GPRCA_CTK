package com.idsscheer.webapps.arcm.ui.components.testmanagement.actioncommands;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;

import com.idsscheer.webapps.arcm.bl.models.objectmodel.IAppObj;
import com.idsscheer.webapps.arcm.bl.models.objectmodel.IAppObjFacade;
import com.idsscheer.webapps.arcm.bl.models.objectmodel.attribute.IEnumAttribute;
import com.idsscheer.webapps.arcm.bl.models.objectmodel.query.IAppObjIterator;
import com.idsscheer.webapps.arcm.bl.models.objectmodel.query.IAppObjQuery;
import com.idsscheer.webapps.arcm.common.constants.metadata.ObjectType;
import com.idsscheer.webapps.arcm.common.constants.metadata.attribute.IControlAttributeType;
import com.idsscheer.webapps.arcm.common.constants.metadata.attribute.IControlAttributeTypeCustom;
import com.idsscheer.webapps.arcm.common.constants.metadata.attribute.IRiskAttributeType;
import com.idsscheer.webapps.arcm.common.constants.metadata.attribute.IRiskAttributeTypeCustom;
import com.idsscheer.webapps.arcm.common.constants.metadata.attribute.ITestCaseAttributeTypeCustom;
import com.idsscheer.webapps.arcm.common.constants.metadata.attribute.ITestcaseAttributeType;
import com.idsscheer.webapps.arcm.common.constants.metadata.attribute.ITestdefinitionAttributeTypeCustom;
import com.idsscheer.webapps.arcm.common.util.ARCMCollections;
import com.idsscheer.webapps.arcm.common.util.ovid.IOVID;
import com.idsscheer.webapps.arcm.config.metadata.enumerations.IEnumerationItem;

public class CustomTestcaseSaveActionCommand extends TestcaseSaveActionCommand {
	
	/*private String origemTeste = "";
	private double riskVuln2line = 0;
	private double riskVuln3line = 0;*/
	/*private String viewName = "customcontrol2risk";
	private String view_column_obj_id = "risk_obj_id";
	private String view_column_version_number = "risk_version_number";
	private String view_testcase = "customtestdef2testaction";
	private String view_testcase_obj_id = "ta_id";
	private String view_testcase_version_number = "ta_version_number";*/
	private String riscoPotencial = "";
	private String fernanda = "";
	private String origemTeste = "";
	final Logger log = Logger.getLogger(CustomTestcaseSaveActionCommand.class.getName());

	protected void addForwardDialog() {
		this.executeCalculation();
		super.addForwardDialog();
	}
	
	//protected void afterExecute(){
	private void executeCalculation(){
		
		IAppObj currAppObj = this.formModel.getAppObj();
		IAppObj currParentCtrlObj = this.parentControl(currAppObj);
		String ownerStatus = this.requestContext.getParameter(ITestcaseAttributeType.STR_OWNER_STATUS);
		//this.origemTeste = this.requestContext.getParameter(ITestCaseAttributeTypeCustom.STR_ORIGEMTESTE);
		
		IEnumAttribute origemTesteAttr = currAppObj.getAttribute(ITestCaseAttributeTypeCustom.ATTR_ORIGEMTESTE);
		IEnumerationItem origemTeste = ARCMCollections.extractSingleEntry(origemTesteAttr.getRawValue(), true);
		this.origemTeste = origemTeste.getId();
		log.info("Origem do Teste: " + this.origemTeste);
		
		if(ownerStatus.equals("3"))
			this.fernanda = "effective";
		
		if(ownerStatus.equals("4"))
			this.fernanda = "noneffective";

		log.info("Status de Proprietário: " + ownerStatus);
		
		try{
			
			IAppObj riskParentObj = this.getRiskFromControl(currParentCtrlObj);
			if(riskParentObj.getAttribute(IRiskAttributeTypeCustom.ATTR_RA_RESULT).isEmpty()){
				this.riscoPotencial = "Nao Avaliado";
			}else{
				this.riscoPotencial = riskParentObj.getAttribute(IRiskAttributeTypeCustom.ATTR_RA_RESULT).getRawValue();
			}
			log.info("Risco Pai: " + riskParentObj.getAttribute(IRiskAttributeType.ATTR_RISK_ID).getRawValue());
			this.affectResidualRisk(riskParentObj);
			
			
		}catch(Exception e){
			//this.environment.getDialogManager().createSilentForwardDialog("ERRO", e.getMessage());
			this.environment.getDialogManager().getNotificationDialog().addInfo(e.getMessage());
		}
		
		
	}
	
	private IAppObj parentControl(IAppObj childAppObj){
		
		IAppObj parentControlObj = null;
		
		List<IAppObj> controlList = childAppObj.getAttribute(ITestcaseAttributeType.LIST_CONTROL).getElements(this.getFullGrantUserContext());
		for(IAppObj controlObj : controlList){
			parentControlObj = controlObj;
			//parentControlName = controlObj.getAttribute(IControlAttributeType.ATTR_NAME).getRawValue();
		}
		
		return parentControlObj;
		
	}
	
	private IAppObj getRiskFromControl(IAppObj controlObj) throws Exception{
		
		IAppObjFacade riskFacade = this.environment.getAppObjFacade(ObjectType.RISK);
		IAppObjQuery riskQuery = riskFacade.createQuery();
		IAppObjIterator riskIterator = riskQuery.getResultIterator();
		IAppObj riskAppObj = null;
		//log.info("infra para obtenção de risco pronta");
		
		while(riskIterator.hasNext()){
			
			IAppObj riskObj = riskIterator.next();
			//log.info("risco lido" + riskObj.getAttribute(IRiskAttributeType.ATTR_NAME).getRawValue());
			
			if(!(riskAppObj == null))
				break;
			
			List<IAppObj> ctrlList = riskObj.getAttribute(IRiskAttributeType.LIST_CONTROLS).getElements(this.getFullGrantUserContext());
			for(IAppObj ctrlObj : ctrlList){
				if(ctrlObj.getGuid().equals(controlObj.getGuid())){
					//log.info("risco do controle" + riskObj.getAttribute(IRiskAttributeType.ATTR_NAME).getRawValue());
					riskAppObj = riskObj;
					break;
				}
			}
			
		}
		riskQuery.release();
		
		return riskAppObj;
		
		/*Map filterMap = new HashMap();
		log.info("filtermap declarado");
		log.info("Código do controle " + controlObj.getAttribute(IControlAttributeType.ATTR_CONTROL_ID).getRawValue());
		filterMap.put("control_obj_id", controlObj.getAttribute(IControlAttributeType.ATTR_CONTROL_ID).getRawValue());
		
		IAppObj riskReturn = null;
		IAppObjFacade riskFacade = this.environment.getAppObjFacade(ObjectType.RISK);
		log.info("risk facade declarado");
		log.info(riskFacade.toString());
		
		IViewQuery query = QueryFactory.createQuery(this.getUserContext(), this.viewName, filterMap, null,
				true, this.getDefaultTransaction());
		try{
			
			log.info("query executada");
			
			Iterator it = query.getResultIterator();
			while(it.hasNext()){
				
				log.info("iterator");
				
				IViewObj viewObj = (IViewObj) it.next();
				Long obj_id = (Long) viewObj.getRawValue(view_column_obj_id);
				log.info("obj_id: " + String.valueOf(obj_id));
				Long version_number = (Long) viewObj.getRawValue(view_column_version_number);
				log.info("version_number: " + String.valueOf(version_number));
				
				IOVID riskOVID = OVIDFactory.getOVID(obj_id.longValue(), version_number.longValue());
				log.info("riskOVID: " + riskOVID.toString());
				riskReturn = riskFacade.load(riskOVID, true);
				log.info("riskOBJ carregado: " + riskReturn.toString());
				
			}
			query.release();
			return riskReturn;
			
		}catch(RightException re){
			log.info("Exceção Direitos: " + re.getMessage());
			throw re;
		}catch(Exception e){
			log.info("Exceção Query: " + e.getMessage());
			throw e;
		}*/
		
	}
	
	private void affectResidualRisk(IAppObj riskObj) throws Exception{
		
		double cntTotal2Line = 0;
		double cntTotal3Line = 0;
		double cntInef2Line = 0;
		double cntInef3Line = 0;
		
		try{
			
			IAppObjFacade riskFacade = this.environment.getAppObjFacade(ObjectType.RISK);
			IOVID riskOVID = riskObj.getVersionData().getHeadOVID();
			IAppObj riskUpdObj = riskFacade.load(riskOVID, true);
			riskFacade.allocateWriteLock(riskOVID);
			
			IAppObjFacade controlFacade = this.environment.getAppObjFacade(ObjectType.CONTROL);
		
			List<IAppObj> controlList = riskObj.getAttribute(IRiskAttributeType.LIST_CONTROLS).getElements(this.getFullGrantUserContext());
			for(IAppObj controlObj : controlList){
				
				IOVID controlOVID = controlObj.getVersionData().getHeadOVID();
				IAppObj controlUpdObj = controlFacade.load(controlOVID, true);
				controlFacade.allocateWriteLock(controlOVID);
				
				List<IAppObj> tstDefList = controlObj.getAttribute(IControlAttributeType.LIST_TESTDEFINITIONS).getElements(this.getFullGrantUserContext());
				for(IAppObj tstDefObj : tstDefList){
					
					if(this.testDefClass(tstDefObj).equals("1linhadefesa"))
						cntTotal2Line += 1;
					
					if(this.testDefClass(tstDefObj).equals("2linhadefesa"))
						cntTotal3Line += 1;
					
					log.info("Total Testes 2a Linha: " + String.valueOf(cntTotal2Line));
					log.info("Total Testes 3a Linha: " + String.valueOf(cntTotal3Line));
					
					List<IAppObj> tstCaseList = this.getTestCaseFromTestDef(tstDefObj);
					for(IAppObj tstCaseObj : tstCaseList){
						
						IEnumAttribute ownerStatusAttr = tstCaseObj.getAttribute(ITestcaseAttributeType.ATTR_OWNER_STATUS);
						IEnumerationItem ownerStatusItem = ARCMCollections.extractSingleEntry(ownerStatusAttr.getRawValue(), true);
						log.info("Caso de Teste: " + tstCaseObj.getAttribute(ITestcaseAttributeType.ATTR_NAME).getRawValue());
						log.info("Efetividade do Teste: " + ownerStatusItem.getId());
						if(ownerStatusItem.getId().equals("noneffective")){
							if(this.testDefClass(tstDefObj).equals("1linhadefesa")){
								cntInef2Line += 1;
								log.info("TC ineficaz 2 linha: " + String.valueOf(cntInef2Line));
							}
							if(this.testDefClass(tstDefObj).equals("2linhadefesa")){
								cntInef3Line += 1;
								log.info("TC ineficaz 3 linha: " + String.valueOf(cntInef3Line));
							}
							//controlUpdObj.getAttribute(IControlAttributeTypeCustom.ATTR_CUSTOM_STATUS).setRawValue("ineficaz");
						}else{
							//controlUpdObj.getAttribute(IControlAttributeTypeCustom.ATTR_CUSTOM_STATUS).setRawValue("eficaz");
						}
						if(ownerStatusItem.getId().equals("new")){
							if(this.fernanda.equals("noneffective")){
								if(this.testDefClass(tstDefObj).equals("1linhadefesa")){
									cntInef2Line += 1;
									log.info("TC ineficaz 2 linha: " + String.valueOf(cntInef2Line));
								}
								if(this.testDefClass(tstDefObj).equals("2linhadefesa")){
									cntInef3Line += 1;
									log.info("TC ineficaz 3 linha: " + String.valueOf(cntInef3Line));
								}
								controlUpdObj.getAttribute(IControlAttributeTypeCustom.ATTR_CUSTOM_STATUS).setRawValue("ineficaz");
							}else{
								controlUpdObj.getAttribute(IControlAttributeTypeCustom.ATTR_CUSTOM_STATUS).setRawValue("eficaz");
							}
						}
					}
					
				}
				
			}
			
			String riskClassFinal = "";
			
			if(cntTotal2Line == 0)
				cntInef2Line = 0;
			
			if(cntTotal3Line == 0)
				cntInef3Line = 0;
			
			if(this.origemTeste.equals("1linhadefesa")){
				riskUpdObj.getAttribute(IRiskAttributeTypeCustom.ATTR_RA_INEF2LINE).setRawValue(cntInef2Line);
				riskUpdObj.getAttribute(IRiskAttributeTypeCustom.ATTR_RA_FINAL2LINE).setRawValue(cntTotal2Line);
				double risk2line = ( cntInef2Line / cntTotal2Line );
				log.info("Ponderacao 2 linha: " + String.valueOf(risk2line));
				String riskClass2line = this.riskClassification(risk2line);
				log.info("Classificacao 2 linha: " + riskClass2line);
				riskUpdObj.getAttribute(IRiskAttributeTypeCustom.ATTR_RA_CONTROL2LINE).setRawValue(riskClass2line);
				log.info("Classificacao 2 linha - ATTR: " + riskUpdObj.getAttribute(IRiskAttributeTypeCustom.ATTR_RA_CONTROL2LINE).getRawValue());
				riskClassFinal = riskClass2line;
				riskUpdObj.getAttribute(IRiskAttributeTypeCustom.ATTR_RA_CONTROLFINAL).setRawValue(riskClassFinal);
				log.info("Classificacao Final - ATTR: " + riskUpdObj.getAttribute(IRiskAttributeTypeCustom.ATTR_RA_CONTROLFINAL).getRawValue());
			}
			
			if(this.origemTeste.equals("2linhadefesa")){
				riskUpdObj.getAttribute(IRiskAttributeTypeCustom.ATTR_RA_INEF3LINE).setRawValue(cntInef3Line);
				riskUpdObj.getAttribute(IRiskAttributeTypeCustom.ATTR_RA_FINAL3LINE).setRawValue(cntTotal3Line);
				double risk3line = ( cntInef3Line / cntTotal3Line );
				log.info("Ponderacao 3 linha: " + String.valueOf(risk3line));
				String riskClass3line = this.riskClassification(risk3line);
				log.info("Classificacao 3 linha: " + riskClass3line);
				riskUpdObj.getAttribute(IRiskAttributeTypeCustom.ATTR_RA_CONTROL3LINE).setRawValue(riskClass3line);
				log.info("Classificacao 3 linha - ATTR: " + riskUpdObj.getAttribute(IRiskAttributeTypeCustom.ATTR_RA_CONTROL3LINE).getRawValue());
				riskClassFinal = riskClass3line;
				riskUpdObj.getAttribute(IRiskAttributeTypeCustom.ATTR_RA_CONTROLFINAL).setRawValue(riskClassFinal);
				log.info("Classificacao Final - ATTR: " + riskUpdObj.getAttribute(IRiskAttributeTypeCustom.ATTR_RA_CONTROLFINAL).getRawValue());
			}
			
			/*String riskClass1line = riskUpdObj.getAttribute(IRiskAttributeTypeCustom.ATTR_RA_CONTROL1LINE).getRawValue();
			if(riskClass1line == null)
				riskClass1line = "";
			
			double risk2line = ( cntInef2Line / cntTotal2Line );
			log.info("Ponderacao 2 linha: " + String.valueOf(risk2line));
			String riskClass2line = this.riskClassification(risk2line);
			log.info("Classificacao 2 linha: " + riskClass2line);
			riskUpdObj.getAttribute(IRiskAttributeTypeCustom.ATTR_RA_CONTROL2LINE).setRawValue(riskClass2line);
			log.info("Classificacao 2 linha - ATTR: " + riskUpdObj.getAttribute(IRiskAttributeTypeCustom.ATTR_RA_CONTROL2LINE).getRawValue());
			
			double risk3line = ( cntInef3Line / cntTotal3Line );
			log.info("Ponderacao 3 linha: " + String.valueOf(risk3line));
			String riskClass3line = this.riskClassification(risk3line);
			log.info("Classificacao 3 linha: " + riskClass3line);
			riskUpdObj.getAttribute(IRiskAttributeTypeCustom.ATTR_RA_CONTROL3LINE).setRawValue(riskClass3line);
			log.info("Classificacao 3 linha - ATTR: " + riskUpdObj.getAttribute(IRiskAttributeTypeCustom.ATTR_RA_CONTROL3LINE).getRawValue());
			
			log.info("Calculando Amb Controle Final");
			String riskClassFinal = this.riskFinalClassification(riskClass1line, riskClass2line, riskClass3line);
			riskUpdObj.getAttribute(IRiskAttributeTypeCustom.ATTR_RA_CONTROLFINAL).setRawValue(riskClassFinal);
			log.info("Classificacao Ctrl Final: " + riskClassFinal);
			log.info("Amb Controle Final Calculado...");*/
			
			log.info("Calculando Residual Final");
			String riskResidualFinal = this.riskResidualFinal(this.riscoPotencial, riskClassFinal);
			riskUpdObj.getAttribute(IRiskAttributeTypeCustom.ATTR_RA_RESIDUALFINAL).setRawValue(riskResidualFinal);
			log.info("Classificacao Res Final: " + riskUpdObj.getAttribute(IRiskAttributeTypeCustom.ATTR_RA_RESIDUALFINAL).getRawValue());
			log.info("Residual Final Calculado...");
			
			log.info("Salvando Risco");
			riskFacade.save(riskUpdObj, this.getDefaultTransaction(), true);
			log.info("Risco Salvo: " + riskUpdObj.getAttribute(IRiskAttributeType.ATTR_RISK_ID).getRawValue());
			riskFacade.releaseLock(riskOVID);
			log.info("Risco Liberado: " + riskUpdObj.getAttribute(IRiskAttributeType.ATTR_RISK_ID).getRawValue());
			
		}
		catch(Exception e){
			log.info("Risco Exception: " + e.getMessage());
			throw e;
		}
		
	}
	
	private String testDefClass(IAppObj tstDefObj){
		
		IEnumAttribute origTestAttr = tstDefObj.getAttribute(ITestdefinitionAttributeTypeCustom.ATTR_ORIGEMTESTE);
		IEnumerationItem origTest = ARCMCollections.extractSingleEntry(origTestAttr.getRawValue(), true);
		
		return origTest.getId();
		
	}
	
	private List<IAppObj> getTestCaseFromTestDef(IAppObj testDefObj) throws Exception{
		
		IAppObjFacade tcFacade = this.environment.getAppObjFacade(ObjectType.TESTCASE);
		IAppObjQuery tcQuery = tcFacade.createQuery();
		IAppObjIterator tcIterator = tcQuery.getResultIterator();
		List<IAppObj> testCaseReturn = new ArrayList<IAppObj>();
		List<IAppObj> testCaseBuffer = new ArrayList<IAppObj>();
		//log.info("Infra para obtenção de Test Case montada");
		
		while(tcIterator.hasNext()){
			
			IAppObj tcObj = tcIterator.next();
			//log.info("TC Lido: " + tcObj.getAttribute(ITestcaseAttributeType.ATTR_NAME).getRawValue());
			
			List<IAppObj> tdList = tcObj.getAttribute(ITestcaseAttributeType.LIST_TESTDEFINITION).getElements(this.getFullGrantUserContext());
			for(IAppObj tdObj : tdList){
				if(tdObj.getGuid().equals(testDefObj.getGuid())){
					//log.info("TestCase Adicionado: " + tcObj.getAttribute(ITestcaseAttributeType.ATTR_NAME).getRawValue());
					testCaseBuffer.add(tcObj);
				}
			}
		}
		
		//Ordenação por data de criação dos Test Case
		testCaseBuffer.sort(new Comparator<IAppObj>(){
			@Override
			public int compare(IAppObj ant, IAppObj post){
				long antTime = ant.getVersionData().getCreateDate().getTime();
				long postTime = post.getVersionData().getCreateDate().getTime();
				return antTime < postTime ? -1 : antTime == postTime ? 0 : 1;
			}
		});
		
		testCaseReturn.add(testCaseBuffer.get(testCaseBuffer.size() - 1));
		tcQuery.release();
		
		return (List<IAppObj>) testCaseReturn;
		
		/*Map filterMap = new HashMap();
		
		List<IAppObj> testCaseReturn = new ArrayList<IAppObj>();
		IAppObjFacade testCaseFacade = this.environment.getAppObjFacade(ObjectType.TESTCASE);
		
		IViewQuery query = QueryFactory.createQuery(this.getUserContext(), this.view_testcase, filterMap, null,
				true, this.getDefaultTransaction());
		try{
			
			Iterator it = query.getResultIterator();
			while(it.hasNext()){
				
				IViewObj viewObj = (IViewObj) it.next();
				Long obj_id = (Long) viewObj.getRawValue(this.view_testcase_obj_id);
				Long version_number = (Long) viewObj.getRawValue(this.view_testcase_version_number);
				
				IOVID testCaseOVID = OVIDFactory.getOVID(obj_id.longValue(), version_number.longValue());
				testCaseReturn.add(testCaseFacade.load(testCaseOVID, true));
				
			}
			query.release();
			return (List<IAppObj>)testCaseReturn;
			
		}catch(Exception e){
			throw e;
		}*/
		
		
	}
	
	private String riskClassification(double riskVuln){
		
		String riskClassif = "";
		
		if( (riskVuln >= 0.00) && (riskVuln <= 0.19) ){
			riskClassif = "Baixo";
		}
		
		if( (riskVuln >= 0.20) && (riskVuln <= 0.49) ){
			riskClassif = "Médio";
		}
		
		if( (riskVuln >= 0.50) && (riskVuln <= 0.69) ){
			riskClassif = "Alto";
		}
		
		if( (riskVuln >= 0.70) && (riskVuln <= 1.00) ){
			riskClassif = "Muito Alto";
		}
		
		return riskClassif;
		
	}
	
	private String riskFinalClassification(String risk1line, String risk2line, String risk3line) throws Exception{
		
		int height_1line = 0;
		int height_2line = 0;
		int height_3line = 0;
		String riskClassFinal = "";
		
		try{
		
			//Classificação - Amb. Controles 1a Linha
			if(risk1line.equalsIgnoreCase("Muito Alto"))
				height_1line = 4;
			if(risk1line.equalsIgnoreCase("Alto"))
				height_1line = 3;
			if(risk1line.equalsIgnoreCase("Médio"))
				height_1line = 2;
			if(risk1line.equalsIgnoreCase("Baixo"))
				height_1line = 1;
			
			log.info("Height 1 Line: " + String.valueOf(height_1line));
			
			//Classificação - Amb. Controles 2a Linha
			if(risk2line.equalsIgnoreCase("Muito Alto"))
				height_2line = 4;
			if(risk2line.equalsIgnoreCase("Alto"))
				height_2line = 3;
			if(risk2line.equalsIgnoreCase("Médio"))
				height_2line = 2;
			if(risk2line.equalsIgnoreCase("Baixo"))
				height_2line = 1;
			
			log.info("Height 2 Line: " + String.valueOf(height_2line));
			
			//Classificação - Amb. Controles 3a Linha
			if(risk3line.equalsIgnoreCase("Muito Alto"))
				height_3line = 4;
			if(risk3line.equalsIgnoreCase("Alto"))
				height_3line = 3;
			if(risk3line.equalsIgnoreCase("Médio"))
				height_3line = 2;
			if(risk3line.equalsIgnoreCase("Baixo"))
				height_3line = 1;		
			
			log.info("Height 3 Line: " + String.valueOf(height_3line));
			
			int maxHeightCtrl = Math.max(height_1line, Math.max(height_2line, height_3line));
			log.info("Height Max: " + String.valueOf(maxHeightCtrl));
			
			switch(maxHeightCtrl){
			case 4:
				riskClassFinal = "Muito Alto";
				break;
			case 3:
				riskClassFinal = "Alto";
				break;
			case 2:
				riskClassFinal = "Médio";
				break;
			case 1:
				riskClassFinal = "Baixo";
				break;
			default:
				riskClassFinal = "Não Avaliado";
				break;
			}
			
			log.info("Class Final Amb Contr: " + riskClassFinal);
			return riskClassFinal;
		
		}catch(Exception e){
			log.info("Risk Final Class Exception: " + e.getMessage());
			throw e;
		}
		
	}
	
	private String riskResidualFinal(String riskPotencial, String riskControlFinal){
		
		String riskResidualReturn = "";
		
		if(riskPotencial.equals("Nao Avaliado"))
			return "Não Avaliado";
			
		if(riskPotencial.equals("Muito Alto") && riskControlFinal.equals("Muito Alto"))
			riskResidualReturn = "Muito Alto";
		
		if(riskPotencial.equals("Muito Alto") && riskControlFinal.equals("Alto"))
			riskResidualReturn = "Muito Alto";
		
		if(riskPotencial.equals("Muito Alto") && riskControlFinal.equals("Médio"))
			riskResidualReturn = "Alto";
		
		if(riskPotencial.equals("Muito Alto") && riskControlFinal.equals("Baixo"))
			riskResidualReturn = "Médio";
		
		if(riskPotencial.equals("Alto") && riskControlFinal.equals("Muito Alto"))
			riskResidualReturn = "Alto";
		
		if(riskPotencial.equals("Alto") && riskControlFinal.equals("Alto"))
			riskResidualReturn = "Alto";
		
		if(riskPotencial.equals("Alto") && riskControlFinal.equals("Médio"))
			riskResidualReturn = "Médio";
		
		if(riskPotencial.equals("Alto") && riskControlFinal.equals("Baixo"))
			riskResidualReturn = "Médio";
		
		if(riskPotencial.equals("Médio") && riskControlFinal.equals("Muito Alto"))
			riskResidualReturn = "Médio";
		
		if(riskPotencial.equals("Médio") && riskControlFinal.equals("Alto"))
			riskResidualReturn = "Médio";
		
		if(riskPotencial.equals("Médio") && riskControlFinal.equals("Médio"))
			riskResidualReturn = "Médio";
		
		if(riskPotencial.equals("Médio") && riskControlFinal.equals("Baixo"))
			riskResidualReturn = "Baixo";
		
		return riskResidualReturn;
		
	}
	
	
/*		
		
		IAppObj currAppObj = this.formModel.getAppObj();
		IUIEnvironment currEnv = this.environment;
		String currCtrlParentName = this.parentControl(currAppObj);
		
		IEnumAttribute ra_controlresattr = currAppObj.getAttribute(ITestcaseAttributeType.ATTR_OWNER_STATUS);
		IEnumerationItem ra_controlresitem = ARCMCollections.extractSingleEntry(ra_controlresattr.getRawValue(), true);
		String ra_controlres = ra_controlresitem.getId();		
		log.info("ra_controlres: " + ra_controlres);
		
		this.defineOrigemTeste(currAppObj);
		this.setRiskVuln(currAppObj);
		log.info("origem teste: " + this.getOrigemTeste());
		
		
		
		try{
			
			IAppObjFacade tcFacade = currEnv.getAppObjFacade(ObjectType.TESTCASE);
			IAppObjQuery tcQuery = tcFacade.createQuery();
			IAppObjIterator tcIterator = tcQuery.getResultIterator();
			
			while(tcIterator.hasNext()){
				
				IAppObj tcAppObj = tcIterator.next();
				IOVID tcOVID = tcAppObj.getVersionData().getHeadOVID();
				IAppObj tcUpdObj = tcFacade.load(tcOVID, true);
				
				if(tcUpdObj.getGuid().equals(currAppObj.getGuid()))
					continue;
				
				String ceCtrlParentName = this.parentControl(tcUpdObj);
				
				if(!(currCtrlParentName.equals(ceCtrlParentName)))
					continue;				
				
				this.copyEditableAttr(currAppObj, tcUpdObj);
				tcFacade.save(tcAppObj, this.getDefaultTransaction(), true);
				tcFacade.releaseLock(tcOVID);
				
				List<IAppObj> tcCtrlList = tcUpdObj.getAttribute(IControlexecutionAttributeType.LIST_CONTROL).getElements(this.getUserContext());
				for(IAppObj tcCtrlObj : tcCtrlList){
					log.info("gravando riscos...");
					this.affectResidualRisk(currEnv, tcCtrlObj, ra_controlres);
				}
				
			}
			tcQuery.release();
			
			List<IAppObj> currCtrlList = currAppObj.getAttribute(IControlexecutionAttributeType.LIST_CONTROL).getElements(this.getUserContext());
			for(IAppObj currCtrlObj : currCtrlList){
				this.affectResidualRisk(currEnv, currCtrlObj, ra_controlres);
			}
			
		}catch(Exception e){
			this.formModel.addControlInfoMessage(NotificationTypeEnum.INFO, e.getMessage(), new String[] { getStringRepresentation(this.formModel.getAppObj()) });
		}
		
	}
	
	private void setOrigemTeste(String origemTeste){
		this.origemTeste = origemTeste;
	}
	
	private String getOrigemTeste(){
		return this.origemTeste;
	}
	
	private void defineOrigemTeste(IAppObj currObj){
		
		List<IAppObj> testDefList = currObj.getAttribute(ITestcaseAttributeType.LIST_TESTDEFINITION).getElements(this.getUserContext());
		for(IAppObj testDefObj : testDefList){
			IEnumAttribute origAttr = testDefObj.getAttribute(ITestdefinitionAttributeTypeCustom.ATTR_ORIGEMTESTE);
			IEnumerationItem origItem = ARCMCollections.extractSingleEntry(origAttr.getRawValue(), true);
			String origemTeste = origItem.getId();
			this.setOrigemTeste(origemTeste);
		}
		
	}
	
	private String parentControl(IAppObj childAppObj){
		
		String parentControlName = "";
		
		List<IAppObj> controlList = childAppObj.getAttribute(ITestcaseAttributeType.LIST_CONTROL).getElements(this.getUserContext());
		for(IAppObj controlObj : controlList){
			parentControlName = controlObj.getAttribute(IControlAttributeType.ATTR_NAME).getRawValue();
		}
		
		return parentControlName;
		
	}
	
	private void copyEditableAttr(IAppObj sourceObj, IAppObj targetObj){
		
		List<IAttribute> attrList = sourceObj.getEditableAttributes(this.getUserContext());
		for(IAttribute attr : attrList){
			log.info(attr.getAttributeType().getId());
			AppObjUtility.copyAttributeValue(sourceObj, targetObj, attr.getAttributeType());
		}
		
	}
	
	private void affectResidualRisk(IUIEnvironment currEnv, IAppObj controlAppObj, String testResult) throws Exception{
		
		IAppObjFacade riskFacade = currEnv.getAppObjFacade(ObjectType.RISK);
		IAppObjQuery riskQuery = riskFacade.createQuery();
		IAppObjIterator riskIterator = riskQuery.getResultIterator();
		
		log.info("Resultado Controle: " + testResult);
		
		//String controlID = controlAppObj.getAttribute(IControlAttributeType.ATTR_CONTROL_ID).getRawValue();
		//log.info("Parametro ControlID: " + controlID);
		
		try{
		
			while(riskIterator.hasNext()){
				
				IAppObj riskObj = riskIterator.next();
				IOVID riskOVID = riskObj.getVersionData().getHeadOVID();
				IAppObj riskUpdObj = riskFacade.load(riskOVID, true);
				riskFacade.allocateWriteLock(riskOVID);
				
				String riskClass2line = this.riskClassification(this.getRiskVuln2Line());
				String riskClass3line = this.riskClassification(this.getRiskVuln3Line());
				List<IAppObj> ctrlList = riskUpdObj.getAttribute(IRiskAttributeType.LIST_CONTROLS).getElements(this.getFullGrantUserContext());
				for(IAppObj ctrlObj : ctrlList){
					String ctrlID = ctrlObj.getAttribute(IControlAttributeType.ATTR_CONTROL_ID).getRawValue();
					//log.info("Parametro CtrlID: " + controlID);
					if(ctrlID.equals(controlID)){
						if(this.getOrigemTeste().equals("1linhadefesa")){
							riskUpdObj.getAttribute(IRiskAttributeTypeCustom.ATTR_RA_CONTROL2LINE).setRawValue(testResult);
						}
						if(this.getOrigemTeste().equals("2linhadefesa")){
							riskUpdObj.getAttribute(IRiskAttributeTypeCustom.ATTR_RA_CONTROL3LINE).setRawValue(testResult);
						}
					}
				}
				//log.info(riskUpdObj);
				
				if(this.getOrigemTeste().equals("1linhadefesa")){
					riskUpdObj.getAttribute(IRiskAttributeTypeCustom.ATTR_RA_CONTROL2LINE).setRawValue(riskClass2line);
				}
				if(this.getOrigemTeste().equals("2linhadefesa")){
					riskUpdObj.getAttribute(IRiskAttributeTypeCustom.ATTR_RA_CONTROL3LINE).setRawValue(riskClass3line);
				}
				
				riskFacade.save(riskUpdObj, this.getDefaultTransaction(), true);
				riskFacade.releaseLock(riskOVID);
			}
			riskQuery.release();
		
		}catch(Exception e){
			throw e;
		}
		
	}
	
	private void setRiskVuln(IAppObj currObj){
		
		List<IAppObj> testDefList = currObj.getAttribute(ITestcaseAttributeType.LIST_TESTDEFINITION).getElements(this.getUserContext());
		double count2line = 0;
		double count3line = 0;
		double countTotal = 0;
		for(IAppObj testDefObj : testDefList){
			IEnumAttribute origAttr = testDefObj.getAttribute(ITestdefinitionAttributeTypeCustom.ATTR_ORIGEMTESTE);
			IEnumerationItem origItem = ARCMCollections.extractSingleEntry(origAttr.getRawValue(), true);
			String origemTeste = origItem.getId();
			countTotal += 1;
			//this.setOrigemTeste(origemTeste);
			if(origemTeste.equals("1linhadefesa"))
				count2line += 1;
			if(origemTeste.equals("2linhadefesa"))
				count3line += 1;
		
		}
		
		this.riskVuln2line = (count2line / countTotal);
		this.riskVuln3line = (count3line / countTotal);
		
	}
	
	private double getRiskVuln2Line(){
		return this.riskVuln2line;
	}
	
	private double getRiskVuln3Line(){
		return this.riskVuln3line;
	}
	
	private String riskClassification(double riskVuln){
		
		String riskClassif = "";
		
		if( (riskVuln >= 0.00) && (riskVuln <= 0.19) ){
			riskClassif = "Baixa";
		}
		
		if( (riskVuln >= 0.20) && (riskVuln <= 0.49) ){
			riskClassif = "Média";
		}
		
		if( (riskVuln >= 0.50) && (riskVuln <= 0.69) ){
			riskClassif = "Alta";
		}
		
		if( (riskVuln >= 0.70) && (riskVuln <= 1.00) ){
			riskClassif = "Muito Alta";
		}
		
		return riskClassif;
		
	}*/

}
