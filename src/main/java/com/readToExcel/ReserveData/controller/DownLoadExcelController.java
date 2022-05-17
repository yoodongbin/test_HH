package com.readToExcel.ReserveData.controller;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

@Controller
public class DownLoadExcelController {

    @GetMapping("excel-download")
    public void download(HttpServletResponse response) throws IOException {

        response.setContentType("ms-vnd/excel");
//        response.setHeader("Content-Disposition", "attachment;filename=example.xls");
        response.setHeader("Content-Disposition", "attachment;C:\\Users\\user\\Desktop\\새 폴더\\안녕.xlsx");
    }
}
