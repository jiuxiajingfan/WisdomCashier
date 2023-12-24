package com.li.wisdomcashier.controller.news.vo;

import com.li.wisdomcashier.entry.News;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @ClassName NewsVO
 * @Description TODO
 * @Author Nine
 * @Date 2023/10/27 20:14
 * @Version 1.0
 */
@Data
@Builder
public class NewsVO {
    List<News> newsList;
    List<News> activeList;
}
