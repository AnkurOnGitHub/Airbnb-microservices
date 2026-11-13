package com.airbnb.property_service.controllers;


import com.airbnb.property_service.services.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/properties")
public class PropertyController {
    private final PropertyService propertyService;

}
