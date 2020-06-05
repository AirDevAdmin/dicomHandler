package DicomHandler.TagCategory;

import org.dcm4che3.data.Tag;
import org.dcm4che3.data.VR;

import java.util.ArrayList;
import java.util.List;


public enum DicomReconTag {

    //Medat
    ImplementationClassUID( Tag.ImplementationClassUID, VR.UI,  DcmTagCategory.META, true, ""),
    TransferSyntaxUID( Tag.TransferSyntaxUID, VR.UI,  DcmTagCategory.META, true, ""),
    MediaStorageSOPInstanceUID( Tag.MediaStorageSOPInstanceUID, VR.UI,  DcmTagCategory.META, true, ""),
    MediaStorageSOPClassUID( Tag.MediaStorageSOPClassUID, VR.UI,  DcmTagCategory.META, true, ""),
    FileMetaInformationVersion( Tag.FileMetaInformationVersion, VR.OB,  DcmTagCategory.META, true, ""),
    //SOP
    SOPInstanceUID( Tag.SOPInstanceUID, VR.UI,  DcmTagCategory.SOP, true, ""),
    SOPClassUID( Tag.SOPClassUID, VR.UI,  DcmTagCategory.SOP, true, ""),
    InstanceCreationDate( Tag.InstanceCreationDate, VR.DA,  DcmTagCategory.SOP, true, ""),
    InstanceCreationTime( Tag.InstanceCreationTime, VR.TM,  DcmTagCategory.SOP, true, ""),
//EQUIPMENT
Manufacturer( Tag.Manufacturer, VR.LO,  DcmTagCategory.EQUIPMENT, true, ""),
    ManufacturerModelName( Tag.ManufacturerModelName, VR.LO,  DcmTagCategory.EQUIPMENT, true, ""),
    StationName( Tag.StationName, VR.SH,  DcmTagCategory.EQUIPMENT, true, ""),
    // PROTOCOL_TAG
    ProtocolName( Tag.ProtocolName, VR.LO,  DcmTagCategory.PROTOCOL, true, ""),
    // SITE_TAG
    InstitutionName( Tag.InstitutionName, VR.LO,  DcmTagCategory.SITE, true, ""),
    // PATIENT_TAG
    PatientID( Tag.PatientID, VR.LO,  DcmTagCategory.PATIENT, true, ""),
    PatientName( Tag.PatientName, VR.PN,  DcmTagCategory.PATIENT, true, ""),
    PatientBirthDate( Tag.PatientBirthDate, VR.DA,  DcmTagCategory.PATIENT, true, ""),
    PatientSex( Tag.PatientSex, VR.CS,  DcmTagCategory.PATIENT, true, ""),
    PatientAge( Tag.PatientAge, VR.AS,  DcmTagCategory.PATIENT, true, ""),
    OtherPatientIDs( Tag.OtherPatientIDs, VR.LO,  DcmTagCategory.PATIENT, false, ""),
    OtherPatientNames( Tag.OtherPatientNames, VR.PN,  DcmTagCategory.PATIENT, false, ""),


