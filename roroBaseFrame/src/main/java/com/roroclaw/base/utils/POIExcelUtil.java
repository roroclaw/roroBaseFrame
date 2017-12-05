package com.roroclaw.base.utils;

import java.io.*;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.roroclaw.base.handler.BizException;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * POI操作Excel工具类
 *
 * @author dxz
 */
public class POIExcelUtil {

    public static String VERSION_2003 = "version2003";
    public static String VERSION_2007 = "version2007";

    private HSSFWorkbook workbook;
    private int colWidth = 3000;
    private HSSFFont titleCellFont;
    private HSSFFont dataCellFont;
    private HSSFFont columCellFont;
    private HSSFCellStyle titleCellStyle;
    private HSSFCellStyle dataCellStyle;
    private HSSFCellStyle columCellStyle;

//	public POIExcelUtil() {
//		super();
//		this.initColumCellStyle();
//		this.initDataCellStyle();
//		this.initTitleCellStyle();
//	}

    public void setColWidth(int colWidth) {
        this.colWidth = colWidth;
    }

    public POIExcelUtil(HSSFWorkbook workbook) {
        this.workbook = workbook;
        this.initColumCellStyle();
        this.initDataCellStyle();
        this.initTitleCellStyle();
    }

    public void setWorkbook(HSSFWorkbook workbook) {
        this.workbook = workbook;
    }

    public void setTitleCellFont(HSSFFont titleCellFont) {
        this.titleCellFont = titleCellFont;
    }

    public void setDataCellFont(HSSFFont dataCellFont) {
        this.dataCellFont = dataCellFont;
    }

    public void setTitleCellStyle(HSSFCellStyle titleCellStyle) {
        this.titleCellStyle = titleCellStyle;
    }

    public void setDataCellStyle(HSSFCellStyle dataCellStyle) {
        this.dataCellStyle = dataCellStyle;
    }

    public HSSFCell createColumCell(HSSFRow row, int col, int type, String value) {
        HSSFCell cell = row.createCell(col, type);
        cell.setCellStyle(this.columCellStyle);
        cell.setCellValue(value);
        return cell;
    }

    public HSSFCell createDataCell(HSSFRow row, int col, int type, String value) {
        HSSFCell cell = row.createCell(col, type);
        cell.setCellStyle(this.dataCellStyle);
        cell.setCellValue(value);
        return cell;
    }

    public HSSFCell createTitleCell(HSSFCell titleCell, String title) {
        titleCell.setCellValue(title);
        titleCell.setCellStyle(this.titleCellStyle);
        return titleCell;
    }

    /**
     * 根据传进的数据导出excel
     *
     * @param dataList
     * @param colNameMap <Excel列名,数据map的key>
     * @param title
     * @return
     */
    public HSSFWorkbook getExcelByData(List dataList, Map colNameMap,
                                       String title) {
        HSSFSheet sheet = workbook.createSheet("sheet1");
        int colNum = colNameMap.size();
        for (int i = 0; i < colNum; i++) {
            sheet.setColumnWidth(i, this.colWidth);
        }

        // 报表标题
        HSSFRow titleRow = sheet.createRow(0);
        titleRow.setHeight((short) 500);
        HSSFCell titleCell = titleRow.createCell(0, HSSFCell.CELL_TYPE_STRING);
        this.createTitleCell(titleCell, title);
        CellRangeAddress range = new CellRangeAddress(0, 0, 0, colNum - 1);// 合并标题列
        sheet.addMergedRegion(range);

        // 报表信息标题列
        HSSFRow colRow = sheet.createRow(1);
        Object[] colNames = colNameMap.keySet().toArray();
        for (int i = 0; i < colNames.length; i++) {
            this.createColumCell(colRow, i, HSSFCell.CELL_TYPE_STRING,
                    String.valueOf(colNames[i]));
        }

        // 初始化数据记录
        int dataSize = dataList.size();
        for (int i = 0; i < dataSize; i++) {
            Map dataMap = (Map) dataList.get(i);
            HSSFRow dataRow = sheet.createRow(i + 2);
            for (int j = 0; j < colNames.length; j++) {
                String datakey = (String) colNameMap.get(colNames[j]);
                this.createDataCell(
                        dataRow,
                        j,
                        HSSFCell.CELL_TYPE_STRING,
                        dataMap.get(datakey) != null ? String.valueOf(dataMap
                                .get(datakey)) : "");
            }
        }
        return this.workbook;
    }

