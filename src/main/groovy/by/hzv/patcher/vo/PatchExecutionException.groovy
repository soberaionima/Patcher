package by.hzv.patcher.vo

/**
 * @author <a href="mailto:dkotsubo@wiley.com">Dmitry Kotsubo</a>
 * @since 25.02.2013
 */
class PatchExecutionException extends RuntimeException {
	PatchExecutionException(String message) {
		super(message)
	}

	PatchExecutionException(String message, Throwable cause) {
		super(message, cause)
	}
}
