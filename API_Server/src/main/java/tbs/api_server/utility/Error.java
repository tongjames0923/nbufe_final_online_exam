package tbs.api_server.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.HashMap;
import java.util.Optional;
import java.util.function.Consumer;

@Service
public class Error {

    public static final int EC_UNKNOWN = 40001,
            EC_InvalidParameter = 40002,
            EC_BAND_COL = 40003,
            EC_LOW_PERMISSIONS = 40004,
            EC_DB_INSERT_FAIL = 40005, EC_DB_SELECT_NOTHING = 40006,
            EC_DB_UPDATE_FAIL = 40007, EC_DB_DELETE_FAIL = 40008, EC_FILESYSTEM_ERROR = 40013;
    public static final int SUCCESS = 40000;
    public static final int FC_NOTFOUND = 40009, FC_WRONG_PASSTEXT = 40010,
            FC_DUPLICATE = 40011, FC_UNAVALIABLE = 40012;
    private static final int[] codes = {
            EC_UNKNOWN, EC_InvalidParameter, EC_BAND_COL, EC_LOW_PERMISSIONS,
            EC_DB_INSERT_FAIL, EC_DB_DELETE_FAIL, EC_DB_UPDATE_FAIL, EC_DB_SELECT_NOTHING,
            FC_DUPLICATE, FC_NOTFOUND, FC_UNAVALIABLE, FC_WRONG_PASSTEXT, EC_FILESYSTEM_ERROR

    };
    private static final String[] texts = {
            "未知错误", "错误参数", "系统控制的数据域", "权限不足", "数据库插入失败", "数据库删除失败", "数据库更新失败", "数据库查找失败",
            "数据重复错误", "数据丢失错误", "功能不可用错误", "验证错误", "文件操作错误"
    };
    @Autowired
    public static Error _ERROR;
    private static HashMap<Integer, String> mp = null;

    @Autowired
    public Error() {
        if (mp == null) {
            mp = new HashMap<>();
            int j = 0;
            for (int i : codes) {
                mp.put(i, texts[j++]);
            }
        }
        System.out.println("error class made!");
        _ERROR = this;
    }

    private static String ERRROR_CODE_TEXT(int error) {
        String errorcode="NULL CODE";
        if (mp.containsKey(error)) {
            errorcode= mp.get(error);
        }
        System.out.println(String.format("ERROR:%d,DETAIL:%s",error,errorcode));

            return errorcode;
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
        try {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public BackendError throwError(int error, String detail, Object data) throws BackendError {
        return BackendError.newError(error, detail,ERRROR_CODE_TEXT(error), data);
    }

    public BackendError throwError(int error, String detail) throws BackendError {
        return throwError(error, detail, null);
    }

    public static class BackendError extends Exception {
        private int code;
        private Object data = null;
        private String detail=null;

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        private BackendError(String msg, String detail) {
            super(msg);
            setDetail(detail);
        }

        public static BackendError newError(int code, String msg,String detail, Object data) {
            BackendError backendError = new BackendError(msg,detail);
            backendError.setCode(code);
            backendError.setData(data);
            return backendError;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }

}