    /**
     * 根据传进的数据导出excel
     *
     * @param dataList
     * @param colNameMap <Excel列名,数据map的key>
     * @param title
     * @return
     * @throws NoSuchFieldException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    public HSSFWorkbook getExcelByData(List dataList, Map colNameMap,
                                       String title, Class classObj) throws SecurityException,
            NoSuchFieldException, IllegalArgumentException,
            IllegalAccessException {
        HSSFSheet sheet = workbook.createSheet("sheet1");
        int colNum = colNameMap.size();
        for (int i = 0; i < colNum; i++) {
            sheet.setColumnWidth(i, this.colWidth);
        }

        // 报表标题
        HSSFRow titleRow = sheet.createRow(0);
        titleRow.setHeight((short) 500);
        HSSFCell titleCell = titleRow.createCell(0, HSSFCell.CELL_TYPE_STRING);
        this.createTitleCell(titleCell, title);
        CellRangeAddress range = new CellRangeAddress(0, 0, 0, colNum - 1);// 合并标题列
        sheet.addMergedRegion(range);

        // 报表信息标题列
        HSSFRow colRow = sheet.createRow(1);
        Object[] colNames = colNameMap.keySet().toArray();
        for (int i = 0; i < colNames.length; i++) {
            this.createColumCell(colRow, i, HSSFCell.CELL_TYPE_STRING,
                    String.valueOf(colNames[i]));
        }

        // 初始化数据记录
        int dataSize = dataList.size();
        for (int i = 0; i < dataSize; i++) {
            Object obj = dataList.get(i);
            HSSFRow dataRow = sheet.createRow(i + 2);
            for (int j = 0; j < colNames.length; j++) {
                String datakey = (String) colNameMap.get(colNames[j]);
                Field field = classObj.getDeclaredField(datakey);
                field.setAccessible(true);
                this.createDataCell(dataRow, j, HSSFCell.CELL_TYPE_STRING,
                        field.get(obj) != null ? String.valueOf(field.get(obj))
                                : "");
            }
        }
        return this.workbook;
    }

    private void initTitleCellStyle() {
        this.titleCellFont = workbook.createFont();
        this.titleCellFont.setFontHeightInPoints((short) 20);
        this.titleCellFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        this.titleCellStyle = workbook.createCellStyle();
        this.titleCellStyle.setFont(this.titleCellFont);
        this.titleCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        this.titleCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
    }

    private void initDataCellStyle() {
        // 报表信息项样式
        this.dataCellStyle = workbook.createCellStyle();
        this.dataCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        this.dataCellStyle.setWrapText(true);
    }

    private void initColumCellStyle() {
        // 报表信息项样式
        this.columCellFont = workbook.createFont();
        this.columCellFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗
        this.columCellStyle = workbook.createCellStyle();
        this.columCellStyle.setFont(this.columCellFont);
        this.columCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        this.columCellStyle.setWrapText(true);
    }


//    /**
//     * 读取Excel表格表头的内容
//     *
//     * @return String 表头内容的数组
//     */
//    public static String[] readExcelTitle(InputStream is) {
//        String[] title = null;
//        try {
//            POIFSFileSystem fs = new POIFSFileSystem(is);
//            Workbook wb = new HSSFWorkbook(fs);
//            HSSFSheet sheet = wb.getSheetAt(0);
//            HSSFRow row = sheet.getRow(0);
//            // 标题总列数
//            int colNum = row.getPhysicalNumberOfCells();
//            System.out.println("colNum:" + colNum);
//            title = new String[colNum];
//            for (int i = 0; i < colNum; i++) {
//                title[i] = getCellFormatValue(row.getCell((short) i));
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return title;
//    }

