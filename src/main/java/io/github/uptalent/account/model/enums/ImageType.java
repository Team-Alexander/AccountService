package io.github.uptalent.account.model.enums;

public enum ImageType {
    AVATAR, BANNER;
    public String getLowerCase() {
        return this.toString().toLowerCase();
    }
}
