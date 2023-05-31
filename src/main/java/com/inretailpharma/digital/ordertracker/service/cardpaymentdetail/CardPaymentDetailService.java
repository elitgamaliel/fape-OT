package com.inretailpharma.digital.ordertracker.service.cardpaymentdetail;

import com.inretailpharma.digital.ordertracker.dto.CardPaymentDetailDTO;
import com.inretailpharma.digital.ordertracker.dto.ResponseDTO;
import com.inretailpharma.digital.ordertracker.entity.CardPaymentDetail;

public interface CardPaymentDetailService {
    ResponseDTO<CardPaymentDetail> save(CardPaymentDetailDTO cardPaymentDetailDTO);
    void asyncUpdatePaymentMethod(CardPaymentDetailDTO cardPaymentDetailDTO);
}
