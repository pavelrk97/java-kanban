package type.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;
import java.util.Objects;

public class DurationAdapter extends TypeAdapter<Duration> {
    @Override
    public void write(JsonWriter jsonWriter, Duration value) throws IOException {
        if (Objects.isNull(value)) {
            jsonWriter.value("null");
        } else {
            jsonWriter.value(value.toString());
        }
    }

    @Override
    public Duration read(JsonReader jsonReader) throws IOException {
        String durationString = jsonReader.nextString();

        if (durationString == null || durationString.trim().isEmpty()) {
            // Вернем null, если строка пуста или null
            return null;
        }

        try {
            return Duration.parse(durationString); // Преобразуем строку в Duration
        } catch (Exception e) {
            // Логируем ошибку и выбрасываем исключение, если строка не может быть преобразована в Duration
            throw new IOException("Invalid duration format: " + durationString, e);
        }
    }
}
