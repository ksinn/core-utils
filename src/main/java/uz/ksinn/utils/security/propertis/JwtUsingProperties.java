package uz.ksinn.utils.security.propertis;

public class JwtUsingProperties {

    private final String key;

    public JwtUsingProperties(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
