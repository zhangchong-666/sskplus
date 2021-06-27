package com.lzs.sskplus.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author 张sir
 * @Company 南邮
 * @Create 2021-04-30-15:24
 **/
@Data
@Component
@ConfigurationProperties(prefix = "xuanketime")
public class XuanKeTime implements Serializable {
    //开始选课/退课时间
    private String startTime;
    //截至选课时间
    private String endTime;
    //退课结束时间
    private String tuikeTime;
}
