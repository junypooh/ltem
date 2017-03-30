package com.kt.giga.home.b2b.domain;

import lombok.Data;

/**
 * com.kt.giga.home.b2b.domain
 * <p>
 * Created by cecil on 2017. 3. 22..
 */
@Data
public class UnmaskRequest {

    String password;
    String reason;
}
