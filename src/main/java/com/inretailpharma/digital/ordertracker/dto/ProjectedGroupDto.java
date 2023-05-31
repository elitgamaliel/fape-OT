package com.inretailpharma.digital.ordertracker.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.inretailpharma.digital.ordertracker.utils.DateUtil;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectedGroupDto implements Serializable {

    private String groupName;
    private String motorizedId;
    private String motorizedEmail;
    private Integer projectedEtaReturn;
    private List<GroupDto> group;
    private PickUpCenterDto pickUpCenter;
    private String source;
    private String updateBy;

    public ProjectedGroupDto(Integer projectedEtaReturn, List<GroupDto> group) {
        this.projectedEtaReturn = projectedEtaReturn;
        this.group = group;
    }
    
    public ProjectedGroupDto(Integer projectedEtaReturn, List<GroupDto> group, PickUpCenterDto pickUpCenter) {
        this.projectedEtaReturn = projectedEtaReturn;
        this.group = group;
        this.pickUpCenter = pickUpCenter;
        this.groupName = "G: " + DateUtil.currentDate().getTime();
    }

}
