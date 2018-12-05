package com.node.demo;

public class Node {
    private String value ;
    private Node leftNode;
    private Node rightNode;
    public Node(String value){
        this.value = value;
    }

    public void addLeftNode(String v){
        if(leftNode!=null){
           return;
        }
       leftNode= new Node(v);
    }

    public  void addRigthNode(String v){
        if(rightNode != null) return;
        rightNode = new Node(v);
    }

    public  String getValue(){

        return this.value;
    }

    public Node getLeftNode(){
        return  leftNode;
    }
    public Node getRightNode(){
        return  rightNode;
    }

}
