package com.wmz.blog.picture.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wmz.blog.picture.mapper.PictureMapper;
import com.wmz.blog.picture.model.TPicture;
import com.wmz.blog.picture.service.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PictureServiceImpl extends ServiceImpl<PictureMapper, TPicture> implements PictureService {

    @Autowired
    private PictureMapper pictureMapper;
    @Override
    public List<TPicture> getAll() {
        return pictureMapper.selectList(new QueryWrapper<TPicture>().eq("status","1"));
    }
}
