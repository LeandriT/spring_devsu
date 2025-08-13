package com.devsu.hackerearth.backend.client.model.dto;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientDto {

	private Long id;
	@NotBlank(message = "dni cannot be blank")
	private String dni;
	@NotBlank(message = "name cannot be blank")
	private String name;
	@NotBlank(message = "password cannot be blank")
	private String password;
	@NotBlank(message = "gender cannot be blank")
	private String gender;
	private int age;
	private String address;
	private String phone;
	private boolean isActive;
}
