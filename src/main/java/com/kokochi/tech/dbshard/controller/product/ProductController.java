package com.kokochi.tech.dbshard.controller.product;

import com.kokochi.tech.dbshard.controller.product.model.ProductDto;
import com.kokochi.tech.dbshard.domain.product.enumType.ProductSeasonType;
import com.kokochi.tech.dbshard.domain.product.enumType.ProductType;
import com.kokochi.tech.dbshard.service.product.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/product")
@AllArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    @GetMapping("/edit")
    public String edit(Model model) {
        model.addAttribute("productTypeList", ProductType.values());
        model.addAttribute("productSeasonTypeList", ProductSeasonType.values());
        return "product/edit";
    }

    @PostMapping("/add")
    public String addProduct(@ModelAttribute ProductDto productDto) {
        Long savedProductId = productService.insertProduct(productDto.convertProduct());
        return "redirect:/";
    }
}
