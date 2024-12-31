package shop.ecoswapshop.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import shop.ecoswapshop.domain.Photo;
import shop.ecoswapshop.domain.Product;
import shop.ecoswapshop.repository.PhotoRepository;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PhotoService {

    private final PhotoRepository photoRepository;
    private final String uploadDir = "uploads";

    private static final Logger logger = (Logger) LoggerFactory.getLogger(PhotoService.class);

    @Transactional
    public Long savePhoto(Photo photo) {
        Photo savePhoto = photoRepository.save(photo);
        return savePhoto.getId();
    }

    @Transactional
    public void deletePhoto(Long photoId) {
        Optional<Photo> photoOptional = photoRepository.findById(photoId);
        if (photoOptional.isPresent()) {
            Photo photo = photoOptional.get();
            String encodedUrl = photo.getUrl().replace("/uploads/", ""); // URL에서 파일명만 추출
            // URL 디코딩 처리
            String decodedFileName = URLDecoder.decode(encodedUrl, StandardCharsets.UTF_8);
            File file = new File(uploadDir, decodedFileName);
            if (file.exists()) {
                file.delete(); // 파일 시스템에서 이미지 파일 삭제
            }
            photoRepository.deleteById(photoId); // 데이터베이스에서 레코드 삭제
        } else {
            logger.warn("Photo with ID {} not found", photoId);
        }
    }

    public Optional<Photo> findPhotoById(Long photoId) {
        return photoRepository.findById(photoId);
    }

    @Transactional
    public String storeFile(MultipartFile file) throws IOException {
        // 파일 이름을 URL 인코딩
        String fileName = System.currentTimeMillis() + "_" + URLEncoder.encode(file.getOriginalFilename(), StandardCharsets.UTF_8);
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdir();
        }
        Path filePath = Paths.get(uploadDir).resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }

    public List<Photo> findPhotosByProductId(Long productId) {
        return photoRepository.findByProductId(productId);
    }
}
