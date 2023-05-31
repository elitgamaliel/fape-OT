package com.inretailpharma.digital.ordertracker.mapper;

import com.inretailpharma.digital.ordertracker.dto.CardPaymentDetailDTO;
import com.inretailpharma.digital.ordertracker.dto.UpdatePaymentMethodDto;
import com.inretailpharma.digital.ordertracker.entity.CardPaymentDetail;

public class CardPaymentDetailMapper {

    private CardPaymentDetailMapper() {}

    public static CardPaymentDetailDTO mapCardPaymentDetailToDto(CardPaymentDetail cardPaymentDetail) {
        CardPaymentDetailDTO cardPaymentDetailDTO = new CardPaymentDetailDTO();
        if(cardPaymentDetail != null) {
            cardPaymentDetailDTO.setId(cardPaymentDetail.getId());
            cardPaymentDetailDTO.setOrderId(cardPaymentDetail.getOrderId());
            cardPaymentDetailDTO.setCodLocal(cardPaymentDetail.getOrderId());
            cardPaymentDetailDTO.setTransactionResult(cardPaymentDetail.getTransactionResult());
            cardPaymentDetailDTO.setCardBin(cardPaymentDetail.getCardBin());
            cardPaymentDetailDTO.setCardBrand(cardPaymentDetail.getCardBrand());
            cardPaymentDetailDTO.setCurrency(cardPaymentDetail.getCurrency());
            cardPaymentDetailDTO.setAmount(cardPaymentDetail.getAmount());
            cardPaymentDetailDTO.setInstallments(cardPaymentDetail.getInstallments());
            cardPaymentDetailDTO.setTransactionId(cardPaymentDetail.getTransactionId());
            cardPaymentDetailDTO.setAuthorizationCode(cardPaymentDetail.getAuthorizationCode());
            cardPaymentDetailDTO.setPurcherNumber(cardPaymentDetail.getPurcherNumber());
            cardPaymentDetailDTO.setTransactionDate(cardPaymentDetail.getTransactionDate());
            cardPaymentDetailDTO.setTransactionTime(cardPaymentDetail.getTransactionTime());
            cardPaymentDetailDTO.setCreatedBy(cardPaymentDetail.getCreatedBy());
            cardPaymentDetailDTO.setDateCreated(cardPaymentDetail.getDateCreated());
            cardPaymentDetailDTO.setLastUpdateBy(cardPaymentDetail.getLastUpdateBy());
            cardPaymentDetailDTO.setDateLastUpdated(cardPaymentDetail.getDateLastUpdated());
        }
        return cardPaymentDetailDTO;
    }

    public static CardPaymentDetail mapDtoToCardPaymentDetail(CardPaymentDetailDTO cardPaymentDetailDTO) {
        CardPaymentDetail cardPaymentDetail = new CardPaymentDetail();
        if(cardPaymentDetailDTO != null) {
            cardPaymentDetail.setOrderId(cardPaymentDetailDTO.getOrderId());
            cardPaymentDetail.setCodLocal(cardPaymentDetailDTO.getCodLocal());
            cardPaymentDetail.setTransactionResult(cardPaymentDetailDTO.getTransactionResult());
            cardPaymentDetail.setCardBin(cardPaymentDetailDTO.getCardBin());
            cardPaymentDetail.setCardBrand(cardPaymentDetailDTO.getCardBrand());
            cardPaymentDetail.setCurrency(cardPaymentDetailDTO.getCurrency());
            cardPaymentDetail.setAmount(cardPaymentDetailDTO.getAmount());
            cardPaymentDetail.setInstallments(cardPaymentDetailDTO.getInstallments());
            cardPaymentDetail.setTransactionId(cardPaymentDetailDTO.getTransactionId());
            cardPaymentDetail.setAuthorizationCode(cardPaymentDetailDTO.getAuthorizationCode());
            cardPaymentDetail.setPurcherNumber(cardPaymentDetailDTO.getPurcherNumber());
            cardPaymentDetail.setTransactionDate(cardPaymentDetailDTO.getTransactionDate());
            cardPaymentDetail.setTransactionTime(cardPaymentDetailDTO.getTransactionTime());
            cardPaymentDetail.setCreatedBy(cardPaymentDetailDTO.getCreatedBy());
            cardPaymentDetail.setDateCreated(cardPaymentDetailDTO.getDateCreated());
            cardPaymentDetail.setLastUpdateBy(cardPaymentDetailDTO.getLastUpdateBy());
            cardPaymentDetail.setDateLastUpdated(cardPaymentDetailDTO.getDateLastUpdated());
        }
        return cardPaymentDetail;
    }

    public static UpdatePaymentMethodDto transformData(CardPaymentDetailDTO cardPaymentDetailDTO) {
        UpdatePaymentMethodDto updatePaymentMethodDto = new UpdatePaymentMethodDto();
        if(cardPaymentDetailDTO != null) {
            updatePaymentMethodDto.setLocalId(cardPaymentDetailDTO.getOrderId());
            updatePaymentMethodDto.setAuthorizationCode(cardPaymentDetailDTO.getAuthorizationCode());
            updatePaymentMethodDto.setPurcherNumber(cardPaymentDetailDTO.getAuthorizationCode());
            updatePaymentMethodDto.setTransactionCode(cardPaymentDetailDTO.getTransactionId());
            updatePaymentMethodDto.setOrderNumber(cardPaymentDetailDTO.getPurcherNumber());
        }
        return updatePaymentMethodDto;
    }
}
