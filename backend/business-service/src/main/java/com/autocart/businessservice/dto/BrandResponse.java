package com.autocart.businessservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BrandResponse {

    private String id;

    private String name;

    private String logoUrl;

    private String originCountry;

}