package DicomHandler.SortDicom;



import org.dcm4che3.data.Tag;

import java.util.*;


public class DicomSortSeriesHandler{


    private   List<DicomSortHandler.SliceInfo> sliceinfos = new ArrayList<>();



    public static final int SORT_INSTNUM = 1;
    public static final int SORT_POSISTION= 2;
    public static final int SORT_SOP    = 3;



    public static final int AXIAL = 2;
    public static final int CORONAL = 1;
    public static final int SAGITTAL = 0;
    public static final int NON_CAL = -1;


    public int sortStatc = 0;

    public void setSliceinfos(List<DicomSortHandler.SliceInfo> input){
        this.sliceinfos = input;
    }


    public String getSortState()
    {
        if(sortStatc == SORT_INSTNUM)
            return "Sorted by Instance Number (0020,0013)";
        if(sortStatc == SORT_POSISTION)
            return "Sorted by Image Position (Patient) (0020,0032)";
        else
            return "Sorted by SOP Instance UID (0008,0018)";
    }

    public List<DicomSortHandler.SliceInfo> getSortFileList() {

        if (sliceinfos.size() == 0) {
            return null;
        }
        List<DicomSortHandler.SliceInfo> output = null;
        output = this.sortInstaceNum();
        if (output != null) {
            sortStatc = SORT_INSTNUM;
            return output;
        }

        output = this.sortPos();
        if (output != null){
            sortStatc = SORT_POSISTION;
            return output;
        }

        output = this.sortSOPUID();
        if (output != null) {
            sortStatc = SORT_SOP;
            return output;
        }

        return null;

    }

    private List<DicomSortHandler.SliceInfo> sortInstaceNum() {
        TreeMap<Integer, DicomSortHandler.SliceInfo> tmpMap = new TreeMap<>();
        for (int idx = 0; idx < this.sliceinfos.size(); idx++) {
            DicomSortHandler.SliceInfo tmp = this.sliceinfos.get(idx);
            if (tmp.instNum != null) {
                int instkey = Integer.parseInt(tmp.instNum);

                if (tmpMap.containsKey(instkey))
                    return null;
                tmpMap.put(instkey, tmp);
            } else {
                return null;
            }
        }
        return new ArrayList<>(tmpMap.values());
    }

    private List<DicomSortHandler.SliceInfo> sortPos() {
        TreeMap<Double, DicomSortHandler.SliceInfo> tmpMap = new TreeMap<>();

        String ori = this.sliceinfos.get(this.sliceinfos.size() / 2).imgOrient;
        if (ori == null || ori.equals(""))
            return null;
        int axis = calAxis(ori);
        for (int idx = 0; idx < this.sliceinfos.size(); idx++) {
            DicomSortHandler.SliceInfo tmp = this.sliceinfos.get(idx);
            if (tmp.imgPos != null && !tmp.imgPos.equals("") ) {
                double posKey = Double.parseDouble(tmp.imgPos.split("\\\\")[axis]);


                if (tmpMap.containsKey(posKey))
                    return null;
                tmpMap.put(posKey, tmp);
            } else {
                return null;
            }
        }
        return new ArrayList<>(tmpMap.values());
    }

    private List<DicomSortHandler.SliceInfo> sortSOPUID() {
        TreeMap<String, DicomSortHandler.SliceInfo> tmpMap = new TreeMap<>();

        for (int idx = 0; idx < this.sliceinfos.size(); idx++) {
            DicomSortHandler.SliceInfo tmp = this.sliceinfos.get(idx);
            if (tmp.sopUID != null) {
                String uidKey = tmp.sopUID;

                if (tmpMap.containsKey(uidKey))
                    return null;
                tmpMap.put(uidKey, tmp);
            } else {
                return null;
            }
        }
        return new ArrayList<>(tmpMap.values());
    }

    private int calAxis(String imgOri) {
        System.out.println(imgOri);
        String[] tmpString = imgOri.split("\\\\");
        double x1 = Double.parseDouble(tmpString[0]);
        double y1 = Double.parseDouble(tmpString[1]);
        double z1 = Double.parseDouble(tmpString[2]);
        double x2 = Double.parseDouble(tmpString[3]);
        double y2 = Double.parseDouble(tmpString[4]);
        double z2 = Double.parseDouble(tmpString[5]);

        double v1 = 0.0;
        double v2 = 0.0;
        double v3 = 0.0;

        v1 = x1 * x1 + x2 * x2;
        v2 = y1 * y1 + y2 * y2;
        v3 = z1 * z1 + z2 * z2;


        if (v1 <= v2 && v1 <= v3) {
            System.out.println("SAGITTAL");
            return SAGITTAL;
        }


        if (v2 <= v1 && v2 <= v3) {
            System.out.println("CORONAL");
            return CORONAL;

        }


        if (v3 <= v1 && v3 <= v2) {
            System.out.println("AXIAL");
            return AXIAL;
        }

        return NON_CAL;
    }


}
