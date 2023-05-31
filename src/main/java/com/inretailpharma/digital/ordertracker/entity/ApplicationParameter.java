package com.inretailpharma.digital.ordertracker.entity;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.springframework.util.StringUtils;

import com.inretailpharma.digital.ordertracker.utils.Constant;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "Informacion de Parametros", description = "Parametros")
@Entity
@SuppressWarnings("all")
@Table(name = "application_parameter")
public class ApplicationParameter implements Serializable {

    @Transient
    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @Id
    private String code;
    private String description;
    private String value;
    private Integer enabled;
    @Version
    private Integer version;

    public Integer getIntValue() {
        return Integer.valueOf(value);
    }

    public Long getLongValue() {
        return Long.valueOf(value);
    }

    public Date getDateValue() {
        try {
            return dateFormat.parse(value);
        } catch (ParseException e) {
            return null;
        }
    }

    public Constant.Logical getLogicalValue() {
        return Constant.Logical.valueOf(value);
    }

    public Collection<String> getStringCollectionValue() {
        return Arrays.asList(StringUtils.tokenizeToStringArray(value, ","));
    }

    public Collection<Integer> getIntegerCollectionValue() {
        return Arrays.stream(StringUtils.tokenizeToStringArray(value, ",")).map(Integer::parseInt).collect(Collectors.toList());
    }

    public Collection<Long> getLongCollectionValue() {
        return Arrays.stream(StringUtils.tokenizeToStringArray(value, ",")).map(Long::parseLong).collect(Collectors.toList());
    }

}
