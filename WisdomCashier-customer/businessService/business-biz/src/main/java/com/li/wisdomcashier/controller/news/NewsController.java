package com.li.wisdomcashier.controller.news;

import com.li.wisdomcashier.controller.news.vo.NewsVO;
import com.li.wisdomcashier.entry.R;
import com.li.wisdomcashier.service.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;

import java.util.List;

/**
 * @ClassName NewsController
 * @Description TODO
 * @Author Nine
 * @Date 2023/10/26 21:08
 * @Version 1.0
 */
@RestController
@RequestMapping("/news")
@Tag(name = "新闻")
public class NewsController {

    @Resource
    private NewsService newsService;


    @GetMapping("/getNews")
    @Operation(summary = "获取新闻")
    public R<NewsVO> getNews() {
        return newsService.getNews();
    }

    @GetMapping("getAdvertising")
    @Operation(summary ="获取广告跑马灯图片")
    public R<List<String>> getAdvertising() {
        return newsService.getAdvertising();
    }

}
