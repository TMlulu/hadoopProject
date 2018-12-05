package com.node.demo;

public class DemoNode {
    public static void main(String[] args) {
       Node root =  new Node("2");
       root.addLeftNode("3");
       root.addRigthNode("6");
       root.addLeftNode("5");
        final Node leftNode1 = root.getLeftNode();
        final Node rightNode1 = root.getRightNode();

        leftNode1.addLeftNode("9");
        leftNode1.addRigthNode("8");

        final Node rightNode2 = leftNode1.getRightNode();
        rightNode2.addRigthNode("7");
        rightNode1.addLeftNode("0");

        getTree(root);

    }

//遍历方法
    public static void  getTree (Node root){
        if(root==null){
           return;
        }
        final String v1 = root.getValue();
        getTree(root.getLeftNode());
        getTree(root.getRightNode());
        System.out.println(v1);
    }
}
