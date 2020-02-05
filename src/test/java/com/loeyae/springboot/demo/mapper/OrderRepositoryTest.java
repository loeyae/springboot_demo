package com.loeyae.springboot.demo.mapper;

//import com.loeyae.springboot.demo.entity.Order;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.math.BigDecimal;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest
//public class OrderRepositoryTest {
//
//    @Autowired
//    private OrderRepository orderRepository;
//
//    @Test
//    public void testIndex() {
//        Order order = new Order();
//        order.setId(1);
//        order.setCommodityId(1);
//        order.setCommodityName("测试菜品1");
//        order.setStoreId(1);
//        order.setStoreName("测试店铺1");
//        order.setPrice(BigDecimal.valueOf(1.20));
//        order.setAmount(BigDecimal.valueOf(40));
//        Order o = orderRepository.index(order);
//        assertEquals(order, o);
//    }
//
//}