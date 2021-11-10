package com.springboot.ecommerce.service;

import java.util.List;

import com.springboot.ecommerce.entity.Status;

public interface IOrderStatusService {

	Status findById(Long id);

	List<Status> findAll();

}
