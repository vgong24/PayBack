package com.example.victor.payback;

/**
 * Created by Victor on 2/14/2015.
 */
import com.parse.*;

public class Interests extends ParseObject {
    public void setCat(String cat) {
        put("category", cat);
    }
    public int getCat() {
        return getInt("category");
    }
    public String getUser() {
        return getString("userid");
    }
    public void setUser(String id) {
        put("userid", id);
    }
    public static ParseQuery<Interests> getQuery() {
        return ParseQuery.getQuery(Interests.class);
    }

}
