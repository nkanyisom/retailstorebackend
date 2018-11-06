package ca.mobilelive.data.repository;

import ca.mobilelive.data.entity.Invoice;
import org.springframework.data.repository.CrudRepository;

public interface InvoiceRepository extends CrudRepository<Invoice, Long> {

}
