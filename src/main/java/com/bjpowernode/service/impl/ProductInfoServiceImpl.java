package com.bjpowernode.service.impl;

import com.bjpowernode.mapper.ProductInfoMapper;
import com.bjpowernode.pojo.ProductInfo;
import com.bjpowernode.pojo.ProductInfoExample;
import com.bjpowernode.pojo.vo.ProductInfoVo;
import com.bjpowernode.service.ProductInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductInfoServiceImpl implements ProductInfoService {

    @Autowired
    ProductInfoMapper mapper;

    @Override
    public List<ProductInfo> getAll() {
        return mapper.selectByExample(null);
    }

    @Override
    public PageInfo splitPage(int pageNum, int pageSize) {
        //分页插件使用PageHelper工具类完成分页设置
        PageHelper.startPage(pageNum, pageSize);
        //进行PageInfo数据封装
        ProductInfoExample example = new ProductInfoExample();
        example.setOrderByClause("p_id desc");
        //设置完排序后，取集合，一定要在取集合之前，设置PageHelper.startPage
        List<ProductInfo> list = mapper.selectByExample(example);
        //将查询的对象封装在PageInfo对象中
        PageInfo<ProductInfo> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public int save(ProductInfo info) {
        return mapper.insert(info);
    }

    @Override
    public ProductInfo getById(int pid) {
        return mapper.selectByPrimaryKey(pid);
    }

    @Override
    public int update(ProductInfo info) {
        return mapper.updateByPrimaryKey(info);
    }

    @Override
    public int delete(int pid) {
        return mapper.deleteByPrimaryKey(pid);
    }

    @Override
    public int deleteBatch(String[] ids) {
        return mapper.deleteBatch(ids);
    }

    @Override
    public List<ProductInfo> selectCondition(ProductInfoVo vo) {
        return mapper.selectCondition(vo);
    }

    @Override
    public PageInfo splitPageVo(ProductInfoVo vo, int pageSize) {
        PageHelper.startPage(vo.getPage(), pageSize);
        List<ProductInfo> list = mapper.selectCondition(vo);
        return new PageInfo<ProductInfo>(list);
    }
}
