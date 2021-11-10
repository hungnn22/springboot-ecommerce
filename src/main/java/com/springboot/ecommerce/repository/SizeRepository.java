package com.springboot.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springboot.ecommerce.entity.Size;

@Repository
public interface SizeRepository extends JpaRepository<Size, Long> {

}
