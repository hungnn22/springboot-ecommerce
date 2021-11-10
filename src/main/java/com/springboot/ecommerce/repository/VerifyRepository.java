package com.springboot.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springboot.ecommerce.entity.Verify;

@Repository
public interface VerifyRepository extends JpaRepository<Verify, Long> {

}
