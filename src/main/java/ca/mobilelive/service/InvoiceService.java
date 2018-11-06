package ca.mobilelive.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ca.mobilelive.data.entity.Invoice;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.mobilelive.data.entity.LineItem;
import ca.mobilelive.data.entity.Product;
import ca.mobilelive.data.repository.LineItemRepository;
import ca.mobilelive.data.repository.InvoiceRepository;
import ca.mobilelive.data.repository.ProductRepository;
import ca.mobilelive.controller.CustomException;
import ca.mobilelive.controller.invoice.InvoiceUpdateInfo;
import ca.mobilelive.controller.invoice.ProductInfoForInvoice;
import ca.mobilelive.util.ProductCategory;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepo;

    @Autowired
    private LineItemRepository lineItemRepo;

    final Logger logger = LogManager.getLogger(getClass());

    @Autowired
    private ProductRepository productRepo;

    private Invoice addProductToInvoice(Long invoiceId, String barCodeId, int quantity) {
        Invoice o1 = invoiceRepo.findOne(invoiceId);
        Product selectedProduct1 = verifyIfProductExists(barCodeId);

        LineItem l1 = new LineItem(selectedProduct1, quantity);
        lineItemRepo.save(l1);

        List<LineItem> currentLineItems = o1.getLineItems();
        if (currentLineItems != null) {
            LineItem existingLi = getLineItemWithBarCodeId(barCodeId, currentLineItems);
            if (existingLi == null) {
                o1.getLineItems().add(l1);
            } else {
                long newQty = existingLi.getQuantity() + quantity;
                existingLi.setQuantity(newQty);
            }

        } else {
            currentLineItems = new ArrayList<>();
            currentLineItems.add(l1);
            o1.setLineItems(currentLineItems);
        }
        invoiceRepo.save(o1);
        return o1;
    }

    private void computeTotalValues(Invoice invoice) {

        int noOfItems = 0;
        double totalValue = 0;
        double totalCost = 0;

        if (null != invoice.getLineItems()) {
            List<LineItem> lineItems = invoice.getLineItems();
            Iterator<LineItem> lineItemsIter = lineItems.iterator();
            while (lineItemsIter.hasNext()) {
                LineItem li = lineItemsIter.next();
                double saleValue = computeValueForItem(li.getQuantity(), li.getProduct().getProductCategory(),
                        li.getProduct().getRate());
                totalValue += saleValue;
                totalCost += li.getQuantity() * li.getProduct().getRate();
                noOfItems++;
            }
        }
        invoice.setNoOfItems(noOfItems);
        invoice.setTotalValue(totalValue);
        invoice.setTotalCost(totalCost);
        invoice.setTotalTax(totalValue - totalCost);
        invoiceRepo.save(invoice);
    }

    private double computeValueForItem(long quantity, ProductCategory productCategory, double rate) {
        double saleValue = 0;
        if (productCategory.equals(ProductCategory.A)) {
            saleValue = quantity * rate * 1.1; // 10% levy

        } else if (productCategory.equals(ProductCategory.B)) {
            saleValue = quantity * rate * 1.2; // 10% levy

        } else if (productCategory.equals(ProductCategory.C)) {
            saleValue = quantity * rate;
        }
        return saleValue;
    }

    public Invoice createInvoice(Invoice invoice) {
        Invoice invoice1 = invoiceRepo.save(invoice);
        return invoice1;

    }

    public void deleteInvoice(Long id) {
        verifyInvoiceExists(id);
        invoiceRepo.delete(id);
    }

    public Iterable<Invoice> getAllInvoices() {
        Iterable<Invoice> invoice = invoiceRepo.findAll();
        return invoice;
    }

    public Invoice getInvoiceById(Long id) {
        verifyInvoiceExists(id);
        return invoiceRepo.findOne(id);
    }

    private LineItem getLineItemWithBarCodeId(String barCodeId, List<LineItem> currentLineItems) {
        for (int i = 0; i < currentLineItems.size(); i++) {
            LineItem li = currentLineItems.get(i);
            if (barCodeId.equals(li.getProduct().getBarCodeId()))
                return li;
        }
        return null;
    }

    private Invoice removeProductFromInvoice(Long invoiceId, String barCodeId) {
        Invoice o1 = invoiceRepo.findOne(invoiceId);
        List<LineItem> currentLineItems = o1.getLineItems();

        verifyIfProductExists(barCodeId);

        if (currentLineItems != null && !currentLineItems.isEmpty()) {
            LineItem lineItem = getLineItemWithBarCodeId(barCodeId, currentLineItems);

            if (null == lineItem)
                throw new CustomException(
                        "Problem with input data: Product does not exist in current list of products. Cannot remove product with BarCode ID "
                                + barCodeId);

            currentLineItems.remove(lineItem);
            o1.setLineItems(currentLineItems);
            invoiceRepo.save(o1);
        } else {
            throw new CustomException(
                    "Problem with input data: There are no line items currently in the Invoice. Cannot remove product with BarCode ID "
                            + barCodeId);
        }
        invoiceRepo.save(o1);
        return o1;
    }

    public Invoice updateInvoice(InvoiceUpdateInfo invoiceUpdateInfo, Long invoiceId) {

        if (null == invoiceUpdateInfo) {
            throw new CustomException("There is no information to be updated for id " + invoiceId);
        }
        verifyInvoiceExists(invoiceId);

        if (null != invoiceUpdateInfo.getProductsToBeAdded()) {
            List<ProductInfoForInvoice> prodToBeAdded = invoiceUpdateInfo.getProductsToBeAdded();
            Iterator<ProductInfoForInvoice> prodToBeAddedIter = prodToBeAdded.iterator();
            while (prodToBeAddedIter.hasNext()) {
                ProductInfoForInvoice pInfo = prodToBeAddedIter.next();
                addProductToInvoice(invoiceId, pInfo.getBarCodeId(), pInfo.getQuantity());
            }
        }

        if (null != invoiceUpdateInfo.getProductsToBeRemoved()) {
            List<ProductInfoForInvoice> prodToBeRemoved = invoiceUpdateInfo.getProductsToBeRemoved();
            Iterator<ProductInfoForInvoice> prodToBeRemovedIter = prodToBeRemoved.iterator();
            while (prodToBeRemovedIter.hasNext()) {
                ProductInfoForInvoice pInfo = prodToBeRemovedIter.next();
                removeProductFromInvoice(invoiceId, pInfo.getBarCodeId());
            }
        }

        Invoice invoice = invoiceRepo.findOne(invoiceId);
        invoice.setInvoiceStatus(invoiceUpdateInfo.getStatus());
        computeTotalValues(invoice);
        return invoice;
    }

    private void verifyInvoiceExists(Long id) {
        Invoice invoice = invoiceRepo.findOne(id);
        if (invoice == null)
            throw new CustomException("Invoice with id " + id + " not found");
    }

    private Product verifyIfProductExists(String barCodeId) {
        List<Product> productsByBarCodeID = productRepo.findByBarCodeId(barCodeId);
        if (null == productsByBarCodeID || productsByBarCodeID.isEmpty())
            throw new CustomException(
                    "Problem with input data: BarCode ID " + barCodeId + " does not exist in Product Master");
        else //debug purposes
            logger.debug("selectedProduct1  = " + productsByBarCodeID.get(0).getId());

        return productsByBarCodeID.get(0);
    }

}
