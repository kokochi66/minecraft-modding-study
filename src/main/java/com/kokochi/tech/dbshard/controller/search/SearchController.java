package com.kokochi.tech.dbshard.controller.search;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/search")
@Slf4j
public class SearchController {


    @GetMapping("")
    public String search() {
        return "search/list";
    }
}
