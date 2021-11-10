package com.springboot.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.springboot.ecommerce.entity.Photo;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
	
}
