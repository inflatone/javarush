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
        Entry<String> result = getEntry(s);
        return result == null ? null : result.parent.elementName;
    }

    private Entry<String> getEntry(String s) {
        Queue<Entry<String>> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            Entry<String> current = queue.poll();
            if (current.elementName.equals(s)) {
                return current;
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
    public boolean remove(Object o) {
        if (o.getClass() != String.class) {
            throw new UnsupportedOperationException();
        }
        String name = (String) o;
        Entry<String> result = getEntry(name);
        if (result == null) {
            return false;
        }
        Entry<String> parent = result.parent;
        if (parent.leftChild.elementName.equals(name)) {
            parent.leftChild = null;
        } else {
            parent.rightChild = null;
        }
        recount();
        clearFlags();
        return true;
    }

    private boolean isAllBlocked() {
        Queue<Entry<String>> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            Entry<String> current = queue.poll();
            if (current.isAvailableToAddChildren()) {
                return false;
            }
            addChildren(queue, current);
        }
        return true;
    }

    private void clearFlags() {
        if (isAllBlocked()) {
            Queue<Entry<String>> queue = new LinkedList<>();
            queue.offer(root);
            while (!queue.isEmpty()) {
                Entry<String> current = queue.poll();
                if (current.leftChild == null) {
                    current.availableToAddLeftChildren = true;
                }
                if (current.rightChild == null) {
                    current.availableToAddRightChildren = true;
                }
                addChildren(queue, current);
            }
        }
    }

    private void recount() {
        size = -1;
        Queue<Entry<String>> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            addChildren(queue, queue.poll());
            size++;
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
