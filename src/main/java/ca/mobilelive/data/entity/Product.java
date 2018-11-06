package ca.mobilelive.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

import ca.mobilelive.util.ProductCategory;

@Getter
@Setter
@Entity
@Table(name = "PRODUCT_MASTER")

public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@NotNull
	@Column(unique = true)
	private String barCodeId;

	@NotNull
	private String name;

	@NotNull
	@Enumerated(EnumType.STRING)
	private ProductCategory productCategory;

	@NotNull
	private double rate;

	public Product() {
		super();
	}

	public Product(String barCodeId, double rate, String name, ProductCategory productCategory) {
		super();
		this.barCodeId = barCodeId;
		this.rate = rate;
		this.name = name;
		this.productCategory = productCategory;
	}

}
