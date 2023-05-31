package com.inretailpharma.digital.ordertracker.entity;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.io.Serializable;

@Data
@MappedSuperclass
@DiscriminatorColumn(name = "type")
@SuppressWarnings("all")
public abstract class Status implements Serializable {

    @Id
    @Enumerated(EnumType.STRING)
    private Status.Code code;

    private String description;

    protected Status() {
    }

    protected Status(Status.Code code) {
        this.code = code;
    }

    public static enum Code {

        DISABLED("DESHABILITADO"), DELETED("ELIMINADO"), ENABLED("HABILITADO"),
        ASSIGNED, ARRIVED, DELIVERED,
        OFFLINE, ONLINE, ON_ROUTE, RETURNING, WAITING, IN_BREAK, ACCIDENT, PACKING,
        AVAILABLE, NOT_AVAILABLE,
        ONLY_TRACKING(ASSIGNED, ARRIVED, DELIVERED),
        SHIFT_TRCKING_ONLINE(ONLINE, RETURNING, PACKING, ON_ROUTE, WAITING);

        private String value = StringUtils.EMPTY;;
        private Status.Code[] child = new Status.Code[0];

        Code(String value) {
            this.value = value;
        }

        Code(Status.Code... child) {
            this.child = child;
        }

        public String value() {
            return value;
        }

        public Status.Code[] child() {
            return child;
        }

        public static Status.Code parse(String status) {
            for (Status.Code code : values()) {

                if (code.name().toString().equals(status)) {
                    return code;
                }
            }
            return null;
        }
    }
}
