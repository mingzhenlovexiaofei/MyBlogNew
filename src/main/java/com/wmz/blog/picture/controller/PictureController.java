package com.wmz.blog.picture.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wmz.blog.blogs.model.TBlogs;
import com.wmz.blog.information.model.TInformation;
import com.wmz.blog.information.service.ITInformationService;
import com.wmz.blog.picture.model.TPicture;
import com.wmz.blog.picture.service.PictureService;
import com.wmz.blog.system.model.SysUser;
import com.wmz.blog.type.model.TType;
import com.wmz.blog.type.service.ITTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping
public class PictureController {

    @Autowired
    private ITInformationService itInformationService;

    @Autowired
    private PictureService pictureService;

    @GetMapping("/picture")
    public String picture(Model model, HttpSession session){
        SysUser user = (SysUser) session.getAttribute("user");
        QueryWrapper<TPicture> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status","1");
        List<TPicture> list = null;
        if (user != null && user.getUsername().equals("lixiaofei")) {
            queryWrapper.eq("type","2");
            list = pictureService.list(queryWrapper);
        } else if (user != null && user.getUsername().equals("2646532324@qq.com")) {
            queryWrapper.eq("type", "1");
            list = pictureService.list(queryWrapper);
        } else if (user != null && user.getUsername().equals("1696719985@qq.com")) {
            queryWrapper.eq("type","2");
            list = pictureService.list(queryWrapper);
        }
        model.addAttribute("pictures", list);
        //信息
        TInformation information = itInformationService.getOne(new QueryWrapper<>());
        model.addAttribute("blogTotal",information.getBlogCount());
        model.addAttribute("blogViewTotal",information.getVisitCount());
        model.addAttribute("blogCommentTotal",information.getCommentCount());
        model.addAttribute("blogMessageTotal",information.getMessageCount());


        return "picture";
    }
}
