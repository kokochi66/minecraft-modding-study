package com.kokochi.tech.dbshard.controller.home;

import com.kokochi.tech.dbshard.service.product.ProductImgService;
import com.kokochi.tech.dbshard.service.product.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@Slf4j
public class HomeController {

    private final ProductService productService;
    private final ProductImgService productImgService;


    @GetMapping("/")
    public String home(Model model) {
        return "content";
    }
}
