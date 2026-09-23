package com.example.msa_monolihic.ordering.controller;


import com.example.msa_monolihic.ordering.domain.Ordering;
import com.example.msa_monolihic.ordering.dto.OrderCreateDTO;
import com.example.msa_monolihic.ordering.service.OrderingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ordering")
public class OrderingController {

    private final OrderingService orderingService;


    public OrderingController(OrderingService service) {
        this.orderingService = service;
    }


    @PostMapping("/create")
    public ResponseEntity<?> ordercreate(@RequestBody OrderCreateDTO dto){

        System.out.println("<OrderingController - ordercreate>");

        Ordering ordering = orderingService.orderCreate(dto);
        return new ResponseEntity<>(ordering.getId(), HttpStatus.CREATED);
    }
}
