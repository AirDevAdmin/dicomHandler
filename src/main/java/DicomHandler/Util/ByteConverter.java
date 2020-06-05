package DicomHandler.Util;

import java.nio.ByteBuffer;

public class ByteConverter {
    public static final  boolean LITTLE_ENDIAN = true;
    public static final boolean BIG_ENDIAN = false;

    private static boolean m_flagLittle = LITTLE_ENDIAN;


    private static byte[] arrayReviers(byte[] inBytes) {
        byte[] outBytes = new byte[inBytes.length];
        for (int i = 0; i < inBytes.length; i++)
            outBytes[i] = inBytes[inBytes.length - 1 - i];
        return outBytes;
    }


    public static byte[] getDouble2Bytes(double inInput) {
        byte[] outBytes = new byte[8];
        ByteBuffer.wrap(outBytes).putDouble(inInput);

        if (m_flagLittle) {
            return arrayReviers(outBytes);
        } else
            return outBytes;

    }

    public static byte[] getFloat2Bytes(float inInput) {
        byte[] outBytes = new byte[4];
        ByteBuffer.wrap(outBytes).putFloat(inInput);

        if (m_flagLittle) {
            return arrayReviers(outBytes);
        } else
            return outBytes;

    }


    public static byte[] getInt2Bytes(int inInput) {
        byte[] outBytes = new byte[4];
        ByteBuffer.wrap(outBytes).putInt(inInput);

        if (m_flagLittle) {
            return arrayReviers(outBytes);
        } else
            return outBytes;
    }


    public static byte[] getShort2Bytes(short inInput) {
        byte[] outBytes = new byte[2];
        ByteBuffer.wrap(outBytes).putShort(inInput);

        if (m_flagLittle) {
            return arrayReviers(outBytes);
        } else
            return outBytes;
    }

    public static byte[] getString2Bytes(String inString) {
        byte[] outBytes = inString.getBytes();
        if (outBytes.length % 2 != 0)
            outBytes = paddingZero(outBytes, outBytes.length + 1);
        return outBytes;
    }

    public static byte[] paddingZero(byte[] inBytes, int inSize) {
        byte[] tmp = new byte[inSize];
        for (int i = 0; i < inBytes.length; i++)
            tmp[i] = inBytes[i];
        for (int i = inBytes.length; i < inSize; i++)
            tmp[i] = 0x00;

        return tmp.clone();
    }
}
