/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    Resume[] storage = new Resume[10000];
    private int size;

    void clear() {
        for (int i = 0; i < size; i++) {
            storage[i] = null;
        }
        size = 0;
    }

    void save(Resume r) {
        if (size == storage.length) {
            return;
        }

        if (r.uuid == null) {
            return;
        }
        storage[size++] = r;
    }

    Resume get(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].uuid.equals(uuid)) {
                return storage[i];
            }
        }
        return null;
    }

    void delete(String uuid) {
        if (size == 0) {
            return;
        }

        if (uuid.isEmpty()) {
            return;
        }

        int indexForDelete = -1;
        for (int i = 0; i < size; i++) {
            if (storage[i].uuid.equals(uuid)) {
                indexForDelete = i;
                break;
            }
        }

        if (indexForDelete == -1) {
            return;
        }

        if(indexForDelete != size - 1) {
            for (int i = indexForDelete + 1; i < size; i++) {
                storage[i - 1] = storage[i];
            }
        }
        storage[size - 1] = null;
        size--;
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        Resume[] resumes = new Resume[size];
        for (int i = 0; i < size; i++) {
            resumes[i] = storage[i];
        }
        return resumes;
    }

    int size() {
        return size;
    }
}
