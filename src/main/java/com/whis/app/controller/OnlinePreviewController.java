package com.whis.app.controller;

import com.whis.base.common.DataResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController("OnlinePreviewController")
@RequestMapping("/online-preview")
@Controller
public class OnlinePreviewController {

    @RequestMapping("/view")
    public DataResponse onlinePreview(String url, Model model, HttpServletRequest req) {
        req.setAttribute("fileKey", req.getParameter("fileKey"));


        return DataResponse.create();
    }

}
