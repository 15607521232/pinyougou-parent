package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbBrand;
import entity.PageResult;

import java.util.List;

public interface BrandService {

    public List<TbBrand> findAll();

    /**
     * 返回分页列表
     */
    public PageResult findPage(int pageNum,int pageSize);

    //增加
    public void add(TbBrand brand);
}
