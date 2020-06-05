package DicomHandler.ReadDicom;

import DicomHandler.Config.CONFIG;

import DicomHandler.Config.DicomStandard;
import DicomHandler.Config.Dictionary;
import javafx.scene.control.Skin;
import org.dcm4che3.data.Tag;
import org.dcm4che3.data.VR;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class DicomReader {

	static Logger logger =  LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private static final int ID_OFFSET = 128;  //location of "DICM"
	private static final String DICM = "DICM";
	//private static final int IMPLICIT_VR = 0x2D2D; // '--'
	//private AC_DicomDictionary..// m_DicomDic =  new AC_DicomDictionary();;

	private String m_sFilePath = null;
	private File m_sFile = null;
	BufferedInputStream m_bisInputStream;
	private boolean m_flagFileEnd = false;
	private boolean m_fIni = false;

	private Long flagSize = 0L;
//	private int ifReadLoactaion =0;
	private boolean m_bLittleEndian = true;
	///	TransferSyntaxUID
	private String m_sTransferSyntaxUID = null;
	private boolean m_bigEndianTransferSyntax = false;
	private boolean m_Compressed = false;

	private static String m_byteSplit = "\\";

	////// Now property
	private VR m_VR;
	private int m_nElementLength = 0;

	private int m_TageID;
	private int m_nLocation = 0;

	private int m_PixelVR = 0;

	private boolean m_flgHasPixelData = false;




	public DicomReader() {

		//

	}


	public DicomReader(String sFilePath) {



		m_fIni = readDCMFile(sFilePath);

	}
	public DicomReader(File sFilePath) {

		m_fIni = readDCMFile(sFilePath);

	}

	public boolean isIni()
	{
		return m_fIni;
	}
	public boolean readDCMFile(File input)
	{
		if(m_sFilePath==null)
			m_sFilePath = input.getAbsolutePath();

		return init(input);
	}


	public boolean readDCMFile(String input)
	{
		m_sFilePath = input;
		m_sFile = new File(m_sFilePath);
		return readDCMFile(m_sFile);
	}

	public void close()
	{

		try {
			m_bisInputStream.close();


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	private boolean init(File sFilePath)
	{
		 FileInputStream fis = null;


		 try {
				fis = new FileInputStream(sFilePath);
				if(fis.getChannel().size()==0 || fis.getChannel().size()< DicomStandard.SKIP_FILESIZE)
				{
					logger.error(m_sFilePath+" - This File Too Small");
					return false;
				}


				m_bisInputStream = new BufferedInputStream(fis);
			    flagSize = fis.getChannel().size();
				m_bisInputStream.mark(400000);

				if(!checkHeaderStart())
					return false;



			} catch (Exception e) {
				// TODO Auto-generated catch block
				//fis.close();
				 logger.error( e.toString() );
				e.printStackTrace();
				return false;
			}
		 return true;
	}



	private boolean checkHeaderStart() throws IOException
	{


		skip(ID_OFFSET);



		if (!getString(4).equals(DICM))
		{
			if (m_bisInputStream==null)
			{
				m_bisInputStream.close();
				logger.error("File Stream null");
				return false;
			}

			if (m_bisInputStream!=null)
			{
				m_bisInputStream.reset();
				int chkBigEndian =  0x0800;
				int chkLittleEndian =  0x0008;
				int igroupWord = getShort();
				if ((igroupWord != chkBigEndian) &&
						(igroupWord != chkLittleEndian)) {
					logger.warn("File has not DICM");
					m_flagFileEnd = true ;
					return false;
				}else{
					m_bisInputStream.reset();
					m_flagFileEnd = false ;
					return true;

				}
			}
		}


		m_flagFileEnd = false ;
		return true;
	}
	private void checkTSUID() throws IOException
	{

		m_sTransferSyntaxUID = getString(m_nElementLength);


		if (m_sTransferSyntaxUID.indexOf("1.2.840.10008.1.2.2")>=0)
			m_bigEndianTransferSyntax = true;

		if (m_sTransferSyntaxUID.indexOf("1.2.840.10008.1.2.4")>=0)
		{
			m_Compressed = true;
			logger.error("Compressed Dicom");
		}
	}

	public HashMap<Integer, String> getAttirbutesWithOutSQ() throws IOException
	{
		m_bisInputStream.reset();
		checkHeaderStart();
        HashMap<Integer, String>  output = new HashMap<>();


		while(!m_flagFileEnd)
		{
			int tag = getNextTag();
			String readValue = "";


			if(m_flagFileEnd)
			{
				break;
			}
			if(tag==Tag.TransferSyntaxUID)
			{
				checkTSUID();
                output.put(tag,m_sTransferSyntaxUID);
			//	skip(m_nElementLength);
				//ouptutAttributes.setAttribute(tag, value);
			}
			else if(m_nElementLength>500 && Tag.PixelData!=tag) {
				skip(m_nElementLength);
				continue;

			}
			else if(tag==Tag.Item)
            {
                readIteme(m_nElementLength, 0);

            }
			/*else if(m_VR==VR.SQ || Dictionary.getVR(tag) == VR.SQ)
			{

				String[] seqncvalue = {m_VR.toString(), Integer.toString(m_nElementLength)};
				readSequnce(m_nElementLength, true);
			}*/
			else if(tag == Tag.PixelData)
			{
				m_flagFileEnd = true;
				m_flgHasPixelData = true;
			}
			else
			{


				String tmpValue = getValue(m_VR);
				readValue = tmpValue;
                if(tag==Tag.SOPInstanceUID)
                    logger.trace("SOP Instace UID : "+tmpValue);

               output.put(tag,tmpValue);
			}
			logger.debug(String.format("TAG : %08x , Lengt : %d, valuse : %s", tag,m_nElementLength, readValue));
		}

		logger.info("done..read : " + m_sFilePath);



		return output;
	}
	public int getPixelVR()
	{
		return m_PixelVR;
	}
	public boolean hasPixelData()
	{
		return m_flgHasPixelData;
	}


	public byte[] getPixelData() throws IOException
	{
		m_bisInputStream.reset();

		checkHeaderStart();

		while(!m_flagFileEnd)
		{
			int tag = getNextTag();

			if(tag == Tag.PixelData)
			{
				getNextTag();
				getNextTag();

 				return getPixelData(m_nElementLength);
			}
			else
			{
				skip(m_nElementLength);
			}
		}

		return null;
	}

    private void readIteme(int sqlen, int idx) throws IOException {
		if(sqlen==-1) {
			while (true) {
				int tag = getNextTag();

				if (tag == Tag.ItemDelimitationItem) {
					//String[] itmeValue = {Integer.toString(AC_Undefined), Integer.toString(m_nElementLength)};
					//outputSequnce.setAttribute(tag,itmeValue);
					break;
				} else if (tag == Tag.Item) {
					readIteme(m_nElementLength, idx++);
				} else {

					skip(m_nElementLength);
				}
				logger.debug(String.format(idx + " IN SQ TAG : %08x , Lengt : %d,", tag, m_nElementLength));
			}
		}
		else {
			int startSQLocatoin = m_nLocation;
			int SQLenght = m_nElementLength;


			while (m_nLocation - startSQLocatoin < SQLenght) {
				int tag = getNextTag();

				if (tag == Tag.ItemDelimitationItem) {
					//String[] itmeValue = {Integer.toString(AC_Undefined), Integer.toString(m_nElementLength)};
					//outputSequnce.setAttribute(tag,itmeValue);
					break;
				} else if (tag == Tag.Item) {
					readIteme(m_nElementLength, idx++);
				} else {

					skip(m_nElementLength);
				}
				logger.debug(String.format(idx + " IN SQ TAG : %08x , Lengt : %d,", tag, m_nElementLength));

			}
		}

    }










	private void readSequnce(int sqenceLenght, boolean flagSkip) throws IOException
	{
	//	AC_DcmStructure outputSequnce = new AC_DcmStructure();


		if(sqenceLenght==-1)
		{

			while(true)
			{
				int tag = getNextTag();




				if(m_flagFileEnd)
				{
					break;
				}
				if(tag == Tag.SequenceDelimitationItem)
				{
					continue;
				}
				else if(m_VR == VR.SQ ||  Dictionary.getVR(tag) == VR.SQ)
				{
				//	String[] seqncvalue = {Integer.toString(m_VR), Integer.toString(m_nElementLength)};
					readSequnce(m_nElementLength, flagSkip);
					//outputSequnce.setAttribute(tag, seqncvalue);
					//outputSequnce.setSequenceValue(tag, readSequnce(m_nElementLength));
				}
				else
				{
                    if (flagSkip)
                        skip(m_nElementLength);
                    else
                        getValue(m_VR);
				//	String[] value = {Integer.toString(m_VR),getValue(m_VR)};
				//	outputSequnce.setAttribute(tag,value);
				}

				logger.debug(String.format("In SQ TYPE : A  TAG : %08x , Lengt : %d", tag,m_nElementLength));

			}
		}
		else
		{
			int startSQLocatoin = m_nLocation;
			int SQLenght = m_nElementLength;


			while(m_nLocation-startSQLocatoin<SQLenght)
			{
				int tag = getNextTag();

				if(m_flagFileEnd)
				{
					break;
				}


				if(tag == Tag.Item)
				{
					//String[] itmeValue = {Integer.toString(AC_Undefined), Integer.toString(m_nElementLength)};
					//outputSequnce.setAttribute(tag,itmeValue);
				}
				else if(m_VR == VR.SQ ||  Dictionary.getVR(tag)== VR.SQ)
				{
                    readSequnce(m_nElementLength, flagSkip);
				}
				else
				{
                    if (flagSkip)
                        skip(m_nElementLength);
                    else
                        getValue(m_VR);
					/*String[] value = {Integer.toString(m_VR),getValue(m_VR)};
					outputSequnce.setAttribute(tag,value);*/
				}

				logger.debug(String.format("In SQ TYPE : B  TAG : %08x , Lengt : %d", tag,m_nElementLength));

			}

		}

		return ;
	}






	private int getNextTag() throws IOException
	{
		int igroupWord = getShort();
		if(m_flagFileEnd)
			return 0;

		if (igroupWord==0x0800 && m_bigEndianTransferSyntax) {
			m_bLittleEndian= false;
			igroupWord = 0x0008;
		}
		int ielementWord = getShort();

		int tag = igroupWord<<16 | ielementWord;


		m_nElementLength = getLength();


		 m_TageID = tag;

		return tag;

	}



  	int getLength() throws IOException {
		int b0 = getByte();
		int b1 = getByte();

		int b2 = getByte();
		int b3 = getByte();

		// We cannot know whether the VR is implicit or explicit
		// without the full DICOM Data Dictionary for public and
		// private groups.

		// We will assume the VR is explicit if the two bytes
		// match the known codes. It is possible that these two
		// bytes are part of a 32-bit length for an implicit

		m_VR = VR.valueOf( ((b0<<8) + b1));
	//	System.out.println("vr : "+((b0<<8) + b1)+ m_VR.toString() + "  "+ m_VR.code());

        if (m_VR == VR.UN) {
            if (VR.UN.code() != ((b0 << 8) + b1)) {
                if (m_bLittleEndian)
                    return ((b3 << 24) + (b2 << 16) + (b1 << 8) + b0);
                else
                    return ((b0 << 24) + (b1 << 16) + (b2 << 8) + b3);
            }
        }


		switch (m_VR) {


			case  SQ:

				return getInt();


			case OB: case  OW: case OF :
		    case  UT: case  UN:


				// Explicit VR with 32-bit length if other two bytes are zero
				if ( (b2 == 0) || (b3 == 0) )
				{
					return getInt();
				}


			case AE: case AS: case AT: case CS: case DA: case DS: case DT:  case FD:
			case FL: case IS: case LO: case LT: case PN: case SH: case SL: case SS:
			case ST: case TM:case UI: case UL: case US:
				// Explicit vr with 16-bit length
				if (m_bLittleEndian)
					return ((b3<<8) + b2);
				else
					return ((b2<<8) + b3);


			default:
				// Implicit VR with 32-bit length...
				m_VR = VR.UN;
				if (m_bLittleEndian)
					return ((b3<<24) + (b2<<16) + (b1<<8) + b0);
				else
					return ((b0<<24) + (b1<<16) + (b2<<8) + b3);
		}
	}

	public  String byteToBinaryString(byte n) {
		StringBuilder sb = new StringBuilder("00000000");
		for (int bit = 0; bit < 8; bit++) {
			if (((n >> bit) & 1) > 0) {
				sb.setCharAt(7 - bit, '1');
			}
		}
		return sb.toString();
	}




	private String getValue(VR iVR) throws IOException
	{
		if(m_nElementLength==-1 ||m_nElementLength==0)
			return "";

		if(VR.UN==iVR)
		{
		//	m_DicomDic = new AC_DicomDictionary();

			m_VR = iVR = Dictionary.getVR(m_TageID);
		}

		String sValue ="";
		int ivm =0;
		String fullS = "";

		byte b0 = 0;
		byte b1 = 0;


		switch(iVR)
		{
		case OB: case UN:
			 ivm = m_nElementLength/2;
			 fullS = "";

			b0 = (byte)getByte();
			b1 = (byte)getByte();

			fullS = Byte.toString(b0) +m_byteSplit+ Byte.toString(b1);
			//
			for(int i=1; i<ivm;i++)
			{
				b0 = (byte)getByte();
				b1 = (byte)getByte();
				fullS +=m_byteSplit+Byte.toString(b0) +m_byteSplit+ Byte.toString(b1);
			}
			sValue =fullS;
			//alue = getString(m_nElementLength);
			break;
		case UL:
			sValue =(Integer.toString(getInt()));
			break;
		case FD:
			 ivm = m_nElementLength/8;
			 fullS = "";


			fullS +=Double.toString(getDouble());
			//
			for(int i=1; i<ivm;i++)
			{
				fullS +=m_byteSplit+Double.toString(getDouble());
			}
			sValue =fullS;
			break;
		case FL:

			if (m_nElementLength==4)
				sValue = Float.toString(getFloat());
			else {
				sValue = "";
				int n = m_nElementLength/4;
				for (int i=0; i<n; i++)
					sValue += Float.toString(getFloat())+m_byteSplit;
			}
			break;

		case AE: case AS: case AT: case CS: case DA: case DS: case  DT:
		case  IS: case  LO: case LT: case PN: case SH: case ST: case TM: case UI:
			sValue = getString(m_nElementLength);
			break;
		case US:
			if (m_nElementLength==2)
				sValue = Integer.toString(getShort());
			else {
				sValue = "";
				int n = m_nElementLength/2;
				for (int i=0; i<n; i++)
					sValue += Integer.toString(getShort())+m_byteSplit;
			}
			break;
		case SQ:
			sValue = "sqens";
			break;

		default:
			skip((long)m_nElementLength);
			sValue = "defult";
		}



		sValue = sValue.trim();

		if(sValue.equals(""))
			sValue = "";

		return sValue;
	}


	private void skip(long lskipCount)
	{
		m_nLocation += lskipCount;

		while (lskipCount > 0)
			try {
				lskipCount -= m_bisInputStream.skip(lskipCount);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	private byte[] getPixelData(int length) throws IOException
	{

		byte[] arrPixelData = new byte[m_nElementLength];

		for(int i=0; i<m_nElementLength;i++)
		{
			arrPixelData[i] =  (byte) m_bisInputStream.read();
		}

		return arrPixelData;
	}



	String getString(int length) throws IOException {
		byte[] buf = new byte[length];

		for(int i=0; i<length;i++)
		{
			buf[i] = (byte)getByte();
		}

		m_nLocation += length;

		String tmp = new String(buf);
		String newTmp = tmp.replaceAll("(^\\p{Z}+|\\p{Z}+$)", "");
		return newTmp;
	}

	int getByte() throws IOException {
		//if(m_bisInputStream.)

		int b = m_bisInputStream.read();
		if (b ==-1)
		{
			 m_flagFileEnd = true;
			//throw new IOException("unexpected EOF");

		}
		++m_nLocation;
		return b;
	}

	int getShort() throws IOException {
		int b0 = getByte();
		int b1 = getByte();
		if (m_bLittleEndian)
			return ((b1 << 8) + b0);
		else
			return ((b0 << 8) + b1);
	}

	final int getInt() throws IOException {
		int b0 = getByte();
		int b1 = getByte();
		int b2 = getByte();
		int b3 = getByte();
		if (m_bLittleEndian)
			return ((b3<<24) + (b2<<16) + (b1<<8) + b0);
		else
			return ((b0<<24) + (b1<<16) + (b2<<8) + b3);
	}

	double getDouble() throws IOException {
		int b0 = getByte();
		int b1 = getByte();
		int b2 = getByte();
		int b3 = getByte();
		int b4 = getByte();
		int b5 = getByte();
		int b6 = getByte();
		int b7 = getByte();
		long res = 0;
		if (m_bLittleEndian) {
			res += b0;
			res += ( ((long)b1) << 8);
			res += ( ((long)b2) << 16);
			res += ( ((long)b3) << 24);
			res += ( ((long)b4) << 32);
			res += ( ((long)b5) << 40);
			res += ( ((long)b6) << 48);
			res += ( ((long)b7) << 56);
		} else {
			res += b7;
			res += ( ((long)b6) << 8);
			res += ( ((long)b5) << 16);
			res += ( ((long)b4) << 24);
			res += ( ((long)b3) << 32);
			res += ( ((long)b2) << 40);
			res += ( ((long)b1) << 48);
			res += ( ((long)b0) << 56);
		}
		return Double.longBitsToDouble(res);
	}

	float getFloat() throws IOException {
		int b0 = getByte();
		int b1 = getByte();
		int b2 = getByte();
		int b3 = getByte();
		int res = 0;
		if (m_bLittleEndian ) {
			res += b0;
			res += ( ((long)b1) << 8);
			res += ( ((long)b2) << 16);
			res += ( ((long)b3) << 24);
		} else {
			res += b3;
			res += ( ((long)b2) << 8);
			res += ( ((long)b1) << 16);
			res += ( ((long)b0) << 24);
		}
		return Float.intBitsToFloat(res);
	}


}
