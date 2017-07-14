package com.venkibellu.myapplication;

import android.content.Context;
import android.content.res.Resources;

import com.bumptech.glide.load.engine.Resource;

import java.util.HashMap;

/**
 * Created by captaindavinci on 14/7/17.
 */

public class URLGetter {
    /*
        Following are the Google Drive URL for syllabus.
        On updating to a new syllabus, just paste the new 'shareable link'
        here.
     */

    private final String firstYearSyllabusURL =
            "https://drive.google.com/open?id=0B1UVfii_DmyGSTBYM3k2aHRIVUk";


    private final String CSEsyllabusURL[] = {
            "https://drive.google.com/open?id=0B1UVfii_DmyGSTBYM3k2aHRIVUk", // second year
            "https://drive.google.com/open?id=0B1UVfii_DmyGSTBYM3k2aHRIVUk", // third year
            "https://drive.google.com/open?id=0B1UVfii_DmyGSTBYM3k2aHRIVUk", // fourth year
    };

    private final String EEEsyllabusURL[] = {
            "https://drive.google.com/open?id=0B1UVfii_DmyGSTBYM3k2aHRIVUk", // second year
            "https://drive.google.com/open?id=0B1UVfii_DmyGSTBYM3k2aHRIVUk", // third year
            "https://drive.google.com/open?id=0B1UVfii_DmyGSTBYM3k2aHRIVUk", // fourth year
    };

    private final String ECEsyllabusURL[] = {
            "https://drive.google.com/open?id=0B1UVfii_DmyGSTBYM3k2aHRIVUk", // second year
            "https://drive.google.com/open?id=0B1UVfii_DmyGSTBYM3k2aHRIVUk", // third year
            "https://drive.google.com/open?id=0B1UVfii_DmyGSTBYM3k2aHRIVUk", // fourth year
    };

    private final String ISEsyllabusURL[] = {
            "https://drive.google.com/open?id=0B1UVfii_DmyGSTBYM3k2aHRIVUk", // second year
            "https://drive.google.com/open?id=0B1UVfii_DmyGSTBYM3k2aHRIVUk", // third year
            "https://drive.google.com/open?id=0B1UVfii_DmyGSTBYM3k2aHRIVUk", // fourth year
    };

    private final String MEsyllabusURL[] = {
            "https://drive.google.com/open?id=0B1UVfii_DmyGSTBYM3k2aHRIVUk", // second year
            "https://drive.google.com/open?id=0B1UVfii_DmyGSTBYM3k2aHRIVUk", // third year
            "https://drive.google.com/open?id=0B1UVfii_DmyGSTBYM3k2aHRIVUk", // fourth year
    };

    private final String CEsyllabusURL[] = {
            "https://drive.google.com/open?id=0B1UVfii_DmyGSTBYM3k2aHRIVUk", // second year
            "https://drive.google.com/open?id=0B1UVfii_DmyGSTBYM3k2aHRIVUk", // third year
            "https://drive.google.com/open?id=0B1UVfii_DmyGSTBYM3k2aHRIVUk", // fourth year
    };

    /*
        Here is map from the string to the String URL array defined above.
     */

    private final  static HashMap<String, String[]> URLmapper = new HashMap<>();

    URLGetter(Context context) {
        final String cse = context.getString(R.string.cse),
                     ise = context.getString(R.string.ise),
                     ce = context.getString(R.string.ce),
                     me = context.getString(R.string.me),
                     eee = context.getString(R.string.eee),
                     ece = context.getString(R.string.ece);

        URLmapper.put(cse, CSEsyllabusURL);
        URLmapper.put(ise, ISEsyllabusURL);
        URLmapper.put(ce, CEsyllabusURL);
        URLmapper.put(me, MEsyllabusURL);
        URLmapper.put(eee, EEEsyllabusURL);
        URLmapper.put(ece, ECEsyllabusURL);
    }

    public String getSyllabusURL(String branch, Integer year) {
        String downloadURL = "https://docs.google.com/uc?id=[FILE_ID]&export=download";
        String ID = "";

        if (year == 1) {
            ID = getID(firstYearSyllabusURL);
        } else {
            String branchArray[] = URLmapper.get(branch);
            ID = getID(branchArray[year - 2]);
        }

        return downloadURL.replace("[FILE_ID]", ID);
    }

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













































































