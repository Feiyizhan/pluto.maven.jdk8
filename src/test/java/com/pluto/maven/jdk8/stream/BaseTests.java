
package com.pluto.maven.jdk8.stream;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Before;
import org.junit.Test;

import com.pluto.maven.jdk8.pojo.UserData;

/**
 *
 * @author 徐明龙 XuMingLong 2019-05-22
 */
public class BaseTests {
    
    protected List<UserData> dataList100000 = new ArrayList<>();
    
    protected List<UserData> dataList100 = new ArrayList<>();
    
    protected List<UserData> dataList10 = new ArrayList<>();
    
    protected List<Integer> statusList = Arrays.asList(10,20,30);

    @Before
    public void initDate() {
        for(int i=0;i<100000;i++) {
            UserData itemData = new UserData();
            itemData.setName(RandomStringUtils.random(3,"abc"));
            itemData.setStatus(RandomUtils.nextInt(1, 4)*10);
            itemData.setDate(LocalDateTime.of(
                RandomUtils.nextInt(2016, 2020), 
                RandomUtils.nextInt(1, 13), 
                RandomUtils.nextInt(1, 29), 
                RandomUtils.nextInt(1,24), 
                RandomUtils.nextInt(1,60)));
            itemData.setAmount1(BigDecimal.valueOf(RandomUtils.nextLong(1, 10000)).divide(new BigDecimal("100"), 2,RoundingMode.HALF_UP));
            itemData.setAmount2(BigDecimal.valueOf(RandomUtils.nextLong(1, 10000)).divide(new BigDecimal("100"), 2,RoundingMode.HALF_UP));
            itemData.setSize(RandomUtils.nextLong(1, 10000));
            dataList100000.add(itemData);
            if(i<100) {
                dataList100.add(itemData);
            }
            if(i<10) {
                dataList10.add(itemData);
            }
        }
    }
    
    @Test
    public void testPrintInitData() {
        System.out.println(dataList100000);
    }
}
