package com.inretailpharma.digital.ordertracker.entity;

import com.inretailpharma.digital.ordertracker.utils.Constant;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.List;

@Data
@Entity
public class Shift implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String code;
    @Column(name="start_hour")
    private LocalTime startHour;
    @Column(name="end_hour")
    private LocalTime endHour;
    @Column(name="start_break_hour")
    private LocalTime startBreakHour;
    @Column(name="end_break_hour")
    private LocalTime endBreakHour;
    @Enumerated(EnumType.STRING)
    @Column(name="is_enabled")
    private Constant.Logical isEnabled = Constant.Logical.Y;
    @Column(name="amount_motorized")
    private Integer amountMotorized;
    @OneToMany
    @JoinColumn(name = "shift_id")
    private List<UserShift> userList;


}
