package hazelnut.core.processor;

final class SuccessfulResponse implements Response {

    SuccessfulResponse() {}

    @Override
    public boolean status() {
        return true;
    }
}
