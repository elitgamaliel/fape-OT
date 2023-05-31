package com.inretailpharma.digital.ordertracker.entity.custom;

import com.inretailpharma.digital.ordertracker.entity.Shift;
import com.inretailpharma.digital.ordertracker.entity.User;
import lombok.Data;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Data
@Embeddable
public class UserShiftId implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shift_id")
    private Shift shift;

    public UserShiftId() {
    }

    public UserShiftId(User user, Shift shift) {
        this.user = user;
        this.shift = shift;
    }

    public UserShiftId(String userId, Integer shiftId) {
        this.user = new User();
        this.user.setId(userId);

        this.shift = new Shift();
        this.shift.setId(shiftId);
    }

    @Override
    public String toString() {
        return "UserShiftId{" +
                "shift=" + shift +
                '}';
    }
}
