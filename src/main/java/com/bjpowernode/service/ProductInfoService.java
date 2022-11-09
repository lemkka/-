package com.bjpowernode.service;

import com.bjpowernode.pojo.ProductInfo;
import com.bjpowernode.pojo.vo.ProductInfoVo;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ProductInfoService {
    //显示全部商品(不分页)
    List<ProductInfo> getAll();
    //分页功能实现
    PageInfo splitPage(int pageNum,int pageSize);

    int save(ProductInfo info);

    ProductInfo getById(int pid);

    public int update(ProductInfo info);

    public int delete(int pid);

    public int deleteBatch(String[] ids);

    List<ProductInfo> selectCondition(ProductInfoVo vo);

    //多条件查询分页
    PageInfo splitPageVo(ProductInfoVo vo,int pageSize);
}
