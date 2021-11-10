package com.springboot.ecommerce.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.springboot.ecommerce.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
	Account findByEmail(String email);

	@Query(value = "SELECT a FROM Account a JOIN Verify b ON a.id = b.account.id WHERE TIME(NOW()) - TIME(b.createAt) <= 60 AND b.code = ?1")
	Account findByOtp(String otp);

	Page<Account> findByNameContainingOrEmailContaining(String name, String email, Pageable pageable);

	Page<Account> findByRole(Integer role, Pageable pageable);

}
