package com.eucsoft.foodex.db.model;

import java.util.Date;

public class FoodPair {

    public long id;
    //user FoodPair props
    public User user = new User();
    //stranger FoodPair props
    public User stranger = new User();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FoodPair foodPair = (FoodPair) o;

        if (stranger != null ? !stranger.equals(foodPair.stranger) : foodPair.stranger != null)
            return false;
        if (user != null ? !user.equals(foodPair.user) : foodPair.user != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = user != null ? user.hashCode() : 0;
        result = 31 * result + (stranger != null ? stranger.hashCode() : 0);
        return result;
    }

    public class User {
        public String foodURL;
        public Date foodDate;
        public int bonAppetit;
        public String mapURL;

        public boolean isBonAppetit() {
            return bonAppetit > 0;
        }

        public String getFoodFileName() {
            return foodURL == null ? null : foodURL.substring(foodURL.lastIndexOf('/') + 1);
        }

        public String getMapFileName() {
            return mapURL == null ? null : mapURL.substring(mapURL.lastIndexOf('/') + 1);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            User user = (User) o;

            if (bonAppetit != user.bonAppetit) return false;
            if (foodDate != null ? !foodDate.equals(user.foodDate) : user.foodDate != null)
                return false;
            if (foodURL != null ? !foodURL.equals(user.foodURL) : user.foodURL != null)
                return false;
            if (mapURL != null ? !mapURL.equals(user.mapURL) : user.mapURL != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = foodURL != null ? foodURL.hashCode() : 0;
            result = 31 * result + (foodDate != null ? foodDate.hashCode() : 0);
            result = 31 * result + bonAppetit;
            result = 31 * result + (mapURL != null ? mapURL.hashCode() : 0);
            return result;
        }
    }

}