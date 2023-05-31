package com.inretailpharma.digital.ordertracker.service.external;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.inretailpharma.digital.ordertracker.config.ExternalTrackerConfig;
import com.inretailpharma.digital.ordertracker.dto.UtilPubSubDto;
import com.inretailpharma.digital.ordertracker.entity.OrderDistanceAudit;
import com.inretailpharma.digital.ordertracker.repository.OrderDistanceAuditRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.inretailpharma.digital.ordertracker.config.OrderTrackerProperties;
import com.inretailpharma.digital.ordertracker.dto.OrderStatusDto;
import com.inretailpharma.digital.ordertracker.dto.SyncOrderDto;
import com.inretailpharma.digital.ordertracker.dto.external.AuditOrderStatusDto;
import com.inretailpharma.digital.ordertracker.dto.external.AuditSyncDto;
import com.inretailpharma.digital.ordertracker.entity.OrderTrackerStatus.Code;
import com.inretailpharma.digital.ordertracker.mapper.ExternalMapper;
import com.inretailpharma.digital.ordertracker.utils.DateUtil;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class AuditTrackerService {

    @Value("${messaging.util-pubsub.topic}")
    private String updatePubsubTopic;

    private OrderTrackerProperties orderTrackerProperties;
    private ExternalMapper mapper;
    private OrderDistanceAuditRepository orderDistanceAuditRepository;
    protected ExternalTrackerConfig externalTrackerConfig;

    public AuditTrackerService(
            OrderTrackerProperties orderTrackerProperties,
            ExternalMapper mapper,
            OrderDistanceAuditRepository orderDistanceAuditRepository
    ) {
        this.orderTrackerProperties = orderTrackerProperties;
        this.mapper = mapper;
        this.orderDistanceAuditRepository = orderDistanceAuditRepository;
    }

    public Mono<Void> updateOrderStatus(Long orderId, OrderStatusDto orderStatusDto, Boolean error) {
        orderStatusDto.setOrderExternalId(orderId);
        AuditOrderStatusDto orderStatusAuditDto = mapper.convertOrderStatusDtoToAuditOrderStatusDto(orderStatusDto, error);
        log.info("[START] call to service - updateOrderStatus - uri:{} - body:{}",
                orderTrackerProperties.getPubsubPublishingUrl(), orderStatusAuditDto);

        Gson objectMapper = new Gson();
        UtilPubSubDto auditPubsubDto = UtilPubSubDto.builder()
                .payload(objectMapper.toJson(orderStatusAuditDto)).topic(updatePubsubTopic).build();

        return WebClient
                .builder()
                .baseUrl(orderTrackerProperties.getPubsubPublishingUrl())
                .build()
                .post()
                .bodyValue(auditPubsubDto)
                .exchange()
                .flatMap(clientResponse -> clientResponse.bodyToMono(Void.class)
                )
                .doOnSuccess(s -> log.info("[END] call updateOrderStatus "))
                .doOnError(e -> {
                    e.printStackTrace();
                    log.error("[ERROR] call to service - updateOrderStatus ", e.getMessage());
                });
    }

    public Mono<Void> syncOrderStatus(Long orderId, SyncOrderDto order, boolean error) {
        order.setEcommerceId(orderId);
        return this.bulkSyncOrderStatus(Collections.singletonList(order), error);
    }

    public Mono<Void> syncOrderStatus(Long orderId, SyncOrderDto order) {
        return this.syncOrderStatus(orderId, order, false);
    }

    public Mono<Void> bulkSyncOrderStatus(List<Long> orderIds, Code statusCode,
                                          String updateBy, String sourceName, String detail, boolean error) {
        OrderStatusDto orderStatusDto = OrderStatusDto.builder()
                .status(statusCode.name())
                .creationDate(DateUtil.currentDateLong())
                .origin("APP")
                .updatedBy(updateBy)
                .sourceName(sourceName)
                .statusDetail(detail)
                .build();

        List<SyncOrderDto> orders = orderIds.stream()
                .map(id -> new SyncOrderDto(null, id, Collections.singletonList(orderStatusDto)))
                .collect(Collectors.toList());

        return this.bulkSyncOrderStatus(orders, error);

    }

    public Mono<Void> bulkSyncOrderStatus(List<Long> orderIds, Code statusCode, String updateBy, String sourceName) {
        return bulkSyncOrderStatus(orderIds, statusCode, updateBy, sourceName, null, false);
    }

    public Mono<Void> bulkSyncOrderStatus(List<SyncOrderDto> orders, boolean error) {
        log.info("[START] call to service - SyncOrderDto - - body:{}",  orders );

        List<OrderStatusDto> outputOrderStatusDto = new ArrayList<>();

        orders.stream().forEach(ordersSync -> {

            ordersSync.getStatusOrderHistory().stream().forEach(statusOrderHistory -> {

                OrderStatusDto orderStatusDto =  OrderStatusDto.builder()
                        .orderExternalId(ordersSync.getEcommerceId())
                        .orderTrackingCode(ordersSync.getTrackingCode())
                        .status(statusOrderHistory.getStatus())
                        .latitude(statusOrderHistory.getLatitude())
                        .longitude(statusOrderHistory.getLongitude())
                        .code(statusOrderHistory.getCode())
                        .detail(statusOrderHistory.getDetail())
                        .note(statusOrderHistory.getNote())
                        .description(statusOrderHistory.getDescription())
                        .origin(statusOrderHistory.getOrigin())
                        .creationDate(statusOrderHistory.getCreationDate())
                        .previousStatus(statusOrderHistory.getPreviousStatus())
                        .updatedBy(statusOrderHistory.getUpdatedBy())
                        .sourceName(statusOrderHistory.getSourceName())
                        .statusDetail(statusOrderHistory.getStatusDetail())
                        .auditable(statusOrderHistory.isAuditable())
                        .build();

                outputOrderStatusDto.add(orderStatusDto);
            });
        });

        log.info("OrderStatusDto: {}", new Gson().toJson(outputOrderStatusDto ));

        return Flux.fromIterable(outputOrderStatusDto)
                .flatMap(s -> this.updateOrderStatus(s.getOrderExternalId(), s, false)
                ).singleOrEmpty();

    }


    public Mono<Void> saveAuditDistance(OrderDistanceAudit orderDistanceAudit) {

        return Mono.just(orderDistanceAuditRepository.save(orderDistanceAudit)).then();

    }


}
