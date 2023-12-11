package ra.restapi_uploadfile.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileInfoResponse {
    private String fileName;
    private String fileUrl;
}
