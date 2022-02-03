package hazelnut.core;

import com.google.common.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;

public interface Message<T> {

    @NotNull TypeToken<T> type();
}
