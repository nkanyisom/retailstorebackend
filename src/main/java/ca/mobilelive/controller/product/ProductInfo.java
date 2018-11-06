package ca.mobilelive.controller.product;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

import ca.mobilelive.util.ProductCategory;

@Getter
@Setter
public class ProductInfo {

	@NotNull
	private String barCodeId;

	@NotNull
	private String name;

	@NotNull
	@Enumerated(EnumType.STRING)
	private ProductCategory productCategory;

	@NotNull
	@DecimalMin(value = "0.1")
	private double rate;

	public ProductInfo() {
		super();
	}

	public ProductInfo(String barCodeId, double rate, String name, ProductCategory productCategory) {
		super();
		this.barCodeId = barCodeId;
		this.name = name;
		this.productCategory = productCategory;
		this.rate = rate;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
