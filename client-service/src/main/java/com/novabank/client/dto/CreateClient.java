package com.novabank.client.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateClient {

    private String firstName;
    private String lastName;
    private String dni;
    private String email;
    private String phone;
}
