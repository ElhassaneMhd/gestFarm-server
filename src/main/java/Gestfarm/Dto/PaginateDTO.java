package Gestfarm.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginateDTO<T> {
    private Integer page;
    private Integer limit;
    private Integer total;
    private List<T> data;
}
