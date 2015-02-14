package com.example.victor.payback;

/**
 * Created by Victor on 2/14/2015.
 */
import com.parse.*;

public class Category extends ParseObject {
    public void setCat(String activity) {
        put("name", activity);
    }
    public String getCat() {
        return getString("name");
    }
    public byte[] getImage() {
        return getBytes("image");
    }
    public void setImage(byte [] image) {
        put("image", image);
    }
    public static ParseQuery<Category> getQuery() {
        return ParseQuery.getQuery(Category.class);
    }

}
