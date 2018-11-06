package ca.mobilelive.controller;

import java.net.URI;

import javax.validation.Valid;

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

import ca.mobilelive.data.entity.Product;
import ca.mobilelive.controller.product.ProductInfo;
import ca.mobilelive.service.ProductService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "onlinestore",description="Manage Products")
public class ProductController {

	final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	private ProductService productService;

	@ApiOperation(value = "Create a new Product", response = String.class, produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "id <id> created"),
			@ApiResponse(code = 401, message = "Bad Credentials") })
	@RequestMapping(value = "/products", method = RequestMethod.POST)
	public ResponseEntity<Product> createProduct(
			@ApiParam(value = "Data for the new product", required = true) @Valid @RequestBody ProductInfo productInfo) {
		Product product = productService.createProduct(productInfo);

		HttpHeaders responseHeaders = new HttpHeaders();
		URI newPollUri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(product.getId())
				.toUri();
		responseHeaders.setLocation(newPollUri);
		return new ResponseEntity<>(product, responseHeaders, HttpStatus.CREATED);
	}

	@ApiOperation(value = "Delete existing Product", response = String.class, produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "id <id> deleted"),
			@ApiResponse(code = 401, message = "Bad Credentials") })
	@RequestMapping(value = "/products/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
		productService.deleteProduct(id);
		return new ResponseEntity<>("{\"status\": \"success\"}", HttpStatus.OK);
	}

	@ApiOperation(value = "View list of available products", response = Iterable.class, produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved list"),
			@ApiResponse(code = 401, message = "Bad Credentials") })
	@RequestMapping(value = "/products", method = RequestMethod.GET)
	public ResponseEntity<Iterable<Product>> getAllProducts() {
		return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
	}

	@ApiOperation(value = "View a specific product", response = Product.class, produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved product details"),
			@ApiResponse(code = 401, message = "Bad Credentials"),
			@ApiResponse(code = 404, message = "Produt does not exist") })
	@RequestMapping(value = "/products/{id}", method = RequestMethod.GET)
	public ResponseEntity<Product> getProductById(
			@ApiParam(value = "id of a particular product", required = true) @PathVariable Long id) {
		return new ResponseEntity<>(productService.getProductById(id), HttpStatus.OK);
	}

	@ApiOperation(value = "Update existing Product", response = String.class, produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "id <id> updated"),
			@ApiResponse(code = 401, message = "Bad Credentials") })

	@RequestMapping(value = "/products/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Product> updateProduct(@Valid @RequestBody ProductInfo productInfo, @PathVariable Long id) {
		Product prod = productService.updateProduct(productInfo, id);
		logger.info("updated product id = " + prod.getId());
		return new ResponseEntity<>(prod, HttpStatus.OK);
	}

}
