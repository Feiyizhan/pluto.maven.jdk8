package com.pluto.maven.jdk8.stream;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.Test;

import com.pluto.maven.jdk8.pojo.UserData;


/**
   *   测试GroupingBy
 * @author 徐明龙 XuMingLong 2019-05-22
 */
public class GroupingByTest extends BaseTests {
    
    private Map<Integer, List<UserData>> getInitListMap(){
        System.out.println("List Map Called");
        return statusList.stream().collect(Collectors.toMap(
            Function.identity(), 
            (v)-> new LinkedList<>(),
            (k1,k2)->k2,
            LinkedHashMap::new
            ));
    }
    
    private Map<Integer, Long> getInitNumbersMap(){
        System.out.println("List Map Called");
        return statusList.stream().collect(Collectors.toMap(
            Function.identity(), 
            (v)-> 0l,
            (k1,k2)->k2,
            LinkedHashMap::new
            ));
    }
    
    @Test
    public void testGroupBy1() {
        Map<Integer, List<UserData>> map1 = dataList10.stream()
            .sorted(Comparator.comparing(UserData::getStatus))
            .collect(Collectors.groupingBy(
                UserData::getStatus
            ));
        System.out.println(map1);
        
        Map<Integer, List<UserData>> map2 = dataList10.stream()
            .sorted(Comparator.comparing(UserData::getStatus))
            .collect(Collectors.groupingBy(
            UserData::getStatus,
            LinkedHashMap::new,
            Collectors.toList()
        ));
        System.out.println(map2);
        
        Map<Integer, List<UserData>> map3 = dataList10.stream()
            .sorted(Comparator.comparing(UserData::getStatus))
            .collect(Collectors.groupingBy(
            UserData::getStatus,
            ()->getInitListMap(),
            Collectors.toList()
        ));
        System.out.println(map3);
    }
    
    @Test
    public void testGroupBy2() {
        Map<Integer, Long> map1 = dataList100.stream()
            .sorted(Comparator.comparing(UserData::getStatus))
            .collect(Collectors.groupingBy(
                UserData::getStatus,
                Collectors.counting()
            ));
        System.out.println(map1);
        
        Map<Integer, Long> map2 = dataList100.stream()
            .sorted(Comparator.comparing(UserData::getStatus))
            .collect(Collectors.groupingBy(
            UserData::getStatus,
            LinkedHashMap::new,
            Collectors.counting()
        ));
        System.out.println(map2);
        map2.clear();
        
        Map<Integer, Long> map3 = dataList100.stream()
            .sorted(Comparator.comparing(UserData::getStatus))
            .collect(Collectors.groupingBy(
            UserData::getStatus,
            ()->map2,
            Collectors.counting()
        ));
        System.out.println(map3);
        
        map3.remove(30);
        Map<Integer, Long> map4 = dataList100.stream()
            .sorted(Comparator.comparing(UserData::getStatus))
            .collect(Collectors.toMap(
            UserData::getStatus,
            (v)->1l,
            Long::sum,
            ()->map3
        ));
        System.out.println(map4);
    }

}
