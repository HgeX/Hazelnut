package hazelnut.core.processor;

import com.google.common.reflect.TypeToken;
import hazelnut.core.Message;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

interface Response extends Message<Response> {
    TypeToken<Response> TYPE = TypeToken.of(Response.class);

    static @NotNull Response noop() {
        return NoOp.INSTANCE;
    }

    static @NotNull Response next() {
        return Next.INSTANCE;
    }

    @Override
    default @NotNull TypeToken<Response> type() {
        return TYPE;
    }

    @NotNull UUID originalId();

    boolean status();

    final class NoOp implements Response {
        private static final UUID NOOP_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");
        private static final NoOp INSTANCE = new NoOp();

        private NoOp() {}

        @Override
        public boolean status() {
            return true;
        }

        @Override
        public @NotNull UUID originalId() {
            return NOOP_ID;
        }
    }

    final class Next implements Response {
        private static final UUID NEXT_ID = UUID.fromString("ffffffff-ffff-ffff-ffff-ffffffffffff");
        private static final Next INSTANCE = new Next();

        private Next() {}

        @Override
        public boolean status() {
            return true;
        }

        @Override
        public @NotNull UUID originalId() {
            return NEXT_ID;
        }
    }
}
