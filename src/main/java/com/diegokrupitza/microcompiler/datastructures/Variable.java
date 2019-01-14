package com.diegokrupitza.microcompiler.datastructures;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.ToString;

/**
 * Project: micro16-compiler
 * Document: Variable.java
 *
 * @author Diego Krupitza
 * @version 1.1
 * @date 12.12.18
 */
@Data
@Builder
@ToString
public class Variable {

    @NonNull
    private String name;

    @NonNull
    private String value;

    private boolean isInRegister = false;

    @NonNull
    private String registerName;

    private int memoryAddress = -1;

    private int accessCount = 0;
}
