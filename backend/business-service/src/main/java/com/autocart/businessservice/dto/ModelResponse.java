package com.autocart.businessservice.dto;

import com.autocart.businessservice.entity.BodyType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ModelResponse {

    private String id;

    private String name;

    private BodyType bodyType;

    private Integer launchYear;

}