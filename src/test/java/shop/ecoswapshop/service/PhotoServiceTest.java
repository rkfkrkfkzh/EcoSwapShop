package shop.ecoswapshop.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import shop.ecoswapshop.domain.Photo;
import shop.ecoswapshop.repository.PhotoRepository;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PhotoServiceTest {

    @Mock
    private PhotoRepository photoRepository;

    @InjectMocks
    private PhotoService photoService;

    @Test
    void savePhoto() {
        // given
        Photo photo = new Photo();
        photo.setId(1L);
        when(photoRepository.save(photo)).thenReturn(photo);

        // when
        Long savedPhotoId = photoService.savePhoto(photo);

        // then
        assertNotNull(savedPhotoId);
        assertEquals(1L, savedPhotoId);
        verify(photoRepository).save(photo);
    }

    @Test
    void deletePhoto_ExistingPhoto() {
        // given
        Long photoId = 1L;
        Photo photo = new Photo();
        photo.setId(photoId);
        photo.setUrl("/uploads/test.jpg");

        when(photoRepository.findById(photoId)).thenReturn(Optional.of(photo));
        doNothing().when(photoRepository).deleteById(photoId);

        // when
        photoService.deletePhoto(photoId);

        // then
        verify(photoRepository).deleteById(photoId);
    }

    @Test
    void deletePhoto_NonExistingPhoto() {
        // given
        Long photoId = 1L;
        when(photoRepository.findById(photoId)).thenReturn(Optional.empty());

        // when
        photoService.deletePhoto(photoId);

        // then
        verify(photoRepository, never()).deleteById(photoId);
    }

    @Test
    void findPhotoById() {
        // given
        Long photoId = 1L;
        Photo photo = new Photo();
        photo.setId(photoId);

        when(photoRepository.findById(photoId)).thenReturn(Optional.of(photo));

        // when
        Optional<Photo> foundPhoto = photoService.findPhotoById(photoId);

        // then
        assertTrue(foundPhoto.isPresent());
        assertEquals(photoId, foundPhoto.get().getId());
    }

    @Test
    void storeFile() throws IOException {
        // given
        MultipartFile file = mock(MultipartFile.class);
        String originalFilename = "test.jpg";
        when(file.getOriginalFilename()).thenReturn(originalFilename);
        when(file.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));

        // when
        String storedFileName = photoService.storeFile(file);

        // then
        assertTrue(storedFileName.contains(originalFilename));
    }

    @Test
    void findPhotosByProductId() {
        // given
        Long productId = 1L;
        List<Photo> photoList = Arrays.asList(new Photo(), new Photo());

        when(photoRepository.findByProductId(productId)).thenReturn(photoList);

        // when
        List<Photo> foundPhotos = photoService.findPhotosByProductId(productId);

        // then
        assertEquals(2, foundPhotos.size());
    }

}