package ca.mobilelive.controller.invoice;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Getter
@Setter
public class ProductInfoForInvoice {

	@NotNull
	private String barCodeId;

	private int quantity;

	public ProductInfoForInvoice() {
		super();
	}
	public ProductInfoForInvoice(String barCodeId, int quantity) {
		super();
		this.barCodeId = barCodeId;
		this.quantity = quantity;
	}


	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
