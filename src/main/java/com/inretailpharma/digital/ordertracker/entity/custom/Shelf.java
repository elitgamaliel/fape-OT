package com.inretailpharma.digital.ordertracker.entity.custom;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class Shelf implements Serializable {

	@Column(name = "lock_code")
    private String lockCode;
	@Column(name = "pack_code")
    private String packCode;
	@Column(name = "product_code")
    private String productCode;
    private Integer quantity;

}