    // STUDY_TAG
    StudyInstanceUID( Tag.StudyInstanceUID, VR.UI,  DcmTagCategory.STUDY, true, ""),
    StudyID( Tag.StudyID, VR.SH,  DcmTagCategory.STUDY, true, ""),
    StudyTime( Tag.StudyTime, VR.TM,  DcmTagCategory.STUDY, true, ""),
    StudyDate( Tag.StudyDate, VR.DA,  DcmTagCategory.STUDY, true, ""),
    StudyDescription( Tag.StudyDescription, VR.LO,  DcmTagCategory.STUDY, true, ""),
    Modality( Tag.Modality, VR.CS,  DcmTagCategory.STUDY, true, ""),
    // SERIES_TAG
    SeriesInstanceUID( Tag.SeriesInstanceUID, VR.UI,  DcmTagCategory.SERIES, true, ""),
    SeriesTime( Tag.SeriesTime, VR.TM,  DcmTagCategory.SERIES, true, ""),
    SeriesDate( Tag.SeriesDate, VR.DA,  DcmTagCategory.SERIES, true, ""),
    SeriesNumber( Tag.SeriesNumber, VR.IS,  DcmTagCategory.SERIES, true, ""),
    SeriesDescription( Tag.SeriesDescription, VR.LO,  DcmTagCategory.SERIES, true, ""),
    // IMAGE_TAG
    InstanceNumber( Tag.InstanceNumber, VR.IS,  DcmTagCategory.IMAGE, true, ""),
    AcquisitionDate( Tag.AcquisitionDate, VR.DA,  DcmTagCategory.IMAGE, true, ""),
    AcquisitionTime( Tag.AcquisitionTime, VR.TM,  DcmTagCategory.IMAGE, true, ""),
    // IMAGE_PLANE_TAG
    PatientPosition( Tag.PatientPosition, VR.CS,  DcmTagCategory.IMAGE_PLANE, true, ""),
    ImagePositionPatient( Tag.ImagePositionPatient, VR.DS,  DcmTagCategory.IMAGE_PLANE, true, "1.0\\1.0\\1.0"),
    ImageOrientationPatient( Tag.ImageOrientationPatient, VR.DS,  DcmTagCategory.IMAGE_PLANE, true, "1.0\\1.0\\1.0\\1.0\\1.0\\1.0"),
    ReconstructionDiameter( Tag.ReconstructionDiameter, VR.DS,  DcmTagCategory.IMAGE_PLANE, true, ""),
    SliceThickness( Tag.SliceThickness, VR.DS,  DcmTagCategory.IMAGE_PLANE, true, "1.0"),
    SliceLocation( Tag.SliceLocation, VR.DS,  DcmTagCategory.IMAGE_PLANE, true, ""),
    PixelSpacing( Tag.PixelSpacing, VR.DS,  DcmTagCategory.IMAGE_PLANE, true, "1.0\\1.0"),
    // IMAGE_PIXEL_TAG
    SamplesPerPixel( Tag.SamplesPerPixel, VR.US,  DcmTagCategory.IMAGE_PIXEL, true, ""),



    Columns( Tag.Columns, VR.US,  DcmTagCategory.IMAGE_PIXEL, true, ""),
    Rows( Tag.Rows, VR.US,  DcmTagCategory.IMAGE_PIXEL, true, ""),
    BitsAllocated( Tag.BitsAllocated, VR.US,  DcmTagCategory.IMAGE_PIXEL, true, ""),
    BitsStored( Tag.BitsStored, VR.US,  DcmTagCategory.IMAGE_PIXEL, true, ""),
    HighBit( Tag.HighBit, VR.US,  DcmTagCategory.IMAGE_PIXEL, true, ""),
    PixelRepresentation( Tag.PixelRepresentation, VR.US,  DcmTagCategory.IMAGE_PIXEL, true, ""),
    RescaleSlope( Tag.RescaleSlope, VR.DS,  DcmTagCategory.IMAGE_PIXEL, true, ""),
    RescaleIntercept( Tag.RescaleIntercept, VR.DS,  DcmTagCategory.IMAGE_PIXEL, true, ""),
    WindowCenter( Tag.WindowCenter, VR.DS,  DcmTagCategory.IMAGE_PIXEL, true, ""),
    WindowWidth( Tag.WindowWidth, VR.DS,  DcmTagCategory.IMAGE_PIXEL, true, ""),
    // OPTIONAL_TAG
    FieldOfViewDimensions( Tag.FieldOfViewDimensions, VR.IS,  DcmTagCategory.IMAGE_PLANE, false, ""),
    BodyPartExamined( Tag.BodyPartExamined, VR.CS,  DcmTagCategory.IMAGE, false, ""),

