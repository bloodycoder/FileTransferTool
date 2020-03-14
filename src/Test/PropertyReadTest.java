package Test;

import Protocol.PropertyRead;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class PropertyReadTest {

    @Test
    public void init() throws IOException {
        PropertyRead.init();
    }
}