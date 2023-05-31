package com.inretailpharma.digital.ordertracker.utils;

import org.apache.commons.lang3.EnumUtils;

import com.inretailpharma.digital.ordertracker.entity.Role;
import com.inretailpharma.digital.ordertracker.exception.InvalidRequestException;
import com.inretailpharma.digital.ordertracker.firebase.util.FirebaseUtil;
import com.inretailpharma.digital.ordertracker.strategy.user.UpdateUser;
import com.inretailpharma.digital.ordertracker.strategy.user.ConfirmAttendance;
import com.inretailpharma.digital.ordertracker.strategy.user.ConfirmAttendanceManually;
import com.inretailpharma.digital.ordertracker.strategy.user.Packing;
import com.inretailpharma.digital.ordertracker.strategy.user.PickUpTravel;
import com.inretailpharma.digital.ordertracker.strategy.user.PickUp;
import com.inretailpharma.digital.ordertracker.strategy.user.Reception;
import com.inretailpharma.digital.ordertracker.strategy.user.LoadOrder;
import com.inretailpharma.digital.ordertracker.strategy.user.OnRoute;
import com.inretailpharma.digital.ordertracker.strategy.user.Arrived;
import com.inretailpharma.digital.ordertracker.strategy.user.Returning;
import com.inretailpharma.digital.ordertracker.strategy.user.Liquidating;

import java.util.Arrays;
import java.util.List;

public final class Constant {

    public static final Integer DEFAULT_LEAD_TIME = 60;
    public static final Integer EARTH_RADIUS = 6371000;
    public static final Integer DS_INKA_TRACKER = 3;
    public static final Integer DS_INKA_TRACKER_LITE = 4;
    public static final String APPLICATION_NAME = "OMNI_DELIVERY";
    public static final String APPLICATION_TARGET = "ORDER_TRACKER";
    public static final String BREAK_STATUS_ALERT = "M01";
    public static final Long MAX_TIME_IN_QUEUE = 5000L;
    public static final int POSITION_NOT_SETTED = 0;
    public static final String STATUS_SCHEDULED = "SCHEDULED";
    public static final String CODE_ERROR = "1";
    public static final String CODE_SUCCESS = "0";
    public static final String MESSAGE_ERROR = "error";
    public static final String MESSAGE_SUCCESS = "success";
    public static final Integer LOW_PRIORITY = 5;
    public static final Integer HIGH_PRIORITY = 1;
    public static final String MESSAGE_NO_DATA = "no data found";
    public static final String ROLE_MOTORIZED = "ROLE_MOTORIZED";
    public static final String INCOMPLETE_AVAILABILITY_IN_DAYS = "INCOMPLETE_AVAILABILITY_IN_DAYS";
    public static final List<String> INVALID_CLIENT_DOCUMENTS = Arrays.asList("0", "00000000");
    public static final String ORIGIN_UNIFIED_POS = "UNIFIED_POS";
    public static final String SOURCE_EXTERNAL_ROUTER = "EXT_ROUTER";
    public static final String SOURCE_DELIVERY_MANAGER = "DELIVERY_MANAGER";

    public static class Integers {

    	private Integers() {}

    	public static final Integer ZERO = 0;
    	public static final Integer ONE = 1;
    	public static final Integer TWO = 2;
    	public static final Integer FIFTEEN = 15;
    	public static final Integer SIXTEEN = 16;
    }

    public enum Logical {

        Y(true), N(false);

        private final boolean value;

        Logical(boolean value) {
            this.value = value;
        }

        public boolean value() {
            return value;
        }

        public static Logical parse(boolean online) {
            if (online) {
                return Y;
            }
            return N;
        }
    }

    public final class  Firebase {

    	private Firebase() {}

    	public static final String ORDER_STATUS_NAME_PATH_LISTENER = "orderStatus/statusName";
    	public static final String ORDER_STATUS_ORDER_PATH_LISTENER = "orderStatus";
    	public static final String GROUP_PATH_LISTENER = "group/name";
    	public static final String ADDRESS_PATH_LISTENER = "address";
    	public static final String MOTORIZED_PATH = "motorized";
    	public static final String ORDER_MOTORIZED_PATH_LISTENER = "motorizedId";
    	public static final String USER_STATUS_NAME_PATH_LISTENER = "status/statusName";
    	public static final String MOTORIZAD_LATLON_PATH_LISTENER = "latlon";
    	public static final String MOTORIZAD_TIMES_PATH_LISTENER = "times";
    	public static final String MOTORIZAD_LATITUDE_PATH_LISTENER = "latitude";
    	public static final String MOTORIZAD_ETARETURN_PATH_LISTENER = "times/etaReturn";
    	public static final String MOTORIZAD_LONGITUDE_PATH_LISTENER = "longitude";
    	public static final String FIREBASE_STATUS_PATH = "status";
    	public static final String FIREBASE_ALERT_PATH = "alerts";
    	public static final String FIREBASE_DEVICE_PATH = "device";
    	public static final String FIREBASE_GROUP_PATH = "group";
    	public static final String FIREBASE_DRUGSTORE_PATH = "drugstore";

