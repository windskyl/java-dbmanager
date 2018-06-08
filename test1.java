package Algorithm;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.*;
import javax.swing.tree.*;

public class test1 implements ActionListener, TreeSelectionListener {

	JFrame f;
	JPanel peast, psouth, psex, pkind;

	JTree tree;
	DefaultMutableTreeNode[] nodes;
	JTable t;
	DefaultTableModel tm;

	JButton add, del;
	JLabel l[];
	JTextField name, tel;
	JRadioButton male, female, family, friend, mate;

	Vector vdata = new Vector();
	Vector vcol = new Vector();

	ResultSet rs = null;
	Connection conn = null;
	Statement stmt = null;

	String[] col = { "姓名", "性别", "电话号码", "类型" };
	String[] nodestr = { "所有人", "家人", "朋友", "同学" };

	public void refreshdb() {
		try {
			Class.forName("com.hxtt.sql.access.AccessDriver");
			String url = "jdbc:Access:///C:\\Users\\apple\\Desktop\\Database.accdb";
			conn = DriverManager.getConnection(url, "", "");
			stmt = conn.createStatement();

			String all = "select * from 通讯录";

			rs = stmt.executeQuery(all);

			vdata.clear();
			while (rs.next()) {
				Vector vr = new Vector();
				vr.add(rs.getString(1));
				vr.add(rs.getString(2));
				vr.add(rs.getString(3));
				vr.add(rs.getString(4));
				vdata.add(vr);
			}

		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}

	}

