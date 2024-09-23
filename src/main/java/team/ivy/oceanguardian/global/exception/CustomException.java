package team.ivy.oceanguardian.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import team.ivy.oceanguardian.global.exception.errorcode.ErrorCode;

@AllArgsConstructor
@Getter
public class CustomException extends RuntimeException {

	private final ErrorCode errorCode;
}
