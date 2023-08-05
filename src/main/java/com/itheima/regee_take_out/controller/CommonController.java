package com.itheima.regee_take_out.controller;

import com.itheima.regee_take_out.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/*
* 文件上传和下载
*
* */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${reggie.path}")
    private  String basepath;
    /*
    * 文件上传
    * */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){ //文件接收的变量名需与前端页面传输的名称保持一致
        // file是个临时文件需要转存到文件夹，上传后会被删除
        log.info("文件上传：{}",file.toString());

        // 原始文件名
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

        // 使用uuid生成唯一的文件名，防止文件名重复
        String fileName = UUID.randomUUID().toString()+suffix;

        // 创建一个目录对象
        File dir = new File(basepath);
        // 判断目录是否存在，不存在则创建
        if (!dir.exists()){
            dir.mkdirs();
        }

        try {
            file.transferTo(new File(basepath+fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(fileName);
    }
    /*
    * 文件下载
    */
    @GetMapping("/download")
    public void douwnload(String name, HttpServletResponse response) {
        // 输入流，读取文件内容
        try {
            FileInputStream fileInputStream = new FileInputStream(basepath + name);
            // 输出流，将文件内容写入到响应体中,文件下载
            ServletOutputStream outputStream = response.getOutputStream();

            response.setContentType("image/jpeg");
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
