package com.dragonnedevelopment.bakingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * BakingApp Created by Muir on 30/04/2018.
 * <p>
 * A {@link Ingredient} object which contains ingredients related to a single Recipe
 */
public class Ingredient implements Parcelable {

    /**
     * {@link Ingredient} attributes
     * Each attribute has a corresponding @SerializedName which is needed for GSON to map the JSON
     * keys with the attributes of the {@link Ingredient} object.
     */
    @SerializedName("quantity")
    private Double ingredientQuantity;
    @SerializedName("measure")
    private String ingredientMeasure;
    @SerializedName("ingredient")
    private String ingredient;

    /**
     * Empty constructor
     */
    public Ingredient() {

    }

    //Getters
    public Double getIngredientQuantity() {
        return ingredientQuantity;
    }

    public String getIngredientMeasure() {
        return ingredientMeasure;
    }

    public String getIngredient() {
        return ingredient;
    }

    // Setters
    public void setIngredientQuantity(Double ingredientQuantity) {
        this.ingredientQuantity = ingredientQuantity;
    }

    public void setIngredientMeasure(String ingredientMeasure) {
        this.ingredientMeasure = ingredientMeasure;
    }
    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.ingredientQuantity);
        dest.writeString(this.ingredientMeasure);
        dest.writeString(this.ingredient);
    }

    protected Ingredient(Parcel in) {
        this.ingredientQuantity = (Double) in.readValue(Double.class.getClassLoader());
        this.ingredientMeasure = in.readString();
        this.ingredient = in.readString();
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel source) {
            return new Ingredient(source);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };



}
