package com.devsu.hackerearth.backend.account.client.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ClientDto implements Serializable {
    private Long id;
    private String name;
    private boolean isActive;
}