package managers;

import tasks.Task;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InMemoryHistoryManager implements HistoryManager {
    private static final Logger logger = Logger.getLogger(HistoryManager.class.getName());
    private final Map<Integer, Node> historyMap = new HashMap<>();
    private int lastId = 0;

    private Node head;
    private Node tail;

    private static class Node {
        Node prev;
        Task task;
        Node next;

        public Node(Node prev, Task task, Node next) {
            this.prev = prev;
            this.task = task;
            this.next = next;
        }
    }

    @Override
    public void remove(int id) {
        if (historyMap.containsKey(id)) {
            Node removingNode = historyMap.remove(id);
            removeNode(removingNode);
        } else {
            logger.log(Level.WARNING, "History map not contains task with current id");
        }
    }

    public void removeNode(Node node) {
        final Node prevNode = node.prev;
        final Node nextNode = node.next;

        if (prevNode == null && nextNode == null) {
            logger.log(Level.INFO, "Node is only one");
            return;
        }

        // is head
        if (prevNode == null) {
            nextNode.prev = null;
            head = nextNode;
            logger.log(Level.INFO,"Head node links removed");

            // is tail
        } else if (nextNode == null) {
            prevNode.next = null;
            tail = prevNode;
            logger.log(Level.INFO,"Tail node links removed");

        } else {
            prevNode.next = nextNode;
            nextNode.prev = prevNode;
            logger.log(Level.INFO,"Node links removed");

        }
    }

    // Реализация метода getHistory должна перекладывать задачи из связного списка
    // в ArrayList для формирования ответа.
    @Override
    public List<Task> getHistory() {
        List<Task> result = new ArrayList<>();
        Node tempNode;

        if (head == null) {

            logger.log(Level.INFO, "History is empty");
            return result;

        } else if (tail == null) {
            result.add(head.task);
            return result;

        } else {
            tempNode = head.next;
            result.add(head.task);
            result.add(tempNode.task);
            while (tempNode.next != null) {
                tempNode = tempNode.next;
                result.add(tempNode.task);
            }
        }

        return result;
    }

    @Override
    public void add(Task task) {
        if (Objects.nonNull(task)) {
            // по условию ФЗ-5 в истории должно сохраняться состояние задачи на момент фиксации
            // поэтому делаем глубокую копию
            Task copyTask = new Task(task.getId(), task.getName(), task.getDescription(), task.getStatus(),
                    task.getDuration(), task.getStartTime());

            if (!historyMap.containsKey(task.getId())) {
                linkLast(copyTask);
                logger.log(Level.INFO,"Added new task in history with id = " + task.getId());

            } else {
                remove(task.getId());
                linkLast(copyTask);
                logger.log(Level.INFO,"Refresh old task with id = " + task.getId());
            }

        } else {
            logger.log(Level.INFO,"Task is null ");
        }
    }

    private void linkLast(Task task) {

        if (historyMap.isEmpty()) {
            head = new Node(null, task, null);
            historyMap.put(task.getId(), head);

        } else {
            tail = new Node(historyMap.get(lastId), task, null);
            Node prevNode = historyMap.get(lastId);
            prevNode.next = tail;
            historyMap.put(task.getId(), tail);
        }

        lastId = task.getId();
    }
}
