package com.kt.giga.home.b2b.view;

import com.kt.giga.home.b2b.annotation.ExcelFieldName;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by junypooh on 2017-02-10.
 * <pre>
 * com.kt.giga.home.b2b.view.GenericExcelView
 *
 * 엑셀 다운로드 View
 *
 * </pre>
 *
 * @author Kyungjun, Park
 * @see
 * @since 2017-02-10 오후 12:04
 */
public class GenericExcelView extends AbstractXlsView {

    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {

        String fileName = (String) model.get("fileName");

        fileName = new String(fileName.getBytes("euc-kr"), "8859_1");
        response.setHeader("Content-Disposition", "attachment; fileName=\"" + fileName + "\";");
        response.setHeader("Content-Transfer-Encoding", "binary");

        List<Object> domains = (List<Object>) model.get("domains");

        Sheet sheet = workbook.createSheet("Report");

        if (domains.size() > 0) {
            Row header = sheet.createRow(0);

            int rowNum = 1;
            if (domains.get(0) instanceof Map) {
                header.createCell(0).setCellValue("DATA");
                for (Object obj : domains) {
                    //create the row data
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(obj.toString());
                }
            }
            else {
                ArrayList<Field> list = (ArrayList<Field>) model.get("listHeaderField");

                for (Object obj : domains) {
                    //create the row data
                    Row row = sheet.createRow(rowNum++);

                    //Excel로 만들 목록을 별도로 지정하지 않았으면
                    if(list == null){
                        Field[] fields = obj.getClass().getDeclaredFields();
                        list = new ArrayList<>(Arrays.asList(fields));
                        for(int idx = list.size()-1; idx >= 0 ; idx--){
                            if (!list.get(idx).isAnnotationPresent(ExcelFieldName.class)) {
                                list.remove(idx);
                            }
                        }

                        //fields 재정렬
                        Collections.sort(list, new FieldSort());
                    }

                    int cellNum = 0;
                    for (Field field : list) {
                        //각 column의 이름 설정
                        ExcelFieldName fn = field.getAnnotation(ExcelFieldName.class);
                        if (header.getCell(cellNum) == null) {
                            if(fn == null){
                                header.createCell(cellNum).setCellValue(field.getName());
                            }else{
                                header.createCell(cellNum).setCellValue(fn.name());
                            }
                        }

                        field.setAccessible(true);
                        if(field.get(obj) != null) {
                            if (field.getType().equals(Integer.class) || field.getType().equals(Integer.TYPE)) {
                                row.createCell(cellNum).setCellValue(String.valueOf((Integer) field.get(obj)));
                            }
                            else if (field.getType().equals(String.class)) {
                                row.createCell(cellNum).setCellValue((String) field.get(obj));
                            }
                            else if (field.getType().equals(Date.class)) {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
                                row.createCell(cellNum).setCellValue(dateFormat.format((Date) field.get(obj)));
                            }
                            else if (field.getType().equals(LocalDateTime.class)) {
                                row.createCell(cellNum).setCellValue(((LocalDateTime) field.get(obj)).toString());
                            }
                            else if (field.getType().equals(LocalDate.class)) {
                                row.createCell(cellNum).setCellValue(((LocalDate) field.get(obj)).toString());
                            }
                            else if (field.getType().equals(Long.class) || field.getType().equals(Long.TYPE)) {
                                row.createCell(cellNum).setCellValue((Long) field.get(obj));
                            }
                            else if (field.getType().equals(Short.class) || field.getType().equals(Short.TYPE)) {
                                row.createCell(cellNum).setCellValue((Short) field.get(obj));
                            }
                            else if (field.getType().equals(Double.class) || field.getType().equals(Double.TYPE)) {
                                DecimalFormat decimalFormat = new DecimalFormat("#####.##");
                                String result = decimalFormat.format(((Double) field.get(obj)).doubleValue());
                                row.createCell(cellNum).setCellValue(result);
                            } else if (field.getType().equals(Boolean.class) || field.getType().equals(Boolean.TYPE)) {
                                if(StringUtils.isNotEmpty(fn.booleanValue())) {
                                    String[] split = fn.booleanValue().split("/");
                                    if((Boolean)field.get(obj)) {
                                        row.createCell(cellNum).setCellValue(split[0]);
                                    } else {
                                        row.createCell(cellNum).setCellValue(split[1]);
                                    }
                                } else {
                                    row.createCell(cellNum).setCellValue((Boolean) field.get(obj));
                                }
                            }
                        }

                        cellNum++;
                    }
                }
            }
        }
    }

    private class FieldSort implements Comparator<Field> {

        @Override
        public int compare(Field o1, Field o2) {
            ExcelFieldName fn1 = o1.getAnnotation(ExcelFieldName.class);
            ExcelFieldName fn2 = o2.getAnnotation(ExcelFieldName.class);
            return fn1.order()-fn2.order();
        }

    }

    public static Field getField(Class<?> clas , String name) throws NoSuchFieldException{
        try{
            return clas.getDeclaredField(name);
        }catch(NoSuchFieldException e){
            Class<?> supClass = clas.getSuperclass();
            if(supClass != null){
                return getField(supClass, name);
            }else{
                throw e;
            }
        }
    }
}
