package io.github.uptalent.account.exception;

public class InvalidImageFormatException extends RuntimeException {
    public InvalidImageFormatException() {
        super("Image format must be JPEG, PNG, BMP, WEBP, GIF, TIFF or SVG");
    }
}
