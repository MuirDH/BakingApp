package com.dragonnedevelopment.bakingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * BakingApp Created by Muir on 30/04/2018.
 * <p>
 * A {@link Step} object that contains steps related to a single {@link Recipe} item.
 */
public class Step implements Parcelable {

    /**
     * {@link Step} attributes
     * each attribute has a corresponding @SerializedName that is needed for GSON to map the JSON
     * keys with the attributes of the {@link Step} object.
     */
    @SerializedName("id")
    private Integer stepId;
    @SerializedName("shortDescription")
    private String stepShortDescription;
    @SerializedName("description")
    private String stepDescription;
    @SerializedName("videoURL")
    private String stepVideoUrl;
    @SerializedName("thumbnailURL")
    private String stepThumbnailUrl;

    // This is used to identify the current step selection and to change the background colour accordingly
    private boolean isSelected;

    /**
     * empty constructor
     */
    public Step() {

    }

    //Getters
    public Integer getStepId() {
        return stepId;
    }

    public String getStepDescription() {
        return stepDescription;
    }

    public String getStepShortDescription() {
        return stepShortDescription;
    }

    public String getStepThumbnailUrl() {
        return stepThumbnailUrl;
    }

    public String getStepVideoUrl() {
        return stepVideoUrl;
    }

    public boolean getIsSelected() {
        return isSelected;
    }

    //Setters
    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void setStepDescription(String stepDescription) {
        this.stepDescription = stepDescription;
    }

    public void setStepId(Integer stepId) {
        this.stepId = stepId;
    }

    public void setStepShortDescription(String stepShortDescription) {
        this.stepShortDescription = stepShortDescription;
    }

    public void setStepThumbnailUrl(String stepThumbnailUrl) {
        this.stepThumbnailUrl = stepThumbnailUrl;
    }

    public void setStepVideoUrl(String stepVideoUrl) {
        this.stepVideoUrl = stepVideoUrl;
    }

    /**
     * default constructor.
     * constructs a new {@link Step} object
     *
     * @param parcel contains the information in the {@link Step} object
     */
    protected Step(Parcel parcel) {
        stepId = parcel.readByte() == 0x00 ? null : parcel.readInt();
        stepShortDescription = parcel.readString();
        stepDescription = parcel.readString();
        stepVideoUrl = parcel.readString();
        stepThumbnailUrl = parcel.readString();
    }

    public static final Parcelable.Creator<Step> CREATOR = new Parcelable.Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (stepId == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(stepId);
        }
        dest.writeString(stepShortDescription);
        dest.writeString(stepDescription);
        dest.writeString(stepVideoUrl);
        dest.writeString(stepThumbnailUrl);
    }

}
