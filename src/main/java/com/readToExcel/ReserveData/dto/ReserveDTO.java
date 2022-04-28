package com.readToExcel.ReserveData.dto;

import lombok.*;

@Data
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ReserveDTO {

    private String date_key;
    private String rst_code;
    private Integer room_seq;
    private Integer season_level;
    private Integer room_fee_dc;
    private Integer room_fee_fix;
    private String temp_flag;
    private Integer res_seq;
    private String app_no;

}