    	public static final String FIREBASE_ETAP_PATH = "etap";

    	public static final String ORDER_SHELF_PATH = "shelf";

    	public static final String ORDER_SHELF_LOCK_CODE = "lockCode";
    	public static final String ORDER_SHELF_PACK_CODE = "packCode";

    	public static final String ORDER_SCHEDULED_PUSH_NOTIFICATION_STATUS = "pushNotificationStatus";
    	public static final String ORDER_PUSH_SEND_SCHEDULE_PUSH = "SEND_SCHEDULE_PUSH";
    }


    public final class MailTemplate {

    	private MailTemplate() {}

    	public static final String TH_START = "<th style=\"border: 1px solid black;padding: 5px;\" >";
    	public static final String TD_START_LEFT = "<td style=\"border: 1px solid black;padding: 5px;\" >";
    	public static final String TD_START_RIGHT = "<td style=\"border: 1px solid black;padding: 5px;text-align:right;\" >";
    	public static final String TD_START_CENTER = "<td style=\"border: 1px solid black;padding: 5px;text-align:center;\" >";

    	public static final String TD_WIDTH_IMAGE = "<td width=\"20%\">";
    	public static final String TD_WIDTH_NAME = "<td width=\"50%\" style=\"border-bottom: 1px solid #c1c2c9;\">";
    	public static final String TD_WIDTH_PRICE = "<td width=\"30%\" style=\"border-bottom: 1px solid #c1c2c9;\">";
    	public static final String IMG_PRODUCT = "<img width=\"180\" src=\"";
    	public static final String P_NAME = "<h4 style=\"margin-bottom: 5px;\"><b>";
    	public static final String P_PRESENTATION = "<h5 style=\"color: #a6a7b1; margin-top: 10px; font-weight: 500;\">";
    	public static final String P_PRICE = "<h3 style=\"color: #009540; font-weight: 500;\">S/ ";
    	public static final String X = "X";

    	public static final String MSG_SCHEDULED = "Entregaremos su pedido el ";
    	public static final String MSG_CONFIRMED = "Entregaremos su pedido entre ";

    	public static final String GIF_CAR = "\"https://s3-us-west-2.amazonaws.com/inkafarmaproductimages/email/carrito-listo.gif\"";
    	public static final String IMG_LOGO = "\"https://s3-us-west-2.amazonaws.com/inkafarmaproductimages/email/logo-01.png\"";
    	public static final String BTN_ORDER = "\"https://s3-us-west-2.amazonaws.com/inkafarmaproductimages/email/boton-01.png\"";
    	public static final String MAILTO = "\"mailto:ayuda@inkafarmadigital.pe?subject=[Confirmación%20Orden]\"";

    	public static final String DEFAULT_IMAGE = "https://s3-us-west-2.amazonaws.com/inkafarmaproductimages/newimages/imagen_default.png";
    }

    public enum PaymentMethodCode {

        NONE(null), CASH("CASH"), CARD("POS"), ONLINE_PAYMENT("3");

        private final String value;

        PaymentMethodCode(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static PaymentMethodCode getByValue(String value) {
            return EnumUtils.getEnumList(PaymentMethodCode.class)
                    .stream()
                    .filter(item -> value.equalsIgnoreCase(item.getValue()))
                    .findFirst()
                    .orElse(NONE);
        }
    }

    public enum DispatchStatus {
        OK("OK"), NOT_OK("NOT_OK");

        private String status;

        public String getStatus() {
            return status;
        }

        DispatchStatus(String status) {
            this.status = status;
        }
    }

    public enum MotorizedType {

        DELIVERY_CENTER(1, "Centro Distribución"), DRUGSTORE(2, "Tienda"), AUDIT(99, "Auditoria");

        private final Integer value;
        private final String description;

        MotorizedType(Integer value, String description) {
            this.value = value;
            this.description = description;
        }

