package com.springboot.ecommerce.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.springboot.ecommerce.entity.Account;
import com.springboot.ecommerce.entity.Order;
import com.springboot.ecommerce.entity.Status;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

	@Query(value = "SELECT o FROM Order o WHERE o.account.id = ?1 AND o.status.id = 1")
	Order findOrderNotConfirmedByAccountId(Long accountId);

	List<Order> findByAccount(Account account);

	@Query(value = "SELECT o FROM Order o WHERE o.account.id = ?1 AND (o.account.name like %?2% OR o.address LIKE %?2% OR o.phone like %?2%)")
	Page<Order> findByAccountInfo(Long accountId, String search, Pageable pageable);

	@Query(value = "FROM Order o WHERE o.account.id = ?1")
	Page<Order> findByAccountId(Long accountId, Pageable pageable);

	@Query(value = "SELECT o FROM Order o WHERE o.account.name like %?1% OR o.address LIKE %?1% OR o.phone like %?1%")
	Page<Order> findBySearchKey(String search, Pageable pageable);

	Page<Order> findByAccount(Account account, Pageable pageable);

	@Query(value = "SELECT COUNT(*) FROM orders o WHERE o.status_id = 3", nativeQuery = true)
	Long getPendingCount();

	Page<Order> findByStatus(Status status, Pageable pageable);

	Page<Order> findByAccountAndStatusNot(Account account, Status status, Pageable pageable);

}
