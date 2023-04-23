package jipdol2.eunstargram.exception;

public class ValidationDuplicateMemberNickname extends JipDol2Exception{

    private static final String MESSAGE = "중복된 닉네임이 존재합니다.";

    public ValidationDuplicateMemberNickname() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 409;
    }
}
