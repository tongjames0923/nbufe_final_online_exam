package tbs.api_server.utility;

import org.springframework.transaction.interceptor.TransactionAspectSupport;
import tbs.api_server.config.constant.const_Text;

import java.util.Optional;
import java.util.function.Consumer;

public final class Error {
    public static final Error _ERROR = new Error();
    public static final int EC_UNKNOWN = 40001, EC_InvalidParameter = 40002, EC_BAND_COL = 40003, EC_LOW_PERMISSIONS = 40004;
    private final int[] errorCode = {EC_UNKNOWN, EC_InvalidParameter, EC_BAND_COL, EC_LOW_PERMISSIONS};


    private Error() {
    }

    public static boolean lengthCheck(String text, int length) {
        final boolean[] isValid = {false};
        Optional.of(text).ifPresent(new Consumer<String>() {
            @Override
            public void accept(String s) {
                if (s.length() >= length)
                    isValid[0] = true;
            }
        });
        return isValid[0];
    }

    public void rollback() {
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    }

    public void throwError(int error, String detail) throws RuntimeException {
        throw new RuntimeException(String.format("error code: %d,msg=%s,Detail:%s", error, const_Text.ERRROR_CODE_TEXT(error), detail));
    }


}
