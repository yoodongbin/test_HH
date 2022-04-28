package com.readToExcel.ReserveData.read;

import com.readToExcel.ReserveData.dbConnect.JdbcConnect;
import com.readToExcel.ReserveData.dto.ReserveDTO;

import com.google.common.io.Resources;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;

public class ReadTOExcel {

    static JdbcConnect jdbcConnect = null;

    static Connection connection = jdbcConnect.getConnection();

    static Statement stmt = null;

    static PreparedStatement pstmt = null;

    public static void main(String[] args) throws IOException, SQLException {

        try
        {
            InputStream file = Resources.getResource("sample_reserve.xls").openStream();

            //Create Workbook instance holding reference to .xls file
            HSSFWorkbook workbook = new HSSFWorkbook(file);

            //Get first/desired sheet from the workbook
            HSSFSheet sheet = workbook.getSheetAt(0);
            //Iterate through each rows one by one
            Iterator<Row> rowIterator = sheet.iterator();

            makeTable();

            while (rowIterator.hasNext())
            {
                Row row = rowIterator.next();

                //헤더 안 읽기
                if(row.getRowNum() ==0)
                    continue;

                Iterator<Cell> cellIterator = row.cellIterator();


                double cells = 0.0;
                String cellss = "";
                ArrayList<String> inputdata = new ArrayList<String>();

                while (cellIterator.hasNext())
                {
                    Cell cell = cellIterator.next();
                    System.out.print(cell.getCellType()+"\t");

                    switch (cell.getCellType())

                    {
                        case NUMERIC:
                            System.out.print(cell.getNumericCellValue() + "\t");
                            if(cell.getColumnIndex() == 0) {
                                cell.getDateCellValue();

                                System.out.println(cell.getDateCellValue());
                                DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

                                System.out.println(dateFormat.format(cell.getDateCellValue()));

                                inputdata.add(dateFormat.format(cell.getDateCellValue()));
                            }else {
                                cells = cell.getNumericCellValue();
                                inputdata.add(String.valueOf((int)cells));
                            }
                            break;
                        case STRING:
                            System.out.print(cell.getStringCellValue() + "\t");
                            cellss = cell.getStringCellValue();
                            inputdata.add(cellss);
                            break;
                    }

                }
                ReserveDTO excelDTO = new ReserveDTO();
                excelDTO.setDate_key(inputdata.get(0));
                excelDTO.setRst_code(inputdata.get(1));
                excelDTO.setRoom_seq(Integer.valueOf(inputdata.get(2)));
                excelDTO.setSeason_level(Integer.valueOf(inputdata.get(3)));
                excelDTO.setRoom_fee_dc(Integer.valueOf(inputdata.get(4)));
                excelDTO.setRoom_fee_fix(Integer.valueOf(inputdata.get(5)));
                excelDTO.setTemp_flag(inputdata.get(6));

                runQuery(excelDTO);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }finally {
            searchForData();


            stmt.close();
            pstmt.close();
            connection.setAutoCommit(true);
            connection.close();
        }
    }

    public static void makeTable() throws SQLException {
        try {
            connection.setAutoCommit(false);
            stmt = connection.createStatement();

            stmt.execute("DROP TABLE IF " +
                    "EXISTS HHSamples.INPUTRESERVE;");

            stmt.execute("CREATE TABLE HHSamples.INPUTRESERVE " +
                    "(date_key VARCHAR(200), rst_code VARCHAR(200), " +
                    "room_seq INTEGER, " +
                    "season_level INTEGER, room_fee_dc INTEGER, room_fee_fix INTEGER," +
                    "temp_flag VARCHAR(100)" +
                    ");");
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println();
        }
    }

    public static void runQuery(ReserveDTO excelDTO) throws IOException, SQLException {
        String sql = "INSERT INTO hhsamples.inputreserve(date_key, rst_code, room_seq, season_level, room_fee_dc, room_fee_fix,temp_flag) VALUES (?, ?, ?, ?, ?, ?, ?);";
        pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, excelDTO.getDate_key());
        pstmt.setString(2, excelDTO.getRst_code());
        pstmt.setInt(3, excelDTO.getRoom_seq());
        pstmt.setInt(4, excelDTO.getSeason_level());
        pstmt.setInt(5, excelDTO.getRoom_fee_dc());
        pstmt.setInt(6, excelDTO.getRoom_fee_fix());
        pstmt.setString(7, excelDTO.getTemp_flag());
        int count = pstmt.executeUpdate();
        System.out.println();
        if( count == 0 ){
            System.out.println("데이터 입력 실패");
        }
    }

    //여기 아직수정 덜 함
    public static void searchForData() throws SQLException {
        ResultSet rs = stmt.executeQuery("SELECT * from hhsamples.inputreserve");
        ArrayList<ReserveDTO> list = new ArrayList<ReserveDTO>();

        while (rs.next()) {
            ReserveDTO dtos = new ReserveDTO();
            dtos.setDate_key(rs.getString("date_key"));
            dtos.setRst_code(rs.getString("rst_code"));
            dtos.setRoom_seq(rs.getInt("room_seq"));
            dtos.setSeason_level(rs.getInt("season_level"));
            dtos.setRoom_fee_dc(rs.getInt("room_fee_dc"));
            dtos.setRoom_fee_fix(rs.getInt("room_fee_fix"));
            dtos.setTemp_flag(rs.getString("temp_flag"));
            list.add(dtos);
        }
        list.forEach(System.out::println);
    }
}
