package com.springboot.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.ecommerce.entity.Status;

public interface StatusRepository extends JpaRepository<Status, Long> {
	
}
