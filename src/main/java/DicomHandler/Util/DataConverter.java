package DicomHandler.Util;



import org.dcm4che3.data.Tag;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.util.HashMap;


public class DataConverter {
	
	
	 final static boolean littleEndian = true;
	 
	 
	 public static String margeDataTime(String sDate, String sTime)
	 {
		 if(sDate==null || sTime == null ||sDate.equals("") || sTime.equals(""))//||sDate== Environment.g_nullVale || sTime == Environment.g_nullVale)
		 {
		 	return "Series Data N/A";
		 }

		 String output;
		 
		String sHour = sTime.substring(0,2);
		String sMin = sTime.substring(2,4);
		String sSec = sTime.substring(4,6);
		String sYear = sDate.substring(0,4);
		String sMonth = sDate.substring(4,6);
		String sDay = sDate.substring(6,8);
		
		
		if(Integer.parseInt(sHour)>=0&& Integer.parseInt(sHour)<=11)
			sHour = "AM "+sHour;
		else if(Integer.parseInt(sHour)>12&& Integer.parseInt(sHour)<=23)
		{
			int iHour = Integer.parseInt(sHour);
			sHour = "PM "+String.format("%2d", iHour);
		}
		else 
			sHour = "AM "+sHour;
		
		output = sYear+"-"+sMonth+"-"+sDay
				+" "+sHour+":"+sMin+":"+sSec;
		 
		 
		return output;
		 
	 }
	 
	 public static String con2AcquisitionDateTime(String input)
	 {
		 String sData = input.substring(0, 8);
		 String sTime = input.substring(8, 16);
		 
		 
		 
		 return margeDataTime(sData, sTime);
	 }
	 
	






	public static double[] DCMPixelData2Singnal(byte[] abPixelData,   HashMap<Integer, String> inputData)
	 {
		 int irow = Integer.parseInt(inputData.get(Tag.Rows));
		 int iColumn =Integer.parseInt(inputData.get(Tag.Columns));
		 int iPixelRepresentation = Integer.parseInt(inputData.get(Tag.PixelRepresentation));
		 int iBitsAllocated = Integer.parseInt(inputData.get(Tag.BitsAllocated));
		 int iBitsStored = Integer.parseInt(inputData.get(Tag.BitsStored));
		 int iSamplesperPixel = Integer.parseInt(inputData.get(Tag.SamplesPerPixel));
		 String tmp = inputData.get(Tag.RescaleIntercept);
		 double dRescaleIntercept = 0.0;
		 double dRescaleSlope = 1.0;
		 if(tmp!=null)
			 dRescaleIntercept = Double.parseDouble(inputData.get(Tag.RescaleIntercept));
		 tmp = inputData.get(Tag.RescaleSlope);
		 if(tmp!=null)
			 dRescaleSlope = Double.parseDouble(inputData.get(Tag.RescaleSlope));



		 double[] output = DCMPixelData2Singnal(abPixelData,
				 irow, iColumn, iPixelRepresentation,
				 iBitsAllocated, iBitsStored,iSamplesperPixel,
				 dRescaleIntercept, dRescaleSlope);


		 return output;
	 }

