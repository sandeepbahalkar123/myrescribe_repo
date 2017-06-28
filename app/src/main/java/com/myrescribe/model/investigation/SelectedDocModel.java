package com.myrescribe.model.investigation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by ganeshshirole on 28/6/17.
 */

public class SelectedDocModel {
    @SerializedName("documents")
    @Expose
    private ArrayList<String> selectedDocPaths;
    @SerializedName("investigations")
    @Expose
    private ArrayList<String> selectedInvestigation;

    public ArrayList<String> getSelectedDocPaths() {
        return selectedDocPaths;
    }

    public void setSelectedDocPaths(ArrayList<String> selectedDocPaths) {
        this.selectedDocPaths = selectedDocPaths;
    }

    public ArrayList<String> getSelectedInvestigation() {
        return selectedInvestigation;
    }

    public void setSelectedInvestigation(ArrayList<String> selectedInvestigation) {
        this.selectedInvestigation = selectedInvestigation;
    }
}
