package com.kokochi.tech.dbshard.controller.image;

import com.kokochi.tech.dbshard.controller.image.model.ImageDto;
import com.kokochi.tech.dbshard.controller.product.model.ProductDto;
import com.kokochi.tech.dbshard.domain.product.ProductImg;
import com.kokochi.tech.dbshard.service.product.ProductImgService;
import com.kokochi.tech.dbshard.service.product.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/image")
@AllArgsConstructor
@Slf4j
public class ImageController {

    private final ProductImgService productImgService;
    private final ProductService productService;

    @GetMapping("/edit")
    public String edit(Model model) {
        List<ProductDto> productList = productService.getProductList().stream().map(ProductDto::convertDto).collect(Collectors.toList());
        model.addAttribute("productList", productList);
        return "image/edit";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute ImageDto imageDto) {
        Long savedImageId = productImgService.insertProductImg(convertProductImg(imageDto), imageDto.getImageFile());
        return "redirect:/image/view/" + savedImageId;
    }

    @GetMapping("/view/{imageId}")
    public String view(Model model, @PathVariable("imageId") Long imageId) {
        ImageDto image = convertImageDto(productImgService.getProductImgById(imageId));
        model.addAttribute("image", image);
        return "image/view";
    }

    public ProductImg convertProductImg(ImageDto dto) {
        return ProductImg.builder()
                .product(productService.getProductById(dto.getProductId()))
                .productImgTitle(dto.getProductImgTitle())
                .productImgType(dto.getProductImgType())
                .productImgUrl(dto.getProductImgUrl())
                .build();
    }

    public ImageDto convertImageDto(ProductImg productImg) {
        return ImageDto.builder()
                .productImageId(productImg.getProductImgId())
                .productId(productImg.getProduct().getProductId())
                .productImgType(productImg.getProductImgType())
                .productImgTitle(productImg.getProductImgTitle())
                .productImgUrl(productImg.getProductImgUrl())
                .build();
    }

}
