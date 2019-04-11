package com.javarush.task.task20.task2028;

import java.io.Serializable;
import java.util.*;

/* 
Построй дерево(1)
*/
public class CustomTree extends AbstractList<String> implements Cloneable, Serializable {
    Entry<String> root;
    private int size;

    public CustomTree() {
        this.root = new Entry<>("root");
    }

    @Override
    public boolean add(String s) {
        Queue<Entry<String>> queue = new LinkedList<>();
        Entry<String> parent = root;
        while (parent != null && !parent.addChild(s)) {
            addChildren(queue, parent);
            parent = queue.poll();
        }
        size++;
        return true;
    }

    @Override
    public int size() {
        return size;
    }

    public String getParent(String s) {
        Queue<Entry<String>> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            Entry<String> current = queue.poll();
            if (!current.availableToAddLeftChildren && current.leftChild.elementName.equals(s)
                    || !current.availableToAddRightChildren && current.rightChild.elementName.equals(s)) {
                return current.elementName;
            }
            addChildren(queue, current);
        }
        return null;
    }

    private void addChildren(Queue<Entry<String>> queue, Entry<String> entry) {
        if (entry.leftChild != null) {
            queue.offer(entry.leftChild);
        }
        if (entry.rightChild != null) {
            queue.offer(entry.rightChild);
        }
    }

    @Override
    public String get(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String set(int index, String element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int index, String element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends String> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    static class Entry<T> implements Serializable {
        String elementName;
        boolean availableToAddLeftChildren;
        boolean availableToAddRightChildren;
        Entry<T> parent, leftChild, rightChild;

        public Entry(String elementName) {
            this.elementName = elementName;
            this.availableToAddLeftChildren = true;
            this.availableToAddRightChildren = true;
        }

        public boolean isAvailableToAddChildren() {
            return availableToAddLeftChildren || availableToAddRightChildren;
        }

        boolean addChild(String name) {
            if (!isAvailableToAddChildren()) {
                return false;
            }
            Entry<T> child = new Entry<>(name);
            child.parent = this;
            if (availableToAddLeftChildren) {
                leftChild = child;
                availableToAddLeftChildren = false;
            } else {
                rightChild = child;
                availableToAddRightChildren = false;
            }
            return true;
        }
    }
}
