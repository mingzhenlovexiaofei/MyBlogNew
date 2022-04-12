package com.wmz.blog.picture.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wmz.blog.picture.model.TPicture;

import java.util.List;

public interface PictureService extends IService<TPicture> {
    List<TPicture> getAll();

}
