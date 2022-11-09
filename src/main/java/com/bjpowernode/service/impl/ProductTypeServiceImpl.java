package com.bjpowernode.service.impl;


import com.bjpowernode.mapper.ProductTypeMapper;
import com.bjpowernode.pojo.ProductType;
import com.bjpowernode.service.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductTypeServiceImpl implements ProductTypeService {
    @Autowired
    ProductTypeMapper mapper;

    @Override
    public List<ProductType> getAll() {
        return mapper.selectByExample(null);
    }
}
