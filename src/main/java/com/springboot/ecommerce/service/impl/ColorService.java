package com.springboot.ecommerce.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.ecommerce.entity.Color;
import com.springboot.ecommerce.repository.ColorRepository;
import com.springboot.ecommerce.service.IColorService;

@Service
public class ColorService implements IColorService {

	@Autowired
	private ColorRepository repository;

	@Override
	public List<Color> findAll() {
		return repository.findAll();
	}
}
