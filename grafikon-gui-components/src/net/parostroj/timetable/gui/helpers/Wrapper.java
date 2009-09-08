package net.parostroj.timetable.gui.helpers;

/**
 * Wrapper class for lists in GUI.
 *
 * @author jub
 */
public class Wrapper<T> implements Comparable<Wrapper<T>> {
    private T wrappedElement;

    public Wrapper(T element) {
        this.setElement(element);
    }

    public void setElement(T elemenet) {
        this.wrappedElement = elemenet;
    }

    public T getElement() {
        return this.wrappedElement;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Wrapper<?> other = (Wrapper<?>) obj;
        if (this.wrappedElement != other.wrappedElement && (this.wrappedElement == null || !this.wrappedElement.equals(other.wrappedElement))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (this.wrappedElement != null ? this.wrappedElement.hashCode() : 0);
        return hash;
    }

    @Override
    public int compareTo(Wrapper<T> o) {
        return this.toString().compareTo(o.toString());
    }
}
