package io.lighter;

import io.lighter.exception.InvalidHighlight;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lighter {

    private List<Highlight> highlights = new ArrayList<>();
    private Set<Object> traversed = new HashSet<>();
    private Builder builder;

    private Lighter(Builder builder) {
        this.builder = builder;
    }

    public List<Highlight> highlight() {
        try {
            highlight(builder.object, null, null);
        } catch (InvocationTargetException | IllegalAccessException | IntrospectionException e) {
            throw new RuntimeException(e);
        }
        return highlights;
    }

    private void highlight(Object object, Integer idx, String path) throws InvocationTargetException, IllegalAccessException, IntrospectionException {
        if (object == null) {
            return;
        }

        if (traversed.contains(object)) {
            return;
        }

        traversed.add(object);

        BeanInfo beanInfo = Introspector.getBeanInfo(object.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            Method method = propertyDescriptor.getReadMethod();

            if (method == null) {
                continue;
            }

            if (builder.ignoredClasses.contains(method.getDeclaringClass())) {
                continue;
            }

            if (!isGetter(method)) {
                continue;
            }

            Object value = method.invoke(object);

            String fixedPath = "";
            if (path != null) {
                fixedPath = path + ".";
            }

            String hierarchy = fixedPath + propertyDescriptor.getName();

            if (isHighlightableType(value)) {
                highlight(value, propertyDescriptor, hierarchy, idx);
            } else if (isArrayOrIterable(value)) {
                List list = toList(value);
                for (int i = 0; i < list.size(); i++) {
                    Object item = list.get(i);
                    if (isHighlightableType(item)) {
                        highlight(item, propertyDescriptor, hierarchy, i);
                    } else {
                        highlight(item, i, hierarchy);
                    }
                }
            } else {
                highlight(value, null, hierarchy);
            }
        }
    }

    private void highlight(Object value, PropertyDescriptor propertyDescriptor, String path, Integer idx) {

        String valueString = value.toString();

        String emphasizedValueString = valueString;
        if (builder.htmlTag != null && !builder.htmlTag.isEmpty()) {
            emphasizedValueString = valueString
                    .replaceAll("(?i)(" + builder.hl + ")", "<" + builder.htmlTag + ">$1</" + builder.htmlTag + ">");
        }

        String lowerCaseValue = valueString.toLowerCase();
        if (isEligibleField(path) && lowerCaseValue.contains(builder.hl)) {
            String suffixIdx = idx != null ? "[" + idx + "]" : "";

            String computedPath = path + suffixIdx;
            Matcher matcher = Pattern.compile("(.*)\\.(.*)").matcher(path);
            if (matcher.find()) {
                computedPath = matcher.group(1) + suffixIdx + "." + matcher.group(2);
            }

            String pathFieldName = (propertyDescriptor.getPropertyType() == String.class)
                    ? propertyDescriptor.getName()
                    : propertyDescriptor.getName() + suffixIdx;

            highlights.add(new Highlight(builder.hl, computedPath,
                    pathFieldName,
                    valueString,
                    emphasizedValueString));
        }
    }

    private boolean isHighlightableType(Object o) {
        return o instanceof String;
    }

    private boolean isEligibleField(String field) {
        if (builder.fields == null || builder.fields.isEmpty()) {
            return true;
        }
        return builder.fields.contains(field);
    }

    private boolean isGetter(Method method) {
        if (method.getName().startsWith("getClass")) return false;
        if (!method.getName().startsWith("get")) return false;
        if (method.getParameterTypes().length != 0) return false;
        return !void.class.equals(method.getReturnType());
    }

    private boolean isArrayOrIterable(Object o) {
        return o != null && (o.getClass().isArray() || o instanceof Iterable);
    }

    private List<Object> toList(Object o) {
        if (o.getClass().isArray()) {
            return Collections.singletonList(o);
        }
        List<Object> objects = new ArrayList<>();
        Iterable<Object> iterable = (Iterable<Object>) o;
        iterable.forEach(objects::add);
        return objects;
    }

    public static class Builder {
        private Object object;
        private String hl;
        private String htmlTag;
        private List<String> fields;
        private List<Class<?>> ignoredClasses = new ArrayList<>();

        public Builder(Object object, String hl) {
            this.object = object;
            if (hl == null || hl.isEmpty()) {
                throw new InvalidHighlight();
            } else {
                this.hl = hl.toLowerCase();
            }
        }

        public Builder htmlTag(String htmlTag) {
            this.htmlTag = htmlTag;
            return this;
        }

        public Builder ignoredClasses(Class<?>... ignoredClasses) {
            this.ignoredClasses = Arrays.asList(ignoredClasses);
            return this;
        }

        public Builder fields(String... fields) {
            this.fields = Arrays.asList(fields);
            return this;
        }

        public Lighter build() {
            return new Lighter(this);
        }
    }
}
