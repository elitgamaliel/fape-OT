package com.inretailpharma.digital.ordertracker.dto.external;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActionSyncDto {

	private Long ecommerceId;
    private String origin;
    private List<ActionHistoryDto> history;
}
