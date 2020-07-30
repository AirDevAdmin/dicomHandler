package DicomHandler.Util;

import java.util.Random;

public class StringUtil {


    public static String getRandomString(int len) {

        StringBuffer temp = new StringBuffer();
        Random rnd = new Random();
        for (int i = 0; i < len; i++) {
            int rIndex = rnd.nextInt(2);
            switch (rIndex) {

                case 0:
                    // A-Z
                    temp.append((char) ((int) (rnd.nextInt(26)) + 65));
                    break;
                case 1:
                    // 0-9
                    temp.append((rnd.nextInt(10)));
                    break;
            }
        }
        return temp.toString();
    }
    public static String getRandomNumber(int len) {
        StringBuffer temp = new StringBuffer();
        for (int i = 0; i < len; i++) {

            double dValue = Math.random();

            int iValue = (int) (dValue * 10);
            temp.append(iValue);


        }

        return temp.toString();
    }




    public static String removeOtherChar(String input) {
        input = input.trim();
		String pateten = "^[0-10a-zA-Z+_ /\\-!@#$%^&*(),\'.?\":\\[\\]{}|<>\\\\]*$";

       return input.replaceAll(pateten, "");


    }

    public static String insertSpaceFrontBigText(String input) {

        StringBuilder builder = new StringBuilder();
        builder.append(input.charAt(0));
        boolean chkBig = true;
        for(int i=1; i<input.length(); i++)
        {
            int tmp =(int)input.charAt(i);

            if(  (65<=tmp) && (tmp<=90) && !chkBig) {

                builder.append(" ");
                chkBig = true;
            }else{
                chkBig = false;
            }
            builder.append(input.charAt(i));

        }
        return builder.toString();

    }



}
