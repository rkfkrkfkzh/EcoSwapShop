package shop.ecoswapshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import shop.ecoswapshop.domain.Photo;
import shop.ecoswapshop.domain.Product;
import shop.ecoswapshop.service.MemberService;
import shop.ecoswapshop.service.PhotoService;
import shop.ecoswapshop.service.ProductService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/photos")
public class PhotoController {

    private final PhotoService photoService;
    private final ProductService productService;
    private final MemberService memberService;

    private static final String UPLOAD_DIR = "uploads";

    private Optional<Long> getLoggedInMemberId() {
        return memberService.findLoggedInMemberId();
    }

    private boolean isUserAuthorized(Long memberId) {
        return getLoggedInMemberId().isPresent() && getLoggedInMemberId().get().equals(memberId);
    }

    @GetMapping("/uploads/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        try {
            Path file = Paths.get(UPLOAD_DIR).resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                throw new RuntimeException("Could not read file: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @PostMapping("/upload/")
    public String handleFileUpload(@RequestParam("files") MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            String fileName = photoService.storeFile(file);
            Photo photo = new Photo();
            photo.setUrl("/uploads/" + fileName);
            photoService.savePhoto(photo);
            redirectAttributes.addFlashAttribute("message", "Successfully upload" + file.getOriginalFilename());

        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("message", "Failed to upload" + file.getOriginalFilename());
        }
        return "redirect:/success";
    }

    @PostMapping("/upload/{productId}")
    public String uploadProductImages(@PathVariable Long productId, @RequestParam("files") List<MultipartFile> files) throws IOException {
        Product product = productService.findProductById(productId).orElseThrow();
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                Photo photo = new Photo();
                photo.setProduct(product);
                String fileName = photoService.storeFile(file);
                photo.setUrl("/uploads/" +fileName);
                photoService.savePhoto(photo);
            }
        }
        return "redirect:/products/details/" + productId;
    }

    @PostMapping("delete/{photoId}")
    public String deleteImage(@PathVariable Long photoId) {
        Photo photo = photoService.findPhotoById(photoId).orElseThrow();
        if (!isUserAuthorized(photo.getProduct().getMember().getId())) {
            return "redirect:/error";
        }
        photoService.deletePhoto(photoId);
        return "redirect:/products/edit/" + photo.getProduct().getId();
    }
}
