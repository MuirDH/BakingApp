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

    public String getIngredient() {
        return ingredient;
    }

    public String getIngredientMeasure() {
        return ingredientMeasure;
    }

    // Setters
    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public void setIngredientMeasure(String ingredientMeasure) {
        this.ingredientMeasure = ingredientMeasure;
    }

    public void setIngredientQuantity(Double ingredientQuantity) {
        this.ingredientQuantity = ingredientQuantity;
    }

    /**
     * default constructor.
     * constructs a new {@link Ingredient} object
     *
     * @param parcel which contains the information the ingredient object is made up of.
     */
    public Ingredient(Parcel parcel) {
        ingredientQuantity = parcel.readByte() == 0x00 ? null : parcel.readDouble();
        ingredientMeasure = parcel.readByte() == 0x00 ? null : parcel.readString();
        ingredient = parcel.readString();
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel parcel) {
            return new Ingredient(parcel);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (ingredientQuantity == null) dest.writeByte((byte) (0x00));
        else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(ingredientQuantity);
        }

        if (ingredientMeasure != null) {
            dest.writeByte((byte) (0x01));

        } else {
            dest.writeByte((byte) (0x00));
            dest.writeString(ingredientMeasure);
        }
        dest.writeString(ingredient);
    }
}
