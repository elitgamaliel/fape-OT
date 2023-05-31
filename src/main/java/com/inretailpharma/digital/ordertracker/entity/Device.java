package com.inretailpharma.digital.ordertracker.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class Device implements Serializable {

    @Id
    private String imei;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "phone_mark")
    private String phoneMark;
    @Column(name = "phone_model")
    private String phoneModel;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_user_id")
    private User user;
    @ElementCollection
    @CollectionTable(name = "device_history", joinColumns = @JoinColumn(name = "imei"))
    private List<DeviceHistory> deviceHistoryList;

    public Device(String imei, String phoneNumber, String phoneMark, String phoneModel) {
        this.imei = imei;
        this.phoneNumber = phoneNumber;
        this.phoneMark = phoneMark;
        this.phoneModel = phoneModel;
    }

}
