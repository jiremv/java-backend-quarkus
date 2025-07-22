package com.empresa.util;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
public class SqlDateDeserializer extends JsonAdapter<Date> {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    @Override
    public Date fromJson(JsonReader reader) throws IOException {
        String dateStr = reader.nextString();
        try {
            java.util.Date utilDate = DATE_FORMAT.parse(dateStr);
            return new Date(utilDate.getTime());
        } catch (ParseException e) {
            throw new IOException("Error parsing SQL date from JSON", e);
        }
    }
    @Override
    public void toJson(JsonWriter writer, Date value) throws IOException {
        writer.value(value != null ? DATE_FORMAT.format(value) : null);
    }

}
