package com.inretailpharma.digital.ordertracker.entity;

import lombok.Data;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Optional;

/**
 * @author Henry Gonzales Segovia
 * @version 9/11/2017
 */
@Data
@Entity
@Table(name = "status")
@DiscriminatorValue("TRACKING")
public class TrackingStatus extends Status {

    public TrackingStatus() {
    }

    public TrackingStatus(Code code) {
        super(code);
    }

    public static Optional<Code> parseStatusCode(String name) {
        for (Code code : Code.values()) {
            if (code.name().equals(name)) {
                return Optional.of(code);
            }
        }
        return Optional.empty();
    }
}