    DeviceSerialNumber( Tag.DeviceSerialNumber, VR.LO,  DcmTagCategory.EQUIPMENT, false, ""),
    AcquisitionNumber( Tag.AcquisitionNumber, VR.IS,  DcmTagCategory.IMAGE, false, ""),
    SoftwareVersions( Tag.SoftwareVersions, VR.LO,  DcmTagCategory.EQUIPMENT, false, ""),
    ImageType( Tag.ImageType, VR.CS,  DcmTagCategory.IMAGE, false, ""),
    ScanOptions( Tag.ScanOptions, VR.CS,  DcmTagCategory.IMAGE, false, ""),
    PhotometricInterpretation( Tag.PhotometricInterpretation, VR.CS,  DcmTagCategory.IMAGE_PIXEL, false, ""),
    PlanarConfiguration( Tag.PlanarConfiguration, VR.US,  DcmTagCategory.IMAGE_PIXEL, false, ""),
    // CT_TAG
    KVP( Tag.KVP, VR.DS,  DcmTagCategory.CT, true, ""),
    XRayTubeCurrent( Tag.XRayTubeCurrent, VR.IS,  DcmTagCategory.CT, true, ""),
    // OPTIONAL_CT_TAG
    DataCollectionDiameter( Tag.DataCollectionDiameter, VR.DS,  DcmTagCategory.CT, false, ""),
    DistanceSourceToDetector( Tag.DistanceSourceToDetector, VR.DS,  DcmTagCategory.CT, false, ""),
    DistanceSourceToPatient( Tag.DistanceSourceToPatient, VR.DS,  DcmTagCategory.CT, false, ""),
    GantryDetectorTilt( Tag.GantryDetectorTilt, VR.DS,  DcmTagCategory.CT, false, ""),
    TableHeight( Tag.TableHeight, VR.DS,  DcmTagCategory.CT, false, ""),
    RotationDirection( Tag.RotationDirection, VR.CS,  DcmTagCategory.CT, false, ""),
    ExposureTime( Tag.ExposureTime, VR.IS,  DcmTagCategory.CT, false, ""),
    Exposure( Tag.Exposure, VR.IS,  DcmTagCategory.CT, false, ""),
    FilterType( Tag.FilterType, VR.SH,  DcmTagCategory.CT, false, ""),
    GeneratorPower( Tag.GeneratorPower, VR.IS,  DcmTagCategory.CT, false, ""),
    FocalSpots( Tag.FocalSpots, VR.DS,  DcmTagCategory.CT, false, ""),
    ConvolutionKernel( Tag.ConvolutionKernel, VR.SH,  DcmTagCategory.CT, false, ""),
    RevolutionTime( Tag.RevolutionTime, VR.FD,  DcmTagCategory.CT, false, ""),
    SingleCollimationWidth( Tag.SingleCollimationWidth, VR.FD,  DcmTagCategory.CT, false, ""),
    TotalCollimationWidth( Tag.TotalCollimationWidth, VR.FD,  DcmTagCategory.CT, false, ""),
    TableSpeed( Tag.TableSpeed, VR.FD,  DcmTagCategory.CT, false, ""),
    TableFeedPerRotation( Tag.TableFeedPerRotation, VR.FD,  DcmTagCategory.CT, false, ""),
    // MR_TAG
    EchoTime( Tag.EchoTime, VR.DS,  DcmTagCategory.MR, true, ""),
    MagneticFieldStrength( Tag.MagneticFieldStrength, VR.DS,  DcmTagCategory.MR, true, ""),
    RepetitionTime( Tag.RepetitionTime, VR.DS,  DcmTagCategory.MR, true, ""),
    // OPTIONAL_MR_TAG
    ScanningSequence( Tag.ScanningSequence, VR.CS,  DcmTagCategory.MR, false, ""),
    SequenceVariant( Tag.SequenceVariant, VR.CS,  DcmTagCategory.MR, false, ""),
    MRAcquisitionType( Tag.MRAcquisitionType, VR.CS,  DcmTagCategory.MR, false, ""),
    NumberOfAverages( Tag.NumberOfAverages, VR.DS,  DcmTagCategory.MR, false, ""),
    ImagingFrequency( Tag.ImagingFrequency, VR.DS,  DcmTagCategory.MR, false, ""),
    ImagedNucleus( Tag.ImagedNucleus, VR.SH,  DcmTagCategory.MR, false, ""),
    EchoNumbers( Tag.EchoNumbers, VR.IS,  DcmTagCategory.MR, false, ""),
    SpacingBetweenSlices( Tag.SpacingBetweenSlices, VR.DS,  DcmTagCategory.MR, false, ""),
    NumberOfPhaseEncodingSteps( Tag.NumberOfPhaseEncodingSteps, VR.IS,  DcmTagCategory.MR, false, ""),
    EchoTrainLength( Tag.EchoTrainLength, VR.IS,  DcmTagCategory.MR, false, ""),
    PercentSampling( Tag.PercentSampling, VR.DS,  DcmTagCategory.MR, false, ""),
    PercentPhaseFieldOfView( Tag.PercentPhaseFieldOfView, VR.DS,  DcmTagCategory.MR, false, ""),
    PixelBandwidth( Tag.PixelBandwidth, VR.DS,  DcmTagCategory.MR, false, ""),
    LowRRValue( Tag.LowRRValue, VR.IS,  DcmTagCategory.MR, false, ""),
    HighRRValue( Tag.HighRRValue, VR.IS,  DcmTagCategory.MR, false, ""),
    IntervalsAcquired( Tag.IntervalsAcquired, VR.IS,  DcmTagCategory.MR, false, ""),
    IntervalsRejected( Tag.IntervalsRejected, VR.IS,  DcmTagCategory.MR, false, ""),
    HeartRate( Tag.HeartRate, VR.IS,  DcmTagCategory.MR, false, ""),
    ReceiveCoilName( Tag.ReceiveCoilName, VR.SH,  DcmTagCategory.MR, false, ""),
    TransmitCoilName( Tag.TransmitCoilName, VR.SH,  DcmTagCategory.MR, false, ""),
    AcquisitionMatrix( Tag.AcquisitionMatrix, VR.US,  DcmTagCategory.MR, false, ""),
    FlipAngle( Tag.FlipAngle, VR.DS,  DcmTagCategory.MR, false, ""),
    SAR( Tag.SAR, VR.DS,  DcmTagCategory.MR, false, ""),
    dBdt( Tag.dBdt, VR.DS,  DcmTagCategory.MR, false, ""),
    AcquisitionDuration( Tag.AcquisitionDuration, VR.FD,  DcmTagCategory.MR, false, ""),
    DiffusionBValue( Tag.DiffusionBValue, VR.FD,  DcmTagCategory.MR, false, ""),
    DiffusionGradientOrientation( Tag.DiffusionGradientOrientation, VR.FD,  DcmTagCategory.MR, false, "");


