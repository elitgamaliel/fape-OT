package com.inretailpharma.digital.ordertracker.entity;

import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Entity
@Table(name = "reason")
public class Reason {
    @Id
    @NotNull
    @Column(name = "reason_id")
    private String reasonId;

    @NotNull
    @Column(name = "reason_description")
    private String reasonDescription;

    @Column(name = "sequence")
    private Integer sequence;

    @NotNull
    @Column(name = "created_at")
    private Date createdAt;
}