    /**
     * 读取Excel数据内容
     * 兼容2003&2007获取sheet
     */
    public static Sheet getExcelSheet(String fileName,InputStream is) {
        Sheet sheet = null;
        try {
            String version = getExcelVersion(fileName);
            if(VERSION_2003.equals(version)){
                POIFSFileSystem fs = new POIFSFileSystem(is);
                HSSFWorkbook wb = new HSSFWorkbook(fs);
                sheet = wb.getSheetAt(0);
            }else if(VERSION_2007.equals(version)){
                XSSFWorkbook xwb = new XSSFWorkbook(is);
                sheet = xwb.getSheetAt(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sheet;
    }

    /**
     * 获取单元格数据内容为字符串类型的数据
     *
     * @param cell Excel单元格
     * @return String 单元格数据内容
     */
    public static String getStringCellValue(Cell cell) {
        String strCell = "";
        if(cell != null){
            switch (cell.getCellType()) {
                case HSSFCell.CELL_TYPE_STRING:
                    strCell = cell.getStringCellValue();
                    break;
                case HSSFCell.CELL_TYPE_NUMERIC:
                    strCell = String.valueOf(cell.getNumericCellValue());
                    break;
                case HSSFCell.CELL_TYPE_BOOLEAN:
                    strCell = String.valueOf(cell.getBooleanCellValue());
                    break;
                case HSSFCell.CELL_TYPE_BLANK:
                    strCell = "";
                    break;
                default:
                    strCell = "";
                    break;
            }
        }
        if (strCell.equals("") || strCell == null) {
            return "";
        }
        if (cell == null) {
            return "";
        }
        return strCell;
    }

    /**
     * 获取单元格数据内容为日期类型的数据
     *
     * @param cell Excel单元格
     * @return String 单元格数据内容
     */
    private static String getDateCellValue(HSSFCell cell) {
        String result = "";
        try {
            int cellType = cell.getCellType();
            if (cellType == HSSFCell.CELL_TYPE_NUMERIC) {
                Date date = cell.getDateCellValue();
                result = (date.getYear() + 1900) + "-" + (date.getMonth() + 1)
                        + "-" + date.getDate();
            } else if (cellType == HSSFCell.CELL_TYPE_STRING) {
                String date = getStringCellValue(cell);
                result = date.replaceAll("[年月]", "-").replace("日", "").trim();
            } else if (cellType == HSSFCell.CELL_TYPE_BLANK) {
                result = "";
            }
        } catch (Exception e) {
            System.out.println("日期格式不正确!");
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 根据HSSFCell类型设置数据
     *
     * @param cell
     * @return
     */
    private static String getCellFormatValue(HSSFCell cell) {
        String cellvalue = "";
        if (cell != null) {
            // 判断当前Cell的Type
            switch (cell.getCellType()) {
                // 如果当前Cell的Type为NUMERIC
                case HSSFCell.CELL_TYPE_NUMERIC:
                case HSSFCell.CELL_TYPE_FORMULA: {
                    // 判断当前的cell是否为Date
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        // 如果是Date类型则，转化为Data格式

                        //方法1：这样子的data格式是带时分秒的：2011-10-12 0:00:00
                        //cellvalue = cell.getDateCellValue().toLocaleString();

                        //方法2：这样子的data格式是不带带时分秒的：2011-10-12
                        Date date = cell.getDateCellValue();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        cellvalue = sdf.format(date);

                    }
                    // 如果是纯数字
                    else {
                        // 取得当前Cell的数值
                        cellvalue = String.valueOf(cell.getNumericCellValue());
                    }
                    break;
                }
                // 如果当前Cell的Type为STRIN
                case HSSFCell.CELL_TYPE_STRING:
                    // 取得当前的Cell字符串
                    cellvalue = cell.getRichStringCellValue().getString();
                    break;
                // 默认的Cell值
                default:
                    cellvalue = " ";
            }
        } else {
            cellvalue = "";
        }
        return cellvalue;
    }

//    private Workbook getWorkBook(InputStream is) throws IOException {
//        Workbook book = null;
//        try {
//            book = new HSSFWorkbook(is);
//        } catch (OfficeXmlFileException ex) {
//            book = new XSSFWorkbook(is);
//        }
//        return book;
//    }

    private static String getExcelVersion(String fileName) {
        String version = "";
        if (fileName != null) {
            String suffex = FileKit.getExtensionName(fileName);
            if ("xls".equals(suffex)) {
                version = VERSION_2003;
            } else if ("xlsx".equals(suffex)) {
                version = VERSION_2007;
            }
        }
        return version;
    }

//    public static void main(String[] args) {
//        File file = new File("E:\\testfile\\testStu.xlsx");
//        try {
//            InputStream in = new FileInputStream(file);
//            Map<Integer, String> resMap = readExcelContent(in);
//            for (Map.Entry<Integer, String> entry : resMap.entrySet()) {
//                System.out.println("key= " + entry.getKey() + " and value= "
//                                + entry.getValue());
//                }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//    }

    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("E:/stu_import.xlsx");
        Sheet sheet = POIExcelUtil.getExcelSheet(file.getName(),new FileInputStream(file));
        if (sheet == null) {
            throw new BizException("excel数据为空!");
        }

        Row row = sheet.getRow(1);
        if(null != row){
            Map<String, Boolean> curMap = new HashMap();
            Cell cell = row.getCell(9);
            String value = POIExcelUtil.getStringCellValue(cell);
            System.out.println(value);
        }
    }

    public static  boolean isEmptyRow(Row row){
        boolean bol = false;
        Cell realNameCell = row.getCell(1);
        if(realNameCell == null || realNameCell.getCellType() == Cell.CELL_TYPE_BLANK){
            bol = true;
        }
        return bol;
    }
}
