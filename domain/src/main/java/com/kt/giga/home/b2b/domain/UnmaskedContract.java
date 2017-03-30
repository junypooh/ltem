package com.kt.giga.home.b2b.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * com.kt.giga.home.b2b.domain
 * <p>
 * Created by cecil on 2017. 3. 22..
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UnmaskedContract implements Serializable {

    private static final long serialVersionUID = -5630624943071810225L;

    String bizContractNumber;
    Long serviceContractNumber;

}
