package com.springboot.ecommerce.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.springboot.ecommerce.dto.ProductSoldDTO;
import com.springboot.ecommerce.entity.Order;
import com.springboot.ecommerce.entity.OrderDetail;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
	List<OrderDetail> findByOrder(Order order);

	@Query(value = "SELECT a FROM OrderDetail a " + "JOIN Order b ON a.order.id = b.id"
			+ " JOIN Account c ON b.account.id = c.id" + " WHERE b.account.id = ?1 AND b.status.id = 1")
	List<OrderDetail> findByAccountId(Long accountId);

	@Query(value = "select c.id, c.name, c.qty, SUM(a.qty), SUM(a.qty * a.price) from order_details a join"
			+ " orders b on a.order_id = b.id join products"
			+ " c on a.product_id = c.id where b.status_id = 3 and week(b.create_at) = week(now()) group by c.id, c.name, c.qty limit ?1 offset ?2", nativeQuery = true)
	List<Object[]> getProductSoldReportByWeek(int limit, int offset);

	@Query(value = "select c.id, c.name, c.qty, SUM(a.qty), SUM(a.qty * a.price) from order_details a join"
			+ " orders b on a.order_id = b.id join products"
			+ " c on a.product_id = c.id where b.status_id = 3 and month(b.create_at) = month(now()) group by c.id, c.name, c.qty limit ?1 offset ?2", nativeQuery = true)
	List<Object[]> getProductSoldReportByMonth(int limit, int offset);

	@Query(value = "select c.id, c.name, c.qty, SUM(a.qty), SUM(a.qty * a.price) from order_details a join"
			+ " orders b on a.order_id = b.id join products"
			+ " c on a.product_id = c.id where b.status_id = 3 and year(b.create_at) = year(now()) group by c.id, c.name, c.qty limit ?1 offset ?2", nativeQuery = true)
	List<Object[]> getProductSoldReportByYear(int limit, int offset);

	@Query(value = "select c.id, c.name, c.qty, SUM(a.qty), SUM(a.qty * a.price) from order_details a join"
			+ " orders b on a.order_id = b.id join products"
			+ " c on a.product_id = c.id where b.status_id = 3 and date(b.create_at) = date(curdate()) group by c.id, c.name, c.qty limit ?1 offset ?2", nativeQuery = true)
	List<Object[]> getProductSoldReportToday(int limit, int offset);

	@Query(value = "SELECT month(a.create_at) as month, SUM(b.qty * b.price) as total FROM orders a \r\n"
			+ "JOIN order_details b on a.id = b.order_id\r\n" + "WHERE a.status_id = ?1 and year(a.create_at) = ?2\r\n"
			+ "GROUP BY month limit ?3 offset ?4", nativeQuery = true)
	List<Object[]> getRevenue(Long statusId, int year, int limit, int offset);

}
