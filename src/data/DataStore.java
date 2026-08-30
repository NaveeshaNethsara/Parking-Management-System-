package data;

import java.util.List;

/**
 * Generic interface for data storage operations.
 * Demonstrates: Abstraction (defines contract without implementation).
 * @param <T> The type of object to store.
 */
public interface DataStore<T> {

    /**
     * Adds a new item to the data store.
     * @param item The item to add.
     */
    void add(T item);

    /**
     * Retrieves all items from the data store.
     * @return A list of all items.
     */
    List<T> getAll();

    /**
     * Updates an existing item in the data store.
     * @param item The updated item.
     */
    void update(T item);

    /**
     * Deletes an item from the data store by its identifier.
     * @param id The identifier of the item to delete.
     */
    void delete(String id);

    /**
     * Saves all current data to persistent storage.
     */
    void saveAll();

    /**
     * Loads all data from persistent storage.
     */
    void loadAll();
}