	 public static double[] DCMPixelData2Singnal(byte[] abPixelData,
				int iRow, int iColumn, int iPixelRepresentation,
				int iBitsAllocated, int iBitsStored, int iSampleSperPixel,
				double dRescaleIntercept, double dRescaleSlope)
		{
			double[] adSignalData = new double[iRow*iColumn*iSampleSperPixel];
			
			//littleEndian = false;
			
			
			
			for(int i=0; i<iRow*iColumn*iSampleSperPixel;i++)
			{
				
				if(iBitsAllocated==8)
				{
					int b0 = abPixelData[i];	
					if(b0<0)
						b0 = (int)(b0+256);
					
					adSignalData[i] = b0*dRescaleSlope+dRescaleIntercept;	
				}
				
				if(iBitsAllocated==16)
				{
				
					int b1 = abPixelData[i*2+1];
					int b0 = abPixelData[i*2];		
		
					
					double tmp =0;
					if(littleEndian)
					{

						if(b0<0)
							b0 = (int)(b0+256);

						if(iPixelRepresentation==0  && (b1<0))

							b1 = (int)(b1+256);




						tmp = ((b1 << 8) + b0);
					}
					else
					{
						if(b1<0)
							b1 = (int)(b1+256);
					
						tmp = ((b0 << 8) + b1);
					}
					
					adSignalData[i] = tmp*dRescaleSlope+dRescaleIntercept;	
				}
				else if(iBitsAllocated==32)
				{
					int b3 = abPixelData[i*2+3];
					int b2 = abPixelData[i*2+2];	
					int b1 = abPixelData[i*2+1];
					int b0 = abPixelData[i*2];
					
					double tmp =0;
					if(littleEndian)
					{
						if(b0<0)
							b0 = (int)(b0+256);
						if(b1<0)
							b0 = (int)(b1+256);
						if(b2<0)
							b0 = (int)(b2+256);
						tmp = ((b3<<24) + (b2<<16) + (b1<<8) + b0);
					}
					else
					{

						if(b0<0)
							b3 = (int)(b3+256);
						if(b1<0)
							b2 = (int)(b2+256);
						if(b2<0)
							b1 = (int)(b1+256);
						tmp = ((b0<<24) + (b1<<16) + (b2<<8) + b3);     
					};
					adSignalData[i] = tmp*dRescaleSlope+dRescaleIntercept;	
				}
				
			}
			return adSignalData;
		}

	
	static public BufferedImage FastSignal2bffImg(double[] inputSignal, 
			int iWidth, int iHeight	,double iwindowCenter, double iWindowWidth,int iSamplesperPixel )
	{

		
		double[] tmpdcm = inputSignal.clone();
	
		if(iSamplesperPixel==1)
		{
			BufferedImage output = new BufferedImage( iWidth,  iHeight, BufferedImage.TYPE_BYTE_GRAY);
			byte[] imagePixelData = ((DataBufferByte)output.getRaster().getDataBuffer()).getData();

			double imin = (int) (iwindowCenter-0.5-(iWindowWidth-1)/2);
			double imax =  (int) (iwindowCenter-0.5+(iWindowWidth-1)/2);


			for(int i=0; i<iWidth*iHeight;i++)
			{
					int idx = i;

					if(tmpdcm[idx]<= imin)
						imagePixelData[idx] =0;
					else if(tmpdcm[idx] > imax)
						imagePixelData[idx] = (byte) 255;
					else {
						byte tmp = (byte) ((tmpdcm[idx]-imin)/(iWindowWidth)*255);
						//int tmp = (int) ((tmpdcm[idx]-(iwindowCenter-0.5))/(iWindowWidth-1)+0*255);
						imagePixelData[idx] = tmp;
					}

				
			}
			tmpdcm =null;
			return output;

		}else if(iSamplesperPixel==3)
		{
			BufferedImage output = new BufferedImage( iWidth,  iHeight,BufferedImage.TYPE_INT_RGB);
			int[] imagePixelData = ((DataBufferInt)output.getRaster().getDataBuffer()).getData();

			double imin = (int) (iwindowCenter-0.5-(iWindowWidth-1)/2);
			double imax =  (int) (iwindowCenter-0.5+(iWindowWidth-1)/2);


			for(int i=0; i<iWidth*iHeight;i++)
			{
				int iR =  (int)inputSignal[(i*3)+0];   
				int iG = (int)inputSignal[(i*3)+1];   
				int iB = (int)inputSignal[(i*3)+2];  
				
				if(iR<= imin)
					iR =0;
				else if(iR > imax)
					iR =  255;
				else {
					iR =  (int) ((iR-imin)/(iWindowWidth)*255);
				}
				
				if(iG<= imin)
					iG =0;
				else if(iG > imax)
					iG =  255;
				else {
					iG =  (int) ((iG-imin)/(iWindowWidth)*255);
				}
				
				if(iB<= imin)
					iB =0;
				else if(iB > imax)
					iB = 255;
				else {
					iB =  (int) ((iB-imin)/(iWindowWidth)*255);
				}
				
				
				
				
				
				

				imagePixelData[i] =  iR<<16 | iG<<8 | iB;
			}
			tmpdcm =null;
			return output;


		}

		return null;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	


	

}
