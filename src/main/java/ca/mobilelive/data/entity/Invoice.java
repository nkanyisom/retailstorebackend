package ca.mobilelive.data.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import ca.mobilelive.util.InvoiceStatus;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "INVOICE")
public class Invoice {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private int noOfItems;
	private double totalCost;

	private double totalTax;

	private double totalValue;

	@NotNull
	@Enumerated(EnumType.STRING)
	private InvoiceStatus invoiceStatus;

	@OneToMany(cascade=CascadeType.REMOVE,fetch = FetchType.EAGER)
	private List<LineItem> lineItems;

	public Invoice() {
		super();
	}

	public Invoice(double totalValue, int noOfItems, InvoiceStatus invoiceStatus) {
		super();
		this.totalValue = totalValue;
		this.noOfItems = noOfItems;
		this.invoiceStatus = invoiceStatus;
	}

	public Invoice(List<LineItem> lineItems) {
		super();
		this.lineItems = lineItems;
	}

}
