package com.pluto.maven.jdk8.stream;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToLongFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

import com.pluto.maven.jdk8.pojo.UserData;
import com.pluto.maven.jdk8.utils.CollectorUtils;


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
            .collect(CollectorUtils.countWithMap(UserData::getStatus,()->map3)
        );
        System.out.println(map3);
        System.out.println(map4);
    }
    
    
    @Test
    public void testGroupBy3() {
        //统计每个状态的Size和最小Size
        Map<Integer, List<Long>> map1 = dataList100.stream()
            .sorted(Comparator.comparing(UserData::getStatus))
            .collect(Collectors.groupingBy(
                UserData::getStatus,
                Collectors.collectingAndThen(
                    Collectors.summarizingLong(UserData::getSize),
                    dss->Arrays.asList(dss.getMax(),dss.getMin())
                        
                    )
            ));
        System.out.println(map1);
    }
    
    @Test
    public void testGroupBy4() {
        //统计size >=10,>=100,>=1000,>=10000的所有元素个数
//        Supplier<Long[]> supplier = ()->{
//            Long[] arrays = {0l,0l,0l,0l};
//            return arrays;
//        };
        
//        BiConsumer<Long, Long[]> summaryer = (t,u)->{
//          if(t>=10) u[0]=u[0]+1;  
//          if(t>=100) u[1]=u[1]+1; 
//          if(t>=1000) u[2]=u[2]+1;  
//          if(t>=10000) u[3]=u[3]+1;  
//        };


//         Collector<UserData,Long[],List<Long>> myCollector = Collector.of(
//             ()->{
//                 Long[] arrays = {0l,0l,0l,0l};
//                 return arrays;
//             },
//             (a,b)->{
//                 summaryer.accept(b.getSize(), a);
//             },
//             (a,b)->{
//                 IntStream.range(0, a.length).forEach(i->a[i]=a[i]+b[i]);
//                 return a;
//              },
//             (a)-> Arrays.asList(a)
//             
//          );
        
        List<Long> list = dataList100000.stream()
            .collect(getMyCollector(
                UserData::getSize,
                Arrays.asList(
                    (t,u)-> {if(t>=10) u[0]=u[0]+1;},
                    (t,u)-> {if(t>=100) u[1]=u[1]+1;},
                    (t,u)-> {if(t>=1000) u[2]=u[2]+1;},
                    (t,u)-> {if(t>=10000) u[3]=u[3]+1;},
                    (t,u)-> {if(t>=100000) u[4]=u[4]+1;}
                    )));
            
        System.out.println(list);
    }
    
    

    @Test
    public void testGroupBy5() {
        // 测试并发
        Clock clock = Clock.system(ZoneId.of("UTC+8"));
        Map<Integer, Long> map1 = new ConcurrentHashMap<>();
        long begin = clock.millis();
        dataList1000000.parallelStream()
        .collect(CollectorUtils.countWithMap(UserData::getStatus, () -> map1));
        long end = clock.millis();
        System.out.println(map1);
        System.out.println(end-begin);
        
        ConcurrentHashMap<Integer, Long> map7 = new ConcurrentHashMap<>();
        long begin7 = clock.millis();
        dataList1000000.parallelStream()
        .collect(CollectorUtils.countWithConcurrentMap(UserData::getStatus, () -> map7));
        long end7 = clock.millis();
        System.out.println(map7);
        System.out.println(end7-begin7);
        
//        
//        Map<Integer, Long> map2 = new HashMap<>();
//        map2.put(10, 0l);
//        map2.put(20, 0l);
//        map2.put(30, 0l);
//        long begin2 = clock.millis();
//        dataList1000000.stream()
//        .collect(CollectorUtils.countWithMap(UserData::getStatus, () -> map2));
//        long end2= clock.millis();
//        System.out.println(map2);
//        System.out.println(end2-begin2);
//        
//        Map<Integer, Long> map3 = new ConcurrentHashMap<>();
//        long begin3 = clock.millis();
//        dataList1000000.parallelStream()
//        .collect(Collectors.groupingBy(UserData::getStatus,()->map3,Collectors.counting()));
//        long end3= clock.millis();
//        System.out.println(map3);
//        System.out.println(end3-begin3);
//        
//        Map<Integer, Long> map4 = new ConcurrentHashMap<>();
//        long begin4 = clock.millis();
//        dataList1000000.stream()
//        .collect(Collectors.groupingBy(UserData::getStatus,()->map4,Collectors.counting()));
//        long end4= clock.millis();
//        System.out.println(map4);
//        System.out.println(end4-begin4);
        
//        ConcurrentHashMap<Integer, Long> map5 = new ConcurrentHashMap<>();
//        long begin5 = clock.millis();
//        long sum5 = dataList1000.parallelStream().mapToLong(UserData::getAge).sum();
//        .collect(Collectors
//            .groupingByConcurrent(UserData::getStatus,()->map5,
//                Collectors.summingLong((e)->{
//                try {TimeUnit.MILLISECONDS.sleep(1);} catch (InterruptedException ex) {}
//                    return 1L;})));
//        long end5= clock.millis();
//        System.out.println(sum5);
//        System.out.println(end5-begin5);
        
//        ConcurrentHashMap<Integer, Long> map6 = new ConcurrentHashMap<>();
//        long begin6 = clock.millis();
//        long sum6 = dataList1000.stream().mapToLong(UserData::getAge).sum();
//        .collect(Collectors
//            .groupingByConcurrent(UserData::getStatus,()->map6,
//                Collectors.summingLong((e)->{
//                try {TimeUnit.MILLISECONDS.sleep(1);} catch (InterruptedException ex) {}
//                    return 1L;})));
//        long end6= clock.millis();
//        System.out.println(sum6);
//        System.out.println(end6-begin6);

        
        
    }
    
   private <T> Collector<T,Long[],List<Long>> getMyCollector(ToLongFunction<T> mapper,
       List<BiConsumer<Long,Long[]>> accumulatorList){
       
       BiConsumer<Long, Long[]> summaryer = (t,u)->{
           accumulatorList.forEach(r->{
               r.accept(t, u);
           }); 
         };
         
       Collector<T,Long[],List<Long>> myCollector = Collector.of(
           ()->{
                List<Long> list = accumulatorList.stream().map(r->0l).collect(Collectors.toList()) ;
                return list.toArray(new Long[accumulatorList.size()]);
           },
           (a,b)->{
               summaryer.accept(mapper.applyAsLong(b), a);
           },
           (a,b)->{
               IntStream.range(0, a.length).forEach(i->a[i]=a[i]+b[i]);
               return a;
            },
           (a)-> Arrays.asList(a)
           
        );
       return myCollector;
   }
    

}
