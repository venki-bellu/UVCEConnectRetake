package com.venkibellu.myapplication;

import android.content.Context;

import java.util.HashMap;


@SuppressWarnings("FieldCanBeLocal")
class URLGetter {
    private Context context;

    /*
        Following are the Google Drive URL for syllabus.
        On updating to a new syllabus, just paste the new 'shareable link'
        here.
     */

    /*****************************************************************************************
     *                                        SYLLABUS                                       *
     *                                  GOOGLE DRIVE LINKS                                   *
     *                                                                                       *
     ****************************************************************************************/

    private final String firstYearSyllabusURL =
            "https://drive.google.com/open?id=0B1UVfii_DmyGSDBYUHZuYVczNDA"; // first year

    private final String ARCH_SyllabusURL[] = {
            "", // first year
            "", // second year
            "", // third year
            "", // fourth year
            "", // fifth year
    };

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





    /*****************************************************************************************
     *                                    QUESTION PAPERS                                    *
     *                                  GOOGLE DRIVE LINKS                                   *
     *                                                                                       *
     ****************************************************************************************/

    private final String firstYearQPURL =
            ""; // first year

    private final String ARCH_QPURL[] = {
            "", // 1st sem
            "", // 2nd sem
            "", // 3rd sem
            "", // 4th sem
            "", // 5th sem
            "", // 6th sem
            "", // 7th sem
            "", // 8th sem
            "", // 9th sem
            "", // 10th sem
    };

    private final String CSE_QPURL[] = {
            "", // 3rd sem
            "", // 4th sem
            "", // 5th sem
            "", // 6th sem
            "", // 7th sem
            "", // 8th sem
    };

    private final String EEE_QPURL[] = {
            "", // 3rd sem
            "", // 4th sem
            "", // 5th sem
            "", // 6th sem
            "", // 7th sem
            "", // 8th sem
    };

    private final String ECE_QPURL[] = {
            "", // 3rd sem
            "", // 4th sem
            "", // 5th sem
            "", // 6th sem
            "", // 7th sem
            "", // 8th sem
    };

    private final String ISE_QPURL[] = {
            "", // 3rd sem
            "", // 4th sem
            "", // 5th sem
            "", // 6th sem
            "", // 7th sem
            "", // 8th sem
    };

    private final String ME_QPURL[] = {
            "", // 3rd sem
            "", // 4th sem
            "", // 5th sem
            "", // 6th sem
            "", // 7th sem
            "", // 8th sem
    };

    private final String CE_QPURL[] = {
            "", // 3rd sem
            "", // 4th sem
            "", // 5th sem
            "", // 6th sem
            "", // 7th sem
            "", // 8th sem
    };

    // Here is map from the string to the String URL array defined above.
    private final static HashMap<String, String[]> syllabusURLmapper = new HashMap<>();
    private final static HashMap<String, String[]> qpURLmapper = new HashMap<>();


    URLGetter(Context _context) {
        context = _context;
        final String arch = context.getString(R.string.arch),
                     cse = context.getString(R.string.cse),
                     ise = context.getString(R.string.ise),
                     ce = context.getString(R.string.ce),
                     me = context.getString(R.string.me),
                     eee = context.getString(R.string.eee),
                     ece = context.getString(R.string.ece);

        syllabusURLmapper.put(arch, ARCH_SyllabusURL);
        syllabusURLmapper.put(cse, CSE_SyllabusURL);
        syllabusURLmapper.put(ise, ISE_SyllabusURL);
        syllabusURLmapper.put(ce, CE_SyllabusURL);
        syllabusURLmapper.put(me, ME_SyllabusURL);
        syllabusURLmapper.put(eee, EEE_SyllabusURL);
        syllabusURLmapper.put(ece, ECE_SyllabusURL);

        qpURLmapper.put(arch, ARCH_QPURL);
        qpURLmapper.put(cse, CSE_QPURL);
        qpURLmapper.put(ise, ISE_QPURL);
        qpURLmapper.put(ce, CE_QPURL);
        qpURLmapper.put(me, ME_QPURL);
        qpURLmapper.put(eee, EEE_QPURL);
        qpURLmapper.put(ece, ECE_QPURL);
    }

    // returns the download URL by obtaining the ID from the google drive URL.
    String getSyllabusURL(String branch, Integer year) {
        String downloadURL = "https://docs.google.com/uc?id=[FILE_ID]&export=download";
        String ID = "";

        if (branch.equals(context.getString(R.string.arch))) {
            ID = getID(ARCH_SyllabusURL[year - 1]);
        } else if (year == 1) {
            ID = getID(firstYearSyllabusURL);
        } else {
            String branchArray[] = syllabusURLmapper.get(branch);
            ID = getID(branchArray[year - 2]);
        }

        if (ID.isEmpty()) {
            return "";
        }

        return downloadURL.replace("[FILE_ID]", ID);
    }

    String getQuestionPaperURL(String branch, Integer semester) {
        String downloadURL = "https://docs.google.com/uc?id=[FILE_ID]&export=download";
        String ID = "";

        if (branch.equals(context.getString(R.string.arch))) {
            ID = getID(ARCH_QPURL[semester - 1]);
        } else if (semester <= 2) {
            ID = getID(firstYearQPURL);
        } else {
            String branchArray[] = qpURLmapper.get(branch);
            ID = getID(branchArray[semester - 3]);
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
