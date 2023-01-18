package com.kokochi.tech.dbshard.controller.product;

import com.kokochi.tech.dbshard.domain.product.enumType.ProductSeasonType;
import com.kokochi.tech.dbshard.domain.product.enumType.ProductType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/product")
@Slf4j
public class ProductController {

    @GetMapping("/edit")
    public String edit(Model model) {
        model.addAttribute("productTypeList", ProductType.values());
        model.addAttribute("productSeasonTypeList", ProductSeasonType.values());
        return "product/edit";
    }

    @GetMapping("/view/{productId}")
    public String view(@PathVariable("productId") Long productId) {
        return "product/view";
    }
}
