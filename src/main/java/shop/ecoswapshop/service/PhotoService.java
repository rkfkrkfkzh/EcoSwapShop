package shop.ecoswapshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.ecoswapshop.domain.Photo;
import shop.ecoswapshop.repository.PhotoRepository;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
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
}
