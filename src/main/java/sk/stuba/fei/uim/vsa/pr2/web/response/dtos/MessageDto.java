package sk.stuba.fei.uim.vsa.pr2.web.response.dtos;

public class MessageDto {

    private String message;
    private Boolean error;

    public MessageDto(String message, Boolean error) {
        this.message = message;
        this.error = error;
    }

    public MessageDto() {
    }

    public static MessageDto buildError(String message) {
        return new MessageDto(message, true);
    }
}
