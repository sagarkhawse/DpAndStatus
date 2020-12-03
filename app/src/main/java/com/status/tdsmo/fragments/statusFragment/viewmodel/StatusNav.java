package com.status.tdsmo.fragments.statusFragment.viewmodel;

import com.status.tdsmo.models.Image;

public interface StatusNav {
    void setProgress(boolean b);

    void setMessage(String server_not_responding);

    void setListEndStatus(Image image);
}
