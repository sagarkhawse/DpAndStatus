package com.status.tdsmo.statusSaver.fragments;

import com.status.tdsmo.statusSaver.models.Status;

import java.util.List;



public class PhotoStatusesFragment extends BaseStatusesFragment {
    @Override
    public List<Status> getStatuses() {
        return getStatusManager().getPhotoStatuses();
    }
}
