package com.inretailpharma.digital.ordertracker.facade;

import java.util.List;

import org.springframework.stereotype.Component;

import com.inretailpharma.digital.ordertracker.dto.DrugstoreDto;
import com.inretailpharma.digital.ordertracker.service.external.ExternalService;
import com.inretailpharma.digital.ordertracker.utils.Constant;

@Component
public class DrugstoreFacade {
	
	private ExternalService externalService;
	
	public DrugstoreFacade(ExternalService externalService) {
		this.externalService = externalService;
	}

	public List<DrugstoreDto> findAllDrugstores(String localType) {
		
		//Test if localType is valid
		Constant.MotorizedType.parseByName(localType);		
		
		return externalService.findAllDrugstores(localType);
	}
	
	public DrugstoreDto findDrugStore(String localCode) {
		return externalService.findDrugstore(localCode);
	}
}
