package com.postvan;

import com.postvan.models.PostmanVariablePool;
import lombok.val;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.regex.Pattern;

public class Utils {
    public static String readStringFromResource(final String resourcePath) {
        try (final var inputStream = Utils.class.getResourceAsStream(resourcePath)) {
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            for (int length; (length = Objects.requireNonNull(inputStream).read(buffer)) != -1; ) {
                result.write(buffer, 0, length);
            }
            return result.toString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String replaceVariables(final String preReplaced, final PostmanVariablePool postmanVariablePool) {
        val postmanVariablePattern = Pattern.compile("\\{\\{(\\w+)\\}\\}");
        val matcher = postmanVariablePattern.matcher(preReplaced);
        return matcher.replaceAll(result -> postmanVariablePool.computeIfAbsent(result.group().replace("{{", "").replace("}}", ""), result::group));
    }
}
