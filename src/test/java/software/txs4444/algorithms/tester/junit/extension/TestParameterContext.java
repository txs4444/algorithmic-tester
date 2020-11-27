package software.txs4444.algorithms.tester.junit.extension;

import org.junit.jupiter.api.extension.ParameterContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Optional;

public class TestParameterContext implements ParameterContext {
    private static final String UNSUPPORTED_OPERATION_ERROR_MSG = "This implementation of ParameterContext is only for testing purpose - not all methods are implemented";
    private final Parameter parameter;

    public TestParameterContext(Parameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public Parameter getParameter() {
        return parameter;
    }

    @Override
    public int getIndex() {
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_ERROR_MSG);
    }

    @Override
    public Optional<Object> getTarget() {
        return Optional.empty();
    }

    @Override
    public boolean isAnnotated(Class<? extends Annotation> annotationType) {
        return findAnnotation(annotationType).isPresent();
    }

    @Override
    public <A extends Annotation> Optional<A> findAnnotation(Class<A> annotationType) {
        return Optional.ofNullable(parameter.getAnnotation(annotationType));
    }

    @Override
    public <A extends Annotation> List<A> findRepeatableAnnotations(Class<A> annotationType) {
        throw new UnsupportedOperationException();
    }
}
