package com.diegokrupitza.microcompiler.datastructures;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Project: micro16-compiler
 * Document: OutsourcedRegister.java
 *
 * @author Diego Krupitza
 * @version 1.1
 * @date 03.01.19
 */
@Data
@Builder
@AllArgsConstructor
public class OutsourcedRegister {

    private String registerName;
    private String instruction;

}
