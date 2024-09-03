package managers;
import model.Task;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import utils.Node;

public class InMemoryHistoryManager implements HistoryManager {

    private Node<Task> head;
    private Node<Task> tail;
    private int size = 0;
    private Map<Integer, Node<Task>> nodes = new HashMap<>();

    // Добавление задачи в конец списка
    public void linkLast(Task task) {
        if (nodes.containsKey(task.getId())) {
            removeNode(nodes.get(task.getId()));
        }
        Node<Task> oldTail = tail;  // Сохраняем текущий tail
        Node<Task> newNode = new Node<>(oldTail, task, null);  // Создаем новый узел
        tail = newNode;  // Новый узел становится tail
        if (oldTail == null) {  // Если список был пуст
            head = newNode;  // Новый узел становится и head
        } else {
            oldTail.setNext(newNode);  // Старый tail теперь указывает вперед на новый узел
        }
        nodes.put(task.getId(), newNode);
        size++;
    }

    // сбор всех задач в список
    private List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        Node<Task> currentNode = head;
        while (currentNode != null) {
            tasks.add(currentNode.getData());
            currentNode = currentNode.getNext();
        }
        return tasks;
    }

    // удаление узла из списка
    private void removeNode(Node<Task> node) {
        if (node != null) {
            final Node<Task> next = node.getNext();
            final Node<Task> previous = node.getPrev();

            if (head == node && tail == node) {  // Если это единственный узел
                head = null;
                tail = null;
            } else if (head == node && tail != node) {  // Если это первый узел
                head = next;
                head.setPrev(null);
            } else if (head != node && tail == node) {  // Если это последний узел
                tail = previous;
                tail.setNext(null);
            } else {  // Если это узел в середине
                previous.setNext(next);
                next.setPrev(previous);
            }
            nodes.remove(node.getData().getId());
            size--;
        }
    }


    @Override
    public void remove(int id) {
        removeNode(nodes.get(id));
    }

    @Override
    public void add(Task task) {
        if (task != null) {
            linkLast(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }
}
