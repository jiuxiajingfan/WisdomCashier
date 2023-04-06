package com.li.wisdomcashier.base.service.impl;

import cn.hutool.extra.cglib.CglibUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.li.wisdomcashier.base.common.R;
import com.li.wisdomcashier.base.entity.dto.AddVipDTO;
import com.li.wisdomcashier.base.entity.dto.GoodQueryDTO;
import com.li.wisdomcashier.base.entity.dto.RenewalDTO;
import com.li.wisdomcashier.base.entity.dto.VipQueryDTO;
import com.li.wisdomcashier.base.entity.po.Vip;
import com.li.wisdomcashier.base.entity.vo.VipVO;
import com.li.wisdomcashier.base.enums.RoleEnum;
import com.li.wisdomcashier.base.enums.VipEnum;
import com.li.wisdomcashier.base.mapper.VipMapper;
import com.li.wisdomcashier.base.service.VipService;
import com.li.wisdomcashier.base.util.UserUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lsw
 * @since 2023-03-21
 */
@Service
public class VipServiceImpl extends ServiceImpl<VipMapper, Vip> implements VipService {

    @Resource
    private VipMapper vipMapper;

    @Override
    public R<IPage<VipVO>> getVipPage(GoodQueryDTO goodQueryDTO) {
        //普通接口
        UserUtils.hasPermissions(goodQueryDTO.getSid(), RoleEnum.SHOP.getCode());
        LambdaQueryWrapper<Vip> wrapper = Wrappers.lambdaQuery(Vip.class)
                .eq(Vip::getShopId, Long.parseLong(goodQueryDTO.getSid()))
                .eq(!StringUtils.isBlank(goodQueryDTO.getGid()), Vip::getPhone, goodQueryDTO.getGid());
        IPage<Vip> Page =new Page(goodQueryDTO.getCurrent(),goodQueryDTO.getPageSize());
        IPage<VipVO> convert = vipMapper.selectPage(Page, wrapper).convert(e -> {
            VipVO copy = new VipVO();
            copy.setStatus(e.getStatus());
            copy.setIntegration(e.getIntegration());
            copy.setId(e.getPhone());
            copy.setLimit(e.getGmtLimit());
            copy.setLevel(e.getLevel());
            return copy;
        });
        return R.ok(convert);
    }

    @Override
    public R<String> addVip(AddVipDTO addVipDTO) {
        //普通接口
        UserUtils.hasPermissions(addVipDTO.getSid(), RoleEnum.SHOP.getCode());
        Vip vip = vipMapper.selectOne(Wrappers.lambdaQuery(Vip.class)
                .eq(Vip::getPhone, addVipDTO.getPhone())
                .eq(Vip::getShopId, Long.parseLong(addVipDTO.getSid())));
        if(!Objects.isNull(vip)){
            return R.error("该会员已经存在！");
        }
        Vip newVip = new Vip();
        newVip.setAge(addVipDTO.getAge());
        newVip.setSex(addVipDTO.getSex());
        newVip.setGmtLimit(LocalDateTime.now().plusMonths(addVipDTO.getLimit()));
        newVip.setPhone(addVipDTO.getPhone());
        newVip.setStatus(VipEnum.ACTIVE.getCode());
        newVip.setShopId(Long.parseLong(addVipDTO.getSid()));
        return R.ok(vipMapper.insert(newVip) == 1 ? "新增成功！": "新增失败，请联系管理员！");
    }

    @Override
    public R<String> renewalVip(RenewalDTO renewalDTO) {
        //普通接口
        UserUtils.hasPermissions(renewalDTO.getSid(), RoleEnum.SHOP.getCode());
        Vip vip = vipMapper.selectOne(Wrappers.lambdaQuery(Vip.class)
                .eq(Vip::getPhone, renewalDTO.getPhone())
                .eq(Vip::getShopId, Long.parseLong(renewalDTO.getSid())));
        if(Objects.isNull(vip)){
            return R.error("不存在该会员！");
        }
        if(vip.getStatus().equals(VipEnum.LIMITED.getCode())) {
            vip.setGmtLimit(LocalDateTime.now().plusMonths(renewalDTO.getLimit()));
            vip.setStatus(VipEnum.ACTIVE.getCode());
        }else {
            vip.setGmtLimit(vip.getGmtLimit().plusMonths(renewalDTO.getLimit()));
        }
        return R.ok(vipMapper.updateById(vip)== 1 ? "更新成功！": "更新失败，请联系管理员！");
    }

    @Override
    public R<Integer> isVip(String sid, String phone) {
        if(StringUtils.isBlank(sid)||StringUtils.isBlank(phone)) {
            return R.error("参数错误！");
        }
        //普通接口
        UserUtils.hasPermissions(sid, RoleEnum.SHOP.getCode());
        return R.ok(vipMapper.selectCount(Wrappers.lambdaQuery(Vip.class)
                .eq(Vip::getPhone, phone)
                .eq(Vip::getShopId, Long.parseLong(sid))
                .eq(Vip::getStatus, VipEnum.ACTIVE.getCode())));
    }

