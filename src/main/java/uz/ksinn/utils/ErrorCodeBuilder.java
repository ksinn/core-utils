package uz.ksinn.utils;


public class ErrorCodeBuilder {

    private final String serviceShortName;
    private final String format;

    public ErrorCodeBuilder(String serviceShortName) {
        this.serviceShortName = serviceShortName;
        format = serviceShortName
                .toUpperCase()
                .replace(" ", "_").replace("-", "_")
                + "__%s";
    }

    public String buildErrorCode(String serviceCode) {
        return String.format(format, serviceCode);
    }
}
