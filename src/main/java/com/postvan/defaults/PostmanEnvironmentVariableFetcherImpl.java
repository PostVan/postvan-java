package com.postvan.defaults;

import com.postvan.models.PostmanEnvironment;
import com.postvan.models.PostmanEnvironmentVariable;
import com.postvan.models.PostmanEnvironmentVariableFetcher;
import lombok.Data;
import lombok.ToString;
import lombok.val;
import lombok.var;
import org.apache.commons.codec.binary.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

@Data
public class PostmanEnvironmentVariableFetcherImpl implements PostmanEnvironmentVariableFetcher<List<PostmanEnvironmentVariable>> {
    @ToString.Exclude
    private static final String POSTMAN_FIND_ENV_REGEX = "\\{\\{([^\\s{}]+)\\}\\}";
    private static final Pattern POSTMAN_FIND_ENV_PATTERN = Pattern.compile(POSTMAN_FIND_ENV_REGEX);

    public PostmanEnvironmentVariableFetcherImpl(PostmanEnvironment self) {
    }

    @Override
    public String replacer(final String valueToReplace) {
        val environment = PostmanEnvironment.getInstance();
        var finalString = valueToReplace;
        val matcher = POSTMAN_FIND_ENV_PATTERN.matcher(finalString);
        while (matcher.find()) {
            val varName = matcher.group();
            val replacement = environment.getValues()
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

    @Override
    public List<PostmanEnvironmentVariable> getValues() {
        return null;
    }
}
