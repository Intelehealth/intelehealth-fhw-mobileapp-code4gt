package org.intelehealth.app.activities.identificationActivity.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.intelehealth.app.app.IntelehealthApplication;
import org.intelehealth.app.utilities.SessionManager;

import java.io.Serializable;
import java.util.List;

public class DistData implements Serializable {
    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("name-hi")
    private String nameHindi;

    @Expose
    @SerializedName("tahasil")
    private List<String> tahasilList;

    @Expose
    @SerializedName("block")
    private List<Block> blocks;

    @Expose
    @SerializedName("block-hi")
    private List<Block> blocksHindi;

    public List<String> getTahasilList() {
        return tahasilList;
    }

    public void setTahasilList(List<String> tahasilList) {
        this.tahasilList = tahasilList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameHindi() {
        return nameHindi;
    }

    public void setNameHindi(String nameHindi) {
        this.nameHindi = nameHindi;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<Block> blocks) {
        this.blocks = blocks;
    }

    public List<Block> getBlocksHindi() {
        return blocksHindi;
    }

    public void setBlocksHindi(List<Block> blocksHindi) {
        this.blocksHindi = blocksHindi;
    }

    @NonNull
    @Override
    public String toString() {
        SessionManager sessionManager = SessionManager.getInstance(IntelehealthApplication.getAppContext());
        if (sessionManager.getAppLanguage().equals("hi")) {
            return nameHindi;
        } else return name;
    }
}
