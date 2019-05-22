
package com.pluto.maven.jdk8.stream;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @Before
    public void initDate() {
        for(int i=0;i<100;i++) {
            UserData itemData = new UserData();
            itemData.setName(RandomStringUtils.random(3,"abc"));
            itemData.setStatus(RandomUtils.nextInt(1, 6)*10);
            itemData.setDate(LocalDateTime.of(
                RandomUtils.nextInt(2016, 2019), 
                RandomUtils.nextInt(1, 12), 
                RandomUtils.nextInt(1, 28), 
                RandomUtils.nextInt(1,24), 
                RandomUtils.nextInt(1,60)));
            itemData.setAmount1(BigDecimal.valueOf(RandomUtils.nextLong(1, 10000)).divide(new BigDecimal("100"), 2,RoundingMode.HALF_UP));
            itemData.setAmount2(BigDecimal.valueOf(RandomUtils.nextLong(1, 10000)).divide(new BigDecimal("100"), 2,RoundingMode.HALF_UP));
            dataList100000.add(itemData);
        }
    }
    
    @Test
    public void testPrintInitData() {
        System.out.println(dataList100000);
    }
}
