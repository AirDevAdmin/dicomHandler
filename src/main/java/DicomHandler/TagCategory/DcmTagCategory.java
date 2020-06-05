package DicomHandler.TagCategory;

public enum DcmTagCategory {
    META("Meta"),
    SOP("SOP"),
    EQUIPMENT("Equipment"),
    PROTOCOL("Protocol"),
    SITE("Site"),
    PATIENT("Patient"),
    STUDY("Study"),

    SERIES("Series"),
    IMAGE("Image"),
    IMAGE_PLANE("Image Plane"),
    IMAGE_PIXEL("Image Pixel"),

    CT( "CT"),
    MR( "MR"),
    ES( "ES"),
    US( "US"),
    PET( "PET");


    private String name;


    DcmTagCategory(String name){
        this.name = name;

    }

    public boolean isMandatroy(){
        return  this.equals(META) ||  this.equals(SOP) || this.equals(EQUIPMENT) || this.equals(PROTOCOL) || this.equals(SITE) ||
                this.equals(PATIENT) ||  this.equals(STUDY) || this.equals(SERIES) || this.equals(IMAGE) || this.equals(IMAGE_PLANE)  ||
        this.equals(IMAGE_PIXEL) ;
    }


    public boolean isCT() {
        return this.equals(CT);
    }

    public boolean isMR() {
        return this.equals(MR);
    }
}
