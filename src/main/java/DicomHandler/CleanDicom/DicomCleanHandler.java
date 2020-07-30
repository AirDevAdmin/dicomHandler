package DicomHandler.CleanDicom;

import DicomHandler.DicomHandler;
import DicomHandler.LogTag.LogTagAttribute;
import DicomHandler.TagCategory.DicomReconTag;
import org.dcm4che3.data.Tag;
import org.dcm4che3.data.UID;
import org.dcm4che3.data.VR;
import org.dcm4che3.io.DicomOutputStream;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class DicomCleanHandler extends DicomHandler {

    protected LogTagAttribute logAttrbutes = new LogTagAttribute(attributes);


    public void setSortRule(String vaule) {
        this.logAttrbutes.setSortRule(vaule);
    }


    public DicomCleanHandler() {
        //  processingTag.addAll(Arrays.stream(ANONYMIZATION_LIST).boxed().collect(Collectors.toList()));

    }



    public boolean buildAttrbutes(Map<Integer, String> arrTagNVaule, byte[] pixelData) {






        setAttributes( DicomReconTag.getMandatroyTagList()  , arrTagNVaule);

        ///Medai SOP Clss == SOP class
       // setAttribute(Tag.SOPClassUID, arrTagNVaule.get(Tag.MediaStorageSOPClassUID));


        String Modal = attributes.getString(Tag.Modality);
        if (Modal == null) {

        } else if (Modal.toUpperCase().contains("CT")) {
            setAttributes(DicomReconTag.getCTTagList(), arrTagNVaule);

        }


        else if (Modal.toUpperCase().contains("MR")){
            setAttributes(DicomReconTag.getMRTagList(), arrTagNVaule);
        }     /*  else if(Modal.toUpperCase().contains("US"))
           return US;
       else if(Modal.toUpperCase().contains("PT"))
           return PT;
       else if(Modal.toUpperCase().contains("N"))*/




        chkPixelReconTag();

        metaData.setValue(Tag.TransferSyntaxUID,VR.UI, UID.ExplicitVRLittleEndian);
        attributes.setBytes(Tag.PixelData, VR.valueOf("OW"), pixelData);

        logAttrbutes.update();



        return true;
    }

    public boolean saveDicomFile(String outFilePath) throws IOException {

        File rootPath = new File(outFilePath);
        if(!rootPath.exists())
            rootPath.mkdirs();
        String fileName = String.format("%05d.dcm",attributes.getInt(Tag.InstanceNumber,0));
        File fi =  new File(outFilePath+File.separator+fileName);



        DicomOutputStream dos = new DicomOutputStream(fi);

        dos.writeDataset(this.metaData, this.attributes);
        dos.finish();
        dos.close();

        return true;
    }

    private void setAttributes(List<DicomReconTag> tagList, Map<Integer, String> arrTagNVaule) {
        for (DicomReconTag reconTag : tagList) {
            String value = arrTagNVaule.get(reconTag.getTag());
            setAttribute(reconTag, value);
        }
    }

    private void chkPixelReconTag() {
        if (attributes.getString(Tag.RescaleIntercept) == null)
            setAttribute(DicomReconTag.RescaleIntercept, "0.0");
        if (attributes.getString(Tag.RescaleSlope) == null)
            setAttribute(DicomReconTag.RescaleSlope, "1.0");

        if (attributes.getString(Tag.WindowCenter) == null)
            setAttribute(DicomReconTag.WindowCenter, "50");
        if (attributes.getString(Tag.WindowWidth) == null)
            setAttribute(DicomReconTag.WindowWidth, "300");
        if (attributes.getString(Tag.PixelSpacing) == null) {
            String value = "1.00\\1.00";
            if (attributes.getString(Tag.ReconstructionDiameter) != null) {

                double dReconstructionDiameter = Double.parseDouble(attributes.getString(Tag.ReconstructionDiameter));
                double rows = Double.parseDouble(attributes.getString(Tag.Rows));
                double Columns = Double.parseDouble(attributes.getString(Tag.Columns));

                double dPixelSpacingX = dReconstructionDiameter / rows;
                double dPixelSpacingY = dReconstructionDiameter / Columns;

                value = dPixelSpacingX + "\\" + dPixelSpacingY;
                logAttrbutes.addLog(Tag.PixelSpacing, logAttrbutes.CREATE, value, " calculated By (0018,1100)");
                setAttribute(DicomReconTag.PixelSpacing, value);
            } else {
                logAttrbutes.addLog(Tag.PixelSpacing, logAttrbutes.CREATE, value, " ini 1.0\\1.0");
                setAttribute(DicomReconTag.PixelSpacing, value);
            }

        }

    }


    protected void setAttribute(DicomReconTag dicomReconTag, String value) {
        if (value == null) {
            value = "";
        } else if (!chkValue(value)) {
            value = "";
        }
        if (!dicomReconTag.getMandatroy() && value.equals(""))
            return;
        else if (value.equals("")) {
            logAttrbutes.addLog(dicomReconTag.getTag(), logAttrbutes.CREATE, "");

            value = dicomReconTag.getDefultValue();
            //      System.out.println(value);
        }
        setAttribute(dicomReconTag.getTag(), value);
    }

    private boolean chkValue(String word) {
        word = word.trim();

        return Pattern.matches("^[0-9a-zA-Z+_ /\\-!@#$%^&*(),\'.?\":\\[\\]{}|<>\\\\]*$", word);
    }

}
