package com.diegokrupitza.microcompiler.datastructures;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * Project: micro16-compiler
 * Document: Variable.java
 * Author: Diego Krupitza
 * Created: 12.12.18
 */
@Data
@Builder
@ToString
public class Variable {

    private String name;
    private String value;
    private boolean isInRegister;
    private String registerName;

}
