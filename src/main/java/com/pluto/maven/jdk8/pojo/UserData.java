
package com.pluto.maven.jdk8.pojo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author 徐明龙 XuMingLong 2019-05-22
 */
@Getter@Setter@ToString@EqualsAndHashCode
public class UserData {
    
    private String name;
    
    private LocalDateTime date;
    
    private Integer status;
    
    private BigDecimal amount1;
    
    private BigDecimal amount2;
    
    private Long size;
    
}
