package hazelnut.core.protocol;

import hazelnut.core.Message;
import org.jetbrains.annotations.NotNull;

public class Heartbeat implements Message<Heartbeat> {

    @Override
    public @NotNull Class<Heartbeat> type() {
        return Heartbeat.class;
    }
}
