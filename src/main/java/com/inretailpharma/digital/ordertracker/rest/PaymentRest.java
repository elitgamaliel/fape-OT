package com.inretailpharma.digital.ordertracker.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.inretailpharma.digital.ordertracker.dto.CardPaymentDetailDTO;
import com.inretailpharma.digital.ordertracker.dto.ResponseDTO;
import com.inretailpharma.digital.ordertracker.entity.CardPaymentDetail;
import com.inretailpharma.digital.ordertracker.facade.CardPaymentDetailFacade;

@RestController
public class PaymentRest {

    @Autowired
    private CardPaymentDetailFacade cardPaymentDetailFacade;

	@PostMapping("/card-payment-detail")
    public ResponseEntity<ResponseDTO<CardPaymentDetail>> createCardPaymentDetail(@RequestBody CardPaymentDetailDTO cardPaymentDetailDTO) {
	    ResponseDTO<CardPaymentDetail> response = cardPaymentDetailFacade.save(cardPaymentDetailDTO);
	    if(response.getCode().equals("200")) {
	        cardPaymentDetailFacade.asyncUpdatePaymentMethod(cardPaymentDetailDTO);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}

