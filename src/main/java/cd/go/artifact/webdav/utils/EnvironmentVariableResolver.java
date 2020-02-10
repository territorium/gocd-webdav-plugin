
package cd.go.artifact.webdav.utils;

import org.apache.commons.lang3.text.StrSubstitutor;

import java.util.Map;
import java.util.regex.Pattern;

public class EnvironmentVariableResolver {

  private static final Pattern ENVIRONMENT_VARIABLE_PATTERN = Pattern.compile("\\$\\{(.*)\\}");
  private final String         property;
  private String               propertyName;

  public EnvironmentVariableResolver(String property, String propertyName) {
    this.property = property;
    this.propertyName = propertyName;
  }

  public String resolve(Map<String, String> environmentVariables) throws UnresolvedPropertyException {
    String evaluatedProperty = StrSubstitutor.replace(property, environmentVariables);
    if (ENVIRONMENT_VARIABLE_PATTERN.matcher(evaluatedProperty).find()) {
      throw new UnresolvedPropertyException(evaluatedProperty, propertyName);
    }
    return evaluatedProperty;
  }

  public static class UnresolvedPropertyException extends Exception {

    private static final long serialVersionUID = 4430820105148753576L;
    private final String      partiallyResolvedTag;

    public UnresolvedPropertyException(String partiallyResolvedTag, String propertyName) {
      super(String.format("Failed to resolve one or more variables in %s: %s", propertyName, partiallyResolvedTag));
      this.partiallyResolvedTag = partiallyResolvedTag;
    }

    public String getPartiallyResolvedTag() {
      return partiallyResolvedTag;
    }
  }
}
