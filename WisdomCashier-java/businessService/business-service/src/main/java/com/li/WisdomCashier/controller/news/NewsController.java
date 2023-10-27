package com.li.WisdomCashier.controller.news;

import com.li.WisdomCashier.controller.news.vo.NewsVO;
import com.li.WisdomCashier.pojo.R;
import com.li.WisdomCashier.service.NewsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @ClassName NewsController
 * @Description TODO
 * @Author Nine
 * @Date 2023/10/26 21:08
 * @Version 1.0
 */
@RestController
@RequestMapping("/news")
@Api(tags = "新闻")
public class NewsController {

    @Resource
    private NewsService newsService;


    @GetMapping("/getNews")
    @ApiOperation("获取新闻")
    public R<NewsVO> getNews(){
        return newsService.getNews();
    }

}
