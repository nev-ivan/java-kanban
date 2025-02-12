package manager;

import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private static class Node {
        Task task;
        Node prev;
        Node next;


        private Node(Task task, Node prev, Node next) {
            this.task = task;
            this.prev = prev;
            this.next = next;
        }
    }

    private final Map<Integer, Node> nodeMap = new HashMap<>();
    private Node first;
    private Node last;


    @Override
    public void remove(int id) {
        final Node node = nodeMap.get(id);
        removeNode(node);
    }

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        Node oldNode = nodeMap.get(task.getId());
        removeNode(oldNode);
        linkLast(task);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    private void linkLast(Task task) {
        final Node node = new Node(task, null, null);

        if (nodeMap.isEmpty()) {
            first = node;
        } else {
            last.next = node;
            node.prev = last;
        }
        last = node;
        nodeMap.put(task.getId(), node);
    }

    private ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        Node node = first;
        while (node != null) {
            tasks.add(node.task);
            node = node.next;
        }
        return tasks;
    }

    private void removeNode(Node node) {
        if (!nodeMap.containsValue(node) || node == null) {
            return;
        }
        if (node.prev == null && node.next == null) {
            first = null;
            last = null;
            nodeMap.remove(node.task.getId());
            return;
        }
        if (node.prev == null) {
            node.next.prev = null;
            first = node.next;
        } else if (node.next != null) {
            node.next.prev = node.prev;
            node.prev.next = node.next;
        } else {
            node.prev.next = null;
            last = node.prev;
        }
        nodeMap.remove(node.task.getId());
    }
}
