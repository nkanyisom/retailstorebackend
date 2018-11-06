package ca.mobilelive.controller;

import java.net.URI;

import ca.mobilelive.data.entity.Invoice;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import ca.mobilelive.controller.invoice.InvoiceUpdateInfo;
import ca.mobilelive.service.InvoiceService;
import ca.mobilelive.util.InvoiceStatus;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "onlinestore",description="Manage Invoices")
@RestController
public class InvoiceController {
	@Autowired
	private InvoiceService invoiceService;

	final Logger logger = LogManager.getLogger(getClass());

	@ApiOperation(produces = "application/json", value = "Creates an Invoice and returns an id.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Invoice details") })
	@RequestMapping(value = "/invoices", method = RequestMethod.POST)
	public ResponseEntity<Invoice> createInvoice() {
		Invoice invoice = invoiceService.createInvoice(new Invoice(0.0, 0, InvoiceStatus.IN_PROGRESS));
		// Set the location header for the newly created resource
		HttpHeaders responseHeaders = new HttpHeaders();
		URI newPollUri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(invoice.getId())
				.toUri();
		responseHeaders.setLocation(newPollUri);
		return new ResponseEntity<>(invoice, responseHeaders, HttpStatus.CREATED);
	}

	@ApiOperation(produces = "application/json", value = "Deletes Invoice")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Status of request"),
			@ApiResponse(code = 404, message = "Invoice does not exist") })
	@RequestMapping(value = "/invoices/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteInvoice(@PathVariable Long id) {
		invoiceService.deleteInvoice(id);
		return new ResponseEntity<>("{\"status\": \"success\"}", HttpStatus.OK);
	}

	@ApiOperation(produces = "application/json", value = "fetches all invoices from the database")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved list") })
	@RequestMapping(value = "/invoices", method = RequestMethod.GET)
	public ResponseEntity<Iterable<Invoice>> getAllInvoices() {
		return new ResponseEntity<>(invoiceService.getAllInvoices(), HttpStatus.OK);
	}

	@ApiOperation(produces = "application/json", value = "fetches a particular invoice details")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved Invoice details"),
			@ApiResponse(code = 404, message = "Invoice Not Found") })
	@RequestMapping(value = "/invoices/{id}", method = RequestMethod.GET)
	public ResponseEntity<Invoice> getInvoiceById(@PathVariable Long id) {
		return new ResponseEntity<>(invoiceService.getInvoiceById(id), HttpStatus.OK);
	}

	@ApiOperation(produces = "application/json", value = "Add or Remove products from the Invoice")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Invoice details"),
			@ApiResponse(code = 404, message = "Data validation error") })
	@RequestMapping(value = "/invoices/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Invoice> updateInvoice(@RequestBody InvoiceUpdateInfo invoiceUpdateInfo, @PathVariable Long id) {
		Invoice updated = invoiceService.updateInvoice(invoiceUpdateInfo, id);
		return new ResponseEntity<>(updated, HttpStatus.OK);
	}

}
