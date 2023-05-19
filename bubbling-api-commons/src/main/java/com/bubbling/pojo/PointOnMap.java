package com.bubbling.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.geo.Distance;

/**
 * 地图上点的实体类
 * 2022-03-19 15:05:18 GMT+8
 * @author k
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PointOnMap {
    private String id;
    private double x;
    private double y;
    private Distance distance;
}
