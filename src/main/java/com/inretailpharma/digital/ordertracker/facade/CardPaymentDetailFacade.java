package com.inretailpharma.digital.ordertracker.facade;

import com.inretailpharma.digital.ordertracker.dto.CardPaymentDetailDTO;
import com.inretailpharma.digital.ordertracker.dto.ResponseDTO;
import com.inretailpharma.digital.ordertracker.entity.CardPaymentDetail;
import com.inretailpharma.digital.ordertracker.service.cardpaymentdetail.CardPaymentDetailService;
import org.springframework.stereotype.Component;

@Component
public class CardPaymentDetailFacade {

    private CardPaymentDetailService cardPaymentDetailService;

    public CardPaymentDetailFacade(CardPaymentDetailService cardPaymentDetailService) {
        this.cardPaymentDetailService = cardPaymentDetailService;
    }

    public ResponseDTO<CardPaymentDetail> save(CardPaymentDetailDTO cardPaymentDetailDTO) {

        return this.cardPaymentDetailService.save(cardPaymentDetailDTO);
    }

    public void asyncUpdatePaymentMethod(CardPaymentDetailDTO cardPaymentDetailDTO) {
        this.cardPaymentDetailService.asyncUpdatePaymentMethod(cardPaymentDetailDTO);
    }
}
