package com.wmz.blog.picture.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TPicture extends Model {
    private static final long serialVersionUID = 1L;

    @TableId
    private String id;

    private String name;

    private String type;

    private String url;

    private String status;

    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;


}
