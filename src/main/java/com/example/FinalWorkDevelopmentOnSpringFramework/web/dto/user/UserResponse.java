package com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.user;

import com.example.FinalWorkDevelopmentOnSpringFramework.model.user.en.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String name;
    private  String  e_mail;
    private List<RoleType> roles;

}