    private int tag;
    private VR vr;
    private DcmTagCategory category;
    private boolean mandatroy;
    private String defultVaule;

    DicomReconTag(int tag, VR vr, DcmTagCategory category, boolean mandatroy, String defultVaule){
        this.tag = tag;
        this.vr = vr;
        this.category = category;
        this.mandatroy = mandatroy;
        this.defultVaule = defultVaule;
    }

    public static List<DicomReconTag> getTagList(int[] input) {
        List<DicomReconTag> output = new ArrayList<>();
        for (int tmpTag :input) {
            if(getByTag(tmpTag)!=null){
                output.add(getByTag(tmpTag));
            }
        }
        return output;
    }

    public static List<DicomReconTag> getMandatroyTagList() {
        List<DicomReconTag> output = new ArrayList<>();
        for (DicomReconTag tmp : DicomReconTag.class.getEnumConstants()) {

            if (tmp.getCategory().isMandatroy())
                output.add(tmp);

        }

        return output;
    }

    public static List<DicomReconTag> getCTTagList() {
        List<DicomReconTag> output = new ArrayList<>();
        for (DicomReconTag tmp : DicomReconTag.class.getEnumConstants()) {

            if (tmp.getCategory().isCT())
                output.add(tmp);
        }
        return output;
    }

    public static List<DicomReconTag> getMRTagList() {
        List<DicomReconTag> output = new ArrayList<>();
        for (DicomReconTag tmp : DicomReconTag.class.getEnumConstants()) {

            if (tmp.getCategory().isMR())
                output.add(tmp);
        }
        return output;
    }

    public static DicomReconTag getByTag(int tag){
        for (DicomReconTag tmp : DicomReconTag.class.getEnumConstants()) {
            if(tmp.getTag()==tag)
            return  tmp;
        }
        return null;
    }

    public DcmTagCategory getCategory() {
        return  this.category ;
    }
    public boolean getMandatroy() {
        return  this.mandatroy ;
    }


    public int getTag() {
        return  this.tag ;
    }

    public VR getVrR() {
        return  this.vr ;
    }


    public String getDefultValue() {
        return this.defultVaule;
    }

}
