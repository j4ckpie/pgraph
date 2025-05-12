package model;

public class Node {
    private int id;
    private int group;

    public Node(int id, int group) {
        this.id = id;
        this.group = group;
    }

    public int getId() {
        return id;
    }

    public int getGroup() {
        return group;
    }
}
