package ca.mobilelive.data.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ca.mobilelive.data.entity.LineItem;

public interface LineItemRepository extends CrudRepository<LineItem, Long> {
	
	public List<LineItem> findByProductId(long prodId);

}
