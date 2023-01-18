package com.kokochi.tech.dbshard.controller.image;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/image")
@Slf4j
public class ImageController {


    @GetMapping("/edit")
    public String edit() {
        return "image/edit";
    }

    @GetMapping("/view")
    public String view() {
        return "image/view";
    }
}
