package shop.ecoswapshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import shop.ecoswapshop.domain.Photo;
import shop.ecoswapshop.repository.PhotoRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PhotoService {

    private final PhotoRepository photoRepository;
    private final String uploadDir = "uploads";

    @Transactional
    public Long savePhoto(Photo photo) {
        Photo savePhoto = photoRepository.save(photo);
        return savePhoto.getId();
    }

    public List<String> findImageUrlsByProductId(Long productId) {
        // db로 부터 해당 상품 ID와 연결된 모든 photo 엔티티 불러옴
        List<Photo> photos = photoRepository.findByProductId(productId);

        // photo 엔티티 목록을 image URL로 변환
        return photos.stream().map(Photo::getUrl)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updatePhoto(Long photoId, MultipartFile file)throws IOException {
        Photo photo = photoRepository.findById(photoId).orElseThrow(NoSuchElementException::new);
        String fileName = storeFile(file); // 새로 업로드된 파일 저장
        String fileDownloadUri = "/uploads/" + fileName; // 여기에 서버에 따라서 경로조정
        photo.setUrl(fileDownloadUri); // 파일의 다운로드 URL로 Photo 엔티티 업데이트
        photoRepository.save(photo);
    }

    @Transactional
    public void deletePhoto(Long photoId) {
        photoRepository.deleteById(photoId);
    }

    public Optional<Photo> findPhotoById(Long photoId) {
        return photoRepository.findById(photoId);
    }

    public String storeFile(MultipartFile file)throws IOException {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir).resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }
}
