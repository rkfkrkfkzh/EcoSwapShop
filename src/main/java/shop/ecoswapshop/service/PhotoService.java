package shop.ecoswapshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.ecoswapshop.domain.Photo;
import shop.ecoswapshop.repository.PhotoRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PhotoService {

    private final PhotoRepository photoRepository;

    @Transactional
    public Long savePhoto(Photo photo) {
        Photo savePhoto = photoRepository.save(photo);
        return savePhoto.getId();
    }
}
