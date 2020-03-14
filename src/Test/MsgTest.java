package Test;

import Protocol.Msg;
import org.junit.Test;

import static org.junit.Assert.*;

public class MsgTest {

    @Test
    public void toSerial() {
        Msg myMsg = new Msg();
        myMsg.setMsgId(1);
        myMsg.setStatusCode(200);
        myMsg.setStr("helloworihhwqiudhqwild19950828!helloworihhwqiudhqwild19950828!helloworihhwqiudhqwild19950828!helloworihhwqiudhqwild19950828!helloworihhwqiudhqwild19950828!helloworihhwqiudhqwild19950828!");
        byte[]bytes = myMsg.toSerial();
        System.out.println(bytes.length);
        Msg newMessage = Msg.getMsg(bytes);
        System.out.println(newMessage.str);
        assertEquals(newMessage.msgId,1);
    }
}