	test1() {
		for (int i = 0; i < col.length; i++) {
			vcol.add(col[i]);
		}
		refreshdb();

		f = new JFrame("通讯录管理");
		l = new JLabel[4];
		for (int i = 0; i < 4; i++) {
			l[i] = new JLabel(col[i]);
		}
		name = new JTextField(10);
		name.setText("");
		tel = new JTextField(10);
		tel.setText("");

		male = new JRadioButton("男");
		female = new JRadioButton("女");
		male.setSelected(true);
		female.setSelected(false);
		ButtonGroup bgs = new ButtonGroup();
		bgs.add(female);
		bgs.add(male);
		psex = new JPanel();
		psex.add(male);
		psex.add(female);

		family = new JRadioButton("家人");
		friend = new JRadioButton("朋友");
		mate = new JRadioButton("同学");
		family.setSelected(true);
		friend.setSelected(false);
		mate.setSelected(false);
		ButtonGroup bg = new ButtonGroup();
		bg.add(family);
		bg.add(friend);
		bg.add(mate);
		pkind = new JPanel();
		pkind.add(family);
		pkind.add(friend);
		pkind.add(mate);

		peast = new JPanel(new GridLayout(4, 2));
		peast.add(l[0]);
		peast.add(name);
		peast.add(l[1]);
		peast.add(psex);
		peast.add(l[2]);
		peast.add(tel);
		peast.add(l[3]);
		peast.add(pkind);
		f.add(BorderLayout.NORTH, peast);

		nodes = new DefaultMutableTreeNode[nodestr.length];
		for (int i = 0; i < nodestr.length; i++) {
			nodes[i] = new DefaultMutableTreeNode(nodestr[i]);
		}
		nodes[0].add(nodes[1]);
		nodes[0].add(nodes[2]);
		nodes[0].add(nodes[3]);
		tree = new JTree(nodes[0]);
		tree.setSelectionPath(new TreePath(nodes[0].getPath()));
		tree.addTreeSelectionListener(this);
		JScrollPane treesp = new JScrollPane(tree);
		f.add(BorderLayout.WEST, treesp);

		tm = new DefaultTableModel(vdata, vcol);
		t = new JTable(tm);
		t.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		JScrollPane tablesp = new JScrollPane(t);
		f.add(BorderLayout.CENTER, tablesp);

		add = new JButton("添加");
		add.addActionListener(this);
		del = new JButton("删除");
		del.addActionListener(this);
		psouth = new JPanel();
		psouth.add(add);
		psouth.add(del);
		f.add(BorderLayout.SOUTH, psouth);

		f.setLocationRelativeTo(null);
		f.setSize(600, 400);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		new test1();
	}

	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == add) {

			String[] newfile = new String[4];
			for (int i = 0; i < 4; i++)
				newfile[i] = new String("");
			newfile[0] = name.getText();
			name.setText("");

			if (male.isSelected())
				newfile[1] = "男";
			else if (female.isSelected())
				newfile[1] = "女";
			male.setSelected(true);
			female.setSelected(false);

			newfile[2] = tel.getText();
			tel.setText("");

			if (family.isSelected())
				newfile[3] = "家人";
			else if (friend.isSelected())
				newfile[3] = "朋友";
			else if (mate.isSelected())
				newfile[3] = "同学";
			family.setSelected(true);
			friend.setSelected(false);
			mate.setSelected(false);

			try {
				Class.forName("com.hxtt.sql.access.AccessDriver");
				String url = "jdbc:Access:///C:\\Users\\apple\\Desktop\\Database.accdb";
				Connection conn = DriverManager.getConnection(url, "", "");
				Statement stmt = conn.createStatement();

				String addone = "insert into 通讯录(姓名,性别,电话号码,类型) values('" + newfile[0] + "','" + newfile[1] + "','"
						+ newfile[2] + "','" + newfile[3] + "')";

				int ret = stmt.executeUpdate(addone);
				if (ret == -1)
					System.out.println("insert error!");

			} catch (SQLException sqle) {
				sqle.printStackTrace();
			} catch (ClassNotFoundException cnfe) {
				cnfe.printStackTrace();
			} finally {
				try {
					if (rs != null)
						rs.close();
					if (stmt != null)
						stmt.close();
					if (conn != null)
						conn.close();
				} catch (SQLException sqle) {
					sqle.printStackTrace();
				}
			}

			refreshdb();
			refreshtable();

		} else if (ae.getSource() == del) {
			int c = JOptionPane.showConfirmDialog(f, "确实要删除吗？", "删除确认", JOptionPane.YES_NO_OPTION);
			if (c == 1)
				return;

			int[] selrows = t.getSelectedRows();
			String[] name = new String[selrows.length];

			refreshdb();
			
			try {
				Class.forName("com.hxtt.sql.access.AccessDriver");
				String url = "jdbc:Access:///C:\\Users\\apple\\Desktop\\Database.accdb";
				conn = DriverManager.getConnection(url, "", "");
				stmt = conn.createStatement();

				refreshtable();

				for (int i = 0; i < selrows.length; i++) {
					name[i] = (String) t.getValueAt(selrows[i], 0);
					String delone = "delete from 通讯录 where 姓名 = '" + name[i] + "'";
					int ret = stmt.executeUpdate(delone);
					if (ret == -1) {
						System.out.println("delete error!");
						return;
					}
				}

			} catch (SQLException sqle) {
				sqle.printStackTrace();
			} catch (ClassNotFoundException cnfe) {
				cnfe.printStackTrace();
			} finally {
				try {
					if (rs != null)
						rs.close();
					if (stmt != null)
						stmt.close();
					if (conn != null)
						conn.close();
				} catch (SQLException sqle) {
					sqle.printStackTrace();
				}
			}

			refreshdb();
			refreshtable();
		}
	}

	public void valueChanged(TreeSelectionEvent arg0) {
		refreshdb();
		refreshtable();
	}

	public void refreshtable() {
		DefaultTableModel tm = new DefaultTableModel(vdata, vcol);
		DefaultMutableTreeNode selnode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		if (selnode != nodes[0]) {
			for (int i = 0; i < tm.getRowCount();) {
				if (((String) (tm.getValueAt(i, 3))).indexOf(selnode.toString()) == -1)
					tm.removeRow(i);
				else
					i++;
			}
		}
		t.setModel(tm);
	}
}