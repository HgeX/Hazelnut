package hazelnut.core;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface MessageHeader {

    @NotNull UUID messageId();

    @NotNull String originId();
}
