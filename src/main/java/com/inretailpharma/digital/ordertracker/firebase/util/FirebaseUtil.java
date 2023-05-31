package com.inretailpharma.digital.ordertracker.firebase.util;

import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.google.firebase.database.core.Path;


public final class FirebaseUtil {

    public static final String FIREBASE_ORDER_PATH = "orders";
    public static final String FIREBASE_MOTORIZED_PATH = "motorized";
    public static final String FIREBASE_PICKER_PATH = "picker";
    public static final String FIREBASE_SUPPLIER_PATH = "supplier";
    public static final String FIREBASE_SETTING_PATH = "settings";
    public static final String FIREBASE_DISPATCHER_PATH = "dispatcher";

    private static final String PATTERN_FOR_ORDER_KEY = "(orders/)([a-zA-Z0-9]+)";
    private static final String PATTERN_FOR_MOTORIZED_KEY = "(motorized/)([a-zA-Z0-9]+)";
    private static final String PATTERN_FOR_PICKER_KEY = "(picker/)([a-zA-Z0-9]+)";
    private static final String PATTERN_FOR_SUPPLIER_KEY = "(supplier/)([a-zA-Z0-9]+)";
    private static final String PATTERN_FOR_DISPATCHER_KEY = "(dispatcher/)([a-zA-Z0-9]+)";


    private static final String PATH_SEPARATOR = "/";
    
    public final class Orders {
    	
    	private Orders() {
    		
    	}
    	
    	public static final String ORDER_STATUS_PATH = "orderStatus";
    	public static final String ORDER_STATUS_DRUGSTORE_PATH = "statusDrugstore";
    	public static final String ORDER_FINALIZED_PATH = "finalized";
    	public static final String ORDER_FINALIZED_TRACKER_PATH = "tracker";
    	public static final String ORDER_ALERTS_PATH = "alerts";
    	public static final String ORDER_GROUP_PATH = "group";
    	public static final String ORDER_DELIVERY_ID_PATH = "inkaDeliveryId";
    	public static final String ORDER_MOTORIZED_ID_PATH = "motorizedId";
    	public static final String ORDER_TIMES_PATH = "times";
    	public static final String ORDER_SCHEDULED_PATH = "scheduled";
        
    }

    public static Optional<Long> findOrderExternalId(Path path) {
        Optional<String> optionalKey = findPrimaryKey(path, PATTERN_FOR_ORDER_KEY);
        return optionalKey.map(Long::valueOf);
    }

    public static Optional<String> findOrderTrackingCode(Path path) {
        return findPrimaryKey(path, PATTERN_FOR_ORDER_KEY);
    }

    public static Optional<String> findMotorizedId(Path path) {
        return findPrimaryKey(path, PATTERN_FOR_MOTORIZED_KEY);
    }

    public static Optional<String> findPickerId(Path path) {
        return findPrimaryKey(path, PATTERN_FOR_PICKER_KEY);
    }

    public static Optional<String> findSupplierId(Path path) {
        return findPrimaryKey(path, PATTERN_FOR_SUPPLIER_KEY);
    }

    public static Optional<String> findDispatcherId(Path path) {
        return findPrimaryKey(path, PATTERN_FOR_DISPATCHER_KEY);
    }

    private static Optional<String> findPrimaryKey(Path path, String template) {
        Pattern pattern = Pattern.compile(template);
        Matcher matcher = pattern.matcher(path.toString());
        if ((matcher.find()) 
            && (StringUtils.isNotBlank(matcher.group(2)))) {
                return Optional.of(matcher.group(2));
            }
        
        return Optional.empty();
    }

    public static String createPath(String basePath, Object... paths) {
        if (paths == null) {
            return basePath;
        }
        StringBuilder builder = new StringBuilder(basePath);
        Arrays.asList(paths).forEach(path -> builder.append(PATH_SEPARATOR).append(path.toString()));
        return builder.toString();
    }
}
