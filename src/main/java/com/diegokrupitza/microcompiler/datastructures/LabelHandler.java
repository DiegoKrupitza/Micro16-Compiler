package com.diegokrupitza.microcompiler.datastructures;

import com.diegokrupitza.microcompiler.helper.MathematicalHelper;
import org.apache.commons.codec.binary.Base32;

import java.util.ArrayList;
import java.util.List;

/**
 * Project: micro16-compiler
 * Document: LabelHandler.java
 *
 * @author Diego Krupitza
 * @version 1.1
 * @date 25.12.18
 */
public class LabelHandler {

    private static final List<String> LABEL_LIST = new ArrayList<>();

    /**
     * Generates a new label with a prefix and adds it to the label list
     * That generated label can be used for jump in micro16
     *
     * @param prefix the prefix of that given
     * @return the generated Label
     */
    public static String generateNewLabel(String prefix) {
        StringBuilder label = new StringBuilder();

        // generating a new label that often
        // until there is one generated that does not exist
        do {
            label = new StringBuilder();
            label.append(prefix);

            byte[] bytes = MathematicalHelper.generateRandom32Byte();
            String tempBase32 = new Base32().encodeAsString(bytes);

            // removing the '=' because they can lead to problems in the micro16 code
            tempBase32 = tempBase32.replace("=", "");

            label.append(tempBase32);
        } while (LABEL_LIST.contains(label.toString()));

        LABEL_LIST.add(label.toString());

        return label.toString();
    }

    /**
     * Generates a new label and adds it to the label list
     * That generated label can be used for jump in micro16
     *
     * @return the generated Label
     */
    public static String generateNewLabel() {
        return generateNewLabel("");
    }


}
