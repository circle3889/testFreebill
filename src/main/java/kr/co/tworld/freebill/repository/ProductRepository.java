package kr.co.tworld.freebill.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {
	List<Product> findByProdId(String prodId);
}
