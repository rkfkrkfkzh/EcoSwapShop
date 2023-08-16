package shop.ecoswapshop.controller;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class MemberForm {
    private String username;
    private String password;
    private String passwordConfirm;
    private String email;
    private String fullName;
    private String phoneNumber;
    private String city;
    private String street;
    private String zipcode;
}
