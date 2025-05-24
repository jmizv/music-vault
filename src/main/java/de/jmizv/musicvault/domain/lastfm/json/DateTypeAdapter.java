package de.jmizv.musicvault.domain.lastfm.json;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class DateTypeAdapter extends TypeAdapter<Long> {

  @Override
  public void write(JsonWriter jsonWriter, Long aLong) {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public Long read(JsonReader jsonReader) throws IOException {
    if (jsonReader.peek() == JsonToken.NULL) {
      jsonReader.nextNull();
      return null;
    }
    if (!jsonReader.getPath().endsWith(".date")) {
      throw new IllegalStateException("Cannot read long from JSON in path " + jsonReader.getPath());
    }
    jsonReader.beginObject();
    jsonReader.nextName();
    var uts = Long.parseLong(jsonReader.nextString());
    jsonReader.nextName();
    jsonReader.nextString();
    jsonReader.endObject();
    return uts;
  }
}