    @Async("taskExecutor")
    @Override
    public void addIntegration(String phone, String sum,String sid) {
        BigDecimal bigDecimal = new BigDecimal(sum);
        BigDecimal multiply = bigDecimal.multiply(new BigDecimal(100));
        Vip vip = vipMapper.selectOne(Wrappers.lambdaQuery(Vip.class)
                .eq(Vip::getPhone,phone)
                .eq(Vip::getShopId, Long.parseLong(sid)));
        vip.setIntegration(vip.getIntegration() + multiply.intValue());
        vip.setLevel(vip.getIntegration()/100000);
        vipMapper.updateById(vip);
    }

    @Override
    public R<IPage<VipVO>> getVipPushPage(VipQueryDTO goodQueryDTO) {
        //普通接口
        UserUtils.hasPermissions(goodQueryDTO.getSid(), RoleEnum.SHOP.getCode());
        LambdaQueryWrapper<Vip> wrapper = Wrappers.lambdaQuery(Vip.class)
                .eq(Vip::getShopId, Long.parseLong(goodQueryDTO.getSid()))
                .in(!CollectionUtils.isEmpty(goodQueryDTO.getAge()), Vip::getAge, goodQueryDTO.getAge())
                .in(!CollectionUtils.isEmpty(goodQueryDTO.getSex()), Vip::getSex, goodQueryDTO.getSex())
                .in(!CollectionUtils.isEmpty(goodQueryDTO.getLevel()), Vip::getLevel, goodQueryDTO.getLevel())
                .in(!CollectionUtils.isEmpty(goodQueryDTO.getStatus()), Vip::getStatus, goodQueryDTO.getStatus());
        IPage<Vip> Page =new Page(goodQueryDTO.getCurrent(),goodQueryDTO.getPageSize());
        IPage<VipVO> convert = vipMapper.selectPage(Page, wrapper).convert(e -> {
            VipVO copy = CglibUtil.copy(e, VipVO.class);
            copy.setId(e.getPhone());
            return copy;
        });
        return R.ok(convert);
    }

    @Override
    public R<String> sendVipMessage() {
//        // HttpClient Configuration
//        /*HttpClient httpClient = new ApacheAsyncHttpClientBuilder()
//                .connectionTimeout(Duration.ofSeconds(10)) // Set the connection timeout time, the default is 10 seconds
//                .responseTimeout(Duration.ofSeconds(10)) // Set the response timeout time, the default is 20 seconds
//                .maxConnections(128) // Set the connection pool size
//                .maxIdleTimeOut(Duration.ofSeconds(50)) // Set the connection pool timeout, the default is 30 seconds
//                // Configure the proxy
//                .proxy(new ProxyOptions(ProxyOptions.Type.HTTP, new InetSocketAddress("<your-proxy-hostname>", 9001))
//                        .setCredentials("<your-proxy-username>", "<your-proxy-password>"))
//                // If it is an https connection, you need to configure the certificate, or ignore the certificate(.ignoreSSL(true))
//                .x509TrustManagers(new X509TrustManager[]{})
//                .keyManagers(new KeyManager[]{})
//                .ignoreSSL(false)
//                .build();*/
//
//        // Configure Credentials authentication information, including ak, secret, token
//        StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
//                .accessKeyId("<your-accessKeyId>")
//                .accessKeySecret("<your-accessKeySecret>")
//                //.securityToken("<your-token>") // use STS token
//                .build());
//
//        // Configure the Client
//        AsyncClient client = AsyncClient.builder()
//                .region("cn-hangzhou") // Region ID
//                //.httpClient(httpClient) // Use the configured HttpClient, otherwise use the default HttpClient (Apache HttpClient)
//                .credentialsProvider(provider)
//                //.serviceConfiguration(Configuration.create()) // Service-level configuration
//                // Client-level configuration rewrite, can set Endpoint, Http request parameters, etc.
//                .overrideConfiguration(
//                        ClientOverrideConfiguration.create()
//                                .setEndpointOverride("dysmsapi.aliyuncs.com")
//                        //.setConnectTimeout(Duration.ofSeconds(30))
//                )
//                .build();
//
//        // Parameter settings for API request
//        SendSmsRequest sendSmsRequest = SendSmsRequest.builder()
//                .phoneNumbers("")
//                .signName("")
//                .templateCode("")
//                // Request-level configuration rewrite, can set Http request parameters, etc.
//                // .requestConfiguration(RequestConfiguration.create().setHttpHeaders(new HttpHeaders()))
//                .build();
//
//        // Asynchronously get the return value of the API request
//        CompletableFuture<SendSmsResponse> response = client.sendSms(sendSmsRequest);
//        // Synchronously get the return value of the API request
//        SendSmsResponse resp = response.get();
//        System.out.println(new Gson().toJson(resp));
//        // Asynchronous processing of return values
//        /*response.thenAccept(resp -> {
//            System.out.println(new Gson().toJson(resp));
//        }).exceptionally(throwable -> { // Handling exceptions
//            System.out.println(throwable.getMessage());
//            return null;
//        });*/
//
//        // Finally, close the client
//        client.close();
    return R.ok();
    }
}
