package ca.mobilelive.controller.invoice;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

import ca.mobilelive.util.InvoiceStatus;

@Getter
@Setter
public class InvoiceUpdateInfo {
	private List<ProductInfoForInvoice> productsToBeAdded;
	private List<ProductInfoForInvoice> productsToBeRemoved;

	@NotNull
	private InvoiceStatus status;

	public InvoiceUpdateInfo() {
		super();

	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
