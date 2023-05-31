package com.inretailpharma.digital.ordertracker.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Henry Gonzales Segovia
 * @version 1/09/2017
 */
@Data
@Entity
@NoArgsConstructor
public class Role implements Serializable {

    @Id
    @Enumerated(EnumType.STRING)
    private Code code;
    private String description;

    public Role(Code code) {
        this.code = code;
    }
    
    public enum Code {

        ROLE_MOTORIZED("MOTORIZADO", null, null),
        ROLE_ADMINISTRATOR("ADMINISTRADOR", "administratorOrderService", "administratorUserService"),
        ROLE_COORDINATOR("COORDINADOR", "coordinatorOrderService", "administratorUserService"),
        ROLE_DISPATCHER("DESPACHADOR", "dispatcherOrderService", "dispatcherUserService"),
        ROLE_CUSTOMER_SUPPORT("ATENCION AL CLIENTE", "dispatcherOrderService", null),
        ROLE_LIQUIDATOR("LIQUIDADOR", "liquidatorOrderService", null),
        ROLE_PICKER("PICKEADOR", null, null),
        ROLE_SUPPLIER("SUPPLIER", null, null),
        ROLE_DISPATCHER_LIQUIDATOR("DISPATCHER_LIQUIDATOR",null,null),
        ROLE_ATM_LIQUIDADOR("ATM_LIQUIDADOR",null,null);

        private final String value;
        private final String orderService;
        private final String userService;

        Code(String value, String orderService, String userService) {
            this.value = value;
            this.orderService = orderService;
            this.userService = userService;
        }

        public String value() {
            return value;
        }

        public String orderService() {
            return orderService;
        }

        public String userService() {
            return userService;
        }
    }

}