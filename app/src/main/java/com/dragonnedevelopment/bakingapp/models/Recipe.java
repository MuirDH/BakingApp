package com.dragonnedevelopment.bakingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * BakingApp Created by Muir on 30/04/2018.
 *
 * A {@link Recipe} object that contains details related to a single Recipe
 *
 */

public class Recipe implements Parcelable {

    /*
     * Recipe attributes.
     *
     * Each attribute has a corresponding SerializedName that is needed for GSON to map the JSON
     * keys with the attributes of the Recipe object.
     *
     */

    @SerializedName("id")
    private Integer recipeId;

    @SerializedName("name")
    private String recipeName;

    @SerializedName("ingredients")
    private List<Ingredient> recipeIngredients = null;

    @SerializedName("steps")
    private List<Step> recipeSteps = null;

    @SerializedName("servings")
    private Integer recipeServings;

    @SerializedName("image")
    private String recipeImage;

    /**
     * empty constructor
     */
    public Recipe() {

    }

    // Getter methods

    public Integer getRecipeId() {
        return recipeId;
    }

    public Integer getRecipeServings() {
        return recipeServings;
    }

    public List<Ingredient> getRecipeIngredients() {
        return recipeIngredients;
    }

    public List<Step> getRecipeSteps() {
        return recipeSteps;
    }

    public String getRecipeImage() {
        return recipeImage;
    }

    public String getRecipeName() {
        return recipeName;
    }

    // Setter methods

    public void setRecipeId(Integer recipeId) {
        this.recipeId = recipeId;
    }

    public void setRecipeImage(String recipeImage) {
        this.recipeImage = recipeImage;
    }

    public void setRecipeIngredients(List<Ingredient> recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public void setRecipeServings(Integer recipeServings) {
        this.recipeServings = recipeServings;
    }

    public void setRecipeSteps(List<Step> recipeSteps) {
        this.recipeSteps = recipeSteps;
    }

    /**
     * default constructor. constructs a new {@link Recipe} object
     * @param parcel the recipe parcel containing all of the recipe information
     */
    protected Recipe(Parcel parcel) {
        recipeId = parcel.readByte() == 0x00 ? null : parcel.readInt();
        recipeName = parcel.readString();
        if (parcel.readByte() == 0x01) {
            recipeIngredients = new ArrayList<>();
            parcel.readList(recipeIngredients, Ingredient.class.getClassLoader());
        }else {
            recipeIngredients = null;
        }
        if (parcel.readByte() == 0x01) {
            recipeSteps = new ArrayList<>();
            parcel.readList(recipeSteps, Step.class.getClassLoader());
        }else {
            recipeSteps = null;
        }
        recipeServings = parcel.readByte() == 0x00 ? null : parcel.readInt();
        recipeImage = parcel.readByte() == 0x00 ? null : parcel.readString();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (recipeId == null){
            dest.writeByte((byte) (0x00));
        }else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(recipeId);
        }

        dest.writeString(recipeName);

        if (recipeIngredients == null){
            dest.writeByte((byte) (0x00));
        }else {
            dest.writeByte((byte) (0x01));
            dest.writeList(recipeIngredients);
        }

        if (recipeSteps == null){
            dest.writeByte((byte) (0x00));
        }else {
            dest.writeByte((byte) (0x01));
            dest.writeList(recipeSteps);
        }

        if (recipeServings == null) {
            dest.writeByte((byte) (0x00));
        }else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(recipeServings);
        }

        dest.writeString(recipeImage);
    }
}
