package hazelnut.core.protocol;

import com.eclipsesource.json.JsonObject;
import hazelnut.core.translation.MessageTranslator;
import hazelnut.core.translation.TranslationException;
import org.jetbrains.annotations.NotNull;

public final class HeartbeatTranslator implements MessageTranslator<Heartbeat> {

    @Override
    public @NotNull Class<Heartbeat> type() {
        return Heartbeat.class;
    }

    @Override
    public @NotNull JsonObject toIntermediary(final @NotNull Heartbeat object) throws TranslationException {
        return new JsonObject();
    }

    @Override
    public @NotNull Heartbeat fromIntermediary(final @NotNull JsonObject intermediary) throws TranslationException {
        return new Heartbeat();
    }
}
