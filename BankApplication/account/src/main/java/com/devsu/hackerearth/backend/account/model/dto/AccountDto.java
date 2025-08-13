package com.devsu.hackerearth.backend.account.model.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {

	private Long id;
	@NotBlank(message = "number canot be null")
	private String number;
	@NotBlank(message = "type canot be null")
	private String type;
	private double initialAmount;
	private boolean isActive;
	private Long clientId;
}
