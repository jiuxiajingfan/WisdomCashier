package com.li.wisdomcashier.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.li.wisdomcashier.controller.goods.dto.*;
import com.li.wisdomcashier.controller.goods.vo.GoodsVO;
import com.li.wisdomcashier.entry.Goods;
import com.li.wisdomcashier.entry.R;
import com.li.wisdomcashier.entry.dto.PayVO;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author lsw
 * @since 2023-02-26
 */
public interface GoodsService extends IService<Goods> {

    /**
     * 查询商品信息
     *
     * @param gid
     * @return
     */
    R<Goods> reqGood(String gid);

    /**
     * 新增商品
     *
     * @param goods
     * @return
     */
    R<String> addGood(GoodsDTO goods);

    /**
     * 根据ID查询
     *
     * @param goodQueryDTO
     * @return
     */
    R<IPage<Goods>> getGoodPage(GoodQueryDTO goodQueryDTO);

    /**
     * 修稿商品信息
     *
     * @param good
     * @return
     */
    R<String> updateGood(GoodsDTO good);

    /**
     * 条形码获取商品信息
     *
     * @param gid
     * @param sid
     * @return
     */
    R<GoodsVO> getGood(String gid, Long sid);

    /**
     * 商品交易
     *
     * @return
     */
    R<String> buyGood(BuyGoodDTO buyGoodDTO);

    /**
     * 返回一个随机订单号
     *
     * @return
     */
    R<String> getRandID();

    /**
     * 失败订单处理
     *
     * @param
     */

    void failTradeLogAsunc(String tradeNo, Long sid, Integer type);

    /**
     * @param deleteDTO
     * @return
     */
    R<String> deleteGood(DeleteDTO deleteDTO);

    /**
     * 获取临期商品
     *
     * @param goodQueryDTO
     * @return
     */
    R<IPage<Goods>> getGoodTemporaryPage(GoodQueryDTO goodQueryDTO);

    /**
     * 更新预览图
     *
     * @param payVO
     * @return
     */
    R<String> updateGoodImg(PayVO payVO);

}
