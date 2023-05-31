package com.inretailpharma.digital.ordertracker.service.cardpaymentdetail;

import com.inretailpharma.digital.ordertracker.config.OrderTrackerProperties;
import com.inretailpharma.digital.ordertracker.dto.CardPaymentDetailDTO;
import com.inretailpharma.digital.ordertracker.dto.ResponseDTO;
import com.inretailpharma.digital.ordertracker.dto.UpdatePaymentMethodDto;
import com.inretailpharma.digital.ordertracker.entity.CardPaymentDetail;
import com.inretailpharma.digital.ordertracker.mapper.CardPaymentDetailMapper;
import com.inretailpharma.digital.ordertracker.repository.CardPaymentDetailRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Implementacion interna de {@link CardPaymentDetailService}
 * . Esta clase no se debe acceder directamente
 *
 * @author
 */

@Slf4j
@Service("CardPaymentDetailService")
public class CardPaymentDetailServiceImpl implements CardPaymentDetailService {

    private OrderTrackerProperties orderTrackerProperties;
    private RestTemplate restTemplate;

    @Autowired
    private CardPaymentDetailRepository cardPaymentDetailRepository;

    public CardPaymentDetailServiceImpl(OrderTrackerProperties orderTrackerProperties,
                              @Qualifier("externalRestTemplate") RestTemplate restTemplate) {
        this.orderTrackerProperties = orderTrackerProperties;
        this.restTemplate = restTemplate;
    }

    @Override

    public ResponseDTO<CardPaymentDetail> save(CardPaymentDetailDTO cardPaymentDetailDTO) {
        ResponseDTO<CardPaymentDetail> responseDTO = new ResponseDTO<>();
        CardPaymentDetail cardPaymentDetail = this.cardPaymentDetailRepository.save(CardPaymentDetailMapper.mapDtoToCardPaymentDetail(cardPaymentDetailDTO));
        if(cardPaymentDetail != null) {
            responseDTO.setCode("200");
            responseDTO.setMessage("Se ha registrado detalle de pago con tarjeta satisfactoriamente!");
            responseDTO.setData(cardPaymentDetail);
        } else {
            responseDTO.setCode("500");
            responseDTO.setMessage("Error, no se puede registrar el detalle de pago con tarjeta!");
            responseDTO.setData(cardPaymentDetail);
        }
        return responseDTO;
    }

    @Override
    @Async("threadPoolTaskExecutor")
    public void asyncUpdatePaymentMethod(CardPaymentDetailDTO cardPaymentDetailDTO) {
        UpdatePaymentMethodDto updatePaymentMethodDto = CardPaymentDetailMapper.transformData(cardPaymentDetailDTO);
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            HttpEntity<UpdatePaymentMethodDto> request = new HttpEntity<>(updatePaymentMethodDto);
            ResponseEntity<ResponseDTO> responsePayment = restTemplate.exchange(
                    orderTrackerProperties.getUpdateMethodPaymentUrl(),
                    HttpMethod.POST,
                    request,
                    ResponseDTO.class
            );
            log.info("************** RESPONSE - **************", responsePayment.getBody());
        } catch (Exception ex) {
            log.error("[ERROR] Método de pago no se ha actualizado", ex);
            responseDTO.setCode("500");
            responseDTO.setMessage(ex.getMessage());
        }
    }



}
