package com.mychauffeurapp.model;

import android.text.style.CharacterStyle;
import com.google.android.gms.common.data.Freezable;
import java.util.List;

public interface AutocompletePrediction extends Freezable<AutocompletePrediction> {
    CharSequence getFullText(CharacterStyle var1);

    CharSequence getPrimaryText(CharacterStyle var1);

    CharSequence getSecondaryText(CharacterStyle var1);

    @Deprecated
    String getDescription();

    @Deprecated
    List<? extends AutocompletePrediction.Substring> getMatchedSubstrings();

    String getPlaceId();

    List<Integer> getPlaceTypes();

    @Deprecated
    public interface Substring {
        int getOffset();

        int getLength();
    }
}
