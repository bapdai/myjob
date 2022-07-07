package ngoquangdai.myjob.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CredentialDto {
    private String accessToken;
    private String refreshToken;
}
