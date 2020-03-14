package Protocol;

import com.alibaba.fastjson.JSON;

public class JsonEncoder {
    public static byte[] encode(Object obj) {
        return JSON.toJSONBytes(obj);
    }
}
