package jipdol2.eunstargram.image.dto.response;

import jipdol2.eunstargram.image.entity.Image;
import jipdol2.eunstargram.image.entity.ImageCode;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class ImageResponseDTO {

    private Long id;

    private String originalFileName;

    private String storedFileName;

    private ImageCode imageCode;

    @Builder
    public ImageResponseDTO(Long id, String originalFileName, String storedFileName, ImageCode imageCode) {
        this.id = id;
        this.originalFileName = originalFileName;
        this.storedFileName = storedFileName;
        this.imageCode = imageCode;
    }

    public ImageResponseDTO(Image image){
        this.id = image.getId();
        this.originalFileName = image.getOriginalFileName();
        this.storedFileName = image.getStoredFileName();
        this.imageCode = image.getImageCode();
    }
}
