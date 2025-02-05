package com.dacs.bookwise.request;

import lombok.Data;
import lombok.NonNull;

@Data
public class UserRequest {
    @NonNull
    private String name;
    @NonNull
    private Long age;
    @NonNull
    private String email;
    @NonNull
    private String username;
    @NonNull
    private String password;
}
