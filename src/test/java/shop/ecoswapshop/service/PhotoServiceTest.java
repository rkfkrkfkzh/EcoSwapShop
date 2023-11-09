//package shop.ecoswapshop.service;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.transaction.annotation.Transactional;
//import shop.ecoswapshop.domain.Photo;
//import shop.ecoswapshop.repository.PhotoRepository;
//
//import static org.junit.Assert.*;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@Transactional
//public class PhotoServiceTest {
//
//    @Autowired
//    PhotoService photoService;
//
//    @Autowired
//    PhotoRepository photoRepository;
//
//    @Test
//    @Rollback(value = false)
//    public void 사진_저장_및_삭제() throws Exception {
//        //given
//        Photo photo = new Photo();
//        photo.setUrl("/uploads/test.jpg");
//
//        //when
//        Long photoId = photoService.savePhoto(photo);
//
//        //thwn
//        assertNotNull(photoId);
//        assertTrue(photoId>0);
//
//        Photo savedPhoto = photoRepository.findById(photoId).orElse(null);
//        assertNotNull(savedPhoto);
//        assertEquals(photo.getUrl(), savedPhoto.getUrl());
//
//        photoService.deletePhoto(photoId);
//        Photo deletedPhoto = photoRepository.findById(photoId).orElse(null);
//        assertNotNull(deletedPhoto);
//    }
//    @Test
//    public void 없는_사진_삭제() throws Exception {
//        //when & then
//        assertThrows(IllegalStateException.class, () -> photoService.deletePhoto(-1L));
//    }
//
//}