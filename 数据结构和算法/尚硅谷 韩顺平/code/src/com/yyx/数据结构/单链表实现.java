package com.yyx.数据结构;

public class 单链表实现 {
    public static void main(String[] args) {
        SingleLinkedList list = new SingleLinkedList();
        list.add2(new HeroNode(3,"杨家祥","是傻蛋"));
        list.add2(new HeroNode(3,"杨家祥1","是傻蛋1"));
        list.add2(new HeroNode(1,"杨家祥2","是傻蛋2"));
        list.add2(new HeroNode(2,"杨家祥3","是傻蛋3"));
        list.update(new HeroNode(2,"杨家祥二货","是傻蛋3"));
        list.delete(new HeroNode(2,"杨家祥二货","是傻蛋3"));
        list.list();
        System.out.println(SingleLinkedList.getLength(list.getHead()));
    }
}


class SingleLinkedList {
    private HeroNode head = new HeroNode(0,null,null);
    private HeroNode temp;

    public HeroNode getHead(){
        return head;
    }

    public void delete(HeroNode node){
        if (head.next == null){
            System.out.println("链表为空");
        }
        HeroNode temp = head;
        while (temp.next != null){
            if (temp.next.no == node.no){
                temp.next = temp.next.next;
                return;
            }
            temp = temp.next;
        }
        System.out.printf("未找到和 $d 相同的节点",node.no);

    }


    public void update(HeroNode node) {
        if (head.next == null){
            System.out.println("链表为空");
        }
        HeroNode temp = head;
        while (temp.next != null){
            if (temp.next.no == node.no){
                node.next = temp.next.next;
                temp.next = node;
                return;
            }
            temp = temp.next;
        }
        System.out.println("未修改成功，不包此次元素");
    }


    public static int getLength(HeroNode node){
        if (node.next == null){
            return 0;
        }
        int length = 0;
        while (node.next != null){
            length ++;
            node = node.next;
        }
        return length;
    }

    /**
     * 添加一个节点到链表中
     * @param node
     */
    public void  add(HeroNode node){
        HeroNode temp = head;
        while (temp.next != null){
            temp = temp.next;
        }
        temp.next = node;
    }

    /**
     * 添加一个节点到链表中
     * @param node
     */
    public void  add2(HeroNode node){
        HeroNode temp = head;
        while (temp.next != null){
            HeroNode t1 = temp;
            temp = temp.next;
            if (temp.no == node.no){
                System.out.println("已有排位");
                return;
            }else if (temp.no > node.no){
                node.next = temp;
                t1.next = node;
                return;
            }
        }
        temp.next = node;
    }


    public void  list (){
        if (head.next == null) {
            System.out.println("链表为空");
            return;
        }
        HeroNode node = head;
        while (node.next != null){
            node = node.next;
            System.out.println(node);
        }
    }
}




class HeroNode{
    public int no;
    private String name;
    private String nickname;
    HeroNode next;


    public HeroNode(int no, String name, String nickname) {
        this.no = no;
        this.name = name;
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "HeroNode{" +
                "no=" + no +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\''+
                '}';
    }
}


