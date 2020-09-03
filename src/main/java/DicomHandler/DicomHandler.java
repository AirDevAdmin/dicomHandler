package DicomHandler;

import DicomHandler.Config.Dictionary;
import DicomHandler.Util.ByteConverter;
import org.dcm4che3.data.*;

import java.nio.ByteBuffer;


public class DicomHandler {

    protected Attributes metaData = new Attributes();
    protected Attributes attributes = new Attributes();


    private static boolean m_flagLittle = true;
    private static String m_byteSplit = "\\\\";



    public Attributes getMetaData() {
        return this.metaData;
    }

    public Attributes getAttribute() {
        return this.attributes;
    }

    protected void setAttribute(int tag, String value) {
        VR vr = Dictionary.getVR(tag);
        if(tag == Tag.SmallestImagePixelValue)
            vr = VR.US;
        if(tag == Tag.LargestImagePixelValue)
            vr = VR.US;

        //   System.out.println("Test : aonoy  VR: "+vr.toString()+ " value : "+ value);
        setAttribute(tag, vr, value);
    }

    protected void setAttribute(int tag, VR vr, String value) {
       //   System.out.println("Test : aonoy  VR: "+vr.toString()+ " value : "+ value);

        if (tag >= 0x00020000 && tag <= 0x0002EEEE) {
            metaData.setBytes(tag, vr, getByteValue(vr, value));
        } else {
            attributes.setValue(tag, vr, getByteValue(vr, value));
        }
    }


    public static byte[] getByteValue(VR vr, String vaule) {




        byte[] abValue = new byte[0];

        String[] split = vaule.split(m_byteSplit);
        if (split.length == 0) {
            split = new String[1];
            split[0] = vaule;
        }


        switch (vr) {
            case AE:
            case AS:
            case AT:
            case CS:
            case DA:
            case DS:
            case DT:
            case IS:
            case LO:
            case LT:
            case PN:
            case SH:
            case ST:
            case TM:
            case UI:

                //	abVR = paddingZero(abVR, 4);
                abValue = ByteConverter.getString2Bytes(vaule);

                break;

            case OB:
            case OW:
            case OF:
            case UN:
            case UT:

                String[] tmpS = split;
                abValue = new byte[tmpS.length];


                for (int i = 0; i < tmpS.length; i++) {
                    if (tmpS[i] != "")
                        abValue[i] = Byte.parseByte(tmpS[i]);
                    else
                        abValue[i] = 0X00;
                }

                if (vaule.equals("")) {
                    abValue = new byte[0];

                }

                break;


            case US:
                int typeSize = Short.BYTES;

                abValue = new byte[split.length * typeSize];

                for (int i = 0; i < split.length; i++) {
                    if (split[i].equals(""))
                        split[i] = "0";


                    Short tmpShort = (short) Integer.parseInt(split[i]);
                    byte[] tmpBytes = ByteConverter.getShort2Bytes(tmpShort);
                    for (int j = 0; j < typeSize; j++)
                        abValue[(typeSize * i) + j] = tmpBytes[j];
                }

                break;

            case UL:

                typeSize = Integer.BYTES;
                abValue = new byte[split.length * typeSize];

                for (int i = 0; i < split.length; i++) {
                    if (split[i].equals(""))
                        split[i] = "0";
                    int tmpInt = Integer.parseInt(split[i]);
                    byte[] tmpBytes = ByteConverter.getInt2Bytes(tmpInt);
                    for (int j = 0; j < typeSize; j++)
                        abValue[(typeSize * i) + j] = tmpBytes[j];
                }
                break;

            case FL:

                typeSize = Float.BYTES;
                abValue = new byte[split.length * typeSize];
                for (int i = 0; i < split.length; i++) {
                    if (split[i].equals(""))
                        split[i] = "0.0";
                    float tmpFloat = Float.parseFloat(split[i]);
                    byte[] tmpBytes = ByteConverter.getFloat2Bytes(tmpFloat);
                    for (int j = 0; j < typeSize; j++)
                        abValue[(typeSize * i) + j] = tmpBytes[j];
                }
                break;

            case FD:
                typeSize = Double.BYTES;
                abValue = new byte[split.length * typeSize];
                for (int i = 0; i < split.length; i++) {
                    if (split[i].equals(""))
                        split[i] = "0.0";
                    double tmpDouble = Double.parseDouble(split[i]);
                    byte[] tmpBytes = ByteConverter.getDouble2Bytes(tmpDouble);
                    for (int j = 0; j < typeSize; j++)
                        abValue[(typeSize * i) + j] = tmpBytes[j];
                }
                break;

        }
        return abValue;
    }


}
