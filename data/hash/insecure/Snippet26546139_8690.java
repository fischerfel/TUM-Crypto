package de.fortis.tcws.client.controller;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public class DummyView extends JFrame {

    private static final long serialVersionUID = 7301762327073611779L;

    private static final String ROOT_NAME = "Root";

    public JTree tree;
    public DefaultTreeModel model;
    public JPanel right;

    public DummyView() {
        super();
        initGui();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void initGui() {
        setSize(1600, 800);

        tree = new JTree();
        tree.setEditable(false);
        tree.addTreeSelectionListener(new DumSelectionListener());
        tree.addTreeExpansionListener(new DumExpansionListener());
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(ROOT_NAME);
        tree.setModel(model = new DefaultTreeModel(root, true));
        refreshNode(root);
        tree.setPreferredSize(new Dimension(200, 800));
        getContentPane().add(new JScrollPane(tree), BorderLayout.WEST);

        JScrollPane rightPan = new JScrollPane(right = new JPanel());
        right.add(new JLabel("Empty"));
        getContentPane().add(rightPan, BorderLayout.CENTER);
    }

    public void manualSelect(Object[] path) {
        DefaultMutableTreeNode target = (DefaultMutableTreeNode) tree.getModel().getRoot();
        for (Object uo : path) {
            refreshNode(target);
            target = getNodeByUserObject(target, uo);
        }

        navigateToNode(target);
    }

    public void navigateToNode(DefaultMutableTreeNode node) {
        TreeNode[] nodes = model.getPathToRoot(node);
        TreePath tpath = new TreePath(nodes);
        tree.scrollPathToVisible(tpath);
        tree.setSelectionPath(tpath);
    }

    public DefaultMutableTreeNode getNodeByUserObject(DefaultMutableTreeNode node, Object o) {
        for (int i = 0; i < node.getChildCount(); i++) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) node.getChildAt(i);
            if (child.getUserObject().equals(o)) {
                return child;
            }
        }
        return null;
    }

    private class DumSelectionListener implements TreeSelectionListener {

        @Override
        public void valueChanged(TreeSelectionEvent e) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                e.getNewLeadSelectionPath().getLastPathComponent();
            right.removeAll();
            right.add(new JLabel("Node: " + node.getUserObject().toString()));
            right.revalidate();
            right.repaint();
        }
    }

    private class DumExpansionListener implements TreeExpansionListener {

        @Override
        public void treeExpanded(TreeExpansionEvent event) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) event.getPath().getLastPathComponent();
            refreshNode(node);
        }

        @Override
        public void treeCollapsed(TreeExpansionEvent event) {
        }
    }

    private void refreshNode(DefaultMutableTreeNode node) {
        node.removeAllChildren();
        Object uo = node.getUserObject();
        if (uo.equals(ROOT_NAME)) {
            for (int i = 1; i <= 10; i++) {
                DefaultMutableTreeNode n = new DefaultMutableTreeNode(new Integer(i));
                node.add(n);
            }
        } else if (uo instanceof Integer) {
            int num = (Integer) uo;
            for (int i = 1; i <= 9; i++) {
                DefaultMutableTreeNode n = new DefaultMutableTreeNode("" + num + "." + i);
                node.add(n);
            }
        } else {
            String uos = (String) uo;
            String[] split = uos.split("[.]");
            if (split.length == 2) {
                for (String let : new String[] {
                    "A", "B", "C", "D", "E", "F", "G", "H", "I", "J"
                }) {
                    DefaultMutableTreeNode n = new DefaultMutableTreeNode(let);
                    node.add(n);
                }
            } else {
                String value = uos;
                DefaultMutableTreeNode parSplit = (DefaultMutableTreeNode) node.getParent();
                value = parSplit + value;
                DefaultMutableTreeNode parNum = (DefaultMutableTreeNode) parSplit.getParent();
                value = parNum + value;
                DefaultMutableTreeNode parRoot = (DefaultMutableTreeNode) parNum.getParent();
                value = parRoot + value;
                value = mdfive(value);

                DefaultMutableTreeNode n = new DefaultMutableTreeNode(value);
                node.add(n);
            }
        }
        model.nodeStructureChanged(node);
    }

    private String mdfive(String in) {
        String out = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(in.getBytes());
            byte[] bytes = md.digest();
            for (int i = 0; i < bytes.length; i++) {
                out += Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return out.substring(0, 10);
    }
}
