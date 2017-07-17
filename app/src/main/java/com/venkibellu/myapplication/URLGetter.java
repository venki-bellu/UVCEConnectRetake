package com.venkibellu.myapplication;

import android.content.Context;

import java.util.HashMap;


@SuppressWarnings("FieldCanBeLocal")
class URLGetter {
    /*
        Following are the Google Drive URL for syllabus.
        On updating to a new syllabus, just paste the new 'shareable link'
        here.
     */

    private final String firstYearSyllabusURL =
            "https://drive.google.com/open?id=0B1UVfii_DmyGSDBYUHZuYVczNDA";


    private final String CSE_SyllabusURL[] = {
            "https://drive.google.com/open?id=0B1UVfii_DmyGclBQeDFJM1p5SzQ", // second year
            "https://drive.google.com/open?id=0B1UVfii_DmyGeS1WVkhERnd5bU0", // third year
            "https://drive.google.com/open?id=0B1UVfii_DmyGLTNfRl83MVlXcVE", // fourth year
    };

    private final String EEE_SyllabusURL[] = {
            "https://drive.google.com/open?id=0B1UVfii_DmyGYWFwV3E3TUJGZEE", // second year
            "https://drive.google.com/open?id=0B1UVfii_DmyGbW1rUS0wZWlOWkU", // third year
            "https://drive.google.com/open?id=0B1UVfii_DmyGUnR2enN6SE1VbkU", // fourth year
    };

    private final String ECE_SyllabusURL[] = {
            "https://drive.google.com/open?id=0B1UVfii_DmyGV1hGMnhWVlBMb2s", // second year
            "https://drive.google.com/open?id=0B1UVfii_DmyGY2lwMkg2cDlzMlE", // third year
            "", // fourth year
    };

    private final String ISE_SyllabusURL[] = {
            "https://drive.google.com/open?id=0B1UVfii_DmyGS29TSlVYcWIzQjA", // second year
            "https://drive.google.com/open?id=0B1UVfii_DmyGS29TSlVYcWIzQjA", // third year
            "https://drive.google.com/open?id=0B1UVfii_DmyGS29TSlVYcWIzQjA", // fourth year
    };

    private final String ME_SyllabusURL[] = {
            "https://drive.google.com/open?id=0B1UVfii_DmyGa1M0ZGlET0lWVWM", // second year
            "https://drive.google.com/open?id=0B1UVfii_DmyGaDdZeWlYd3VuS2c", // third year
            "https://drive.google.com/open?id=0B1UVfii_DmyGWHR6MU8tb2IzWFU", // fourth year
    };

    private final String CE_SyllabusURL[] = {
            "", // second year
            "", // third year
            "", // fourth year
    };




    // Here is map from the string to the String URL array defined above.
    private final  static HashMap<String, String[]> URLmapper = new HashMap<>();

    URLGetter(Context context) {
        final String cse = context.getString(R.string.cse),
                     ise = context.getString(R.string.ise),
                     ce = context.getString(R.string.ce),
                     me = context.getString(R.string.me),
                     eee = context.getString(R.string.eee),
                     ece = context.getString(R.string.ece);

        URLmapper.put(cse, CSE_SyllabusURL);
        URLmapper.put(ise, ISE_SyllabusURL);
        URLmapper.put(ce, CE_SyllabusURL);
        URLmapper.put(me, ME_SyllabusURL);
        URLmapper.put(eee, EEE_SyllabusURL);
        URLmapper.put(ece, ECE_SyllabusURL);
    }

    // returns the download URL by obtaining the ID from the google drive URL.
    String getSyllabusURL(String branch, Integer year) {
        String downloadURL = "https://docs.google.com/uc?id=[FILE_ID]&export=download";
        String ID = "";

        if (year == 1) {
            ID = getID(firstYearSyllabusURL);
        } else {
            String branchArray[] = URLmapper.get(branch);
            ID = getID(branchArray[year - 2]);
        }

        if (ID.isEmpty()) {
            return "";
        }

        return downloadURL.replace("[FILE_ID]", ID);
    }

    // returns the ID from the google drive URL.
    private String getID(String url) {
        String ID = "";

        for (int i = url.length() - 1; i >= 0; --i) {
            if (url.charAt(i) != '=') {
                ID = url.charAt(i) + ID;
            } else {
                break;
            }
        }

        return ID;
    }
}
