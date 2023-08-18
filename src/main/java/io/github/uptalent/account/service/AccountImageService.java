package io.github.uptalent.account.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import io.github.uptalent.account.exception.EmptyImageException;
import io.github.uptalent.account.exception.InvalidImageFormatException;
import io.github.uptalent.account.model.entity.Sponsor;
import io.github.uptalent.account.model.entity.Talent;
import io.github.uptalent.account.model.enums.ImageType;
import io.github.uptalent.starter.security.Role;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Set;

import static io.github.uptalent.account.model.enums.ImageType.AVATAR;
import static io.github.uptalent.starter.security.Role.SPONSOR;
import static io.github.uptalent.starter.security.Role.TALENT;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountImageService {
    private static final double IMAGE_RESIZE_RATIO = 1.2;
    public static final String AMAZON_S3_URL = ".s3.amazonaws.com/";

    @Value("${aws.bucket.name}")
    private String bucketName;

    private final AmazonS3 s3;
    private final TalentService talentService;
    private final SponsorService sponsorService;
    private final Set<String> validFormats = Set.of("jpeg", "jpg", "png", "gif", "bmp", "webp", "tiff", "svg");
    private final Set<String> validFormatsForCompression = Set.of("jpeg", "jpg", "png");

    @SneakyThrows
    public void uploadImage(Long id, Role role, MultipartFile image, String imageTypeString) {
        validateImage(image);

        ImageType imageType = ImageType.valueOf(imageTypeString);
        String type = role == TALENT ? imageType.getLowerCase() : AVATAR.getLowerCase();
        String key = generateImagePath(id, role);
        String filename = generateFilename(type, image);
        String url = generateUrl(id, filename, role);

        InputStream inputStream;
        if (canCompressImage(image))
            inputStream = compressImage(image);
        else
            inputStream = image.getInputStream();
        saveImage(key, filename, inputStream);
        updateEntity(id, role, url, imageType);
    }

    @SneakyThrows
    private InputStream compressImage(MultipartFile image) {
        BufferedImage originalImage = ImageIO.read(image.getInputStream());
        BufferedImage compressedImage = Scalr.resize(
                originalImage,
                Scalr.Method.BALANCED,
                Scalr.Mode.AUTOMATIC,
                (int) (originalImage.getWidth()/IMAGE_RESIZE_RATIO),
                (int) (originalImage.getHeight()/IMAGE_RESIZE_RATIO));
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(compressedImage, image.getContentType().split("/")[1], os);
        return new ByteArrayInputStream(os.toByteArray());
    }

    public void deleteImageByUserIdAndRole(Long id, Role role) {
        if (role == TALENT) {
            talentService.findAvatarById(id).ifPresent(this::deleteImage);
            talentService.findBannerById(id).ifPresent(this::deleteImage);
        } else if (role == SPONSOR) {
            sponsorService.findAvatarById(id).ifPresent(this::deleteImage);
        }
    }

    private void deleteImage(String imageUrl) {
        int bucketNameEndIndex = imageUrl.indexOf(AMAZON_S3_URL);
        String bucketName = imageUrl.substring("https://".length(), bucketNameEndIndex);
        String key = imageUrl.substring(bucketNameEndIndex + AMAZON_S3_URL.length());
        s3.deleteObject(bucketName, key);
    }

    private void validateImage(MultipartFile image) {
        if(image.isEmpty())
            throw new EmptyImageException();
        if(!validFormats.contains(getFileExtension(image)))
            throw new InvalidImageFormatException();
    }

    public boolean canCompressImage(MultipartFile image) {
        String extension = getFileExtension(image);
        return validFormatsForCompression.contains(extension);
    }

    private String getFileExtension(MultipartFile image) {
        String filename = image.getOriginalFilename();
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex == -1) ? "" : filename.substring(dotIndex + 1);
    }

    private String generateImagePath(Long id, Role role) {
        return String.format("%s/%s/%s",
                bucketName, role.name().toLowerCase(), id);
    }

    private String generateFilename(String imageType, MultipartFile image) {
        return String.format("%s.%s", imageType, getFileExtension(image));
    }

    private String generateUrl(Long id, String filename, Role role) {
        return String.format("https://%s.s3.amazonaws.com/%s/%s/%s",
                bucketName, role.name().toLowerCase(), id, filename);
    }

    @SneakyThrows
    private void saveImage(String key, String filename, InputStream inputStream) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(inputStream.available());
        s3.putObject(key, filename, inputStream, metadata);
    }

    private void updateEntity(Long id, Role role, String imageUrl, ImageType imageType) {
        if (role == TALENT) {
            Talent talent = talentService.getTalentById(id);
            if (imageType == AVATAR) talent.setAvatar(imageUrl);
            else talent.setBanner(imageUrl);
            talentService.save(talent);
        } else if (role == SPONSOR) {
            Sponsor sponsor = sponsorService.getSponsorById(id);
            sponsor.setAvatar(imageUrl);
            sponsorService.save(sponsor);
        }
    }
}
