package com.example.dkovalev.waveproject.Assets;


import com.example.dkovalev.waveproject.R;

/**
 * Created by ardmn on 25.11.2015.
 */
public class WaterContainerDataModel {

    public static final int PICKER_VOLUME_DEGREE_DEFAULT = 50;
    public static final int CONTAINER_VOLUME_DEFAULT = 100;
    public static final int PICKER_MAX_VOLUME_DEFAULT = 2000;
    public static final int PICKER_MIN_VOLUME_DEFAULT = 50;


    public enum WaterContainerIcons {
        GLASS(R.drawable.water_glass_full, 528f, 90f, 66f),
        DROP(R.drawable.water_drop_full, 528f, 42f, 52f),
        BOTTLE(R.drawable.water_botle_full, 528f, 57f, 13f);

        WaterContainerIcons(int iconResId, float originalImgHeight, float topContentOffset, float bottomContentOffset) {
            this.iconResId = iconResId;
            this.topContentOffset = topContentOffset;
            this.bottomContentOffset = bottomContentOffset;
            this.originalImgHeight = originalImgHeight;
        }

        private int iconResId = -1;
        private float topContentOffset = 0;
        private float bottomContentOffset = 0;
        private float originalImgHeight = 0;

        public int getIconResId() {
            return iconResId;
        }

        public float getTopContentOffset() {
            return topContentOffset;
        }

        public float getBottomContentOffset() {
            return bottomContentOffset;
        }

        public float getOriginalImgHeight() {
            return originalImgHeight;
        }
    }

    public enum WaterContainerEmptyIcons {
        GLASS(R.drawable.water_glass),
        DROP(R.drawable.water_drop),
        BOTTLE(R.drawable.water_botle);

        WaterContainerEmptyIcons(int iconResId) {
            this.iconResId = iconResId;
        }

        private int iconResId = -1;

        public int getIconResId() {
            return iconResId;
        }

    }

    public enum WaterContainerFillIcons {
        GLASS(R.drawable.water_glass_fill),
        DROP(R.drawable.water_drop_fill),
        BOTTLE(R.drawable.water_botle_fill);

        WaterContainerFillIcons(int iconResId) {
            this.iconResId = iconResId;
        }

        private int iconResId = -1;

        public int getIconResId() {
            return iconResId;
        }

    }

    public static int getFillWaterContainerIcon(int resId) {
        int position = 0;
        WaterContainerDataModel.WaterContainerIcons[] array = WaterContainerIcons.values();
        for (int i = 0; i < array.length; i++)
            if (array[i].getIconResId() == resId) {
                position = i;
                break;
            }

        return WaterContainerFillIcons.values()[position].getIconResId();
    }

    public static int getEmptyWaterContainerIcon(int resId) {
        int position = 0;
        WaterContainerDataModel.WaterContainerIcons[] array = WaterContainerIcons.values();
        for (int i = 0; i < array.length; i++)
            if (array[i].getIconResId() == resId) {
                position = i;
                break;
            }

        return WaterContainerEmptyIcons.values()[position].getIconResId();
    }

    public static WaterContainerIcons getEnumByImageId(int imageId) {
        int position = 0;
        WaterContainerDataModel.WaterContainerIcons[] array = WaterContainerIcons.values();
        for (int i = 0; i < array.length; i++)
            if (array[i].getIconResId() == imageId) {
                position = i;
                break;
            }

        return array[position];
    }

    public enum WaterContainerTypes {
        STANDARD,
        CUSTOM;

    }

    public WaterContainerDataModel() {
    }

    public WaterContainerDataModel(String name, int iconID, float volume) {
        this.name = name;
        this.iconID = iconID;
        this.volume = volume;
    }

    public WaterContainerDataModel(String name, int iconID, float volume, WaterContainerTypes containerType) {
        this.name = name;
        this.iconID = iconID;
        this.volume = volume;
        this.containerType = containerType;
    }

    public WaterContainerDataModel(String name, int iconID, float volume, WaterContainerTypes containerType, boolean selected, int containerBD_id) {
        this.name = name;
        this.iconID = iconID;
        this.volume = volume;
        this.containerType = containerType;
        this.selected = selected;
        this.containerBD_id = containerBD_id;
    }

    private int containerBD_id = -1;
    private int iconID = WaterContainerIcons.GLASS.getIconResId();
    private String name = "";
    private float volume = CONTAINER_VOLUME_DEFAULT;
    private int picker_volume_degree = PICKER_VOLUME_DEGREE_DEFAULT;
    private int picker_max_volume = PICKER_MAX_VOLUME_DEFAULT;
    private int picker_min_volume = PICKER_MIN_VOLUME_DEFAULT;
    private boolean selected = false;
    private WaterContainerTypes containerType = WaterContainerTypes.CUSTOM;

    public int getIconID() {
        return iconID;
    }

    public void setIconID(int iconID) {
        this.iconID = iconID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public int getContainerBD_id() {
        return containerBD_id;
    }

    public void setContainerBD_id(int containerBD_id) {
        this.containerBD_id = containerBD_id;
    }


    public int getPicker_volume_degree() {
        return picker_volume_degree;
    }

    public void setPicker_volume_degree(int picker_volume_degree) {
        this.picker_volume_degree = picker_volume_degree;
    }

    public int getPicker_max_volume() {
        return picker_max_volume;
    }

    public void setPicker_max_volume(int picker_max_volume) {
        this.picker_max_volume = picker_max_volume;
    }

    public int getPicker_min_volume() {
        return picker_min_volume;
    }

    public void setPicker_min_volume(int picker_min_volume) {
        this.picker_min_volume = picker_min_volume;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public WaterContainerTypes getContainerType() {
        return containerType;
    }

    public void setContainerType(WaterContainerTypes containerType) {
        this.containerType = containerType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WaterContainerDataModel that = (WaterContainerDataModel) o;

        if (containerBD_id != that.containerBD_id) return false;
        if (iconID != that.iconID) return false;
        if (Float.compare(that.volume, volume) != 0) return false;
        if (picker_volume_degree != that.picker_volume_degree) return false;
        if (picker_max_volume != that.picker_max_volume) return false;
        if (picker_min_volume != that.picker_min_volume) return false;
        if (selected != that.selected) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return containerType == that.containerType;

    }

    @Override
    public int hashCode() {
        int result = containerBD_id;
        result = 31 * result + iconID;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (volume != +0.0f ? Float.floatToIntBits(volume) : 0);
        result = 31 * result + picker_volume_degree;
        result = 31 * result + picker_max_volume;
        result = 31 * result + picker_min_volume;
        result = 31 * result + (selected ? 1 : 0);
        result = 31 * result + (containerType != null ? containerType.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "WaterContainerDataModel{" +
                "containerBD_id=" + containerBD_id +
                ", iconID=" + iconID +
                ", name='" + name + '\'' +
                ", volume=" + volume +
                ", picker_volume_degree=" + picker_volume_degree +
                ", picker_max_volume=" + picker_max_volume +
                ", picker_min_volume=" + picker_min_volume +
                ", selected=" + selected +
                ", containerType=" + containerType +
                '}';
    }
}
