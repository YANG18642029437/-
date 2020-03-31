package com.yyx.数据结构;

public class 模拟队列 {
    public static void main(String[] args) {
        QueueDemo queueDemo = new QueueDemo(3);
        for (int i = 0; i < 20; i++) {
            if (i % 2 ==0)
                queueDemo.addQueue(i);
            else
                System.out.println(queueDemo.getQueue());
        }

    }
}


class QueueDemo {
    private Integer maxSize;
    private Integer front;
    private Integer rear;
    private int[] arr;

    public QueueDemo(Integer maxSize) {
        this.maxSize = maxSize;
        this.arr = new int[maxSize];
        this.front = 0;
        this.rear = 0;
    }

    public boolean isFull() {
        return (rear+1) % maxSize == front;
    }

    public boolean isEmpty() {
        return rear == front;
    }

    public void addQueue(int n) {
        if (isFull()) {
            System.out.println("超过最大容器");
            return;
        }
        arr[rear] = n;
        rear = (rear + 1) % maxSize;
    }


    public int getQueue() {
        if (isEmpty()) {
            System.out.println("内容为空");
            throw new RuntimeException("内容为空");
        }
        int res = arr[front];
        front = (front + 1) % maxSize;
        return res;
    }
}
