package com.inretailpharma.digital.ordertracker.service.external.tracker;

import com.inretailpharma.digital.ordertracker.config.ExternalTrackerConfig;
import com.inretailpharma.digital.ordertracker.dto.OrderStatusDto;
import com.inretailpharma.digital.ordertracker.dto.SyncOrderDto;
import com.inretailpharma.digital.ordertracker.dto.external.ActionDto;
import com.inretailpharma.digital.ordertracker.dto.external.ActionSyncDto;
import com.inretailpharma.digital.ordertracker.dto.external.ExternalMotorizedStatusDto;
import com.inretailpharma.digital.ordertracker.mapper.ExternalMapper;
import com.inretailpharma.digital.ordertracker.utils.Constant.MotorizedType;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public abstract class BaseExternalTrackerService implements ExternalTrackerService  {

	protected RestTemplate restTemplate;
	protected ExternalTrackerConfig externalTrackerConfig;
	protected ExternalMapper mapper;
	
	protected BaseExternalTrackerService(ExternalTrackerConfig externalTrackerConfig,
			RestTemplate restTemplate,
			ExternalMapper mapper) {
		this.externalTrackerConfig = externalTrackerConfig;
		this.restTemplate = restTemplate;
		this.mapper = mapper;
	}
	
	@Override
	public MotorizedType getMotorizedType() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Mono<Void> updateOrderStatus(Long orderId, OrderStatusDto orderStatusDto) {
			ActionDto actionDto = mapper.convertOrderStatusDtoToActionDto(orderStatusDto);
			log.info("[START] call to service - updateOrderStatus - uri:{} - orderId:{} - body:{}",
					externalTrackerConfig.getDeliveryManagerProperties().getUpdateOrderStatus().getUpdateOrderStatusUrl(),
					orderId, actionDto);

			return WebClient
					.builder()
					.clientConnector(
							generateClientConnector(
									Integer.parseInt(externalTrackerConfig.getDeliveryManagerProperties().getUpdateOrderStatus().getUpdateOrderStatusConnectTimeout()),
									Long.parseLong(externalTrackerConfig.getDeliveryManagerProperties().getUpdateOrderStatus().getUpdateOrderStatusReadTimeout())
							)
					)
					.baseUrl(externalTrackerConfig.getDeliveryManagerProperties().getUpdateOrderStatus().getUpdateOrderStatusUrl())
					.build()
					.patch()
					.uri(builder ->
							builder
									.path("/{orderId}")
									.build(String.valueOf(orderId)))
					.bodyValue(actionDto)
					.exchange()
					.flatMap(clientResponse -> clientResponse.bodyToMono(Void.class)
					)
					.doOnSuccess(s -> log.info("[END] call to service -  updateOrderStatus "))
					.doOnError(e -> {
						e.printStackTrace();
						log.error("[ERROR] call to service -  updateOrderStatus ",e.getMessage());
					});	
	}

	@Override
	public void updateMotorizedStatus(String motorizedId, ExternalMotorizedStatusDto motorizedStatusDto) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Mono<Void> syncOrderStatus(SyncOrderDto order) {
		return bulkSyncOrderStatus(Collections.singletonList(order));
	}

	@Override
	public Mono<Void> bulkSyncOrderStatus(List<SyncOrderDto> orders) {
			List<ActionSyncDto> actions = mapper.convertSyncOrderDtoToSyncActionDto(orders);
			log.info("[START] call to service - bulkUpdateOrderStatus - uri:{} - body:{}",
					externalTrackerConfig.getDeliveryManagerProperties().getBulkSyncOrderStatus().getBulkSyncOrderStatusUrl(),
					actions);

			return WebClient
					.builder()
					.clientConnector(
							generateClientConnector(
									Integer.parseInt(externalTrackerConfig.getDeliveryManagerProperties().getBulkSyncOrderStatus().getBulkSyncOrderStatusConnectTimeout()),
									Long.parseLong(externalTrackerConfig.getDeliveryManagerProperties().getBulkSyncOrderStatus().getBulkSyncOrderStatusReadTimeout())
							)
					)
					.baseUrl(externalTrackerConfig.getDeliveryManagerProperties().getBulkSyncOrderStatus().getBulkSyncOrderStatusUrl())
					.build()
					.patch()
					.bodyValue(actions)
					.exchange()
					.flatMap(clientResponse -> clientResponse.bodyToMono(Void.class)
					)
					.doOnSuccess(s -> log.info("[END] call to service - bulkSyncOrderStatus"))
					.doOnError(e -> {
					e.printStackTrace();
					log.error("[ERROR] call to service - bulkSyncOrderStatus",e.getMessage());
				});		
	}


	protected ClientHttpConnector generateClientConnector(int connectionTimeOut, long readTimeOut) {
		log.info("generateClientConnector, connectionTimeOut:{}, readTimeOut:{}",connectionTimeOut,readTimeOut);
		HttpClient httpClient = HttpClient.create()
				.tcpConfiguration(tcpClient -> {
					tcpClient = tcpClient.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectionTimeOut);
					tcpClient = tcpClient.doOnConnected(conn -> conn
							.addHandlerLast(
									new ReadTimeoutHandler(readTimeOut, TimeUnit.MILLISECONDS))
					);
					return tcpClient;
				});



		return new ReactorClientHttpConnector(httpClient);

	}

}
