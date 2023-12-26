package com.li.wisdomcashier.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.li.wisdomcashier.controller.news.vo.NewsVO;
import com.li.wisdomcashier.entry.News;
import com.li.wisdomcashier.entry.R;

import java.util.List;

/**
* @author Li
* @description 针对表【t_news(新闻表)】的数据库操作Service
* @createDate 2023-10-27 20:04:49
*/
public interface NewsService extends IService<News> {

    R<NewsVO> getNews();

    R<List<String>> getAdvertising();
}
