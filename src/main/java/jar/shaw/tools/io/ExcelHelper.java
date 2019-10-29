package jar.shaw.tools.io;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static jar.shaw.tools.util.ExceptionHelper.runtime;

/**
 * Excel帮助类，可以读取excel表格为对象数组
 * @author 肖佳
 * @since 1.8
 * <p>创建时间：2018/11/6 </p>
 */
public class ExcelHelper
{

    private static Logger log = LoggerFactory.getLogger(ExcelHelper.class);


    /**
     * 读取Excel为对象数组，一行对应一个对象。<p />
     * 对象中的字段用{@link ExcelTitle}定义好要读取的列，例如@ExcelTitle(“列名”)，字段都读取为String类型
     * @param clazz 对象类型
     * @param path excel文件对应的Path
     * @return 对象数组
     */
    public static <T> List<T> readObject(Class<T> clazz, Path path)
    {
        List<T> result = new ArrayList<>();
        runtime(() -> {
            Workbook workbook;
            String filePath = path.toUri().toURL().getPath();
            if (filePath.endsWith(".xls")) {
                workbook = new HSSFWorkbook(Files.newInputStream(path));
            } else if (filePath.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(Files.newInputStream(path));
            } else {
                throw new RuntimeException("only xls or xlsx supported");
            }
            Sheet sheet = workbook.getSheetAt(0);
            Row firstRow = sheet.getRow(0);
            Map<Field, Integer> fieldToIndex = getFieldIndex(clazz, firstRow);
            int rowNum = sheet.getLastRowNum();
            for (int i = 1; i <= rowNum; i++) {
                Row row = sheet.getRow(i);
                T object = clazz.newInstance();
                fieldToIndex.forEach((field, index) -> {
                    Cell cell = row.getCell(index);
                    if (cell != null)
                    {
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        runtime(() -> field.set(object, cell.getStringCellValue()));
                    }
                });
                result.add(object);
            }
            log.info("success load excel from: " + filePath);
            workbook.close();
        });
        return result;
    }

    /**
     * 获取对象字段在excel中的列号
     * @param clazz 装入数据的对象
     * @param titleRow 标题行
     * @return field到列号的map
     */
    private static Map<Field, Integer> getFieldIndex(Class<?> clazz, Row titleRow)
    {
        Map<Field, Integer> fieldToIndex = new HashMap<>();
        Map<String, Integer> titleNameToIndex = new HashMap<>();
        int cellNum = titleRow.getLastCellNum();
        for (int i = 0; i < cellNum; ++i) {
            Cell cell = titleRow.getCell(i);
            cell.setCellType(Cell.CELL_TYPE_STRING);
            titleNameToIndex.put(cell.getStringCellValue(), i);
        }
        Field[] fields = clazz.getDeclaredFields();
        for (Field field: fields) {
            field.setAccessible(true);
            ExcelTitle annotation = field.getAnnotation(ExcelTitle.class);
            if (annotation != null) {
                Integer index = titleNameToIndex.get(annotation.value());
                if (index == null) {
                    throw new RuntimeException("column name '" + annotation.value() + "' not found");
                }
                fieldToIndex.put(field, index);
            }
        }
        return fieldToIndex;
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ExcelTitle {
        String value();
    }

}