        public Integer value() {
            return value;
        }

        public String description() {
            return description;
        }

        public static MotorizedType parseByName(String name) {
            for (MotorizedType type : MotorizedType.values()) {
                if (type.name().equals(name)) {
                    return type;
                }
            }
            throw new InvalidRequestException(Constant.Error.INVALID_MOTORIZED_TYPE);
        }

        public static MotorizedType parseByNameNV(String name) {
            for (MotorizedType type : MotorizedType.values()) {
                if (type.name().equals(name)) {
                    return type;
                }
            }
            return null;
        }
    }

    public enum ServiceType {

        INKATRACKER_LITE_RAD
        , INKATRACKER_RAD
    	, INKATRACKER_AM_PM
    	, INKATRACKER_EXP(HIGH_PRIORITY)
    	, INKATRACKER_PROG
    	, INKATRACKER_LITE_AM_PM
    	, INKATRACKER_LITE_EXP(HIGH_PRIORITY)
    	, INKATRACKER_LITE_PROG
    	, TEMPORARY_AM_PM
    	, TEMPORARY_EXP(HIGH_PRIORITY)
    	, TEMPORARY_PROG
    	, INKATRACKER_LITE_CALL_AM_PM
    	, INKATRACKER_LITE_CALL_EXP(HIGH_PRIORITY)
    	, INKATRACKER_LITE_CALL_PROG
    	, INKATRACKER_LITE_CALL_RAD
    	, INKATRACKER_CALL_AM_PM
    	, INKATRACKER_CALL_EXP(HIGH_PRIORITY)
    	, INKATRACKER_CALL_PROG
    	, INKATRACKER_CALL_RAD;

        private final Integer priority;

        ServiceType() {
            this.priority = LOW_PRIORITY;
        }

        ServiceType(Integer priority) {
            this.priority = priority;
        }

        public Integer getPriority() {
        	return priority;
        }

        public static ServiceType parseByName(String name) {
            for (ServiceType type : ServiceType.values()) {
                if (type.name().equals(name)) {
                    return type;
                }
            }
            throw new InvalidRequestException(Constant.Error.INVALID_SERVICE_TYPE);
        }
    }

    public static final String START_TIME_PERIOD_IN_MOTORIZED = "START_TIME_PERIOD_IN_MOTORIZED";

    public enum Application {

        INKA_MOTO("com.inkafarma.inkamoto", Role.Code.ROLE_MOTORIZED, FirebaseUtil.FIREBASE_MOTORIZED_PATH, "MOTO,WEB"),
        INKA_PICKER("com.inkafarma.inkapicker", Role.Code.ROLE_PICKER, FirebaseUtil.FIREBASE_PICKER_PATH, "PICKER"),
        INKA_SUPPLIER("com.inkafarma.insupply", Role.Code.ROLE_SUPPLIER, FirebaseUtil.FIREBASE_SUPPLIER_PATH, "SUPPLIER"),
        INKA_DISPATCHER("com.inkafarma.dispatcher", Role.Code.ROLE_DISPATCHER_LIQUIDATOR, FirebaseUtil.FIREBASE_DISPATCHER_PATH, "DISPATCHER"),
        INKA_TRACKER("com.inkafarma.inkatracker", Role.Code.ROLE_PICKER, null, "WEB"),
        INKA_ATM_LIQUIDADOR("com.inkafarma.dispatcher", Role.Code.ROLE_ATM_LIQUIDADOR, null, "WEB");

        private final String id;
        private final Role.Code role;
        private final String userPath;
        private final String alertFilter;

        Application(String id, Role.Code role, String userPath, String alertFilter) {
            this.id = id;
            this.role = role;
            this.userPath = userPath;
            this.alertFilter = alertFilter;
        }

        public static Application parse(String id) {
            for (Application application : Application.values()) {
                if (application.id.equals(id)) {
                    return application;
                }
            }
            return INKA_TRACKER;
        }

        public static Application parseByRole(Role.Code code) {
            for (Application application : Application.values()) {
                if (application.role.equals(code)) {
                    return application;
                }
            }
            return INKA_TRACKER;
        }

        public Role.Code role() {
            return role;
        }

        public String path() {
            return userPath;
        }

        public String alertFilter() {
            return alertFilter;
        }
    }

