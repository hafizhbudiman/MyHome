package com.kecepret.myhome.model;

import com.google.gson.annotations.SerializedName;
import com.kecepret.myhome.model.Result;

import java.util.List;

public class TokenResponse {

        @SerializedName("count")
        public Integer count;
        @SerializedName("next")
        public Object next;
        @SerializedName("previous")
        public Object previous;
        @SerializedName("results")
        public List<Result> results = null;

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public Object getNext() {
            return next;
        }

        public void setNext(Object next) {
            this.next = next;
        }

        public Object getPrevious() {
            return previous;
        }

        public void setPrevious(Object previous) {
            this.previous = previous;
        }

        public List<Result> getResults() {
            return results;
        }

        public void setResults(List<Result> results) {
            this.results = results;
        }

}
