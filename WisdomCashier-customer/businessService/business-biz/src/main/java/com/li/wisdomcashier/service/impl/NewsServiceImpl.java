package com.li.wisdomcashier.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.li.wisdomcashier.controller.news.vo.NewsVO;
import com.li.wisdomcashier.entry.News;
import com.li.wisdomcashier.enums.BaseEnum;
import com.li.wisdomcashier.enums.news.NewsTypeEnum;
import com.li.wisdomcashier.mapper.NewsMapper;
import com.li.wisdomcashier.entry.R;
import com.li.wisdomcashier.service.NewsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
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
@RefreshScope
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News> implements NewsService {

    @Resource
    private NewsMapper newsMapper;

    @Value(value = "${business.advertising-url}")
    private List<String> advertisingList;

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

    @Override
    public R<List<String>> getAdvertising() {
        return R.ok(advertisingList);
    }
}




