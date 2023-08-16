package com.postvan.defaults;

import com.postvan.models.PostmanEnvironment;
import com.postvan.models.PostmanEnvironmentVariableFetcher;
import lombok.Data;
import lombok.ToString;
import lombok.val;
import lombok.var;
import org.apache.commons.codec.binary.StringUtils;

import java.util.Objects;
import java.util.regex.Pattern;

@Data
public class PostmanEnvironmentVariableFetcherImpl implements PostmanEnvironmentVariableFetcher<PostmanEnvironment> {
    @ToString.Exclude
    private final PostmanEnvironment self;
    private static final String POSTMAN_FIND_ENV_REGEX = "\\{\\{([^\\s{}]+)\\}\\}";
    private static final Pattern POSTMAN_FIND_ENV_PATTERN = Pattern.compile(POSTMAN_FIND_ENV_REGEX);

    @Override
    public String replacer(final String valueToReplace) {
        var finalString = valueToReplace;
        val matcher = POSTMAN_FIND_ENV_PATTERN.matcher(finalString);
        while (matcher.find()) {
            val varName = matcher.group();
            val replacement = this.self.getValues()
                    .stream()
                    .filter(value -> StringUtils.equals(String.format("{{%s}}", value.getKey()), varName))
                    .findFirst()
                    .orElse(null);

            if (Objects.nonNull(replacement)) {
                finalString = matcher.replaceFirst(replacement.getValue());
            }
        }
        return finalString;
    }

    @Override
    public String revealer(final String secretToReveal) {
        return null;
    }
}
