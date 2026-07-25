import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Implementation of a set.
 *
 * @param <T> Type of elements in the set.
 */
public class MySet<T> {
    // Invariants:
    // Elements should be unique.
    // Two sets are equal if they contain the same elements.

    /**
     * Elements of the set.
     */
    private Collection<T> elements;

    /**
     * Creates an empty set.
     */
    public MySet() {
        elements = new ArrayList<>();
    }

    /**
     * Creates a set from a collection.
     *
     * @param elems Collection of elements.
     */
    public MySet(Collection<? extends T> elems) {
        this();
        for (T el : elems) {
            add(el);
        }

    }

    /**
     * Creates a set from elements passed as params.
     *
     * @param params elements to be added to the set.
     */
    public MySet(T... params) {
        this();
        for (T el : params) {
            add(el);
        }
    }

    /**
     * Checks if the set is empty.
     *
     * @return true if the set is empty, false otherwise.
     */
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    /**
     * Check if the set contains a given element.
     *
     * @param elem element to be checked.
     * @return true if the set contains the element, false otherwise.
     */
    public boolean contains(T elem) {
        return elements.contains(elem);
    }

    /**
     * Returns the size of the set.
     *
     * @return size of the set.
     */
    public int size() {
        return elements.size();
    }

    /**
     * Converts the set to a string.
     *
     * @return string representation of the set.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        boolean first = true;
        for (T el : elements) {
            if (first) first = false;
            else sb.append(", ");
            sb.append(el);
        }
        sb.append("}");
        return sb.toString();
    }

    /**
     * Adds an element to the set.
     *
     * @param elem element to be added.
     * @return true if the element was added, false if it was already in the set.
     */
    public boolean add(T elem) {
        if (!contains(elem)) {
            elements.add(elem);
            return true;
        }
        return false;
    }

    /**
     * Removes an element from the set.
     *
     * @param elem element to be removed.
     * @return true if the element was removed, false otherwise.
     */
    public boolean remove(T elem) {
        if (!contains(elem)) {
            return false;
        }
        elements.remove(elem);
        return true;
    }

    /**
     * Adds all elements from the other set to this set.
     *
     * @param other Other set.
     * @return this set with elements from the other set added.
     */
    public MySet<T> union(MySet<? extends T> other) {
        for (T el : other.elements) {
            add(el);
        }
        return this;
    }

    /**
     * Check if this set contains all elements from the other set.
     *
     * @param other Other set.
     * @return true if all elements from the other set are in this set, false otherwise.
     */
    public boolean contains(MySet<? extends T> other) {
        for (T el : other.elements) {
            if (!contains(el)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if this set is a subset of the other set.
     *
     * @param other Other set.
     * @return true if this set is a subset of the other set, false otherwise.
     */
    public boolean isSubsetOfV2(MySet<? super T> other) {
        return other.contains(this);
    }

    /**
     * Check if this set is a subset of the other set.
     *
     * @param other Other set.
     * @return true if this set is a subset of the other set, false otherwise.
     */
    public boolean isSubsetOf(MySet<? extends T> other) {
        return other.elements.containsAll(elements);
    }

    /**
     * Check if this set is equal to the other set.
     *
     * @param other Other set.
     * @return true if this set is equal to the other set, false otherwise.
     */
    public boolean equals(MySet<? extends T> other) {
        return contains(other) && isSubsetOf(other);
    }


    /**
     * Check if this set is equal to the other set.
     *
     * @param o Other set.
     * @return true if this set is equal to the other set, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MySet<?> set = (MySet<?>) o;
        return (elements.size() == set.elements.size() && elements.containsAll(set.elements));
    }

    /**
     * Hash code of the set.
     *
     * @return hash code of the set.
     */
    @Override
    public int hashCode() {
        int hash = 0;
        for (T el : elements) {
            hash += Objects.hashCode(el);
        }
        return hash;
    }

    /**
     * Removes all elements from the set that are in the other set.
     *
     * @param other Other set.
     * @return this set with elements from the other set removed.
     */
    public MySet<T> difference(MySet<? extends T> other) {
        for (T el : new ArrayList<>(other.elements)) {  // copy to avoid removing elements while iterating
            remove(el);
        }
        return this;
    }

    /**
     * Leaves only elements that are in both sets.
     *
     * @param other Other set.
     * @return this set with elements that are in both sets.
     */
    public MySet<T> intersection(MySet<? extends T> other) {
        Collection<T> tmp = new ArrayList<T>();
        for (T el : other.elements) {
            if (contains(el)) tmp.add(el);
        }
        this.elements = tmp;
        return this;
    }


}


