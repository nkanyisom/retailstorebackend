package ca.mobilelive.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import ca.mobilelive.data.entity.Invoice;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ca.mobilelive.controller.CustomException;
import ca.mobilelive.controller.invoice.InvoiceUpdateInfo;
import ca.mobilelive.controller.invoice.ProductInfoForInvoice;
import ca.mobilelive.util.InvoiceStatus;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InvoiceServiceTest {

	@Autowired
	private InvoiceService invoiceService;

	@Test(expected = CustomException.class)
	public void testInvoiceNotExist() {

		// create a new invoice to update information.
		Invoice o1 = invoiceService.createInvoice(new Invoice(0.0, 0, InvoiceStatus.IN_PROGRESS));
		InvoiceUpdateInfo invoiceupdateInfo = new InvoiceUpdateInfo();
		List<ProductInfoForInvoice> productsToBeAdded = new ArrayList<ProductInfoForInvoice>();
		List<ProductInfoForInvoice> productsToBeRemoved = new ArrayList<ProductInfoForInvoice>();

		productsToBeRemoved.add(new ProductInfoForInvoice("DDD-abc-0003", 2));
		invoiceupdateInfo.setProductsToBeAdded(productsToBeAdded);
		invoiceupdateInfo.setProductsToBeRemoved(productsToBeRemoved);
		invoiceupdateInfo.setStatus(InvoiceStatus.RELEASED);

		System.out.println("invoiceupdateInfo = " + invoiceupdateInfo);
		invoiceService.updateInvoice(invoiceupdateInfo, (long) 9999);
		Invoice retrieveUpdatedInvoice = invoiceService.getInvoiceById(o1.getId());
		System.out.println("retrieveUpdatedInvoice = " + retrieveUpdatedInvoice.getNoOfItems() + "  value ="
				+ retrieveUpdatedInvoice.getTotalValue() + "   list of items = " + retrieveUpdatedInvoice.getLineItems());

	}

	@Test
	public void testInvoiceUpdateAddAndRemoveProducts() {

		// create a new invoice to update information.
		Invoice o1 = invoiceService.createInvoice(new Invoice(0.0, 0, InvoiceStatus.IN_PROGRESS));

		Long invoiceId = o1.getId();
		InvoiceUpdateInfo invoiceupdateInfo = new InvoiceUpdateInfo();
		List<ProductInfoForInvoice> productsToBeAdded = new ArrayList<ProductInfoForInvoice>();
		List<ProductInfoForInvoice> productsToBeRemoved = new ArrayList<ProductInfoForInvoice>();

		productsToBeAdded.add(new ProductInfoForInvoice("#prd1-0001", 2));
		productsToBeAdded.add(new ProductInfoForInvoice("#prd2-0002", 2));
		productsToBeAdded.add(new ProductInfoForInvoice("#prd3-0003", 2));
		productsToBeAdded.add(new ProductInfoForInvoice("#prd4-0004", 2));
		productsToBeAdded.add(new ProductInfoForInvoice("#prd5-0005", 2));
		invoiceupdateInfo.setProductsToBeAdded(productsToBeAdded);

		productsToBeRemoved.add(new ProductInfoForInvoice("#prd5-0005", 2));
		invoiceupdateInfo.setProductsToBeRemoved(productsToBeRemoved);
		invoiceupdateInfo.setStatus(InvoiceStatus.RELEASED);

		System.out.println("invoiceupdateInfo = " + invoiceupdateInfo);
		invoiceService.updateInvoice(invoiceupdateInfo, invoiceId);
		Invoice retrieveUpdatedInvoice = invoiceService.getInvoiceById(o1.getId());
		System.out.println("retrieveUpdatedInvoice = " + retrieveUpdatedInvoice.getNoOfItems() + "  value ="
				+ retrieveUpdatedInvoice.getTotalValue() + "   list of items = " + retrieveUpdatedInvoice.getLineItems());

		assertThat(retrieveUpdatedInvoice.getNoOfItems()).isEqualTo(4); // 5
																		// products
																		// add
		assertThat(retrieveUpdatedInvoice.getTotalValue())
				.isEqualTo(25.00 * 2 * 1.1 + 10.00 * 2 * 1.2 + 55.10 * 2 * 1 + 12.99 * 2 * 1.1);
	}

	@Test
	public void testInvoiceUpdateAddMultipleProducts() {

		// create a new invoice to update information.
		Invoice o1 = invoiceService.createInvoice(new Invoice(0.0, 0, InvoiceStatus.IN_PROGRESS));

		Long invoiceId = o1.getId();
		InvoiceUpdateInfo invoiceupdateInfo = new InvoiceUpdateInfo();
		List<ProductInfoForInvoice> productsToBeAdded = new ArrayList<ProductInfoForInvoice>();
		List<ProductInfoForInvoice> productsToBeRemoved = new ArrayList<ProductInfoForInvoice>();

		productsToBeAdded.add(new ProductInfoForInvoice("#prd1-0001", 2));
		productsToBeAdded.add(new ProductInfoForInvoice("#prd2-0002", 2));
		productsToBeAdded.add(new ProductInfoForInvoice("#prd3-0003", 2));
		productsToBeAdded.add(new ProductInfoForInvoice("#prd4-0004", 2));
		productsToBeAdded.add(new ProductInfoForInvoice("#prd5-0005", 2));
		invoiceupdateInfo.setProductsToBeAdded(productsToBeAdded);
		invoiceupdateInfo.setProductsToBeRemoved(productsToBeRemoved);
		invoiceupdateInfo.setStatus(InvoiceStatus.RELEASED);

		System.out.println("invoiceupdateInfo = " + invoiceupdateInfo);
		invoiceService.updateInvoice(invoiceupdateInfo, invoiceId);
		Invoice retrieveUpdatedInvoice = invoiceService.getInvoiceById(o1.getId());
		System.out.println("retrieveUpdatedInvoice = " + retrieveUpdatedInvoice.getNoOfItems() + "  value ="
				+ retrieveUpdatedInvoice.getTotalValue());
		assertThat(retrieveUpdatedInvoice.getNoOfItems()).isEqualTo(5); // 5
																		// products
																		// add
		assertThat(retrieveUpdatedInvoice.getTotalValue())
				.isEqualTo(25.00 * 2 * 1.1 + 10.00 * 2 * 1.2 + 55.10 * 2 * 1 + 12.99 * 2 * 1.1 + 29.99 * 2 * 1.2);
	}

	@Test(expected = CustomException.class)
	public void testInvoiceUpdateAddNonExistingProduct() {

		// create a new invoice to update information.
		Invoice o1 = invoiceService.createInvoice(new Invoice(0.0, 0, InvoiceStatus.IN_PROGRESS));

		Long invoiceId = o1.getId();
		InvoiceUpdateInfo invoiceupdateInfo = new InvoiceUpdateInfo();
		List<ProductInfoForInvoice> productsToBeAdded = new ArrayList<ProductInfoForInvoice>();
		List<ProductInfoForInvoice> productsToBeRemoved = new ArrayList<ProductInfoForInvoice>();

		productsToBeAdded.add(new ProductInfoForInvoice("DDD-abc-0003", 2));
		invoiceupdateInfo.setProductsToBeAdded(productsToBeAdded);
		invoiceupdateInfo.setProductsToBeRemoved(productsToBeRemoved);
		invoiceupdateInfo.setStatus(InvoiceStatus.RELEASED);

		System.out.println("invoiceupdateInfo = " + invoiceupdateInfo);
		invoiceService.updateInvoice(invoiceupdateInfo, invoiceId);
		Invoice retrieveUpdatedInvoice = invoiceService.getInvoiceById(o1.getId());
		System.out.println("retrieveUpdatedInvoice = " + retrieveUpdatedInvoice.getNoOfItems() + "  value ="
				+ retrieveUpdatedInvoice.getTotalValue() + "   list of items = " + retrieveUpdatedInvoice.getLineItems());
		assertThat(retrieveUpdatedInvoice.getNoOfItems()).isEqualTo(1);
		assertThat(retrieveUpdatedInvoice.getTotalValue()).isEqualTo(55.10 * 2 * 1);
	}

	@Test
	public void testInvoiceUpdateAddSingleProductCatA() {
		// create a new Invoice to update information.
		Invoice o1 = invoiceService.createInvoice(new Invoice(0.0, 0, InvoiceStatus.IN_PROGRESS));

		Long invoiceId = o1.getId();
		InvoiceUpdateInfo invoiceupdateInfo = new InvoiceUpdateInfo();
		List<ProductInfoForInvoice> productsToBeAdded = new ArrayList<ProductInfoForInvoice>();
		List<ProductInfoForInvoice> productsToBeRemoved = new ArrayList<ProductInfoForInvoice>();

		productsToBeAdded.add(new ProductInfoForInvoice("#prd1-0001", 2));
		invoiceupdateInfo.setProductsToBeAdded(productsToBeAdded);
		invoiceupdateInfo.setProductsToBeRemoved(productsToBeRemoved);
		invoiceupdateInfo.setStatus(InvoiceStatus.RELEASED);

		System.out.println("invoiceupdateInfo = " + invoiceupdateInfo);
		invoiceService.updateInvoice(invoiceupdateInfo, invoiceId);
		Invoice retrieveUpdatedInvoice = invoiceService.getInvoiceById(o1.getId());
		System.out.println("retrieveUpdatedInvoice = " + retrieveUpdatedInvoice.getNoOfItems() + "  value ="
				+ retrieveUpdatedInvoice.getTotalValue());
		assertThat(retrieveUpdatedInvoice.getNoOfItems()).isEqualTo(1);
		assertThat(retrieveUpdatedInvoice.getTotalValue()).isEqualTo(25.00 * 2 * 1.1);
	}

	@Test
	public void testInvoiceUpdateAddSingleProductCatB() {

		// create a new invoice to update information.
		Invoice o1 = invoiceService.createInvoice(new Invoice(0.0, 0, InvoiceStatus.IN_PROGRESS));

		Long invoiceId = o1.getId();
		InvoiceUpdateInfo invoiceupdateInfo = new InvoiceUpdateInfo();
		List<ProductInfoForInvoice> productsToBeAdded = new ArrayList<ProductInfoForInvoice>();
		List<ProductInfoForInvoice> productsToBeRemoved = new ArrayList<ProductInfoForInvoice>();

		productsToBeAdded.add(new ProductInfoForInvoice("#prd2-0002", 2));
		invoiceupdateInfo.setProductsToBeAdded(productsToBeAdded);
		invoiceupdateInfo.setProductsToBeRemoved(productsToBeRemoved);
		invoiceupdateInfo.setStatus(InvoiceStatus.RELEASED);

		System.out.println("invoiceupdateInfo = " + invoiceupdateInfo);
		invoiceService.updateInvoice(invoiceupdateInfo, invoiceId);
		Invoice retrieveUpdatedInvoice = invoiceService.getInvoiceById(o1.getId());
		System.out.println("retrieveUpdatedInvoice = " + retrieveUpdatedInvoice.getNoOfItems() + "  value ="
				+ retrieveUpdatedInvoice.getTotalValue());
		assertThat(retrieveUpdatedInvoice.getNoOfItems()).isEqualTo(1);
		assertThat(retrieveUpdatedInvoice.getTotalValue()).isEqualTo(10.00 * 2 * 1.2);

	}

	@Test
	public void testInvoiceUpdateAddSingleProductCatC() {

		// create a new invoice to update information.
		Invoice o1 = invoiceService.createInvoice(new Invoice(0.0, 0, InvoiceStatus.IN_PROGRESS));

		Long invoiceId = o1.getId();
		InvoiceUpdateInfo invoiceupdateInfo = new InvoiceUpdateInfo();
		List<ProductInfoForInvoice> productsToBeAdded = new ArrayList<ProductInfoForInvoice>();
		List<ProductInfoForInvoice> productsToBeRemoved = new ArrayList<ProductInfoForInvoice>();

		productsToBeAdded.add(new ProductInfoForInvoice("#prd3-0003", 2));
		invoiceupdateInfo.setProductsToBeAdded(productsToBeAdded);
		invoiceupdateInfo.setProductsToBeRemoved(productsToBeRemoved);
		invoiceupdateInfo.setStatus(InvoiceStatus.RELEASED);

		System.out.println("invoiceupdateInfo = " + invoiceupdateInfo);
		invoiceService.updateInvoice(invoiceupdateInfo, invoiceId);
		Invoice retrieveUpdatedInvoice = invoiceService.getInvoiceById(o1.getId());
		System.out.println("retrieveUpdatedInvoice = " + retrieveUpdatedInvoice.getNoOfItems() + "  value ="
				+ retrieveUpdatedInvoice.getTotalValue());
		assertThat(retrieveUpdatedInvoice.getNoOfItems()).isEqualTo(1);
		assertThat(retrieveUpdatedInvoice.getTotalValue()).isEqualTo(55.10 * 2 * 1);
	}

	@Test(expected = CustomException.class)
	public void testInvoiceUpdateRemoveExistingProductFromEmptyInvoice() {

		// create a new invoice to update information.
		Invoice o1 = invoiceService.createInvoice(new Invoice(0.0, 0, InvoiceStatus.IN_PROGRESS));

		Long invoiceId = o1.getId();
		InvoiceUpdateInfo invoiceupdateInfo = new InvoiceUpdateInfo();
		List<ProductInfoForInvoice> productsToBeAdded = new ArrayList<ProductInfoForInvoice>();
		List<ProductInfoForInvoice> productsToBeRemoved = new ArrayList<ProductInfoForInvoice>();

		productsToBeRemoved.add(new ProductInfoForInvoice("#prd1-0001", 2));
		invoiceupdateInfo.setProductsToBeAdded(productsToBeAdded);
		invoiceupdateInfo.setProductsToBeRemoved(productsToBeRemoved);
		invoiceupdateInfo.setStatus(InvoiceStatus.RELEASED);

		System.out.println("invoiceupdateInfo = " + invoiceupdateInfo);
		invoiceService.updateInvoice(invoiceupdateInfo, invoiceId);
		Invoice retrieveUpdatedInvoice = invoiceService.getInvoiceById(o1.getId());
		System.out.println("retrieveUpdatedInvoice = " + retrieveUpdatedInvoice.getNoOfItems() + "  value ="
				+ retrieveUpdatedInvoice.getTotalValue() + "   list of items = " + retrieveUpdatedInvoice.getLineItems());

	}

	@Test(expected = CustomException.class)
	public void testInvoiceUpdateRemoveNonExistingProduct() {

		// create a new invoice to update information.
		Invoice o1 = invoiceService.createInvoice(new Invoice(0.0, 0, InvoiceStatus.IN_PROGRESS));

		Long invoiceId = o1.getId();
		InvoiceUpdateInfo invoiceupdateInfo = new InvoiceUpdateInfo();
		List<ProductInfoForInvoice> productsToBeAdded = new ArrayList<ProductInfoForInvoice>();
		List<ProductInfoForInvoice> productsToBeRemoved = new ArrayList<ProductInfoForInvoice>();

		productsToBeRemoved.add(new ProductInfoForInvoice("DDD-abc-0003", 2));
		invoiceupdateInfo.setProductsToBeAdded(productsToBeAdded);
		invoiceupdateInfo.setProductsToBeRemoved(productsToBeRemoved);
		invoiceupdateInfo.setStatus(InvoiceStatus.RELEASED);

		System.out.println("invoiceupdateInfo = " + invoiceupdateInfo);
		invoiceService.updateInvoice(invoiceupdateInfo, invoiceId);
		Invoice retrieveUpdatedInvoice = invoiceService.getInvoiceById(o1.getId());
		System.out.println("retrieveUpdatedInvoice = " + retrieveUpdatedInvoice.getNoOfItems() + "  value ="
				+ retrieveUpdatedInvoice.getTotalValue() + "   list of items = " + retrieveUpdatedInvoice.getLineItems());

	}

	@Test
	public void testCreateInvoice() {
		Invoice o1 = invoiceService.createInvoice(new Invoice(0.0, 0, InvoiceStatus.IN_PROGRESS));
		Invoice o2 = invoiceService.getInvoiceById(o1.getId());
		assertThat(o1.getId()).isEqualTo(o2.getId());
	}

}
