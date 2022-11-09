package com.bjpowernode.controller;

import com.bjpowernode.pojo.ProductInfo;
import com.bjpowernode.pojo.vo.ProductInfoVo;
import com.bjpowernode.service.ProductInfoService;
import com.bjpowernode.service.ProductTypeService;
import com.bjpowernode.service.impl.ProductInfoServiceImpl;
import com.bjpowernode.utils.FileNameUtil;
import com.github.pagehelper.PageInfo;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/prod")
public class ProductInfoAction {
    public static final int PAGE_SIZE = 5;
    String saveFileName = "";
    @Autowired
    ProductInfoService productInfoService;

    @RequestMapping("/getAll")
    public String getAll(HttpServletRequest request) {
        List<ProductInfo> list = productInfoService.getAll();
        request.setAttribute("list", list);
        return "product";
    }

    @RequestMapping("/split")
    public String split(HttpServletRequest request) {
        PageInfo pageInfo = null;
        Object vo = request.getSession().getAttribute("prodVo");
        if (vo != null) {
            pageInfo = productInfoService.splitPageVo((ProductInfoVo) vo, PAGE_SIZE);
            request.setAttribute("pname", ((ProductInfoVo) vo).getPname());
            request.setAttribute("hprice", ((ProductInfoVo) vo).getHprice());
            request.setAttribute("lprice", ((ProductInfoVo) vo).getLprice());
            request.getSession().removeAttribute("prodVo");
        } else {
            pageInfo = productInfoService.splitPage(1, PAGE_SIZE);
        }
        request.setAttribute("pb", pageInfo);
        return "product";
    }

    @RequestMapping("/ajaxSplit")
    @ResponseBody
    public void ajaxSplit(ProductInfoVo vo, HttpSession session) {
        PageInfo pageInfo = productInfoService.splitPageVo(vo, PAGE_SIZE);
        session.setAttribute("pb", pageInfo);
    }

    @ResponseBody
    @RequestMapping("/ajaxImg")
    public Object ajaxImg(MultipartFile pimage, HttpServletRequest request) {
        //提取生成文件名UUID+上传图片后缀.jpg或.png
        saveFileName = FileNameUtil.getUUIDFileName() + FileNameUtil.getFileType(pimage.getOriginalFilename());
        //得到项目中图片存储路径
        String path = request.getServletContext().getRealPath("image_big");
        try {
            //转存
            pimage.transferTo(new File(path + File.separator + saveFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //返回客户端json对象，封装图片路径，为了在页面实现立即回显
        JSONObject object = new JSONObject();
        object.put("imgurl", saveFileName);
        return object.toString();
    }

    @RequestMapping("/save")
    public String save(ProductInfo info, HttpServletRequest request) {
        info.setpImage(saveFileName);
        info.setpDate(new Date());
        int num = -1;
        try {
            num = productInfoService.save(info);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (num > 0) {
            request.setAttribute("msg", "增加成功！");
        } else {
            request.setAttribute("msg", "增加失败！");
        }
        saveFileName = "";
        return "forward:/prod/split.action";
    }

    @RequestMapping("/one")
    public String one(int pid, ProductInfoVo vo, Model model, HttpSession session) {
        ProductInfo info = productInfoService.getById(pid);
        //将多条件及页面放入session中，更新处理结束后读取条件和页码进行处理
        session.setAttribute("prodVo", vo);
        model.addAttribute("prod", info);
        return "update";
    }

    @RequestMapping("/update")
    public String update(ProductInfo info, HttpServletRequest request) {
        /*
        因为ajax的异步图片上传，如果有上传过，则saveFileName里有上传上来的图片名称，
        如果没有使用异步ajax上传过图片，则saveFileName="",
        实体类info使用隐藏表单域提供上来的pImage原始图片的名称
        */
        if (!saveFileName.equals("")) {
            info.setpImage(saveFileName);
        }
        int num = -1;
        try {
            num = productInfoService.update(info);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (num > 0) {
            request.setAttribute("msg", "更新成功！");
        } else {
            request.setAttribute("msg", "更新失败！");
        }
        saveFileName = "";
        return "forward:/prod/split.action";
    }

    @RequestMapping("/delete")
    public String delete(int pid, HttpServletRequest request) {
        int num = -1;
        try {
            num = productInfoService.delete(pid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (num > 0) {
            request.setAttribute("msg", "删除成功！");
        } else {
            request.setAttribute("msg", "删除失败！");
        }
        return "forward:/prod/deleteAjaxSplit.action";
    }

    @RequestMapping(value = "/deleteAjaxSplit", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public Object deleteAjaxSplit(HttpServletRequest request) {
        PageInfo info = productInfoService.splitPage(1, PAGE_SIZE);
        request.getSession().setAttribute("pb", info);
        return request.getAttribute("msg");
    }

    @RequestMapping("/deleteBatch")
    public String deleteBatch(String pids, HttpServletRequest request) {
        String ids[] = pids.split(",");
        try {
            int num = productInfoService.deleteBatch(ids);
            if (num > 0) {
                request.setAttribute("msg", "批量删除成功！");
            } else {
                request.setAttribute("msg", "批量删除失败！");
            }
        } catch (Exception e) {
            request.setAttribute("msg", "商品不可删除！");
            e.printStackTrace();
        }
        return "forward:/prod/deleteAjaxSplit.action";
    }

    @ResponseBody
    @RequestMapping("/condition")
    public void condition(ProductInfoVo vo, HttpSession session) {
        List<ProductInfo> list = productInfoService.selectCondition(vo);
        session.setAttribute("list", list);
    }

}
