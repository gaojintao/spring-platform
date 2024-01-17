package com.example.spring.redis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName ListDemoController
 * @Description TODO
 * @Author GaoJinTao
 * @Date 2024/01/15 17:17
 * @Version 1.0
 **/
@RestController
@RequestMapping("/redisList")
public class ListDemoController {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping("/demo1")
    public void demo1() {
        //从左边插入，即插入到列表头部
        redisTemplate.opsForList().leftPush("product:list", "iphone xs max");
        redisTemplate.opsForList().leftPush("product:list", "thinkpad x1 carbon");
        redisTemplate.opsForList().leftPush("product:list", "mackBook pro13");
        redisTemplate.opsForList().leftPush("product:list", "HuaWei Mate20 pro");
    }

    @GetMapping("/demo2")
    public void demo2() {
        //从左边插入一个数组
        String[] books = new String[] {"java编程思想", "springboot从入门到精通"};
        redisTemplate.opsForList().leftPushAll("book:list", books);
    }

    @GetMapping("/demo3")
    public void demo3() {
        //从左边插入一个集合
        List<String> list = new ArrayList<String>();
        list.add("鬼泣5");
        list.add("荒野大镖客2");
        list.add("仙剑奇侠传7");
        redisTemplate.opsForList().leftPushAll("game:list", list);
    }

    @GetMapping("/demo4")
    public void demo4() {
        //如果存在key对应的列表，则从左插入，不存在不做操作
        redisTemplate.opsForList().leftPushIfPresent("fruit:list", "1");
    }

    @GetMapping("/demo5")
    public void demo5() {
        //在key对应的列表中从左边开始找，找到第一个pivot，然后把value插到pivot左边，没有不做操作
        redisTemplate.opsForList().leftPush("product:list", "HuaWei Mate20X", "xiaomi mix");
    }

    //也可以从右边插入，把上面的left改为right即可

    @GetMapping("/demo6")
    public void demo6() {
        //指定位置重新设置指定值
        redisTemplate.opsForList().set("product:list", 1, "dell xps13");
    }

    @GetMapping("/demo7")
    public void demo7() {
        //删除和value相同的count个元素，count < 0，从右开始,count > 0，从左开始,count = 0，全部
        redisTemplate.opsForList().remove("product:list", -1, "HuaWei Mate20 pro");
    }

    @GetMapping("/demo8")
    public void demo8() {
        //获取制定下标对应的值 index,从0开始，有正负两套下标
        //[a,b,c,d] 下标有[0,1,2,3]和[0,-3,-2,-1];
        String value = redisTemplate.opsForList().index("product:list", 1);
        System.out.println(value);
    }

    @GetMapping("/demo9")
    public void demo9() {
        //查询list中指定范围的内容
        List<String> list = redisTemplate.opsForList().range("product:list", 0, -1);
        System.out.println(list);

        //修剪列表，使其只包含指定范围内的元素
        redisTemplate.opsForList().trim("product:list", 0, 2);

        //查询列表长度
        System.out.println(redisTemplate.opsForList().size("product:list"));
    }

    @GetMapping("/demo10")
    public void demo10() {
        //弹出最左边元素
        redisTemplate.opsForList().leftPop("product:list");
        //移出并获取列表的第一个元素， 如果列表没有元素会阻塞列表直到等待超时。
        redisTemplate.opsForList().leftPop("product:list", 10, TimeUnit.SECONDS);


        //弹出最右边元素
        redisTemplate.opsForList().rightPop("product:list");

        //弹出k1最右边元素并放入k2最左边
        redisTemplate.opsForList().rightPopAndLeftPush("product:list", "game:list");
    }
}
