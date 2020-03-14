package Protocol;

import com.alibaba.fastjson.JSON;

public class JsonDecoder {
    public static<T> T decode(byte[] bytes, Class<T> clazz) {
        return JSON.parseObject(bytes,clazz);
    }
}
