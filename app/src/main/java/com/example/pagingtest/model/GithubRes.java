package com.example.pagingtest.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

/**
 * @author 胡森
 * @description GitHub model
 * @date 2020-10-27
 */
public class GithubRes {
    @SerializedName("incomplete_results")
    public boolean complete;
    @SerializedName("items")
    public List<Item> items;
    @SerializedName("total_count")
    public int count;

    public static class Item {
        @SerializedName("id")
        public long id;
        @SerializedName("name")
        public String name;
        @SerializedName("owner")
        public Owner owner;
        @SerializedName("description")
        public String description;
        @SerializedName("stargazers_count")
        public int starCount;
        @SerializedName("forks_count")
        public int forkCount;

        public static class Owner {
            @SerializedName("login")
            public String name;
            @SerializedName("avatar_url")
            public String avatar;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Item item = (Item) o;
            return id == item.id &&
                    starCount == item.starCount &&
                    forkCount == item.forkCount &&
                    Objects.equals(name, item.name) &&
                    Objects.equals(owner, item.owner) &&
                    Objects.equals(description, item.description);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, name, owner, description, starCount, forkCount);
        }
    }
}



