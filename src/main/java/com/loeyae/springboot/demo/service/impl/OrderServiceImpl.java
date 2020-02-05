package com.loeyae.springboot.demo.service.impl;

//import com.loeyae.springboot.demo.entity.Order;
//import com.loeyae.springboot.demo.mapper.OrderRepository;
//import com.loeyae.springboot.demo.service.OrderService;
//import org.elasticsearch.index.query.MatchQueryBuilder;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
//import org.springframework.data.elasticsearch.core.query.SearchQuery;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Order Service.
// *
// * @date: 2020-02-04
// * @version: 1.0
// * @author: zhangyi07@beyondsoft.com
// */
//public class OrderServiceImpl implements OrderService {
//
//
//    @Autowired
//    OrderRepository orderRepository;
//
//    @Override
//    public long count() {
//        return orderRepository.count();
//    }
//
//    @Override
//    public Order save(Order order) {
//        return orderRepository.save(order);
//    }
//
//    @Override
//    public void delete(Order order) {
//        orderRepository.delete(order);
//    }
//
//    @Override
//    public Iterable<Order> getAll() {
//        return orderRepository.findAll();
//    }
//
//    @Override
//    public List<Order> getByCommodityName(String name) {
//        List<Order> list = new ArrayList<>();
//        MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("commodityName", name);
//        Iterable<Order> iterable = orderRepository.search(matchQueryBuilder);
//        iterable.forEach(e->list.add(e));
//        return list;
//    }
//
//    @Override
//    public Page<Order> pageQuery(Integer pageNo, Integer pageSize, String kw) {
//        SearchQuery searchQuery = new NativeSearchQueryBuilder()
//                .withQuery(QueryBuilders.matchPhraseQuery("commodityName", kw))
//                .withPageable(PageRequest.of(pageNo, pageSize))
//                .build();
//        return orderRepository.search(searchQuery);
//    }
//}