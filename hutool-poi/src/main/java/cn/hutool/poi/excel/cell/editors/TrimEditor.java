package cn.hutool.poi.excel.cell.editors;

import org.apache.poi.ss.usermodel.Cell;

import cn.hutool.core.text.StrUtil;
import cn.hutool.poi.excel.cell.CellEditor;

/**
 * 去除String类型的单元格值两边的空格
 * @author Looly
 *
 */
public class TrimEditor implements CellEditor{

	@Override
	public Object edit(final Cell cell, final Object value) {
		if(value instanceof String) {
			return StrUtil.trim((String)value);
		}
		return value;
	}

}
