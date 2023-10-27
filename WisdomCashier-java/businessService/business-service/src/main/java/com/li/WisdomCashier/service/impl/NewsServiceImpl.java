package com.li.WisdomCashier.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.li.WisdomCashier.controller.news.vo.NewsVO;
import com.li.WisdomCashier.entry.News;
import com.li.WisdomCashier.enums.BaseEnum;
import com.li.WisdomCashier.enums.news.NewsTypeEnum;
import com.li.WisdomCashier.mapper.NewsMapper;
import com.li.WisdomCashier.pojo.R;
import com.li.WisdomCashier.service.NewsService;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Li
 * @description 针对表【t_news(新闻表)】的数据库操作Service实现
 * @createDate 2023-10-27 20:04:49
 */
@Service
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News> implements NewsService {

    @Resource
    private NewsMapper newsMapper;

    @Override
    public R<NewsVO> getNews() {
        List<News> newsList = newsMapper.selectList(Wrappers.lambdaQuery(News.class)
                .select(News::getId, News::getTitle, News::getType)
                .eq(News::getStatus, BaseEnum.TRUE.getValue()));
        return R.ok(
                NewsVO.builder()
                        .newsList(newsList.stream().
                                filter(e ->
                                        e.getType().equals(NewsTypeEnum.NEWS.getValue())
                                )
                                .collect(Collectors.toList()))
                        .activeList(newsList.stream().
                                filter(e ->
                                        e.getType().equals(NewsTypeEnum.ACTIVITY.getValue())
                                )
                                .collect(Collectors.toList()))
                        .build()
        );

    }
}




