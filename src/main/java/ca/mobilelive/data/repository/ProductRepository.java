package ca.mobilelive.data.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ca.mobilelive.data.entity.Product;

public interface ProductRepository extends CrudRepository<Product, Long> {

	public long count();

	public List<Product> findByBarCodeId(String barCodeId);

}
