package com.leenx.learn.mavenproject;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author leen-x
 * @Description: MapStruct BeanCopy工具 https://mapstruct.org/
 * @date 2021/09/06 10:41 上午
 **/
public class MapStructDemo {
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DemoA {
        String name;

        Integer age;

        List<String> sons;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DemoB {
        String name;

        Integer age;

        List<String> sons;
    }

    @Mapper
    public interface DemoCovertMapper {
        DemoCovertMapper INSTANCE = Mappers.getMapper(DemoCovertMapper.class);

        MapStructDemo.DemoB convertDemoA2DemoB(MapStructDemo.DemoA a);

        MapStructDemo.DemoA convertDemoB2DemoA(MapStructDemo.DemoB b);
    }

    public static void main(String[] args) {
        DemoA demoA = DemoA.builder()
                .name("xxx")
                .build();
        DemoCovertMapper mapper = DemoCovertMapper.INSTANCE;
        DemoB demoB = mapper.convertDemoA2DemoB(demoA);
        System.out.println(JSON.toJSONString(demoB));
    }
}
