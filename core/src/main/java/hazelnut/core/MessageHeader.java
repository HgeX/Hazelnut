package hazelnut.core;

import com.google.common.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface MessageHeader {

    @NotNull UUID messageId();

    @NotNull String originId();

    @NotNull TypeToken<?> type();
}
