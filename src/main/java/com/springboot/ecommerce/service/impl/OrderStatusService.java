package com.springboot.ecommerce.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.ecommerce.entity.Status;
import com.springboot.ecommerce.repository.StatusRepository;
import com.springboot.ecommerce.service.IOrderStatusService;

@Service
public class OrderStatusService implements IOrderStatusService {

	@Autowired
	private StatusRepository repo;

	@Override
	public Status findById(Long id) {
		return repo.getById(id);
	}

	@Override
	public List<Status> findAll() {
		return repo.findAll();
	}
}
