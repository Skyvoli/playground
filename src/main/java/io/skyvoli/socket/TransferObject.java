package io.skyvoli.socket;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
@Getter
@AllArgsConstructor
public class TransferObject implements Serializable {
    private String text;
    private int steps;
    private String decodedText;
}
