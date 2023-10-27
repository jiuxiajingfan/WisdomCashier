package com.li.WisdomCashier.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.li.WisdomCashier.controller.news.vo.NewsVO;
import com.li.WisdomCashier.entry.News;
import com.li.WisdomCashier.pojo.R;

/**
* @author Li
* @description 针对表【t_news(新闻表)】的数据库操作Service
* @createDate 2023-10-27 20:04:49
*/
public interface NewsService extends IService<News> {

    R<NewsVO> getNews();
}
