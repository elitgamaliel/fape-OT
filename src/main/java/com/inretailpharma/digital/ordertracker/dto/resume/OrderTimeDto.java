package com.inretailpharma.digital.ordertracker.dto.resume;

import com.inretailpharma.digital.ordertracker.entity.DeliveryTravelDetail;
import com.inretailpharma.digital.ordertracker.utils.DateUtil;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Optional;

@Data
public class OrderTimeDto implements Serializable {

    private String order;
    private ReturnTimeDto arrived;
    private ReturnTimeDto waited;
    private String status;
    private long startTime;

    public void fillWith(DeliveryTravelDetail detail, Integer delayTime){
        Optional.ofNullable(detail.getOrderTracker().getOrderTrackerStatus())
                .ifPresent(orderTrackerStatus -> setStatus(orderTrackerStatus.getCode().name()));

        setOrder(String.valueOf(detail.getOrderTracker().getExternalPurchaseId()));

        fillArrivedTime(detail);
        fillWaitedTime(detail.getWaitTime(), delayTime);
    }

    private void fillArrivedTime(DeliveryTravelDetail detail) {
    	ReturnTimeDto arrivedDto = new ReturnTimeDto();

        Integer etaTimeInMinutes = detail.getProjectedEta();
        Date startDate = detail.getStartDate();
        Date endDate = detail.getEndDate();

        if (startDate == null || endDate == null) {
            return;
        }

        startTime = startDate.getTime();

        Long deliveredTimeInMinutes = DateUtil.timeBetween(endDate, startDate, DateUtil.TimeUnits.MINUTES);

        Long arrivedDelayInMinutes = deliveredTimeInMinutes - etaTimeInMinutes;

        arrivedDto.setTime(deliveredTimeInMinutes.intValue());
        arrivedDto.setDelay(arrivedDelayInMinutes > 0 ? arrivedDelayInMinutes.intValue() : 0);

        setArrived(arrivedDto);
    }

    private void fillWaitedTime(Integer waitTimeValue, Integer delayTime) {
        Optional.ofNullable(waitTimeValue).ifPresent(waitTime -> {
        	ReturnTimeDto waitedDto = new ReturnTimeDto();
        	waitedDto.setTime(waitTime);
            int delayWaitTime = waitedDto.getTime() - delayTime;
            waitedDto.setDelay(delayWaitTime > 0 ? delayWaitTime : 0);
            setWaited(waitedDto);
        });
    }

}
