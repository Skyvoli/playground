package io.skyvoli.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class FullRecord {
    private List<StringData> fullRecord;

    public FullRecord(List<String> fullRecord) {
        this.fullRecord = fullRecord
                .stream()
                .map(StringData::new)
                .toList();
    }
}