    public static final String DRUGSTORE_RADIUS = "DRUGSTORE_RADIUS";
    public static final String ORDER_DISTANCE_TO_MARK_ARRIVE = "ORDER_DISTANCE_TO_MARK_ARRIVE";
    public static final String DELIVERY_DISTANCE = "DELIVERY_DISTANCE";
    public static final String TRACKING_TIME = "TRACKING_TIME";
    public static final String TRACKING_DISTANCE = "TRACKING_DISTANCE";
    public static final String TRAVEL_SPEED = "TRAVEL_SPEED";
    public static final String CUSTOMER_DELAY_TIME = "CUSTOMER_DELAY_TIME";
    public static final String ROUTE_BUFFER = "ROUTE_BUFFER";
    public static final String ARRIVED_STATUS_VALIDATED = "ARRIVED_STATUS_VALIDATED";
    public static final String MARK_ARRIVE_VALIDATED = "MARK_ARRIVE_VALIDATED";
    public static final String STATUS_ALERT_TIME = "STATUS_ALERT_TIME";
    public static final String WARNING_PERCENTAGE_ETA = "WARNING_PERCENTAGE_ETA";
    public static final String CANCEL_ALARM_TIME = "CANCEL_ALARM_TIME";
    public static final String PICKER_SCAN_COMMAND = "PICKER_SCAN_COMMAND";
    public static final String PICKER_SCAN_TRAY = "PICKER_SCAN_TRAY";
    public static final String BREAK_STATUS_MESSAGE = "BREAK_STATUS_MESSAGE";
    public static final String BREAK_TIME = "BREAK_TIME";
    public static final String VERIFY_CELLPHONE_BY_SMS = "VERIFY_CELLPHONE_BY_SMS";
    public static final String SEARCH_ORDER_PAGE_SIZE = "SEARCH_ORDER_PAGE_SIZE";

    public final class Error {

    	private Error() {}

    	public static final String NO_HABILITADO = "No esta habilitado en el sistema";
    	public static final String NOT_FIND_DEVICE = "Could not find in device with imei";
    	public static final String NOT_PROFILE = "no tiene el perfil correcto";
    	public static final String ORDER_EXITS = "order already exists";
    	public static final String USER_CANNOT_LOGOUT = "El usuario tiene ordenes sin finalizar";
    	public static final String NOT_ORDER = "Order not found";
    	public static final String ORDER_CANCELLED = "order already cancelled";
    	public static final String SMS_ERROR = "Hubo un problema al enviar SMS";
    	public static final String DRUGSTORE_ERROR = "Error al obtener los locales";
    	public static final String INVALID_MOTORIZED_TYPE = "Tipo de motorizado invalido";
    	public static final String TOKEN_BLACK_LIST = "Token invalido por cierre de sesion.";
    	public static final String INVALID_SERVICE_TYPE = "Tipo de servicio invalido";
    	public static final String ASSIGN_ORDERS_ERROR = "Error al asignar ordenes";
    	public static final String CANCEL_TRAVEL_ERROR = "Error al cancelar el viaje";
    	public static final String UPDATE_STATUS_ERROR = "Error actualizar el estado";
    	public static final String ORDER_STATUS_ERROR = "Error en el estado de la Orden";
    	public static final String TRAVEL_EXISTS_ERROR = "El nombre de viaje ya existe";
    	public static final String ORDER_ERROR = "Error al obtener la órden";
        public static final String VERSION_ERROR = "Versión de dispositivo invalida";
    }

    public final class ErrorCode {

    	private ErrorCode() {}

    	public static final String DEFAULT = "1";
    	public static final String NOT_FIND_DEVICE = "2";
    	public static final String USER_CANNOT_LOGOUT = "3";
    	public static final String ORDER_EXITS = "4";
    	public static final String SMS_ERROR = "5";
    	public static final String TOKEN_BLACK_LIST = "6";
    	public static final String ORDER_STATUS_ERROR = "7";
    	public static final String ORDER_FINALIZED_ERROR = "8";
    	public static final String SAME_ORDER_STATUS_ERROR = "9";
    	public static final String USER_NO_REGISTERED = "10";
    }

    public final class Response {

    	private Response() {}

    	public static final String SUCCESS = "0";
    	public static final String ERROR = "1";
    }

