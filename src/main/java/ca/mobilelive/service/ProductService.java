package ca.mobilelive.service;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.mobilelive.data.entity.LineItem;
import ca.mobilelive.data.entity.Product;
import ca.mobilelive.data.repository.LineItemRepository;
import ca.mobilelive.data.repository.ProductRepository;
import ca.mobilelive.controller.CustomException;
import ca.mobilelive.controller.product.ProductInfo;

@Service
public class ProductService {

    final Logger logger = LogManager.getLogger(getClass());

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private LineItemRepository lineItemRepo;

    public Product createProduct(ProductInfo productInfo) {
        verifyIfProductExists(productInfo.getBarCodeId());
        Product product = new Product();
        product.setBarCodeId(productInfo.getBarCodeId());
        product.setName(productInfo.getName());
        product.setProductCategory(productInfo.getProductCategory());
        product.setRate(productInfo.getRate());

        product = productRepo.save(product);
        return product;

    }

    public void deleteProduct(Long id) {
        verifyProductExists(id);
        verifyLineItemExists(id);
        productRepo.delete(id);
    }

    private void verifyLineItemExists(Long id) {
        List<LineItem> lineItems = lineItemRepo.findByProductId(id);
        if (null != lineItems && !lineItems.isEmpty())
            throw new CustomException("Product -  " + id + " is associated with invoices. Cannot be deleted.");

    }


    public Iterable<Product> getAllProducts() {
        Iterable<Product> products = productRepo.findAll();
        return products;
    }

    public Product getProductById(Long id) {
        verifyProductExists(id);
        return productRepo.findOne(id);
    }

    public Product updateProduct(ProductInfo productInfo, Long id) {
        verifyProductExists(id);
        Product product = productRepo.findOne(id);
        product.setBarCodeId(productInfo.getBarCodeId());
        product.setName(productInfo.getName());
        product.setProductCategory(productInfo.getProductCategory());
        product.setRate(productInfo.getRate());
        Product p = productRepo.save(product);
        return p;
    }

    private void verifyIfProductExists(String barCodeId) {
        List<Product> productsByBarCodeID = productRepo.findByBarCodeId(barCodeId);
        if (null != productsByBarCodeID && !productsByBarCodeID.isEmpty())
            throw new CustomException(
                    "Problem with input data: BarCode ID  " + barCodeId + " already exists in Product Master");

    }

    private void verifyProductExists(Long id) {
        Product product = productRepo.findOne(id);
        if (product == null)
            throw new CustomException("Product with id " + id + " not found");

    }

}
