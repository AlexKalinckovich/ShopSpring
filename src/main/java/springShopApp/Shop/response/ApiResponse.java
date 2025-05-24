package springShopApp.Shop.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor // Добавьте эту аннотацию
public class ApiResponse {

    private String message;
    private Object data;

}
