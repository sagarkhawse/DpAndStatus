package com.status.tdsmo.models;

import java.util.List;

public class DpDataResponse {
    private boolean error;
    private List<Image> res;

    public boolean isError() {
        return error;
    }

    public List<Image> getRes() {
        return res;
    }
}


