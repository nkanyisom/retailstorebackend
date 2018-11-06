package ca.mobilelive;

import java.util.ArrayList;
import java.util.List;

import ca.mobilelive.data.entity.Invoice;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import ca.mobilelive.controller.invoice.InvoiceUpdateInfo;
import ca.mobilelive.controller.invoice.ProductInfoForInvoice;
import ca.mobilelive.controller.product.ProductInfo;
import ca.mobilelive.service.InvoiceService;
import ca.mobilelive.service.ProductService;
import ca.mobilelive.util.InvoiceStatus;
import ca.mobilelive.util.ProductCategory;

@Component
public class SampleDataSetupRunner implements CommandLineRunner {

	@Autowired
	private InvoiceService invoiceService;

	final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	private ProductService productService;

	@Override
	public void run(String... arg0) throws Exception {
		logger.info("Inside Runner..");
		setUpProductData();
		setupInvoiceData();
		logger.info("Exiting Runner.. ");
	}

	public void setupInvoiceData() {

		// create a new Invoice to update information.
		Invoice o1 = invoiceService.createInvoice(new Invoice(0.0, 0, InvoiceStatus.IN_PROGRESS));

		Long invoiceId = o1.getId();
		InvoiceUpdateInfo invoiceUpdateInfo = new InvoiceUpdateInfo();
		List<ProductInfoForInvoice> productsToBeAdded = new ArrayList<>();
		List<ProductInfoForInvoice> productsToBeRemoved = new ArrayList<>();

		productsToBeAdded.add(new ProductInfoForInvoice("#prd1-0001", 2));
		productsToBeAdded.add(new ProductInfoForInvoice("#prd2-0002", 2));
		productsToBeAdded.add(new ProductInfoForInvoice("#prd3-0003", 2));
		productsToBeAdded.add(new ProductInfoForInvoice("#prd4-0004", 2));
		productsToBeAdded.add(new ProductInfoForInvoice("#prd5-0005", 2));
		invoiceUpdateInfo.setProductsToBeAdded(productsToBeAdded);
		invoiceUpdateInfo.setProductsToBeRemoved(productsToBeRemoved);
		invoiceUpdateInfo.setStatus(InvoiceStatus.RELEASED);

		logger.info("invoiceUpdateInfo = " + invoiceUpdateInfo);
		invoiceService.updateInvoice(invoiceUpdateInfo, invoiceId);
		Invoice retrieveUpdatedinvoice = invoiceService.getInvoiceById(o1.getId());
		logger.info("retrieveUpdatedinvoice = " + retrieveUpdatedinvoice.getNoOfItems() + "  value ="
				+ retrieveUpdatedinvoice.getTotalValue());

	}

	private void setUpProductData() {
		productService.createProduct(new ProductInfo("#prd1-0001", 25.00, "Tomato", ProductCategory.A));
		productService.createProduct(new ProductInfo("#prd2-0002", 10.00, "Onion", ProductCategory.B));
		productService.createProduct(new ProductInfo("#prd3-0003", 55.10, "Potato", ProductCategory.C));
		productService.createProduct(new ProductInfo("#prd4-0004", 12.99, "Bread", ProductCategory.A));
		productService.createProduct(new ProductInfo("#prd5-0005", 29.99, "Apples", ProductCategory.B));
		productService.createProduct(new ProductInfo("#prd6-0006", 45.00, "Banana", ProductCategory.C));
		productService.createProduct(new ProductInfo("#prd7-0007", 86.50, "Strawberry", ProductCategory.A));
		productService.createProduct(new ProductInfo("#prd8-0008", 39.45, "Apricot", ProductCategory.B));
		productService.createProduct(new ProductInfo("#prd9-0009", 119.99, "Raisins", ProductCategory.C));
		productService.createProduct(new ProductInfo("#prd10-0010", 249.90, "CashewNut", ProductCategory.A));
	}
}
