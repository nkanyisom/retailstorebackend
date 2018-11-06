package ca.mobilelive;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.junit4.SpringRunner;

import ca.mobilelive.controller.invoice.InvoiceUpdateInfo;
import ca.mobilelive.controller.invoice.ProductInfoForInvoice;
import ca.mobilelive.util.InvoiceStatus;

@RunWith(SpringRunner.class)
@JsonTest
public class TestInvoiceUpdate {

	@Autowired
	private JacksonTester<InvoiceUpdateInfo> json;

	public InvoiceUpdateInfo createTestBean() {
		InvoiceUpdateInfo updateInfo = new InvoiceUpdateInfo();
		updateInfo.setStatus(InvoiceStatus.IN_PROGRESS);
		List<ProductInfoForInvoice> productsToBeAdded = new ArrayList<ProductInfoForInvoice>();
		productsToBeAdded.add(new ProductInfoForInvoice("ABC-abc-1234", 20));
		productsToBeAdded.add(new ProductInfoForInvoice("ABC-abc-2234", 30));
		productsToBeAdded.add(new ProductInfoForInvoice("ABC-abc-3234", 10));

		List<ProductInfoForInvoice> productsToBeRemoved = new ArrayList<ProductInfoForInvoice>();
		productsToBeRemoved.add(new ProductInfoForInvoice("ABC-abc-1235", 2));
		productsToBeRemoved.add(new ProductInfoForInvoice("ABC-abc-1236", 3));
		productsToBeRemoved.add(new ProductInfoForInvoice("ABC-abc-1237", 1));

		updateInfo.setProductsToBeAdded(productsToBeAdded);
		updateInfo.setProductsToBeRemoved(productsToBeRemoved);
		return updateInfo;
	}

	@Test
	public void testSerialize() throws Exception {

		InvoiceUpdateInfo updateInfo = createTestBean();

		assertThat(this.json.write(updateInfo)).isEqualToJson("expected.json");

		System.out.println(this.json.write(updateInfo).toString());
	}

}
