package fr.itris.glips.svgeditor.actions.frame;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;

import com.nbtoptec.db.DBHelper;

/**
 * 读取查询数据的配置信息
 * 
 * @author zmm
 *
 */
public final class MeasSelDb {

	private static Map<String, String> sqlMap = new MeasSelConfigs()
			.getSqlMap();

	private static DBHelper db = new DBHelper("cac2");

	public MeasSelDb() {
	}

	public static List<Psr> getAllPsr() {
		ResultSet rs = db.query(sqlMap.get("getPsr"));
		List<Psr> list = new ArrayList<Psr>();
		try {
			while (rs.next()) {// 判断有没有下一行
				Psr p = new Psr();
				p.setId(rs.getInt("psr_id"));
				p.setName(rs.getString("name"));
				p.setPid(rs.getInt("parent_id"));
				list.add(p);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		db.close();
		return list;
	}

	public static List<MeasValue> getMeasValueByPsrId(int psrId) {
		String sql = sqlMap.get("getMeasValue").replace("{0}",
				String.valueOf(psrId));
		ResultSet rs = db.query(sql);
		List<MeasValue> list = new ArrayList<MeasValue>();
		try {
			while (rs.next()) {// 判断有没有下一行
				MeasValue mv = new MeasValue();
				mv.setId(rs.getInt("meas_value_id"));
				mv.setName(rs.getString("name"));
				mv.setPsrId(rs.getInt("monitor_impl_id"));
				list.add(mv);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		db.close();
		return list;
	}

	public static void initTree(List<Psr> list, int pid,
			DefaultMutableTreeNode parent) {
		for (Psr psr : list) {
			if (psr.getPid() == pid) {
				DefaultMutableTreeNode other = new DefaultMutableTreeNode(psr);

				initTree(list, psr.getId(), other);
				parent.add(other);
			}
		}
	}
}
