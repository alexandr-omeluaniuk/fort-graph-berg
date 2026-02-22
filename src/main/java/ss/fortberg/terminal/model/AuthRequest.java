package ss.fortberg.terminal.model;

public record AuthRequest(
    String pin,
    Boolean rememberPin,
    Boolean returnToken
) {
}
