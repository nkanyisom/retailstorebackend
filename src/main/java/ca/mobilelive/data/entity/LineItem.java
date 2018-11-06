package ca.mobilelive.data.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
@Getter
@Setter
@Table(name = "LINE_ITEM")
public class LineItem {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@OneToOne(fetch = FetchType.EAGER)
	private Product product;

	private long quantity;

	public LineItem() {
		super();
	}

	public LineItem(Product product, int quantity) {
		super();
		this.product = product;
		this.quantity = quantity;
	}


}
