package com.inretailpharma.digital.ordertracker.rest;

import com.inretailpharma.digital.ordertracker.dto.DrugstoreDto;
import com.inretailpharma.digital.ordertracker.facade.DrugstoreFacade;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/locals")
public class DrugstoreRest {

    private DrugstoreFacade drugstoreFacade;

    public DrugstoreRest(DrugstoreFacade drugstoreFacade) {
        this.drugstoreFacade = drugstoreFacade;
    }

    @GetMapping("/{localType}")
    public List<DrugstoreDto> findAllDrugstores(@PathVariable(name = "localType") String localType) {
        return drugstoreFacade.findAllDrugstores(localType);
    }

}
