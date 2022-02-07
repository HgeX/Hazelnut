package hazelnut.core.processor;

import com.google.common.reflect.TypeToken;
import hazelnut.core.Message;
import org.jetbrains.annotations.NotNull;

interface Response extends Message<Response> {
    TypeToken<Response> TYPE = TypeToken.of(Response.class);

    static @NotNull Response success() {
        return new SuccessfulResponse();
    }

    static @NotNull Response failed(final @NotNull Throwable cause) {
        return new FailedResponse(cause.getClass().getName(), cause.getMessage());
    }

    static @NotNull Response failed() {
        return new FailedResponse(null, null);
    }

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

    boolean status();

    final class NoOp implements Response {
        private static final NoOp INSTANCE = new NoOp();

        private NoOp() {}

        @Override
        public boolean status() {
            return true;
        }
    }

    final class Next implements Response {
        private static final Next INSTANCE = new Next();

        private Next() {}

        @Override
        public boolean status() {
            return true;
        }
    }
}