    public static final String ERROR_NOT_DEFINED = "ERROR_NOT_DEFINED";
    public static final String ERROR_INSERT_TRACKER = "ERROR_INSERT_TRACKER";
    public static final String ERROR_INSERT_INKAVENTA = "ERROR_INSERT_INKAVENTA";
    public static final String ERROR_RESERVED_ORDER = "ERROR_RESERVED_ORDER";
    public static final String ERROR_PICKED = "ERROR_PICKED";
    public static final String ERROR_READY_FOR_PICKUP = "ERROR_READY_FOR_PICKUP";
    public static final String ERROR_ASSIGNED = "ERROR_ASSIGNED";
    public static final String ERROR_ON_ROUTED = "ERROR_ON_ROUTED";
    public static final String ERROR_ARRIVED = "ERROR_ARRIVED";
    public static final String ERROR_DELIVERED = "ERROR_DELIVERED";
    public static final String ERROR_CANCELLED = "ERROR_CANCELLED";
    public static final String CANCELLED_ORDER_NOT_ENOUGH_STOCK = "CANCELLED_ORDER_NOT_ENOUGH_STOCK";
    public static final String CANCELLED_ORDER_ONLINE_PAYMENT = "CANCELLED_ORDER_ONLINE_PAYMENT";
    public static final String CANCELLED_ORDER_ONLINE_PAYMENT_NOT_ENOUGH_STOCK = "CANCELLED_ORDER_ONLINE_PAYMENT_NOT_ENOUGH_STOCK";
    public static final String ERROR_UPDATE = "ERROR_UPDATE";
    public static final String ERROR_ORDER_CANCELLED_TRACKER = "ERROR_ORDER_CANCELLED_TRACKER";
    public static final String ERROR_INVOICED = "ERROR_INVOICED";
    public static final String ERROR_CHECKOUT = "ERROR_CHECKOUT";
    public static final String ERROR_PARTIAL_UPDATE = "ERROR_PARTIAL_UPDATE";
    public static final String REJECTED_ORDER = "REJECTED_ORDER";
    public static final String REJECTED_ORDER_ONLINE_PAYMENT = "REJECTED_ORDER_ONLINE_PAYMENT";
    public static final String READY_PICKUP_ORDER = "READY_PICKUP_ORDER";
    public static final String RELEASED_ORDER = "RELEASED_ORDER";
    public static final String CONFIRMED_ORDER = "CONFIRMED";
    public static final String CONFIRMED_TRACKER = "CONFIRMED_TRACKER";
    public static final String PICKED_ORDER = "PICKED_ORDER";
    public static final String PREPARED_ORDER = "PREPARED_ORDER";
    public static final String ON_ROUTED_ORDER = "ON_ROUTED_ORDER";
    public static final String ARRIVED_ORDER = "ARRIVED_ORDER";
    public static final String CHECKOUT_ORDER = "CHECKOUT_ORDER";
    public static final String ORDER_FAILED = "ORDER_FAILED";
    public static final String INVOICED = "INVOICED";
    public static final String PARTIAL_UPDATE_ORDER = "PARTIAL_UPDATE_ORDER";
    public static final String ASSIGNED = "ASSIGNED";
    public static final String SUCCESS_DELIVERY_SEND = "SUCCESS_DELIVERY_SEND";
    public static final String DELIVERED_ORDER = "DELIVERED_ORDER";
    public static final String CANCELLED_ORDER = "CANCELLED_ORDER";

    public enum UserAction {

    	NOT_AVAILABLE("NOT_AVAILABLE", UpdateUser.class)
        , AVAILABLE_QR("AVAILABLE", ConfirmAttendance.class)
        , AVAILABLE_MANUAL("AVAILABLE", ConfirmAttendanceManually.class)
        , PACKING("PACKING", Packing.class)
        , PICK_UP_TRAVEL("PICK_UP_TRAVEL", PickUpTravel.class)
        , PICK_UP("PICK_UP", PickUp.class)
        , RECEPTION("RECEPTION", Reception.class)
        , LOAD_ORDER("LOAD_ORDER", LoadOrder.class)
        , ON_ROUTE("ON_ROUTE", OnRoute.class)
        , ARRIVED("ARRIVED", Arrived.class)
        , RETURNING("RETURNING", Returning.class)
        , LIQUIDATING("LIQUIDATING", Liquidating.class)
        , NONE("", UpdateUser.class);

    	private String statusName;
    	private Class<?> implementationClass;

    	UserAction(String statusName, Class<?> implementationClass) {
    		this.statusName = statusName;
    		this.implementationClass = implementationClass;
    	}

    	public String getStatusName() {
    		return this.statusName;
    	}

    	public Class<?> getImplementationClass() {
    		return this.implementationClass;
    	}

    	public static UserAction getByName(String name) {

            return EnumUtils.getEnumList(UserAction.class).stream().filter(item -> item.name().equalsIgnoreCase(name))
                    .findFirst().orElse(NONE);
        }

    }

}
