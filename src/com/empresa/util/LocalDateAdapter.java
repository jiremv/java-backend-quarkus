package com.empresa.util;

import com.squareup.moshi.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
public class LocalDateAdapter extends JsonAdapter<LocalDate> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @Override
    public LocalDate fromJson(JsonReader reader) throws IOException {
        String date = reader.nextString();
        return date != null ? LocalDate.parse(date, FORMATTER) : null;
    }
    @Override
    public void toJson(JsonWriter writer, LocalDate value) throws IOException {
        writer.value(value != null ? value.format(FORMATTER) : null);
    }
}